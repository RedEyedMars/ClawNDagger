package com.rem.clawndagger.graphics.images;

import java.nio.FloatBuffer;

import com.rem.clawndagger.entities.motion.Position;

public class ImageTemplate {



	public static ImageTemplate CLAWMENT_BASE = new ImageTemplate();

	private int texture;
	private FloatBuffer[][] textureBuffer;
	private int flipped_texture;

	private FloatBuffer[][] flipped_textureBuffer;

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
