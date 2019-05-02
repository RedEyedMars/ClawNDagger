package com.rem.clawndagger.levels;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.Entity;
import com.rem.clawndagger.entities.hero.Hero;
import com.rem.clawndagger.entities.motion.Encroachment;
import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.entities.transitions.CollisionBucket;
import com.rem.clawndagger.file.manager.Refiner;
import com.rem.clawndagger.file.manager.Smelter;
import com.rem.clawndagger.game.Game;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Draw.Unfocus;
import com.rem.clawndagger.graphics.Gui;
import com.rem.clawndagger.interfaces.Collisionable;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Levelable;
import com.rem.clawndagger.interfaces.Loadable;
import com.rem.clawndagger.interfaces.Moveable;
import com.rem.clawndagger.interfaces.Savable;
import com.rem.clawndagger.interfaces.Tickable;

public class Level implements Tickable, Drawable.Focusable, Loadable {

	private List<Collisionable> colliders = new ArrayList<Collisionable>();
	private List<Collisionable> collisions = new ArrayList<Collisionable>();
	private List<CollisionBucket> collisionbuckets  = new ArrayList<CollisionBucket>();

	private List<Savable> savers = new ArrayList<Savable>();
	private List<Loadable> loaders = new ArrayList<Loadable>();
	private List<Tickable> tickers = new ArrayList<Tickable>();
	private List<Moveable> movers = new ArrayList<Moveable>();
	//	private List<Drawable> drawers = new ArrayList<Drawable>();
	private List<Drawable.Focusable> focusers = new ArrayList<Drawable.Focusable>();

	protected Hero hero;

	private List<Events.Movement> motions = new ArrayList<Events.Movement>();

	public Level(){
	}
	public Boolean add(Entity<?> entity){
		loaders.add(entity);
		tickers.add(entity);
		savers.add(entity);
		//collisions.add(entity);
		movers.add(entity);
		focusers.add(entity);
		return true;
	}
	public Boolean add(Terrain terrain){
		collisions.add(terrain);
		focusers.add(terrain);
		loaders.add(terrain);
		savers.add(terrain);
		return true;
	}
	public Boolean add(Backdrop backdrop){
		focusers.add(backdrop);
		loaders.add(backdrop);
		savers.add(backdrop);
		return true;
	}
	public Boolean add(CollisionBucket collisionBucket) {
		collisionbuckets.add(collisionBucket);
		return true;
	}

	
	public Boolean on(Events.Load load){	
		this.loaders.forEach(load);
		Smelter.startSaveThread();
		return true;
	}

	@Override
	public Tickable on(Events.Tick tick) {
		motions.clear();

		colliders.clear();
		colliders.addAll(collisions);
		tickers.parallelStream().forEach(tick);
		movers.forEach(M->{
			M.getMovementEvent().set(tick.get());
			if(M.isMoving()){
				motions.add(M.getMovementEvent());
			}
			else {
				colliders.add(M);
			}
		});
		IntStream.range(0, motions.size()).boxed().parallel().flatMap(
				I->{
					Rectangle currentRect = motions.get(I).getRect();
					return IntStream.range(I+1, motions.size()).boxed().parallel().map(J->
					{
						Encroachment[] result = currentRect.findCollision(motions.get(J).getRect());
						return result!=null?new Events.Collision.WithSomeone(result,motions.get(I),motions.get(J)):null;
					}
							).filter(C->C!=null);})
		.collect(Collectors.toList()).forEach(Events.Collision.WithSomeone::collide);
		motions.parallelStream().flatMap(M->colliders.parallelStream().map(C->{
			Encroachment[] result = M.getRect().findCollision(C.getRect());
			return result!=null?new Events.Collision.WithSomething(result,M,C):null;
		}).filter(C->C!=null))
		.collect(Collectors.toList()).forEach(Events.Collision.WithSomething::collide);
		motions.forEach(Events.Movement::end);

		collisionbuckets.parallelStream().forEach(CollisionBucket::clear);
		movers.parallelStream().forEach(M->{
			Rectangle currentRect = M.getRect();
			collisionbuckets.parallelStream()
			.filter(CB->CB.getRect().findCollision(currentRect)!=null)
			.forEach(CB->CB.add(M));
		});
		return this;
	}
	public Boolean on(Events.Draw.Focus focus) {
		focusers.parallelStream().forEach(focus);
		return true;
	}
	@Override
	public Boolean on(Unfocus focus) {
		focusers.parallelStream().forEach(focus);
		return true;
	}
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public static Level load(String fileName){
		long time = System.currentTimeMillis();
		System.out.println("Load Start");
		Level level = new Level();
		Refiner.refine(Paths.get(fileName)).stream()
		.filter(Levelable::typeOf)
		.map(Levelable::cast)
		.forEach(level::levelify);
		level.on(new Events.Load());
		System.out.println("Load end:"+(System.currentTimeMillis()-time));
		return level;
	}
	public void levelify(Levelable leveler){
		leveler.level(this);
	}


}
