package com.rem.clawndagger.graphics.images;

import java.nio.FloatBuffer;

import com.rem.clawndagger.entities.motion.Position;

public class ImageTemplate {



	public static final ImageTemplate CLAWMENT_BASE_2 = new ImageTemplate("./res/entities/BaseClawment.png");

	public static final ImageTemplate CLAWMENT_BASE = new ImageTemplate("./res/entities/BaseClawment2.png");

	private int texture;
	private FloatBuffer[][] textureBuffer;
	private int flipped_texture;

	private FloatBuffer[][] flipped_textureBuffer;

	private String fileName;
	public ImageTemplate(String fileName){
		this.fileName = fileName;
	}
	public String getFileName(){
		return fileName;
	}
	public Image create(Position position, int x, int y) {
		return new Image(textureBuffer[x][y],texture,position);
	}
	public Image createFlipped(Position position, int x, int y) {
		return new Image(flipped_textureBuffer[x][y],flipped_texture,position);
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
}
