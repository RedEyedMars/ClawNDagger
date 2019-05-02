package com.rem.clawndagger.entities.motion;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.rem.clawndagger.interfaces.Collisionable;

public class Motion extends Position {

	
	public Acceleration a = new Acceleration(0,0);
	private Set<Predicate<Collisionable>> collisionListeners = new HashSet<Predicate<Collisionable>>();
	public Motion(double x, double y) {
		super(x, y);
	}
	public Rectangle next(Rectangle origin, double t){
		Rectangle nextRectangle = new Rectangle(origin.getX(),origin.getY(),origin.getWidth(),origin.getHeight());
		nextRectangle.setX(nextRectangle.getX() + getX()*t);
		nextRectangle.setY(nextRectangle.getY() + getY()*t);
		setX(getX() + a.getX()*t);
		setY(getY() + a.getY()*t);
		a.next(this,t);
		return nextRectangle;
	}
	public Boolean next(Position nextRectangle, double t){
		nextRectangle.setX(nextRectangle.getX() + getX()*t);
		nextRectangle.setY(nextRectangle.getY() + getY()*t);
		setX(getX() + a.getX()*t);
		setY(getY() + a.getY()*t);
		a.next(this,t);
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
		if(a.getX()==0){
			return  -Math.PI/2;
		}
		else {
			return Math.atan2(a.getY(), a.getX());
		}
	}
	public double getAngleToSpeed() {
		if(getX()==0){
			if(getY()>=0){
				return  Math.PI/2;
			}
			else {
				return  -Math.PI/2;
			}
		}
		else {
			return Math.atan2(getY(), getX());
		}
	}
	public Boolean isMoving() {
		return Math.abs(getX())>0.000000001||Math.abs(getY())>0.000000001;
	}
}
