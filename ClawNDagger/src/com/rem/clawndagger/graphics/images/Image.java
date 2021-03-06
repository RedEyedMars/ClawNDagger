package com.rem.clawndagger.graphics.images;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.game.events.Events.Draw;
import com.rem.clawndagger.interfaces.Drawable;

public class Image implements Drawable {

	private static FloatBuffer vertexBuffer; 
	static{
		float[] vertices = new float[]{0.0f,0.0f,0f,0.0f,1.0f,0f,1.0f,0.0f,0f,1.0f,1.0f,0f};
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(48);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer=byteBuffer.asFloatBuffer();
		vertexBuffer.clear();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	private FloatBuffer textureBuffer;
	private final int texture;
	private Rectangle dimensions;
	public Image(FloatBuffer buffer,int texture, Rectangle dimensions){
		this.textureBuffer = buffer;
		this.texture = texture;
		this.dimensions = dimensions;
	}
	public Draw on(Draw draw) {
		if(texture!=draw.bind) {
			draw.bind = texture;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture);
		}
		GL11.glTexCoordPointer(2,0,textureBuffer);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)dimensions.getX(),(float)dimensions.getY(),0.0f);
		//System.out.println(position.getX()+":"+position.getY()+":"+getTexture()+":"+Gui.renderer.viewX+":"+Gui.renderer.viewY+":"+Gui.renderer.viewZ);
		/*
		if(visualAngle !=0f){
			GL11.glTranslatef(visualRotationPointX,visualRotationPointY,0.0f);
			GL11.glRotatef(visualAngle,0,0,1);
			GL11.glTranslatef(-1f*visualRotationPointX,-1f*visualRotationPointY,0.0f);
		}*/
		GL11.glScalef((float)(dimensions.getWidth()) , (float)(dimensions.getHeight()) ,1f);
		GL11.glVertexPointer(3,0,vertexBuffer);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,0,4);
		GL11.glPopMatrix();
		return draw;
	}

}
