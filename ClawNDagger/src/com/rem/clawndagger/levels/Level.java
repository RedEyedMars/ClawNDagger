package com.rem.clawndagger.levels;

import java.util.ArrayList;
import java.util.List;

import com.rem.clawndagger.entities.Entity;
import com.rem.clawndagger.entities.motion.Motion;
import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Draw;
import com.rem.clawndagger.game.events.Events.Draw.Focus;
import com.rem.clawndagger.game.events.Events.Draw.Unfocus;
import com.rem.clawndagger.game.events.Events.Tick;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Tickable;

public class Level implements Tickable, Drawable, Drawable.Focusable {

	private List<Collisionable> collisions = new ArrayList<Collisionable>();
	
	private List<Tickable> tickers = new ArrayList<Tickable>();
	private List<Drawable> drawers = new ArrayList<Drawable>();
	private List<Drawable.Focusable> focusers = new ArrayList<Drawable.Focusable>();
	
	public Level(){
	}
	public Boolean add(Entity<?> entity){
		tickers.add(entity);
		if(entity instanceof Drawable){
			drawers.add((Drawable)entity);
			focusers.add((Drawable.Focusable)entity);
		}
		return true;
	}
	public Boolean move(Motion motion, double t){
		collisions.parallelStream().filter(C->C.hasCollision(motion,t)).sequential().forEach(C->C.rectifyCollision(motion,t));
		motion.next(t);
		return true;
	}

	@Override
	public int getTexture() {
		return 0;
	}
	@Override
	public Boolean on(Tick tick) {
		tickers.parallelStream().forEach(T->T.on(tick));
		return true;
	}

	public Boolean on(Events.Draw draw) {
		drawers.parallelStream().forEach(D->D.on(draw));
		return true;
	}

	public Boolean on(Events.Draw.Focus focus) {
		focusers.parallelStream().forEach(D->D.on(focus));
		return true;
	}
	@Override
	public Boolean on(Unfocus focus) {
		focusers.parallelStream().forEach(D->D.on(focus));
		return true;
	}

}
