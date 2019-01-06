package com.rem.clawndagger.entities.hero;

import com.rem.clawndagger.entities.Entity;
import com.rem.clawndagger.game.events.Events.Tick;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.levels.Level;

public class Hero extends Entity._3<Hero.State> {

	private static final int ANIMATION_NEUTRAL = 0;
	private static final int ANIMATION_JUMP = 1;
	public static class State {
		private boolean jumping = false;
	}
	private State state = new State();
	public Hero(int x, int y, Level level) {
		super(x, y, level);
		animation = new Animation<Hero.State>(null,null){ 
			@Override
			public Boolean update(State state) {
				if(state.jumping){
					this.flipBookIndex = ANIMATION_JUMP;
				}
				else {
					this.flipBookIndex = ANIMATION_NEUTRAL;
				}
				return true;
			}};
	}
	public Boolean jump(){
		if(state.jumping){
			return false;
		}
		else {
			state.jumping = true;
			animation.update(state);
			motion.addCollisionListener(S->{state.jumping=false; return false;});
			return true;
		}
	}
}
