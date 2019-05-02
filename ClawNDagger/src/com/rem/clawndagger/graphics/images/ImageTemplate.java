package com.rem.clawndagger.graphics.images;

import java.nio.FloatBuffer;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.interfaces.Nameable;

public enum ImageTemplate implements Nameable{
	CLAWMENT_BASE("./res/entities/BaseClawment.png"),
	CLAWMENT_BASE_2("./res/entities/BaseClawment2.png"),
	LIL_RAT("./res/entities/LilRat.png"),

	BLUE_SKY("./res/terrain/BlueSky.png"),
	SQUARE("./res/misc/Square.png")

	;
	
	private int texture;
	private FloatBuffer[][] textureBuffer;
	private int flipped_texture;
	private FloatBuffer[][] flipped_textureBuffer;

	private String fileName;

	private double visualHeight = 1;

	private double visualWidth = 1;
	
	private ImageTemplate(String fileName){
		this.fileName = fileName;
	}
	public String getFileName(){
		return fileName;
	}
	public Image create(Rectangle dimensions, int x, int y) {
		return new Image(textureBuffer[x][y],texture,new Rectangle(0.0,0.0,0.0,0.0){
			public double getX(){
				return dimensions.getX();
			}
			public double getY(){
				return dimensions.getY();
			}
			public double getWidth(){
				return visualWidth;
			}
			public double getHeight(){
				return visualHeight;
			}
		});
	}
	public Image createFlipped(Rectangle dimensions, int x, int y) {
		return new Image(flipped_textureBuffer[x][y],flipped_texture,new Rectangle(0.0,0.0,0.0,0.0){
			public double getX(){
				return dimensions.getX();
			}
			public double getY(){
				return dimensions.getY();
			}
			public double getWidth(){
				return visualWidth;
			}
			public double getHeight(){
				return visualHeight;
			}
		});
	}

	public void setTexture(int i) {
		this.texture = i;
	}

	public void setTextureBuffer(FloatBuffer[][] textureBuffer) {
		this.textureBuffer = textureBuffer;
	}
	public void setFlippedTexture(int i) {
		this.flipped_texture = i;
	}

	public void setFlippedTextureBuffer(FloatBuffer[][] textureBuffer) {
		this.flipped_textureBuffer = textureBuffer;
	}
	public void setVisualPixelDimensions(double width, double height) {
		this.visualWidth = width/320;
		this.visualHeight = height/320;
		
	}
	public String getName() {
		return super.toString();
	}
}
