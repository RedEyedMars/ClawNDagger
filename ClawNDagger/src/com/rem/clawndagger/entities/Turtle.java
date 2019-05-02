package com.rem.clawndagger.entities;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.graphics.Renderer;
import com.rem.clawndagger.graphics.Renderer.Layer;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Nameable;
import com.rem.clawndagger.interfaces.Savable;

public class Turtle<ChildClass extends Entity<?>> extends Entity._3<ChildClass> {
	private static final int ANIMATION_NEUTRAL = 0;
	private static final int ANIMATION_WALK = 1;
	private static final int ANIMATION_JUMP = 2;

	private EntityState state = new EntityState();
	private EntityTransition<?> transition = null;
	private class Anim extends Animation{
		public Anim(Layer layer, Rectangle dimensions, ImageTemplate template, int[]... positions) {
			super(layer, dimensions, template, positions);
		}
		public Boolean walk(EntityState state, int amount){
			if(!state.jumping)animation.flipBookIndex = ANIMATION_WALK;
			return true;
		}
		public Boolean jump(EntityState state, int amount){
			animation.flipBookIndex = ANIMATION_JUMP;
			return true;
		}
		public Boolean land(EntityState state, int amount){
			if(state.dead){return true;}
			else if(state.walking){animation.flipBookIndex = ANIMATION_WALK;return true; }
			else {             animation.flipBookIndex = ANIMATION_NEUTRAL;return true; }
		}
		public Boolean idle(EntityState state, int amount){
			animation.flipBookIndex = ANIMATION_NEUTRAL;
			return true;
		}
	}
	public ChildClass animation(ImageTemplate image){

		Anim anim = new Anim(Renderer.topLayer ,dimensions,image,
				new int[]{0,0},
				new int[]{0,0},
				new int[]{0,0});
		animation = anim;
		state.listeners.get(EntityState.Change.WALK).add(anim::walk);
		state.listeners.get(EntityState.Change.JUMP).add(anim::jump);
		state.listeners.get(EntityState.Change.LAND).add(anim::land);
		state.listeners.get(EntityState.Change.IDLE).add(anim::idle);

		return self;
	}
	public Boolean jump(){
		if(state.jumping){
			return false;
		}
		else {
			state.change(EntityState.Change.JUMP);
			motion.addCollisionListener(S->{state.change(EntityState.Change.LAND); return false;});
			return true;
		}
	}
	public Boolean walkRight(){
		motion.a.player.setX(1);
		return state.change(EntityState.Change.WALK,1);
	}
	public Boolean walkLeft(){
		motion.a.player.setX(-1);
		return state.change(EntityState.Change.WALK,-1);
	}
	public Boolean walkUp(){
		motion.a.player.setY(1);
		return state.change(EntityState.Change.WALK,1);
	}
	public boolean walkDown(){
		motion.a.player.setY(-1);
		return state.change(EntityState.Change.WALK,-1);
	}
	public Boolean idleHorizontal(){

		motion.a.player.setX(0.0);
		//motion.a.player.x = 0.0;
		//motion.x = 0.0;
		if(motion.a.player.getX() ==0.0)return state.change(EntityState.Change.IDLE);
		else return state.change(EntityState.Change.WALK);
	}
	public Boolean idleVertical(){
		motion.a.player.setY(0.0);
		//motion.y = 0.0;
		if(motion.a.player.getX() ==0.0)return state.change(EntityState.Change.IDLE);
		else return state.change(EntityState.Change.WALK);
	}

	public void setTransitions(EntityTransition<? extends Turtle<ChildClass>> state) {
		//System.out.println(state);
		this.transition = state;
		this.tickers3.add(state);
		if(state instanceof Savable){
			this.savers.add((Savable)state);
		}
	}
	public List<Lineable> getSaveLines(){
		return transition!=null&&(transition instanceof Nameable)?
				Stream.concat(
						super.getSaveLines().stream(),
						Stream.of(()->Savable.concat("setTransitions",((Nameable)transition).getName()))
						).collect(Collectors.toList())
				:
				super.getSaveLines();
	}
	
}
