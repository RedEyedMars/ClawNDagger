package com.rem.clawndagger.entities.transitions;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.EntityTransition;
import com.rem.clawndagger.entities.Turtle;
import com.rem.clawndagger.entities.enemies.Enemy.Patrol;
import com.rem.clawndagger.entities.hero.Hero;
import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.file.manager.Smelter;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Tick;
import com.rem.clawndagger.interfaces.Collisionable;
import com.rem.clawndagger.interfaces.Moveable;
import com.rem.clawndagger.interfaces.Savable;
import com.rem.clawndagger.levels.Level;

public class EntityTransitions {


	public static class Normal {
		public static class Triggered {
			public static class GoTo <TurtleType extends Turtle<?>>implements BiFunction<TurtleType,Events.Tick,Boolean>{
				private Supplier<CollisionBucket> destination;
				private CollisionBucket originalSpot = null;
				private Position originalPoint = null;
				private Position originalSpeed = null;
				private Boolean isFirst = true;
				private Boolean hasCollided = false;
				private Boolean onReturnEntityCollision;
				private BiFunction<Collisionable, Collisionable, Boolean> onCollide;
				public GoTo(Level level, Boolean onReturnEntityCollision,Supplier<CollisionBucket> destination,BiFunction<Collisionable,Collisionable,Boolean> onCollide){
					this.onReturnEntityCollision = onReturnEntityCollision;
					this.destination = destination;
					this.onCollide = onCollide;
					this.originalSpot = new CollisionBucket().dimensions(0, 0, 0, 0).level(level);
				}
				@Override
				public Boolean apply(TurtleType t, Tick u) {
					if(isFirst){
						originalPoint = new Position(t.getRect().getX(),t.getRect().getY());
						originalSpeed = new Position(t.getMotion().getX(),t.getMotion().getY());
						if(destination.get().getRect().getX()>=t.getRect().getX()){
							if(destination.get().getRect().getY()>=t.getRect().getY()){
								originalSpot = originalSpot.dimensions(
										t.getRect().getX(), t.getRect().getY(),
										t.getRect().getWidth(), t.getRect().getHeight());
							}
							else {
								originalSpot = originalSpot.dimensions(
										t.getRect().getX(), t.getRect().getY()+t.getRect().getHeight(),
										t.getRect().getWidth(), t.getRect().getHeight());
							}
						}
						else {
							if(destination.get().getRect().getY()>=t.getRect().getY()){
								originalSpot = originalSpot.dimensions(
										t.getRect().getX()+t.getRect().getWidth(), t.getRect().getY(),
										t.getRect().getWidth(), t.getRect().getHeight());
							}
							else {
								originalSpot = originalSpot.dimensions(
										t.getRect().getX()+t.getRect().getWidth(), t.getRect().getY()+t.getRect().getHeight(),
										t.getRect().getWidth(), t.getRect().getHeight());
							}
						}
						if(onReturnEntityCollision){
							t.getMotion().addCollisionListener(C->{
								isFirst = true;
								hasCollided  = true;
								onCollide.apply(t,C);
								return false;
							});
						}
						isFirst = false;
						hasCollided = false;
					}
					if(destination.get().getRect().getX()>t.getRect().getX()){
						t.walkRight();
					}
					else if(destination.get().getRect().getX()<t.getRect().getX()){
						t.walkLeft();
					}
					if(destination.get().getRect().getY()>t.getRect().getY()){
						t.walkUp();
					}
					else if(destination.get().getRect().getY()<t.getRect().getY()){
						t.walkDown();
					}
					if(!hasCollided&&destination.get().contains(t)){
						hasCollided = true;
						isFirst = true;
					}
					return true;
				}
				public Boolean hasCollided(){
					if(hasCollided){
						hasCollided = false;
						return true;
					}
					else return false;
				}
				public CollisionBucket originalSpot(){
					return originalSpot;
				}
				public Position getOrigin() {
					return originalPoint;
				}
				public Position getOriginSpeed() {
					return originalSpeed;
				}
			}
			public static class Charge <TurtleType extends Turtle<?>> extends EntityTransition<TurtleType> implements Savable {

