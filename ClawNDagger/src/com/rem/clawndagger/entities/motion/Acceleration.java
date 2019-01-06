package com.rem.clawndagger.entities.motion;

import com.rem.clawndagger.game.Game;

public class Acceleration extends Position {

	public Acceleration(double x, double y) {
		super(x, y);
	}

	public void next(double t) {
		x = x < 0.0001?0.0:x/t;
		y = y < 0.0001?0.0:y/t;
	}

	public double getY(){
		return y - Game.gravity;
	}
}
