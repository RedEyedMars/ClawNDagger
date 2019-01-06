package com.rem.clawndagger.levels;

import com.rem.clawndagger.entities.motion.Motion;

public interface Collisionable {

	boolean hasCollision(Motion motion, double t);
	boolean rectifyCollision(Motion motion, double t);

	
}
