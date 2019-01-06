package com.rem.clawndagger.levels;

import java.util.ArrayList;
import java.util.List;

import com.rem.clawndagger.entities.motion.Motion;
import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.game.events.Events.Tick;
import com.rem.clawndagger.interfaces.Tickable;

public class Level implements Tickable {

	private List<Collisionable> collisions = new ArrayList<Collisionable>();
	private List<Tickable> tickers = new ArrayList<Tickable>();
	
	public Boolean move(Motion motion, double t){
		collisions.parallelStream().filter(C->C.hasCollision(motion,t)).sequential().forEach(C->C.rectifyCollision(motion,t));
		motion.next(t);
		return true;
	}

	@Override
	public Boolean on(Tick tick) {
		tickers.parallelStream().forEach(T->T.on(tick));
		return true;
	}
}
