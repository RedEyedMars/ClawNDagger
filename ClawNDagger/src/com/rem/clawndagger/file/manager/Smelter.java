package com.rem.clawndagger.file.manager;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.graphics.Gui;
import com.rem.clawndagger.interfaces.Savable;

public class Smelter {
	private static Stream.Builder<Savable> unsaved = null;
	private static Map<Savable,Supplier<Boolean>> saved = new HashMap<Savable,Supplier<Boolean>>();
	private static Set<Supplier<Boolean>> savers = new HashSet<Supplier<Boolean>>();
	private static boolean saveThreadStarted = false;
	public static void link(Savable unsavedActor){

		synchronized(saved){
			if(!saved.containsKey(unsavedActor)){
				if(unsaved==null){
					unsaved = Stream.builder();
				}
				if(unsavedActor==null){
					throw new RuntimeException("No Nulls!");
				}
				unsaved.add(unsavedActor);
			}
		}
	}
	public static void startSaveThread(){
		if(!saveThreadStarted){
			saveThreadStarted = true;
			new Thread(new Runnable(){
				@Override
				public void run() {
					while(Gui.isRunning){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							Gui.isRunning = false;
							e.printStackTrace();
						}
						synchronized(saved){

							if(unsaved!=null){
								unsaved.build()
									.parallel()
									.map(S->Smelter.saver(S,"./res/save"))
									.collect(Collectors.toList()).stream()
									.peek(savers::add)
									.collect(Collectors.toMap(
											Saver::getHost,
											S->S
											));
		
								unsaved = null;
							}
						}
						savers.parallelStream().forEach(Supplier::get);
					}
				}}).start();
		}
	}

	private static abstract class Saver implements Supplier<Boolean> {
		private Savable host;

		public Saver(Savable host){
			this.host = host;
		}
		public Savable getHost(){
			return host;
		}
	}


	private static boolean getClassName(Class<?> enclosedClass, StringBuilder className){
		if(enclosedClass!=null){
			if(getClassName(enclosedClass.getEnclosingClass(),className)){
				className.append(".");
			}
			className.append(enclosedClass.getSimpleName());
			return true;
		}
		else {
			return false;
		}
	}
	private static Path createPath(Path packageName, String className) throws IOException {
		synchronized(unsaved){
			Path result = null;
			if(!Files.exists(packageName)){
				Files.createDirectory(packageName);
				result = packageName.resolve(className);
				Files.createDirectory(result);				
			}
			else {
				result = packageName.resolve(className);
				if(!Files.exists(result)){
					Files.createDirectory(result);
				}
			}
			return result;
		}
	}
	public static Saver saver(Savable savable, String fileName) {
		StringBuilder className = new StringBuilder();
		if(getClassName(savable.getClass().getEnclosingClass(),className)){
			className.append(".");
		}
		className.append(savable.getClass().getSimpleName());
		try{
			Path directory = createPath(Paths.get(fileName,savable.getClass().getPackage().getName()),className.toString());
			List<Savable.Lineable> lines = savable.getSaveLines();
			return new Saver(savable){public Boolean get(){
				try(Writer writer = Files.newBufferedWriter(directory.resolve(savable.getName()+".dat"), StandardOpenOption.CREATE);){
					lines.forEach(L->{
						try {
							writer.write(L.getSaveLine());
							writer.write('\n');
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}};
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
