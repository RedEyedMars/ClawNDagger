package com.rem.clawndagger.graphics;
import java.util.HashMap;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.util.glu.GLU;

import com.rem.clawndagger.graphics.images.ImageTemplate;

import java.awt.image.PixelGrabber;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.io.File;
public class ImageLoader {
	protected FloatBuffer placeHolder = null;
	protected Map<Integer, Map<Integer, FloatBuffer[][]>> textureBuffers = new HashMap<Integer, Map<Integer, FloatBuffer[][]>>();
	protected Thread loadTextureBuffer3x8Loader = new ImageLoader.TextureBuffer3x8Loader();
	public FloatBuffer getPlaceHolder(){
		return placeHolder;
	}
	public void setPlaceHolder(FloatBuffer newPlaceHolder){
		placeHolder=newPlaceHolder;
	}
	public void load(){
		ImageData144x384Loader ___Resource_gui_images_BaseClawmentDataLoader = new ImageData144x384Loader("./res/entities/BaseClawment.png");
		___Resource_gui_images_BaseClawmentDataLoader.start();
		loadTextureBuffer3x8Loader.start();
		try{
			loadTextureBuffer3x8Loader.join();
		}
		catch(InterruptedException e0){
			e0.printStackTrace();
		}
		___Resource_gui_images_BaseClawmentDataLoader.attachTextures(ImageTemplate.CLAWMENT_BASE);
	}
	
	public ImageLoader (){
		super();
	}
	
