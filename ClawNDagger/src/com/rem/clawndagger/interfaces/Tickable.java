package com.rem.clawndagger.interfaces;

import com.rem.clawndagger.game.events.Events;

public interface Tickable {
	Boolean on(Events.Tick tick);
}
