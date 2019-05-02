package com.rem.clawndagger.file.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.interfaces.Nameable;
import com.rem.clawndagger.tests.Tests;

public class Refiner <Type>{
	public static Map<String, Object> dataMap;
	private static Map<Class<?>, Refiner<?>> refiners = new HashMap<Class<?>, Refiner<?>>();

	private Map<String,DataProcessor> methods = new HashMap<String,DataProcessor>();
	private Class<Type> c;
	public DataProcessor getMethod(String name,int length){
		DataProcessor result = methods.get(name+":"+length);
		if(result==null){
			result = 
					create(Stream.of(c.getMethods())
							.filter(M->M.getName().equals(name))
							.filter(M->M.getParameterCount()==length)
							.findAny()
							.orElseThrow(
									()->new RuntimeException(c.toString()+" Could not find method: "+name+"["+length+"]"))
							);
			methods.put(name+":"+length, result);
		}
		return result;
	}
	public static <ClassType> Refiner<ClassType> setup(Class<ClassType> myClass){
		Refiner<ClassType> refiner = new Refiner<ClassType>();
		refiner.c = myClass;
		return refiner;
	}
	public static class FailedToParseFile extends RuntimeException {
		private static final long serialVersionUID = -7511009016152293439L;
		public String lastParse;

		public FailedToParseFile(String lastParse){
			super(lastParse);
			this.lastParse = lastParse;
		}
	}
	public static class Ore<T> {
		private Path fullPath;
		private T obj;
		private String name;
		public static <U> Ore<U> create(U obj, String name, Path fullPath){
			Ore<U> ore = new Ore<U>();
			ore.fullPath = fullPath;
			ore.obj = obj;
			ore.name = name;
			if(!refiners.containsKey(obj.getClass())){
				refiners.put(obj.getClass(), setup(obj.getClass()));
			}
			return ore;
		}
		public void setName() {
			if(obj instanceof Nameable.Settable) {
				((Nameable.Settable)obj).setName(name);
			}
		}
		private static class WordBuilders{
			private Stream.Builder<WordBuilder> words = Stream.builder();
			private WordBuilder current = new WordBuilder();
			private WordBuilder first = current;
			private int length = 0;

			//private String line;

