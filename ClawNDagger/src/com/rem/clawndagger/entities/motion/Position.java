package com.rem.clawndagger.entities.motion;

import static java.lang.Math.abs;


public class Position {


	  public static final double EPS = 1e-15;
	private double x;
	private double y;
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object pt) {
	      return abs(x - ((Position)pt).x) < EPS && abs(y - ((Position)pt).y) < EPS;
	    }
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
}
