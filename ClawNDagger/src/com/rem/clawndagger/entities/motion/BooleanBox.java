package com.rem.clawndagger.entities.motion;

import com.rem.clawndagger.interfaces.Savable;

public interface BooleanBox extends Savable.Lineable{
	public static final BooleanBox allTrue = new BooleanBox(){
		public boolean getLeft(){
			return true;
		}
		public boolean getRight(){
			return true;
		}
		public boolean getUp(){
			return true;
		}
		public boolean getDown(){
			return true;
		}
	};
	public static final BooleanBox allFalse = new BooleanBox(){
		public boolean getLeft(){
			return false;
		}
		public boolean getRight(){
			return false;
		}
		public boolean getUp(){
			return false;
		}
		public boolean getDown(){
			return false;
		}
	};
	public boolean getLeft();
	public boolean getRight();
	public boolean getUp();
	public boolean getDown();
	public static BooleanBox find(boolean left, boolean right, boolean up, boolean down) {
		if(left&&right&&up&&down){
			return allTrue;
		}
		else if(left||right||up||down){
			return new BooleanBox(){
				public boolean getLeft(){
					return left;
				}
				public boolean getRight(){
					return right;
				}
				public boolean getUp(){
					return up;
				}
				public boolean getDown(){
					return down;
				}
			};
		}
		else return allFalse;
	}
	
	@Override
	public default String getSaveLine() {
		return Savable.concat("bound",getLeft(),getRight(),getUp(),getDown());
	}
	
}
