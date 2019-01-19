package com.rem.clawndagger.entities.hero;

import com.rem.clawndagger.entities.Entity;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Draw;
import com.rem.clawndagger.game.events.Events.Draw.Focus;
import com.rem.clawndagger.game.events.Events.Tick;
import com.rem.clawndagger.graphics.Renderer;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.levels.Level;

public class Hero extends Entity._3<Hero.State> implements Drawable, Drawable.Focusable{

	private static final int ANIMATION_NEUTRAL = 0;
	private static final int ANIMATION_JUMP = 1;
	public static class State {
		private boolean jumping = false;
	}
	private State state = new State();
	public Hero(ImageTemplate heroImageTemplate,double x, double y, Level level) {
		super(x, y, level);
		animation = new Animation<Hero.State>(Renderer.topLayer ,dimensions,heroImageTemplate,new int[]{0,0}){ 
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
	@Override
	public Boolean on(Events.Draw.Focus focus) {
		return animation.on(focus);
	}
	@Override
	public Boolean on(Events.Draw.Unfocus focus) {
		return animation.on(focus);
	}
	@Override
	public Boolean on(Events.Draw draw) {
		return animation.on(draw);
	}
	@Override
	public int getTexture() {
		return animation.getTexture();
	}
}
