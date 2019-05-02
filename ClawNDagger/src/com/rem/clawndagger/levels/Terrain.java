package com.rem.clawndagger.levels;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.BooleanBox;
import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.game.events.Events.Load;
import com.rem.clawndagger.graphics.Renderer;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Collisionable;
import com.rem.clawndagger.interfaces.Drawable;
import com.rem.clawndagger.interfaces.Levelable;
import com.rem.clawndagger.interfaces.Loadable;
import com.rem.clawndagger.interfaces.Nameable;
import com.rem.clawndagger.interfaces.Savable;

public class Terrain implements Collisionable, Drawable.Focusable, Levelable, Savable, Loadable, Nameable.Settable {
	
	private Rectangle collision = null;
	private Level level;
	private Animation animation;
	private BooleanBox edges;
	private String name;

	public Terrain(){
	}
	public Terrain dimensions(double x, double y, double width, double height){
		this.collision = new Rectangle(x,y,width,height);
		return this;
	}
	public Terrain level(Level level){
		this.level = level;
		this.level.add(this);
		return this;
	}
	public Terrain animation(ImageTemplate image){

		animation = new Animation(Renderer.midLayer ,collision,image,
				new int[]{1,0});
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
	public Terrain bound(boolean left,boolean right, boolean up, boolean down){
		this.edges = BooleanBox.find(left,right,up,down);
		return this;
	}
	public BooleanBox getEdges(){
		return edges;
	}
	/*
	@Override
	public Collisionable.Bump findCollision(Rectangle rect) {
		return Rectangle.findCollisions(collision,rect,this);
	}
	@Override
	public Boolean rectifyCollision(Rectangle oldRectangle, Encroachment[] ofAttack, Motion motion) {
		Rectangle.rectifyCollision(collision,oldRectangle,ofAttack,motion);
		return true;
	}*/
	@Override
	public Rectangle getRect() {
		return collision;
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
		return Arrays.asList(
				collision,
				edges,
				animation
				);
	}
}
