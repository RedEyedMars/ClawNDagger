package com.rem.clawndagger.graphics;
public class Colours {
	public Colours (){
		super();
	}
	public static class Red extends Gui.Colour {
		public Float[] asFloatArray(){
			return new Float[]{1.0f,0.0f,0.0f};
		}
		public int[] asIntArray(){
			return new int[]{256,0,0};
		}
		public Integer getRedI(){
			return 256;
		}
		public Integer getGreenI(){
			return 0;
		}
		public Integer getBlueI(){
			return 0;
		}
		public Float getRedF(){
			return 1.0f;
		}
		public Float getGreenF(){
			return 0.0f;
		}
		public Float getBlueF(){
			return 0.0f;
		}
		public Red (){
			super();
		}
	}
	public static class Green extends Gui.Colour {
		public Float[] asFloatArray(){
			return new Float[]{0.0f,0.0f,1.0f};
		}
		public int[] asIntArray(){
			return new int[]{0,0,256};
		}
		public Integer getRedI(){
			return 0;
		}
		public Integer getGreenI(){
			return 0;
		}
		public Integer getBlueI(){
			return 256;
		}
		public Float getRedF(){
			return 0.0f;
		}
		public Float getGreenF(){
			return 0.0f;
		}
		public Float getBlueF(){
			return 1.0f;
		}
		public Green (){
			super();
		}
	}
	public static class Blue extends Gui.Colour {
		public Float[] asFloatArray(){
			return new Float[]{0.0f,1.0f,0.0f};
		}
		public int[] asIntArray(){
			return new int[]{0,256,0};
		}
		public Integer getRedI(){
			return 0;
		}
		public Integer getGreenI(){
			return 256;
		}
		public Integer getBlueI(){
			return 0;
		}
		public Float getRedF(){
			return 0.0f;
		}
		public Float getGreenF(){
			return 1.0f;
		}
		public Float getBlueF(){
			return 0.0f;
		}
		public Blue (){
			super();
		}
	}
}
