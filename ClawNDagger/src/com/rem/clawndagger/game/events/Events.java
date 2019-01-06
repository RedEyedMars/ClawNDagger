package com.rem.clawndagger.game.events;

import java.util.function.Supplier;

import com.rem.clawndagger.game.events.Events.Tick;

public class Events {

	public static class Draw {
		public class Unfocus {
		}

		public static class  Focus{
		}

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