				protected String saveCommand = "set";
				protected TurtleType turtle;
				protected Supplier<String> distanceSupplier;
				protected Supplier<String> onCollideSupplier;
				protected Supplier<String> onPassiveSupplier;
				protected Supplier<String> typeOfTriggerEntitySupplier;

				public String getName() {
					return turtle.getName()+"_charge";
				}
				public void setName(String name) {}
				public Stream<Savable> getSubsideraties(){
					return Stream.of(this);
				}
				public List<Lineable> getSaveLines(){
					return Arrays.asList(
							()->Savable.concat(saveCommand,turtle.getName(),typeOfTriggerEntitySupplier.get(),onPassiveSupplier.get(),onCollideSupplier.get(),distanceSupplier.get()));
				}
				public static class Enemy extends Charge<com.rem.clawndagger.entities.enemies.Enemy> {
					private Patrol patrol = null;
					public Stream<Savable> getSubsideraties(){
						return Stream.of(patrol,this);
					}
					public Enemy set_standard(com.rem.clawndagger.entities.enemies.Enemy host, Patrol onPassive, Double dx, Double dy, Double d){
						set(host,E->(E instanceof Hero),onPassive,(Ca,Cb)->true,dx,dy,d);
						saveCommand = "set_standard";
						this.turtle = host;
						this.patrol = onPassive;
						this.typeOfTriggerEntitySupplier = ()->null;
						this.distanceSupplier = ()->dx+" "+dy+" "+d;
						this.onCollideSupplier = ()->null;
						this.onPassiveSupplier = ()->onPassive.getName();
						return this;
					}
				}
				public Charge<TurtleType> set(TurtleType entity,
						Predicate<? extends Moveable> typeOfTriggerEntity,
						BiFunction<TurtleType,Events.Tick,Boolean> onPassive,
						BiFunction<Collisionable,Collisionable,Boolean> onCollide,
						Double dx, Double dy,
						Double d){
					entity.addListener(new Events.Load(), E->charge(entity,typeOfTriggerEntity,onPassive,onCollide,dx,dy,d));
					return this;
				}
				public Charge<TurtleType> charge(
						TurtleType entity,
						Predicate<? extends Moveable> typeOfTriggerEntity,
						BiFunction<TurtleType,Events.Tick,Boolean> onPassive,
						BiFunction<Collisionable,Collisionable,Boolean> onCollide,
						Double dx, Double dy,
						Double d) {
					CollisionBucket collisionBox = new CollisionBucket().dimensions(
							entity.getRect().getX()+dx,entity.getRect().getY()+dy,
							entity.getRect().getWidth(),entity.getRect().getHeight()).level(entity.getLevel());
					GoTo<TurtleType> chargeCharge = new GoTo<TurtleType>(entity.getLevel(),true,()->collisionBox,onCollide);
					GoTo<TurtleType> chargeReturn = new GoTo<TurtleType>(entity.getLevel(),false,()->chargeCharge.originalSpot,(E,C)->{
						entity.getRect().setX(chargeCharge.getOrigin().getX());
						entity.getRect().setY(chargeCharge.getOrigin().getY());
						entity.getMotion().setX(chargeCharge.getOriginSpeed().getX());
						entity.getMotion().setY(chargeCharge.getOriginSpeed().getY());
						return true;
					});
					EntityTransition<TurtleType> passive = create(entity,onPassive);
					EntityTransition<TurtleType> charge = new EntityTransition<TurtleType>().create(entity,chargeCharge);
					EntityTransition<TurtleType> recoil = new EntityTransition<TurtleType>().create(entity,chargeReturn);

					add(()->collisionBox.couldFind(typeOfTriggerEntity), charge);
					charge.add(()->chargeCharge.hasCollided(),  recoil);
					recoil.add(()->chargeReturn.hasCollided(),  passive);

					return this;
				}
			}
		}
	}


}

