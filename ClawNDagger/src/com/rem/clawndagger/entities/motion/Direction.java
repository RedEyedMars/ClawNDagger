package com.rem.clawndagger.entities.motion;

public class Direction extends Position {

	public static final Direction right = new Direction("right",1.0,0.0);
	public static final Direction left = new Direction("left",-1.0,0.0);
	public static final Direction down = new Direction("down",0.0,-1.0);
	public static final Direction up = new Direction("up",0.0,1.0);
	static {
		right.mirror = left;
		left.mirror = right;
		up.mirror = down;
		down.mirror = up;
	}
	private String name;
	private Direction mirror;
	public Direction(String name, double x, double y) {
		super(x, y);
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Direction self(){
		return this;
	}
	public Direction mirror(){
		return mirror;
	}
}

