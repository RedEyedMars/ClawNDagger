package com.rem.clawndagger.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.interfaces.Tickable;

public class EntityTransition<TurtleType extends Turtle<?>> implements Tickable {

	private static Supplier<Boolean> NONE = new Supplier<Boolean>(){
		@Override
		public Boolean get() {
			return false;
		}};
	private TurtleType entity;
	private BiFunction<TurtleType,Events.Tick,Boolean> onTick;
	private Map<Supplier<Boolean>,EntityTransition<TurtleType>> transitions = new HashMap<Supplier<Boolean>, EntityTransition<TurtleType>>();
	
	public EntityTransition<TurtleType> create(
			TurtleType currentEntity,
			BiFunction<TurtleType,Events.Tick,Boolean> onTick){
		this.entity = currentEntity;
		this.onTick = onTick;
		return this;
	}
	public EntityTransition<TurtleType> add(
			Supplier<Boolean> condition,
			EntityTransition<TurtleType> toState){
		transitions.put(condition, toState);
		return this;
	}

	@Override
	public Tickable on(Events.Tick tick) {
		EntityTransition<TurtleType> result = transitions.get(transitions.keySet().parallelStream().filter(Supplier::get).findAny().orElse(NONE));
		if(result!=null){
			result.onTick.apply(entity,tick);
			return result;
		}
		else {
			this.onTick.apply(entity,tick);
			return this;
		}
	}
	
	
}
