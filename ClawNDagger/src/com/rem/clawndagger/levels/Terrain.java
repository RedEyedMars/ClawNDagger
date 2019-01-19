package com.rem.clawndagger.levels;

import java.util.Arrays;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.Direction;
import com.rem.clawndagger.entities.motion.Encroachment;
import com.rem.clawndagger.entities.motion.Motion;
import com.rem.clawndagger.entities.motion.Position;
import com.rem.clawndagger.entities.motion.Rectangle;

public class Terrain implements Collisionable {

	private Rectangle collision = null;
	private Level level;

	public Terrain(Rectangle collision, Level level){
		this.collision = collision;
		this.level = level;
	}
	public Collisionable.Bump findCollision(Rectangle rect) {
		Encroachment[] result = rect.findCollision(this.collision);
		if(result == null){
			return null;
		}
		else {
			return new Collisionable.Bump(this, result);
		}
	}
	@Override
	public Boolean rectifyCollision(Rectangle oldRectangle, Encroachment[] ofAttack, Motion motion) {
		if(ofAttack.length>0){
			if(ofAttack.length==2) {

				Stream.of(ofAttack).forEach(E->System.out.println(E.direction.getName()));
				double angle = Math.abs(motion.getAngleToSpeed());
				System.out.println(angle);
				
				if(ofAttack[0].direction==Direction.right&&ofAttack[1].direction==Direction.down){
					if(angle>=Math.PI/4&&angle<=Math.PI*3/4){
						oldRectangle.y = collision.top();
						if(motion.s.y<0){
							motion.s.y = 0;
						}
					}
					else {
						oldRectangle.x = collision.left()-oldRectangle.width;
						if(motion.s.x>0){
							motion.s.x = 0;
						}
						
					}
				}
				else if(ofAttack[0].direction==Direction.left&&ofAttack[1].direction==Direction.down){
					if(angle>=Math.PI/4&&angle<=Math.PI*3/4){
						oldRectangle.y = collision.top();
						if(motion.s.y<0){
							motion.s.y = 0;
						}
					}
					else {
						oldRectangle.x = collision.right();
						if(motion.s.x<0){
							motion.s.x = 0;
						}
					}
				}
				else if(ofAttack[0].direction==Direction.right&&ofAttack[1].direction==Direction.up){
					if(angle>=Math.PI/4&&angle<=Math.PI*3/4){
						oldRectangle.y = collision.bottom()-oldRectangle.height;
						if(motion.s.y>0){
							motion.s.y = 0;
						}
					}
					else {
						oldRectangle.x = collision.left()-oldRectangle.width;
						if(motion.s.x>0){
							motion.s.x = 0;
						}
					}
				}
				else if(ofAttack[0].direction==Direction.left&&ofAttack[1].direction==Direction.up){
					if(angle>=Math.PI/4&&angle<=Math.PI*3/4){
						oldRectangle.y = collision.bottom()-oldRectangle.height;
						if(motion.s.y>0){
							motion.s.y = 0;
						}
					}
					else {
						oldRectangle.x = collision.right();
						if(motion.s.x<0){
							motion.s.x = 0;
						}
					}
				}
				else {
					System.err.println("ERRRORRRRRR: in terrain collision1"+ofAttack[0].direction.getName()+":"+ofAttack[1].direction.getName());
				}
			}
			else {
				if(ofAttack[1].direction==Direction.left){
					motion.s.y = 0.0;
					if(ofAttack[2].direction==Direction.down){
						oldRectangle.y = collision.top();
					}
					else if(ofAttack[2].direction==Direction.up){
						oldRectangle.y = collision.bottom()-oldRectangle.height;
					}
				}
				else if(ofAttack[1].direction==Direction.up&&ofAttack[2].direction==Direction.down){
					motion.s.x = 0.0;

					if(ofAttack[0].direction==Direction.right){
						oldRectangle.x = collision.left()-oldRectangle.width;	
					}
					else if(ofAttack[0].direction==Direction.left){
						oldRectangle.x = collision.right();
					}
					else {
						System.err.println("ERRRORRRRRR: in terrain collision2");
					}
				}
				else {
					System.err.println("ERRRORRRRRR: in terrain collision3");
				}
			}
		}
		motion.collide(this);
		return true;
	}
}
