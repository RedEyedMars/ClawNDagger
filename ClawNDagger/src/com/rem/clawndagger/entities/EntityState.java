package com.rem.clawndagger.entities;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.Direction;

public class EntityState {
	public static enum Change {
		IDLE, WALK, JUMP, LAND, HP, DIE
	}
	public boolean dead = false;
	public boolean jumping = false;
	public boolean walking = false;
	public Direction facing = Direction.right;
	public int maxHp = 3;
	public int hp = maxHp;
	public Map<Change,List<BiFunction<EntityState,Integer,Boolean>>> listeners = new EnumMap<Change,List<BiFunction<EntityState,Integer,Boolean>>>(Change.class);

	public EntityState(){
		Stream.of(Change.values()).forEach(C->listeners.put(C,new ArrayList<BiFunction<EntityState,Integer,Boolean>>() ));
	}
	public boolean change(EntityState.Change change, int amount){
		switch(change){
		case HP:{
			if(hp+amount>maxHp){
				hp = maxHp;
			}
			else {
				if(hp+amount<=0){
					hp+=amount;
					dead = true;
					listeners.get(Change.DIE).parallelStream().forEach(L->L.apply(EntityState.this,amount));
				}
				else {
					hp+=amount;
				}
			}
			break;
		}
		case DIE:{break;}
		case JUMP:{ jumping = true;	 break; }
		case LAND:{ jumping = false; break; }
		case WALK:{ if(amount==1)facing = Direction.right;else facing = Direction.left; walking = true;	 break; }
		case IDLE:{	walking = false; break; }
		}
		listeners.get(change).parallelStream().forEach(L->L.apply(EntityState.this,amount));
		return true;
	}
	public boolean change(EntityState.Change change){
		return change(change,0);
	}

}
