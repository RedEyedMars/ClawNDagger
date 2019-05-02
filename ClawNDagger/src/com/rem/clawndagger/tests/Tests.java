package com.rem.clawndagger.tests;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.hero.Hero;
import com.rem.clawndagger.interfaces.Nameable;

public class Tests {
	public enum BiFunctions implements Nameable {
		isBifunctionTrue(Tests::isBifunctionTrue)
		;
		private final BiFunction<?,?,Boolean> bifunction;
		private BiFunctions(BiFunction<?,?,Boolean> bifunction) {
			this.bifunction = bifunction;
		}
		public BiFunction<?,?,Boolean> get(){
			return bifunction;
		}
		@Override
		public String getName() {
			return "BiFunction::"+super.toString();
		}
	}
	public enum Predicates implements Nameable {
		isHero(Tests::isHero)
		;
		private final Predicate<?> predicate;
		private Predicates(Predicate<?> predicate) {
			this.predicate = predicate;
		}
		public Predicate<?> get(){
			return predicate;
		}
		@Override
		public String getName() {
			return "Predicate::"+super.toString();
		}
	}
	public static Stream<Nameable> setup(){
		return Stream.of(
				BiFunctions.values(),
				Predicates.values()
				).flatMap(S->Stream.of(S));
	}
	public static Boolean isBifunctionTrue(Object arg0, Object arg1){
		return true;
	}
	public static Boolean isHero(Object entity){
		return entity instanceof Hero;
	}
}
