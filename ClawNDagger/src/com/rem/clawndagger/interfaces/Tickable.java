package com.rem.clawndagger.interfaces;

import java.util.stream.Stream;

import com.rem.clawndagger.game.events.Events;

public interface Tickable {
	public Tickable on(Events.Tick tick);

	public static Stream.Builder<Tickable> builder(){
		return Stream.builder();
	}
}
