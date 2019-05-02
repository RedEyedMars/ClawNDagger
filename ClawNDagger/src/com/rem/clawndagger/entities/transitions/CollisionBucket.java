package com.rem.clawndagger.entities.transitions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.rem.clawndagger.entities.motion.BooleanBox;
import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.interfaces.Collisionable;
import com.rem.clawndagger.interfaces.Moveable;
import com.rem.clawndagger.levels.Level;

public class CollisionBucket implements Collisionable {

	private Rectangle collision = null;
	private Level level;
	private BooleanBox edges;
	private List<Moveable> collisions = new ArrayList<Moveable>();
	

	public CollisionBucket(){
	}
	public CollisionBucket dimensions(double x, double y, double width, double height){
		this.collision = new Rectangle(x,y,width,height);
		this.edges = BooleanBox.find(false,false,false,false);
		return this;
	}
	public CollisionBucket level(Level level){
		this.level = level;
		this.level.add(this);
		return this;
	}
	public BooleanBox getEdges(){
		return edges;
	}
	@Override
	public Rectangle getRect() {
		return collision;
	}
	public Boolean clear() {
		this.collisions.clear();
		return true;
	}
	public Boolean add(Moveable m) {
		return collisions.add(m);
	}
	public Boolean contains(Moveable m){
		return collisions.contains(m);
	}
	@SuppressWarnings("unchecked")
	public <Type extends Moveable> Boolean couldFind(Predicate<Type> test){
		return collisions.parallelStream().map(M->(Type)M).anyMatch(test);
	}
}
