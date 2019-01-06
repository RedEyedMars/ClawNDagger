package com.rem.clawndagger.graphics.images.animation;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.graphics.images.Image;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Tickable;

public abstract class Animation <UpdateType> implements Tickable, Drawable {
	protected Image[][] flipBooks;
	protected int index = 0;
	protected int flipBookIndex = 0;
	protected int startIndex = 0;
	protected int endIndex = 0;
	protected int direction = 1;
	protected double duration = 1.0;
	protected double current = 0;
	public Animation(Position position, ImageTemplate[][] imageTemplates){
		this.flipBooks = new Image[imageTemplates.length][];
		IntStream.range(0,imageTemplates.length).forEach(I->{
			flipBooks[I] = new Image[imageTemplates[I].length];
			IntStream.range(0, imageTemplates[I].length).forEach(J->flipBooks[I][J]=(Image) imageTemplates[I][J].create(position));});
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
	public abstract Boolean update(UpdateType type);
}
