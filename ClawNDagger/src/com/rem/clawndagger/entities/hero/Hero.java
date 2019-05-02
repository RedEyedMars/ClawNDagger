package com.rem.clawndagger.entities.hero;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.rem.clawndagger.entities.Turtle;
import com.rem.clawndagger.file.manager.Refiner;
import com.rem.clawndagger.game.Game;
import com.rem.clawndagger.graphics.InputHandler;
import com.rem.clawndagger.graphics.InputHandler.Events.KeyboardEvent.Press;
import com.rem.clawndagger.graphics.InputHandler.Events.KeyboardEvent.Release;
import com.rem.clawndagger.levels.Level;

@SuppressWarnings("unused")
public class Hero extends Turtle<Hero> implements InputHandler.Events.KeyboardEvent.Listener {

	private static Map<Integer,Consumer<Hero>> keyPresses = new HashMap<Integer,Consumer<Hero>>();
	private static Map<Integer,Consumer<Hero>> keyReleases = new HashMap<Integer,Consumer<Hero>>();
	static {
		if(Game.gravity!=0.0){
			keyPresses.put(203,Hero::walkLeft);
			keyPresses.put(205,Hero::walkRight);
			keyPresses.put(200,Hero::jump);
		}
		else {
			keyPresses.put(203,Hero::walkLeft);
			keyPresses.put(205,Hero::walkRight);
			keyPresses.put(200,Hero::walkUp);
			keyPresses.put(208,Hero::walkDown);
		}
		keyReleases.put(203,Hero::idleHorizontal);
		keyReleases.put(205,Hero::idleHorizontal);
		keyReleases.put(200,Hero::idleVertical);
		keyReleases.put(208,Hero::idleVertical);
	};
	
	public Hero() {
		System.out.println("Made Hero");
	}
	
	public Hero level(Level level){
		super.level(level);
		this.level.setHero(this);
		return this;
	}

	@Override
	public void listen(Press event) {
		if(keyPresses.containsKey(event.getKeyInt())){
			keyPresses.get(event.getKeyInt()).accept(this);
		}
	}

	@Override
	public void listen(Release event) {
		if(keyReleases.containsKey(event.getKeyInt())){
			keyReleases.get(event.getKeyInt()).accept(this);
		}
	}
}
