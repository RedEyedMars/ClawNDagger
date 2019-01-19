package com.rem.clawndagger.entities.motion;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.rem.clawndagger.levels.Collisionable;

public class Motion {

	public Position s = new Position(0,0);
	public Acceleration a = new Acceleration(0,0);
	private Set<Predicate<Collisionable>> collisionListeners = new HashSet<Predicate<Collisionable>>();
	private Position leftBound;
	private Position rightBound;
	
	public Boolean next(Position position, double t){
		//System.out.println(s.x);
		position.x = position.x + s.x*t;
		position.y = position.y + s.y*t;
		s.x = s.x + a.x*t;
		s.y = s.y + a.y*t;
		a.next(t);
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
	public double getAngleToAcceleration() {
		if(a.x==0){
			return  -Math.PI/2;
		}
		else {
			return Math.atan2(a.y, a.x);
		}
	}
	public double getAngleToSpeed() {
		if(s.x==0){
			if(s.y>=0){
				return  Math.PI/2;
			}
			else {
				return  -Math.PI/2;
			}
		}
		else {
			return Math.atan2(s.y, s.x);
		}
	}
	public void mustProceed(Position left, Position right) {
		leftBound = left;
		rightBound = right;
	}
}
