package com.rem.clawndagger.interfaces;

import com.rem.clawndagger.levels.Level;

public interface Levelable {
	public Levelable level(Level level);
	public static Boolean typeOf(Object object){
		return object instanceof Levelable;
	}
	public static Levelable cast(Object object){
		return (Levelable)object;
	}
}
