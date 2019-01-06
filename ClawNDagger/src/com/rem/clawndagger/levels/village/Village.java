package com.rem.clawndagger.levels.village;

import com.rem.clawndagger.entities.hero.Hero;
import com.rem.clawndagger.levels.Level;

public class Village extends Level {

	public Village(){
		add(new Hero(0,0,this));
	}
}
