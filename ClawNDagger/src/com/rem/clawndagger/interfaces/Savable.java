package com.rem.clawndagger.interfaces;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public interface Savable extends Nameable{

	public default Stream<Savable> getSubsideraties(){
		return Stream.of(this);
	}
	public abstract List<Lineable> getSaveLines();


	public static interface Lineable{
		public abstract String getSaveLine(); 
	}


	public static String concat(Object...objs) {
		return Stream.of(objs)
					 .map(Object::toString)
					 .collect(Collectors.joining(" "));
	}
}
