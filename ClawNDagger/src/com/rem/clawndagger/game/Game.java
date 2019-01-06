package com.rem.clawndagger.game;

import com.rem.clawndagger.entities.Entity;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.graphics.Gui;
import com.rem.clawndagger.graphics.InputHandler;
import com.rem.clawndagger.graphics.InputHandler.Events.KeyboardEvent.Listener;
import com.rem.clawndagger.graphics.InputHandler.Events.MouseEvent.Drag;
import com.rem.clawndagger.graphics.InputHandler.Events.MouseEvent.Move;
import com.rem.clawndagger.graphics.InputHandler.Events.MouseEvent.Press;
import com.rem.clawndagger.graphics.InputHandler.Events.MouseEvent.Release;
import com.rem.clawndagger.levels.Level;
import com.rem.clawndagger.levels.village.Village;

public class Game implements InputHandler.Events.KeyboardEvent.Listener, InputHandler.Events.MouseEvent.Listener {

	public static final double gravity = 1.0;
	protected Double lastTick = System.currentTimeMillis()/1000.0;
	protected Double thisTick = lastTick;
	protected Level focus = new Village();
	protected Entity hero = null;
	protected boolean[] pressedKeys = new boolean[256];
	public void load() {

		focus.on(new Events.Draw.Focus());
		InputHandler.addKeyboardListener(this);
		InputHandler.addMouseListener(this);
	}

	public void update() {
		thisTick=System.currentTimeMillis()/1000.0;
		focus.on(new Events.Tick(thisTick));
		if(pressedKeys[1]){
			Gui.isRunning=false;
		}
		if(pressedKeys[17]){
			Gui.renderer.moveView(0.0f,-0.01f);
		}
		if(pressedKeys[30]){
			Gui.renderer.moveView(0.01f,0.0f);
		}
		if(pressedKeys[31]){
			Gui.renderer.moveView(0.0f,0.01f);
		}
		if(pressedKeys[32]){
			Gui.renderer.moveView(-0.01f,0.0f);
		}
		Gui.renderer.render();
		lastTick=thisTick;
	}


	public void listen(InputHandler.Events.KeyboardEvent.Release event){
		pressedKeys[event.getKeyInt()]=false;
	}
	public void listen(InputHandler.Events.KeyboardEvent.Press event){
		pressedKeys[event.getKeyInt()]=true;
	}
	public void listen(InputHandler.Events.MouseEvent.Release event){
	}
	public void listen(InputHandler.Events.MouseEvent.Press event){
	}
	public void listen(InputHandler.Events.MouseEvent.Drag event){
	}
	public void listen(InputHandler.Events.MouseEvent.Move event){
	}

}
