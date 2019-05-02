package com.rem.clawndagger.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.BooleanBox;
import com.rem.clawndagger.entities.motion.Motion;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Movement;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Collisionable;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Levelable;
import com.rem.clawndagger.interfaces.Loadable;
import com.rem.clawndagger.interfaces.Moveable;
import com.rem.clawndagger.interfaces.Savable;
import com.rem.clawndagger.interfaces.Tickable;
import com.rem.clawndagger.levels.Level;
import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.file.manager.Smelter;

public abstract class Entity<ChildClass extends Entity<?>> implements Drawable.Focusable, Tickable, Collisionable, Moveable, Loadable, Levelable, Savable {
	
	protected Level level = null;
	protected Motion motion = null;
	protected Rectangle dimensions = null;
	private BooleanBox edges = null;
	protected Animation animation = null;
	protected List<Savable> savers = new ArrayList<Savable>();
	protected List<Consumer<ChildClass>> listeners_load = new ArrayList<Consumer<ChildClass>>();
	protected Stream.Builder<Tickable> tickers1 = Stream.builder();
	@SuppressWarnings("unchecked")
	protected ChildClass self = (ChildClass) this;
	private Movement movement;
	private String name;
	public Entity(){
		this.motion = new Motion(0.0,0.0);
		//this.motion.addCollisionListener(C->{System.out.println(Entity.this+">>"+C);return true;});
		this.dimensions = null;
	}
	public ChildClass level(Level level){
		this.level = level;
		this.level.add(this);
		return self;
	}

	public void addListener(Events.Load load, Consumer<ChildClass> onLoad) {
		this.listeners_load.add(onLoad);
	}
	@SuppressWarnings("unchecked")
	public Boolean on(Events.Load event){
		this.listeners_load.forEach(C->C.accept((ChildClass) Entity.this));
		return true;
	}
	public ChildClass dimensions(double x, double y, double width, double height){
		this.dimensions = new Rectangle(x,y,width,height);
		this.movement = new Events.Movement(this,dimensions,motion);
		return self;
	}
	public ChildClass bound(boolean left,boolean right, boolean up, boolean down){
		this.edges = BooleanBox.find(left,right,up,down);
		return self;
	}
	public BooleanBox getEdges(){
		return edges;
	}
	public abstract ChildClass animation(ImageTemplate imageTemplate);
	public Tickable on(Events.Tick tick){
		tickers1 = tickers1.build().parallel().map(tick).filter(T->T!=null).reduce(Tickable.builder(),(B,T)->B.add(T),(P,N)->P);
		return this;
	}
	public Motion getMotion(){
		return motion;
	}
	@Override
	public Rectangle getRect(){
		return movement.getRect();
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
	public Events.Movement getMovementEvent(){
		return movement;
	}
	@Override
	public Boolean isMoving(){
		return motion.isMoving();
	}
	public Level getLevel(){
		return level;
	}
	
	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	public Stream<Savable> getSubsideraties(){
		return Stream.concat(savers.stream().flatMap(Savable::getSubsideraties),Stream.of(this));
	}
	public List<Lineable> getSaveLines(){
		return Arrays.asList(
				dimensions,
				edges,
				animation
				);
	}
	/*
	@Override
	public Collisionable.Bump findCollision(Rectangle rect){
		return Rectangle.findCollisions(movement.getRect(), rect, this);
	}
	@Override
	public Boolean rectifyCollision(Rectangle oldRectangle, Encroachment[] ofAttack, Motion motion){
		Rectangle.rectifyCollision(dimensions, oldRectangle, ofAttack, motion, this);
		return true;
	}*/
	public abstract static class _2<ChildClass extends Entity<?>> extends Entity<ChildClass> {
		protected Stream.Builder<Tickable> tickers2 = Stream.builder();
		public _2(){
			super();
		}
		public Tickable on(Events.Tick tick){
			super.on(tick);
			tickers2 = tickers2.build().parallel().map(tick).filter(T->T!=null).reduce(Tickable.builder(),(B,T)->B.add(T),(P,N)->P);
			return this;
		}	
	}
	public abstract static class _3<ChildClass extends Entity<?>> extends _2<ChildClass> {
		protected Stream.Builder<Tickable> tickers3 = Stream.builder();
		
		public Tickable on(Events.Tick tick){
			super.on(tick);
			tickers3 = tickers3.build().parallel()
					.map(tick)
					.filter(T->T!=null)
					.reduce(Tickable.builder(),(B,T)->B.add(T),(P,N)->P);
			return this;
		}	
	}
}
