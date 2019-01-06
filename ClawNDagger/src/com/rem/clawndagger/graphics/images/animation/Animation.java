package com.rem.clawndagger.graphics.images.animation;

import java.util.stream.IntStream;

import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.graphics.Renderer;
import com.rem.clawndagger.graphics.images.Image;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Tickable;

public abstract class Animation <UpdateType> implements Tickable, Drawable, Drawable.Focusable {
	protected Image[][] flipBooks;
	protected int index = 0;
	protected int flipBookIndex = 0;
	protected int startIndex = 0;
	protected int endIndex = 0;
	protected int direction = 1;
	protected double duration = 1.0;
	protected double current = 0;
	protected Renderer.Layer layer;
	
	public Animation(Renderer.Layer layer, Position position, ImageTemplate template, int[]...positions) {
		this.layer = layer;
		this.flipBooks = new Image[positions.length][];
		IntStream.range(0,positions.length).forEach(I->{
			flipBooks[I] = new Image[positions[I].length/2];
			IntStream.range(0, positions[I].length/2).forEach(J->
			  flipBooks[I][J*2]=(Image) template.create(position,positions[I][J*2],positions[I][J*2+1]));});
	}
	public Boolean on(Events.Tick tick){
		current += tick.get();
		while(current>=duration){
			index+=direction;
			if(index==endIndex){
				onEndReached();
			}
			current-=duration;
		}
		return true;
	}

	public Boolean on(Events.Draw draw){
		return flipBooks[flipBookIndex][index].on(draw);
	}
	public void onEndReached(){
		direction *= -1;
		int temp = endIndex;
		endIndex = startIndex;
		startIndex = temp;
	}
	public int getTexture(){
		return flipBooks[0][0].getTexture();
	}
	public Boolean on(Events.Draw.Focus focus) {
		layer.add(this);
		return true;
	}
	public Boolean on(Events.Draw.Unfocus focus) {
		layer.remove(this);
		return true;
	}
	public abstract Boolean update(UpdateType type);
}
