package com.rem.clawndagger.game;

import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.graphics.Gui;
import com.rem.clawndagger.graphics.InputHandler;
import com.rem.clawndagger.levels.Level;
import com.rem.clawndagger.levels.village.Village;

public class Game implements InputHandler.Events.KeyboardEvent.Listener, InputHandler.Events.MouseEvent.Listener {

	public static final double gravity = 0.00;
	protected Double lastTick = System.currentTimeMillis()/1000.0;
	protected Double thisTick = lastTick;
	protected Level focus = null;
	protected boolean[] pressedKeys = new boolean[256];
	public void load() {
		focus = Level.load("./res/load");
		//focus = new Village();focus.on(new Events.Load());
		focus.on(new Events.Draw.Focus());
		InputHandler.Listeners.keyboard.add(this);
		InputHandler.Listeners.keyboard.add(focus.getHero());
		InputHandler.Listeners.mouse.add(this);
	}

	public void update() {
		thisTick=System.currentTimeMillis()/1000.0;
		focus.on(new Events.Tick(thisTick-lastTick));
		if(pressedKeys[1]){
			Gui.isRunning=false;
		}
		
		Gui.renderer.render();
		lastTick=thisTick;
	}


	public void listen(InputHandler.Events.KeyboardEvent.Release event){
		pressedKeys[event.getKeyInt()]=false;
		//System.out.println("RELEASE");
	}
	public void listen(InputHandler.Events.KeyboardEvent.Press event){
		pressedKeys[event.getKeyInt()]=true;
		//System.out.println("RELEASE");
	}
	public void mouseRelease(InputHandler.Events.MouseEvent event){
	}
	public void mousePress(InputHandler.Events.MouseEvent event){
	}
	public void mouseDrag(InputHandler.Events.MouseEvent event){
	}
	public void mouseMove(InputHandler.Events.MouseEvent event){
	}

}
