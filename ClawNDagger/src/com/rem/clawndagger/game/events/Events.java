package com.rem.clawndagger.game.events;

import java.util.function.Supplier;

public class Events {

	public class Draw {

	}

	public static class Tick implements Supplier<Double>{
		private Double millisecond;
		public Tick(double millisecond){
			this.millisecond = millisecond;
		}
		public Double get(){
			return this.millisecond;
		}
	}
	
}
