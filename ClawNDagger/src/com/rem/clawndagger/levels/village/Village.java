package com.rem.clawndagger.levels.village;

import com.rem.clawndagger.entities.hero.Hero;
import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.levels.Level;
import com.rem.clawndagger.levels.Terrain;

public class Village extends Level {

	public Village(){
		hero = new Hero(ImageTemplate.CLAWMENT_BASE_2,0.5,0.5,this);
		add(hero);
		add(new Terrain(
				new Rectangle(0,0,0.4,0.4),this));
	}
}
