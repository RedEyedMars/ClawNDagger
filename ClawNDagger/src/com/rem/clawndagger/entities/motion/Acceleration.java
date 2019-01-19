package com.rem.clawndagger.entities.motion;

import com.rem.clawndagger.game.Game;

public class Acceleration extends Position {

	public Position player = new Position(0.00,0);
	private Position gravity = new Position(0.0,Game.gravity);
	public Position friction = new Position(0.0,0);
	public Acceleration(double x, double y) {
		super(0, y);
	}

	public void next(double t) {
		x = player.x+gravity.x+friction.x;
		y = friction.y+gravity.y+player.y;
	}

}