			public void append(int c) {
				appendBuilder(current.append(c));
			}
			public void append(WordBuilder builder) {
				appendBuilder(current.append(builder));

			}
			public void appendBuilder(WordBuilder newBuilder) {
				if(newBuilder!=current) {
					push();
					current = newBuilder;
				}
			}
			private void push() {
				if(current!=first&&current.started) {
					words.add(current);
					++length;
				}
			}
			public void merge(WordBuilders builders) {
				builders.words.build().forEach(this::append);
			}
			/*
			public WordBuilders setLine(String line) {
				this.line = line;
				return this;
			}*/
			public void invoke(Object obj) {
				//System.out.println(obj.getClass().getName()+":"+line);
				push();
				try {
					refiners.get(obj.getClass()).getMethod(
							first.get(),
							length).apply(
									words.build().map(WordBuilder::get))
					.accept(obj);
				}
				catch(Throwable t) {
					System.err.print(first.get());
					words.build().map(WordBuilder::get)
					.forEach(S->System.err.print(" >"+S+"<"));
					System.err.println();
					throw t;
				}
			}
		}
		private static class WordBuilder{
			private boolean quoting = false;
			private boolean escaping = false;
			private boolean ended = false;
			private boolean started = false;
			private Consumer<StringBuilder> builder = B->{};
			public String get() {
				StringBuilder word = new StringBuilder();
				builder.accept(word);
				return word.toString();
			}
			//private int length = 0;
			private void appendChar(char c) {
				Consumer<StringBuilder> b = builder;
				builder = B->{b.accept(B);B.append(c);};
				started = true;
			}
			public WordBuilder append(WordBuilder word) {
				if(ended) {
					return word;
				}
				if(!word.started) {
					return this;
				}
				if(started) {
					Consumer<StringBuilder> b = builder;
					builder = B->{b.accept(B);
					word.builder.accept(B);};

					quoting = word.quoting;
					escaping = word.escaping;
					ended = word.ended;
					
				}
				if(word.ended) {
					return new WordBuilder();
				}
				else {
					return word;
				}
			}
			public WordBuilder append(int C){
				if(C=='\r'||C=='\n'){
					ended = true;
					return new WordBuilder();
				}
				if(escaping){
					appendChar((char)C);
					escaping = false;
					return this;
				}
				else {
					if(quoting){
						if(C!='\"'){
							appendChar((char)C);
							if(C=='\\'){
								escaping = true;
							}
							return this;
						}
						else {
							quoting = false;
							return new WordBuilder();
						}
					}
					else {
						if(C!='\"'){
							if(C!=' '){
								appendChar((char)C);
								return this;
							}
							else {
								ended = true;
								return new WordBuilder();
							}
						}
						else {
							quoting = true;
							return this;
						}
					}
				}
			}

		}
		public T read(){
			try(Stream<String> lines = Files.newBufferedReader(fullPath).lines();){
				lines.parallel()
				.filter(S->S.length()!=0)
				.map(S->S.chars().boxed()
						.collect(
								WordBuilders::new,
								WordBuilders::append,
								WordBuilders::merge)
						//.setLine(S)
						)
				.forEach(W->W.invoke(obj));
				return obj;
			}
			catch(IOException e){
				e.printStackTrace();
			}
			return obj;
		}
	}
	public static List<Object> refine(Path root){
		dataMap = new HashMap<String,Object>();
		//dataMap = Stream.of(
		/*(Supplier<Stream<Nameable>>)
				ImageTemplate::setup,
				Tests::setup*//*)
				.parallel()
		.flatMap(S->S.get())
		.collect(Collectors.toMap(N->N.getName(),
								  N->N));*/
		try(Stream<Path> packages = Files.walk(root);){
			return packages.parallel().filter(Files::isRegularFile).map(Dat->{
				String fileName = Dat.getFileName().toString();
				String className = Dat.subpath(Dat.getNameCount()-2,Dat.getNameCount()-1).toString();
				String packageName = Dat.subpath(Dat.getNameCount()-3,Dat.getNameCount()-2).toString();


				//System.out.println(packageName+"."+className.replace('.', '$')+"::"+fileName.substring(0, fileName.length()-4));
				try {
					return Ore.create(
							Class.forName(packageName+"."+className.replace('.', '$')).newInstance(),
							fileName.substring(0, fileName.length()-4),
							Dat);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			})
					.peek(Ore::setName)
					.collect(Collectors.toList()).stream()
					.peek(O->dataMap.put(O.name,O.obj))
					.collect(Collectors.toList()).stream()
					.parallel()
					.map(Ore::read)
					.collect(Collectors.toList());
		}
		catch(IOException e){e.printStackTrace();}
		return null;
	}
	public DataProcessor create(Method method){
		DataProcessor result = new DataProcessor(
				method,
				Stream.of(method.getParameterTypes()).reduce(new ArrayList<Function<String,?>>(),(A,C)->{
					if(C==Byte.class||C==byte.class){
						A.add(Byte::parseByte);
					}
					else if(C==Boolean.class||C==boolean.class){
						A.add(Boolean::parseBoolean);
					}
					else if(C==Short.class||C==short.class){
						A.add(Short::parseShort);
					}
					else if(C==Long.class||C==long.class){
						A.add(Long::parseLong);
					}
					else if(C==Float.class||C==float.class){
						A.add(Float::parseFloat);
					}
					else if(C==Double.class||C==double.class){
						A.add(Double::parseDouble);
					}
					else if(C==Integer.class||C==int.class){
						A.add(Integer::parseInt);
					}
					else if(C==String.class){
						A.add(S->S);
					}
					else if(C==ImageTemplate.class) {
						A.add(ImageTemplate::valueOf);
					}
					else if(C==Tests.BiFunctions.class) {
						A.add(Tests.BiFunctions::valueOf);
					}
					else if(C==Tests.Predicates.class) {
						A.add(Tests.Predicates::valueOf);
					}
					else {
						A.add(dataMap::get);
					}
					return A;
				},(P,N)->P));
		return result;
	}
	private class DataProcessor implements Function<Stream<String>,Consumer<Object>> {
		private final List<Function<String,?>> parameters;
		private final Method method;
		public DataProcessor(Method method, List<Function<String,?>> parameters){
			this.parameters = parameters;
			this.method = method;
		}
		@Override
		public Consumer<Object> apply(Stream<String> array) {
			Iterator<Function<String,?>> itr = parameters.iterator();
			return Obj->{
				try {
					method.invoke(Obj, array.map(A->itr.next().apply(A)).toArray());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					System.err.println(method.getName()+","+Obj);
					throw new RuntimeException(e);
				};
			};
		}
	}
}
