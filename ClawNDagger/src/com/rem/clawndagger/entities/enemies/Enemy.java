package com.rem.clawndagger.entities.enemies;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.Turtle;
import com.rem.clawndagger.file.manager.Smelter;
import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.interfaces.Savable;
import com.rem.clawndagger.interfaces.Tickable;

public class Enemy extends Turtle<Enemy> {
	public Enemy() {
	}
	public static abstract class Patrol  implements Tickable, BiFunction<Enemy,Events.Tick,Boolean>, Savable {
		private static List<Consumer<Enemy>> movementMethods = Arrays.asList(
				Enemy::walkRight,
				Enemy::walkLeft);
		
		
		protected Enemy enemy = null;

		protected Double initialX;
		protected Double initialY;
		protected Double flipX;
		protected Double flipY;
		protected Double periodX;
		protected Double periodY;
		protected int movementMethodIndex;
		public Patrol set(Enemy enemy){
			this.enemy = enemy;
			this.enemy.addListener(new Events.Load(),E->{
				this.initialX = E.getRect().getX();
				this.initialY = E.getRect().getY();
				this.flipX = initialX+periodX;
				this.flipY = initialY+periodY;
				this.periodX*=2;
				this.periodY*=2;
			});
			return this;
		}
		public void init(double initialX, double initialY, 
				double flipX, double flipY, 
				double periodX, double periodY, 
				Enemy enemy, int movementMethodIndex){
			this.initialX = initialX;
			this.initialY = initialY;
			this.flipX = flipX;
			this.flipY = flipY;
			this.periodX=periodX;
			this.periodY=periodY;
			this.enemy = enemy;
			this.movementMethodIndex = movementMethodIndex;
		}
		public Patrol period(Double periodX, Double periodY){
			this.periodX = periodX;
			this.periodY = periodY;
			return this;
		}
		public Boolean apply(Enemy enemy, Events.Tick tick){
			on(tick);
			return true;
		}
		public Stream<Savable> getSubsideraties(){
			return Stream.of(this);
		}
		public List<Lineable> getSaveLines(){
			return Arrays.asList(
					()->Savable.concat("init",initialX,initialY,flipX,flipY,periodX,periodY,enemy.getName(),movementMethodIndex)
					);
		}
		public String getName(){
			return enemy.getName()+"_patrol";
		}
		
		public void setName(String name){}
		public static class HorizontalBackAndForth extends Patrol{
			private static Consumer<Enemy> walkRight = Enemy::walkRight;
			public HorizontalBackAndForth period(Double periodX){
				super.period(periodX,0.0);
				return this;
			}
			public HorizontalBackAndForth set(Enemy enemy){
				super.set(enemy);
				this.movementMethodIndex = 0;
				return this;
			}
			public Tickable on(Events.Tick tick){
				if(flipX>initialX&&enemy.getRect().getX()>flipX){
					this.movementMethodIndex = 1;
					flipX-=periodX;
				}
				else if(flipX<initialX&&enemy.getRect().getX()<flipX){
					this.movementMethodIndex = 0;
					flipX+=periodX;
				}
				movementMethods.get(movementMethodIndex).accept(enemy);
				return this;
			}
		}
	}



	
}