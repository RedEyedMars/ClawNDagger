package com.rem.clawndagger.levels.village;

import com.rem.clawndagger.entities.enemies.Enemy;
import com.rem.clawndagger.entities.hero.Hero;
import com.rem.clawndagger.entities.transitions.EntityTransitions;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.levels.Backdrop;
import com.rem.clawndagger.levels.Level;
import com.rem.clawndagger.levels.Terrain;
import com.rem.clawndagger.tests.Tests;

public class Village extends Level{

	public Village(){
		hero = new Hero().dimensions(0.05, 0.05, 0.051, 0.051)
						 .bound(true, true, true, true)
						 .animation(ImageTemplate.CLAWMENT_BASE)
						 .level(this);
		hero.setName("hero");
		Enemy enemy1 = new Enemy().dimensions(0.5, 0.5, 0.05, 0.05)
	 	   .bound(true, true, true, true)
	       .animation(ImageTemplate.LIL_RAT)
	       //.setPatrol(
	    		   //new Enemy.Patrol.HorizontalBackAndForth(1.0))
	       .level(this);
		enemy1.setName("rat1");
		enemy1.setTransitions(new EntityTransitions.Normal.Triggered.Charge.Enemy().set_standard(
				enemy1,
				new Enemy.Patrol.HorizontalBackAndForth().period(0.015).set(enemy1),
				-0.2,0.0,
				5.0));
		Terrain terrain1 = new Terrain().dimensions(0.4,0.0,0.2,0.2)
				 .bound(true, true, true, true)
				 .animation(ImageTemplate.CLAWMENT_BASE_2)
				 .level(this);
		terrain1.setName("terrain1");
		add(terrain1);
		Backdrop backdrop1 = new Backdrop().dimensions(4,1)
				  .animation(ImageTemplate.BLUE_SKY)
				  .level(this);
		backdrop1.setName("backdrop1");
		add(backdrop1);
	}
}