	public class TextureBuffer3x8Loader extends Thread {
		public void run(){
			FloatBuffer[][] textureBuffer = new FloatBuffer[3][8];
			ByteBuffer byteBuffer = null;
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][0]=byteBuffer.asFloatBuffer();
			textureBuffer[0][0].put(new float[]{0.0f,0.125f,0.0f,0.0f,0.33333334f,0.125f,0.33333334f,0.0f});
			textureBuffer[0][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][1]=byteBuffer.asFloatBuffer();
			textureBuffer[0][1].put(new float[]{0.0f,0.25f,0.0f,0.125f,0.33333334f,0.25f,0.33333334f,0.125f});
			textureBuffer[0][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][2]=byteBuffer.asFloatBuffer();
			textureBuffer[0][2].put(new float[]{0.0f,0.375f,0.0f,0.25f,0.33333334f,0.375f,0.33333334f,0.25f});
			textureBuffer[0][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][3]=byteBuffer.asFloatBuffer();
			textureBuffer[0][3].put(new float[]{0.0f,0.5f,0.0f,0.375f,0.33333334f,0.5f,0.33333334f,0.375f});
			textureBuffer[0][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][4]=byteBuffer.asFloatBuffer();
			textureBuffer[0][4].put(new float[]{0.0f,0.625f,0.0f,0.5f,0.33333334f,0.625f,0.33333334f,0.5f});
			textureBuffer[0][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][5]=byteBuffer.asFloatBuffer();
			textureBuffer[0][5].put(new float[]{0.0f,0.75f,0.0f,0.625f,0.33333334f,0.75f,0.33333334f,0.625f});
			textureBuffer[0][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][6]=byteBuffer.asFloatBuffer();
			textureBuffer[0][6].put(new float[]{0.0f,0.875f,0.0f,0.75f,0.33333334f,0.875f,0.33333334f,0.75f});
			textureBuffer[0][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][7]=byteBuffer.asFloatBuffer();
			textureBuffer[0][7].put(new float[]{0.0f,1.0f,0.0f,0.875f,0.33333334f,1.0f,0.33333334f,0.875f});
			textureBuffer[0][7].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][0]=byteBuffer.asFloatBuffer();
			textureBuffer[1][0].put(new float[]{0.33333334f,0.125f,0.33333334f,0.0f,0.6666667f,0.125f,0.6666667f,0.0f});
			textureBuffer[1][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][1]=byteBuffer.asFloatBuffer();
			textureBuffer[1][1].put(new float[]{0.33333334f,0.25f,0.33333334f,0.125f,0.6666667f,0.25f,0.6666667f,0.125f});
			textureBuffer[1][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][2]=byteBuffer.asFloatBuffer();
			textureBuffer[1][2].put(new float[]{0.33333334f,0.375f,0.33333334f,0.25f,0.6666667f,0.375f,0.6666667f,0.25f});
			textureBuffer[1][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][3]=byteBuffer.asFloatBuffer();
			textureBuffer[1][3].put(new float[]{0.33333334f,0.5f,0.33333334f,0.375f,0.6666667f,0.5f,0.6666667f,0.375f});
			textureBuffer[1][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][4]=byteBuffer.asFloatBuffer();
			textureBuffer[1][4].put(new float[]{0.33333334f,0.625f,0.33333334f,0.5f,0.6666667f,0.625f,0.6666667f,0.5f});
			textureBuffer[1][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][5]=byteBuffer.asFloatBuffer();
			textureBuffer[1][5].put(new float[]{0.33333334f,0.75f,0.33333334f,0.625f,0.6666667f,0.75f,0.6666667f,0.625f});
			textureBuffer[1][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][6]=byteBuffer.asFloatBuffer();
			textureBuffer[1][6].put(new float[]{0.33333334f,0.875f,0.33333334f,0.75f,0.6666667f,0.875f,0.6666667f,0.75f});
			textureBuffer[1][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][7]=byteBuffer.asFloatBuffer();
			textureBuffer[1][7].put(new float[]{0.33333334f,1.0f,0.33333334f,0.875f,0.6666667f,1.0f,0.6666667f,0.875f});
			textureBuffer[1][7].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][0]=byteBuffer.asFloatBuffer();
			textureBuffer[2][0].put(new float[]{0.6666667f,0.125f,0.6666667f,0.0f,1.0f,0.125f,1.0f,0.0f});
			textureBuffer[2][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][1]=byteBuffer.asFloatBuffer();
			textureBuffer[2][1].put(new float[]{0.6666667f,0.25f,0.6666667f,0.125f,1.0f,0.25f,1.0f,0.125f});
			textureBuffer[2][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][2]=byteBuffer.asFloatBuffer();
			textureBuffer[2][2].put(new float[]{0.6666667f,0.375f,0.6666667f,0.25f,1.0f,0.375f,1.0f,0.25f});
			textureBuffer[2][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][3]=byteBuffer.asFloatBuffer();
			textureBuffer[2][3].put(new float[]{0.6666667f,0.5f,0.6666667f,0.375f,1.0f,0.5f,1.0f,0.375f});
			textureBuffer[2][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][4]=byteBuffer.asFloatBuffer();
			textureBuffer[2][4].put(new float[]{0.6666667f,0.625f,0.6666667f,0.5f,1.0f,0.625f,1.0f,0.5f});
			textureBuffer[2][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][5]=byteBuffer.asFloatBuffer();
			textureBuffer[2][5].put(new float[]{0.6666667f,0.75f,0.6666667f,0.625f,1.0f,0.75f,1.0f,0.625f});
			textureBuffer[2][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][6]=byteBuffer.asFloatBuffer();
			textureBuffer[2][6].put(new float[]{0.6666667f,0.875f,0.6666667f,0.75f,1.0f,0.875f,1.0f,0.75f});
			textureBuffer[2][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][7]=byteBuffer.asFloatBuffer();
			textureBuffer[2][7].put(new float[]{0.6666667f,1.0f,0.6666667f,0.875f,1.0f,1.0f,1.0f,0.875f});
			textureBuffer[2][7].position(0);
			ImageTemplate.CLAWMENT_BASE.setTextureBuffer(textureBuffer);
			ImageTemplate.CLAWMENT_BASE.setFlippedTextureBuffer(textureBuffer);
		}
		public TextureBuffer3x8Loader (){
			super();
		}
	}
	public static class ImageData144x384Loader extends Thread {
		protected String fileName = null;
		protected ByteBuffer pixelBuffer = null;
		protected ByteBuffer flippedPixelBuffer = null;
		protected Boolean failed = false;
		public String getFileName(){
			return fileName;
		}
		public void setFileName(String newFileName){
			fileName=newFileName;
		}
		public ByteBuffer getPixelBuffer(){
			return pixelBuffer;
		}
		public void setPixelBuffer(ByteBuffer newPixelBuffer){
			pixelBuffer=newPixelBuffer;
		}
		public ByteBuffer getFlippedPixelBuffer(){
			return flippedPixelBuffer;
		}
		public void setFlippedPixelBuffer(ByteBuffer newFlippedPixelBuffer){
			flippedPixelBuffer=newFlippedPixelBuffer;
		}
		public Boolean getFailed(){
			return failed;
		}
		public void setFailed(Boolean newFailed){
			failed=newFailed;
		}
		public ImageData144x384Loader (String initialFileName){
			super();
			this.fileName=initialFileName;
		}
		public void run(){
			BufferedImage bufferedImage = null;
			try{
				bufferedImage=ImageIO.read(new File(fileName));
			}
			catch(Exception e0){
				e0.printStackTrace();
				failed=true;
				return ;
			}
			int[] pixels = new int[55296];
			PixelGrabber pixelGrabber = new PixelGrabber(bufferedImage,0,0,144,384,pixels,0,144);
			try{
				pixelGrabber.grabPixels();
			}
			catch(Exception e0){
				System.err.println("Pixel Grabbing interrupted!");
				failed=true;
				return ;
			}
			byte[] bytes = new byte[221184];
			byte[] flippedBytes = new byte[221184];
			int p = 0;
			int r = 0;
			int g = 0;
			int b = 0;
			int a = 0;
			int i = 0;
			int j = 0;
			int k = 0;
			for(int y = 0;y<384;++y){
				for(int x = 0;x<144;++x){
					i=(144*y)+x;
					j=i*4;
					k=4*(144*(y+1)-x-1);
					p=pixels[i];
					a=(p>>24)&0xFF;
					r=(p>>16)&0xFF;
					g=(p>>8)&0xFF;
					b=(p>>0)&0xFF;
					bytes[j+0]=(byte)r;
					bytes[j+1]=(byte)g;
					bytes[j+2]=(byte)b;
					bytes[j+3]=(byte)a;
					flippedBytes[k+0]=(byte)r;
					flippedBytes[k+1]=(byte)g;
					flippedBytes[k+2]=(byte)b;
					flippedBytes[k+3]=(byte)a;
				}
			}
			pixelBuffer=ByteBuffer.allocateDirect(221184).order(ByteOrder.nativeOrder());
			pixelBuffer.put(bytes).flip();
			flippedPixelBuffer=ByteBuffer.allocateDirect(221184).order(ByteOrder.nativeOrder());
			flippedPixelBuffer.put(flippedBytes).flip();
		}
		public void attachTextures(ImageTemplate clawmentBase){
			try{
				super.join();
			}
			catch(InterruptedException e0){
				e0.printStackTrace();
			}
			if(failed==true){
				clawmentBase.setTexture(0);
				return ;
			}
			clawmentBase.setTexture(getTextureFromPixelBuffer(pixelBuffer));
			clawmentBase.setFlippedTexture(getTextureFromPixelBuffer(flippedPixelBuffer));
		}
		public static int getTextureFromPixelBuffer(ByteBuffer pixels){
			int texture = 0;
			IntBuffer textureHandle = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
			GL11.glGenTextures(textureHandle);
			texture=textureHandle.get(0);
			GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S,GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T,GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA8,144,384,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
			GL11.glPopAttrib();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture);
			int result = GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D,GL11.GL_RGBA8,144,384,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
			if(result!=0){
				System.err.println("GLApp.makeTextureMipMap(): Error occured while building mip map, result="+result+" error="+GLU.gluErrorString(result));
			}
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
			GL11.glTexEnvf(GL11.GL_TEXTURE_ENV,GL11.GL_TEXTURE_ENV_MODE,GL11.GL_MODULATE);
			return texture;
		}
		public ImageData144x384Loader (String fileName,ByteBuffer pixelBuffer,ByteBuffer flippedPixelBuffer,Boolean failed){
			super();
			this.fileName=fileName;
			this.pixelBuffer=pixelBuffer;
			this.flippedPixelBuffer=flippedPixelBuffer;
			this.failed=failed;
		}
		public ImageData144x384Loader (){
			super();
		}
	}
}
