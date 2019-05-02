package com.rem.clawndagger.game.events;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.BooleanBox;
import com.rem.clawndagger.entities.motion.Encroachment;
import com.rem.clawndagger.entities.motion.Motion;
import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.file.manager.Smelter;
import com.rem.clawndagger.interfaces.Collisionable;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Drawable.Focusable;
import com.rem.clawndagger.interfaces.Loadable;
import com.rem.clawndagger.interfaces.Savable;
import com.rem.clawndagger.interfaces.Tickable;

public class Events {

	public static class Save{
		
	}
	public static class Load implements Consumer<Loadable>{

		@Override
		public void accept(Loadable t) {
			t.on(this);
			if(t instanceof Savable){
				try{
					((Savable) t).getSubsideraties().forEach(Smelter::link);
				}
				catch(Exception e){
					System.err.println(t);
					throw e;
				}
			}
		}

	}
	public class Pause implements Supplier<Events.Tick>{
		private Events.Tick tick;
		public Pause(Events.Tick tick){
			this.tick = tick;
		}
		public Events.Tick get(){
			return tick;
		}
	}
	public class Unpause implements Supplier<Events.Tick>{
		private Events.Tick tick;
		public Unpause(Events.Tick tick){
			this.tick = tick;
		}
		public Events.Tick get(){
			return tick;
		}
	}

	public static class Collision {
		public static class WithSomeone {
			private Encroachment[] ofAttacks;
			private Events.Movement colliderA;
			private Events.Movement colliderB;
			public WithSomeone(Encroachment[] result, Events.Movement movementA, Events.Movement movementB) {
				ofAttacks = result;
				colliderA = movementA;
				colliderB = movementB;
			}
			public Boolean collide() {
				Rectangle.rectifyCollision(colliderB.getRect(), colliderA.getRect(),
						colliderB.getEdges(),colliderA.getEdges(),
						ofAttacks, colliderA.getMotion());
				colliderB.getMotion().collide(colliderA.getOwner());
				colliderA.getMotion().collide(colliderB.getOwner());
				return true;
			}
		}
		public static class WithSomething {
			private Encroachment[] ofAttacks;
			private Events.Movement colliderA;
			private Collisionable colliderB;
			public WithSomething(Encroachment[] result, Events.Movement movementA, Collisionable colliderB) {
				ofAttacks = result;
				colliderA = movementA;
				this.colliderB = colliderB;
			}
			public Boolean collide() {
				Rectangle.rectifyCollision(
						colliderB.getRect(), colliderA.getRect(),
						colliderB.getEdges(),colliderA.getEdges(),
						ofAttacks, colliderA.getMotion());
				colliderA.getMotion().collide(colliderB);
				return true;
			}
		}

	}

	public static class Movement {
		private Collisionable owner;
		private Rectangle originRectangle;
		private Rectangle nextRectangle;
		private Motion motion;
		public Movement(Collisionable owner, Rectangle originRectangle, Motion motion){
			this.owner = owner;
			this.originRectangle = originRectangle;
			this.motion = motion;
		}
		public BooleanBox getEdges() {
			return owner.getEdges();
		}
		public Motion getMotion() {
			return motion;
		}
		public Collisionable getOwner(){
			return owner;
		}
		public Movement set(Double t) {
			this.nextRectangle = motion.next(originRectangle, t);
			return this;
		}
		public Rectangle getRect() {
			return nextRectangle!=null?nextRectangle:originRectangle;
		}
		public Boolean end(){
			if(nextRectangle!=null){
				originRectangle.setX(nextRectangle.getX());
				originRectangle.setY(nextRectangle.getY());
				nextRectangle = null;
			}
			return true;
		}

	}

	public static class Draw implements Consumer<Drawable>{
		public class Unfocus implements Consumer<Focusable> {
			@Override
			public void accept(Focusable t) {
				t.on(this);
			}
		}

		public static class  Focus implements Consumer<Focusable> {
			@Override
			public void accept(Focusable t) {
				t.on(this);
			}
		}
		public int bind;
		@Override
		public void accept(Drawable t) {
			t.on(this);
		}
	}

	public static class Tick implements Function<Tickable,Tickable>, Consumer<Tickable>, Supplier<Double>{
		private Double millisecond;
		public Tick(double millisecond){
			this.millisecond = millisecond;
		}
		public Double get(){
			return this.millisecond;
		}
		@Override
		public Tickable apply(Tickable t) {
			return t.on(this);
		}
		@Override
		public void accept(Tickable t) {
			t.on(this);
		}
	}

}
