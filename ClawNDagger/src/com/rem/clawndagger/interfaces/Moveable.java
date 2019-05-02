package com.rem.clawndagger.interfaces;

import com.rem.clawndagger.game.events.Events;

public interface Moveable extends Collisionable {
	public Events.Movement getMovementEvent();
	public Boolean isMoving();
}
