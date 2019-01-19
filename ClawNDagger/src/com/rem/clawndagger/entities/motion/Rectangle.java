package com.rem.clawndagger.entities.motion;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Position {

	public Rectangle(double x, double y, double width, double height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}
	public double width;
	public double height;
	public Encroachment[] findCollision(Rectangle collision) {

		List<Encroachment> result = new ArrayList<Encroachment>();
		if(width>=collision.width){
			addXCollision(result,collision);
		}
		else {
			collision.addXCollision(result,this);
		}
		if(result.size()==0){
			return null;
		}
		int before = result.size();
		if(height>=collision.height){
			addYCollision(result,collision);
		}
		else {
			collision.addYCollision(result,this);
		}
		if(before==result.size()){
			return null;
		}
		return result.toArray(new Encroachment[0]);
		
	}
	private void addXCollision(List<Encroachment> result, Rectangle collision) {
		if(collision.x + collision.width>=x&&collision.x + collision.width<=x+width){
			result.add(new Encroachment(Direction.right,collision.x-collision.width-x));
		}
		if(collision.x<=x + width&&collision.x>=x){
			result.add(new Encroachment(Direction.left,collision.x-x-width));
		}
	}
	private void addYCollision(List<Encroachment> result, Rectangle collision) {
		if(collision.y+collision.height>=y&&collision.y+collision.height<=y+height){
			result.add(new Encroachment(Direction.up,collision.y+collision.height-y));
		}
		if(collision.y<=y+height&&collision.y>=y){
			result.add(new Encroachment(Direction.down,collision.y-y-height));
		}
	}
	public double right(){
		return x + width;
	}
	public double top(){
		return y + height;
	}
	public double left() {
		return x;
	}
	public double bottom() {
		return y;
	}
}
