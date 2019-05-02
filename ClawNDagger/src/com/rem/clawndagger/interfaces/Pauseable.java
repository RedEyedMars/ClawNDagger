package com.rem.clawndagger.interfaces;

import com.rem.clawndagger.game.events.Events;

public interface Pauseable extends Tickable{
	public Boolean on(Events.Pause pause);
	public Boolean on(Events.Unpause unpause);
	@Override
	public Tickable on(Events.Tick t); 
}
