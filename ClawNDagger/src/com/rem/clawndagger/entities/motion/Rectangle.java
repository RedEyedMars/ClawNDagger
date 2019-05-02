package com.rem.clawndagger.entities.motion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.rem.clawndagger.interfaces.Savable;

public class Rectangle extends Position implements Savable.Lineable {

	public Rectangle(double x, double y, double width, double height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}
	private double width;
	private double height;
	public Encroachment[] findCollision(Rectangle collision) {
		List<Encroachment> result = new ArrayList<Encroachment>();
		if(width>=collision.width){
			addXCollision(result,collision,Direction::mirror);
		}
		else {
			collision.addXCollision(result,this,Direction::self);
		}
		if(result.size()==0){
			return null;
		}
		int before = result.size();
		if(height>=collision.height){
			addYCollision(result,collision,Direction::mirror);
		}
		else {
			collision.addYCollision(result,this,Direction::self);
		}
		if(before==result.size()){
			return null;
		}
		return result.toArray(new Encroachment[0]);

	}
	private void addXCollision(List<Encroachment> result, Rectangle collision, Function<Direction,Direction> getMethod) {
		if(collision.getX() + collision.width>=getX()&&collision.getX() + collision.width<=getX()+width){
			result.add(new Encroachment(getMethod.apply(Direction.right),collision.getX()-collision.width-getX()));
		}
		if(collision.getX()<=getX() + width&&collision.getX()>=getX()){
			result.add(new Encroachment(getMethod.apply(Direction.left),collision.getX()-getX()-width));
		}
	}
	private void addYCollision(List<Encroachment> result, Rectangle collision, Function<Direction,Direction> getMethod) {
		if(collision.getY()+collision.height>=getY()&&collision.getY()+collision.height<=getY()+height){
			result.add(new Encroachment(getMethod.apply(Direction.up),collision.getY()+collision.height-getY()));
		}
		if(collision.getY()<=getY()+height&&collision.getY()>=getY()){
			result.add(new Encroachment(getMethod.apply(Direction.down),collision.getY()-getY()-height));
		}
	}
	public double right(){
		return getX() + width;
	}
	public double top(){
		return getY() + height;
	}
	public double left() {
		return getX();
	}
	public double bottom() {
		return getY();
	}
	public double getWidth(){
		return width;
	}
	public double getHeight(){
		return height;
	}
	public void setWidth(double width){
		this.width = width;
	}
	public void setHeight(double height){
		this.height = height;
	}
	public static void rectifyCollision(
			Rectangle collisionDimensions1, Rectangle collisionDimensions2,
			BooleanBox collisionEdges1,	BooleanBox collisionEdges2,
			Encroachment[] ofAttack,
			Motion motion) {
		if(ofAttack.length>0){
			Stream.of(ofAttack).forEach(E->System.out.println(E.direction.getName()));
			//System.out.println("---");
			if(ofAttack.length==2) {
				if(ofAttack[0].direction==Direction.right&&ofAttack[1].direction==Direction.down){
					adjustRectangleForCollision(
							collisionDimensions2,motion,
							collisionDimensions2.right()-collisionDimensions1.left(),
							collisionDimensions2.bottom()-collisionDimensions1.top(),
							collisionDimensions1.left()-collisionDimensions2.getWidth(),
							collisionDimensions1.top(),
							collisionEdges2.getRight()==true&&collisionEdges1.getLeft()==true,
							collisionEdges2.getDown()==true&&collisionEdges1.getUp()==true
							);
				}
				else if(ofAttack[0].direction==Direction.left&&ofAttack[1].direction==Direction.down){
					adjustRectangleForCollision(
							collisionDimensions2,motion,
							collisionDimensions2.left()-collisionDimensions1.right(),
							collisionDimensions2.bottom()-collisionDimensions1.top(),
							collisionDimensions1.right(),
							collisionDimensions1.top(),
							collisionEdges2.getLeft()==true&&collisionEdges1.getRight()==true,
							collisionEdges2.getDown()==true&&collisionEdges1.getUp()==true
							);
				}
				else if(ofAttack[0].direction==Direction.right&&ofAttack[1].direction==Direction.up){
					adjustRectangleForCollision(
							collisionDimensions2,motion,
							collisionDimensions2.right()-collisionDimensions1.left(),
							collisionDimensions2.top()-collisionDimensions1.bottom(),
							collisionDimensions1.left()-collisionDimensions2.getWidth(),
							collisionDimensions1.bottom()-collisionDimensions2.getHeight(),
							collisionEdges2.getRight()==true&&collisionEdges1.getLeft()==true,
							collisionEdges2.getUp()==true&&collisionEdges1.getDown()==true
							);
				}
				else if(ofAttack[0].direction==Direction.left&&ofAttack[1].direction==Direction.up){
					adjustRectangleForCollision(
							collisionDimensions2,motion,
							collisionDimensions2.left()-collisionDimensions1.right(),
							collisionDimensions2.top()-collisionDimensions1.bottom(),
							collisionDimensions1.right(),
							collisionDimensions1.bottom()-collisionDimensions2.getHeight(),
							collisionEdges2.getLeft()==true&&collisionEdges1.getRight()==true,
							collisionEdges2.getUp()==true&&collisionEdges1.getDown()==true
							);
				}
				else {
					System.err.println("ERRRORRRRRR: in terrain collision1"+ofAttack[0].direction.getName()+":"+ofAttack[1].direction.getName());
				}
			}
			else {
				if(ofAttack[1].direction==Direction.left||ofAttack[1].direction==Direction.right){
					if(ofAttack[2].direction==Direction.down){
						if(collisionEdges1.getUp()&&collisionEdges2.getDown()){
							collisionDimensions2.setY(collisionDimensions1.top());
							if(motion.getY()<0){
								motion.setY(0.0);
							}
						}
					}
					else if(ofAttack[2].direction==Direction.up){
						if(collisionEdges1.getDown()&&collisionEdges2.getUp()){
							collisionDimensions2.setY(collisionDimensions1.bottom()-collisionDimensions2.getHeight());
							if(motion.getY()>0){
								motion.setY(0.0);
							}
						}
					}
				}
				else if((ofAttack[1].direction==Direction.up&&ofAttack[2].direction==Direction.down)||(ofAttack[2].direction==Direction.up&&ofAttack[1].direction==Direction.down)){
					if(ofAttack[0].direction==Direction.right){
						if(collisionEdges1.getLeft()&&collisionEdges2.getRight()){
							collisionDimensions2.setX(collisionDimensions1.left()-collisionDimensions2.getWidth());
							if(motion.getX()>0){
								motion.setX(0.0);
							}
						}
					}
					else if(ofAttack[0].direction==Direction.left){
						if(collisionEdges1.getRight()&&collisionEdges2.getLeft()){
							collisionDimensions2.setX(collisionDimensions1.right());
							if(motion.getX()<0){
								motion.setX(0.0);
							}
						}
					}
					else {
						System.err.println("ERRRORRRRRR: in terrain collision2");
					}
				}
				else {
					System.err.println("ERRRORRRRRR: in terrain collision3");
				}
			}
			System.out.println(collisionDimensions1.getY()+"///"+collisionDimensions2.getY());
		}
	}
	public static void adjustRectangleForCollision(Rectangle affectedRectangle,
			Motion affectedMotion,
			double differenceX, double differenceY,
			double ifX, double ifY,
			boolean Xedge, boolean Yedge){
		if(Math.abs(differenceX)>=Math.abs(differenceY)){
			if(Yedge){
				affectedRectangle.setY(ifY);
				if(affectedMotion.getY()<0){
					affectedMotion.setY(0);
				}
			}
		}
		else {
			if(Xedge){
				affectedRectangle.setX(ifX);
				if(affectedMotion.getX()>0){
					affectedMotion.setX(0);
				}
			}
		}
	}
	@Override
	public String getSaveLine() {
		return Savable.concat("dimensions",getX(),getY(),width,height);
	}
}
