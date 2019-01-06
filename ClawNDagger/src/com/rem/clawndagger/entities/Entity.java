package com.rem.clawndagger.entities;

import java.util.ArrayList;
import java.util.List;

import com.rem.clawndagger.entities.motion.Motion;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Tickable;
import com.rem.clawndagger.levels.Level;

public class Entity <UpdateType> implements Tickable {
	protected Level level = null;
	protected Motion motion = null;
	protected Animation<UpdateType> animation = null;
	protected List<Tickable> tickers1 = new ArrayList<Tickable>();
	public Entity(int x, int y, Level level){
		this.motion = new Motion(x,y);
		this.level = level;
		this.tickers1.add(T->move(T.get()));
	}
	public Boolean on(Events.Tick tick){
		tickers1.parallelStream().forEach(T->T.on(tick));
		return true;
	}
	public Boolean move(Double t){
		return level.move(motion, t);
	}
	public static class _2 <UpdateType> extends Entity<UpdateType> {
		protected List<Tickable> tickers2 = new ArrayList<Tickable>();
		public _2(int x, int y, Level level) {
			super(x, y, level);
		}
		public Boolean on(Events.Tick tick){
			super.on(tick);
			tickers2.parallelStream().forEach(T->T.on(tick));
			return true;
		}	
	}
	public static class _3 <UpdateType> extends _2<UpdateType> {
		protected List<Tickable> tickers3 = new ArrayList<Tickable>();
		public _3(int x, int y, Level level) {
			super(x, y, level);
		}
		public Boolean on(Events.Tick tick){
			super.on(tick);
			tickers3.parallelStream().forEach(T->T.on(tick));
			return true;
		}	
	}
}
