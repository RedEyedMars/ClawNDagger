package com.rem.clawndagger.entities.motion;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.rem.clawndagger.levels.Collisionable;

public class Motion extends Position{

	private Position speed = new Position(0,0);
	private Acceleration acceleration = new Acceleration(0,0);
	private Set<Predicate<Collisionable>> collisionListeners = new HashSet<Predicate<Collisionable>>();
	public Motion(double x, double y) {
		super(x, y);
	}
	public Boolean next(double t) {
		x = x + speed.getX()*t;
		y = y + speed.getY()*t;
		speed.x = speed.x + acceleration.getX()*t;
		speed.y = speed.y + acceleration.getY()*t;
		acceleration.next(t);
		return true;
	}
	public Boolean next(Position position, double t){
		position.x = x + speed.getX()*t;
		position.y = y + speed.getY()*t;
		return true;
	}
	public Boolean collide(Collisionable collider){
		collisionListeners = collisionListeners.parallelStream().filter(C->C.test(collider)).collect(Collectors.toSet());
		return true;
	}
	public Boolean addCollisionListener(Predicate<Collisionable> listener) {
		return this.collisionListeners .add(listener);
	}
	public Boolean removeCollisionListener(Predicate<Collisionable> listener) {
		return this.collisionListeners .remove(listener);
	}
}
