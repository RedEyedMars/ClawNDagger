package com.rem.clawndagger.entities.motion;

import static java.lang.Math.abs;


public class Position {


	  public static final double EPS = 1e-15;
	public double x;
	public double y;
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object pt) {
	      return abs(x - ((Position)pt).x) < EPS && abs(y - ((Position)pt).y) < EPS;
	    }
}
