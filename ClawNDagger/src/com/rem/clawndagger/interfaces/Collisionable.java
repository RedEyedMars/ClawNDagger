package com.rem.clawndagger.interfaces;

import com.rem.clawndagger.entities.motion.BooleanBox;
import com.rem.clawndagger.entities.motion.Encroachment;
import com.rem.clawndagger.entities.motion.Rectangle;

public interface Collisionable {

	//public Bump findCollision(Rectangle rect);
	//public Boolean rectifyCollision(Rectangle oldRectangle, Encroachment[] ofAttack, Motion motion);
	public Rectangle getRect();
	public BooleanBox getEdges();
	public static class Bump {
		public Collisionable bumper;
		public Encroachment[] solution;

		public Bump(Collisionable bumper, Encroachment[] result){
			this.bumper = bumper;
			this.solution = result;
		}
	}
	/*
	public boolean rectifyCollision(Motion p1, Position p2);
	public boolean hasCollision(Rectangle rect, Motion m);
	*/
}
