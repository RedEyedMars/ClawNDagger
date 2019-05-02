package com.rem.clawndagger.levels;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Load;
import com.rem.clawndagger.graphics.Renderer;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Levelable;
import com.rem.clawndagger.interfaces.Loadable;
import com.rem.clawndagger.interfaces.Nameable;
import com.rem.clawndagger.interfaces.Savable;

public class Backdrop implements Drawable.Focusable, Levelable, Savable, Loadable, Nameable.Settable {
	protected Animation animation = null;
	private Rectangle dimensions; 
	private Level level;
	private String name;
	public Backdrop(){
	}
	public Backdrop level(Level level){
		this.level = level;
		this.level.add(this);
		return this;
	}
	public Backdrop dimensions(double width, double height){
		this.dimensions = new Rectangle(0,0,width,height);
		return this;
	}
	public Backdrop animation(ImageTemplate image){
		animation = new Animation(Renderer.botLayer ,dimensions,image,
				new int[]{0,0});
		return this;
	}
	@Override
	public Boolean on(Events.Draw.Focus focus) {
		return animation.on(focus);
	}
	@Override
	public Boolean on(Events.Draw.Unfocus focus) {
		return animation.on(focus);
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public Boolean on(Load load) {
		return true;
	}
	@Override
	public List<Lineable> getSaveLines() {
		return List.of(
				(Lineable)()->Savable.concat(
						"dimensions",dimensions.getWidth(),dimensions.getHeight()),
				animation
				);
	}
}
