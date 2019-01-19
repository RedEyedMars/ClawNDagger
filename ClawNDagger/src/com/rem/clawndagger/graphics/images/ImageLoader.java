package com.rem.clawndagger.graphics.images;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.util.glu.GLU;

import java.awt.image.PixelGrabber;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.io.File;
public class ImageLoader {
	protected Map<Integer, Map<Integer, FloatBuffer[][]>> textureBuffers = new HashMap<Integer, Map<Integer, FloatBuffer[][]>>(); 
	private static Map<BiFunction<String, ImageTemplate,DataLoader>,List<ImageTemplate>> templateMap = new HashMap<BiFunction<String, ImageTemplate,DataLoader>,List<ImageTemplate>>();
	static {
		templateMap.put(ImageData256x512Loader::new, Arrays.asList(
				ImageTemplate.CLAWMENT_BASE,
				ImageTemplate.CLAWMENT_BASE_2
				));
	}
	private static interface DataLoader {
		public void attach();
	}
	public void load(){
		ImageLoader.TextureBuffer4x8Loader loadTextureBuffer4x8Loader = new ImageLoader.TextureBuffer4x8Loader();
		loadTextureBuffer4x8Loader.start();
		templateMap.keySet().parallelStream().flatMap(factory->
		  templateMap.get(factory).stream().map(IT->factory.apply(IT.getFileName(),IT))).sequential().forEach(Loader->Loader.attach());
		
		loadTextureBuffer4x8Loader.attach(
				ImageTemplate.CLAWMENT_BASE,
				ImageTemplate.CLAWMENT_BASE_2
				);
		
	}
	
	public ImageLoader (){
		super();
	}
	
	public class TextureBuffer4x8Loader extends Thread {
		private FloatBuffer[][] textureBuffer = new FloatBuffer[4][8];
		public void run(){
			ByteBuffer byteBuffer = null;
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][0]=byteBuffer.asFloatBuffer();
			textureBuffer[0][0].put(new float[]{0.0f,0.125f,0.0f,0.0f,0.25f,0.125f,0.25f,0.0f});
			textureBuffer[0][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][1]=byteBuffer.asFloatBuffer();
			textureBuffer[0][1].put(new float[]{0.0f,0.25f,0.0f,0.125f,0.25f,0.25f,0.25f,0.125f});
			textureBuffer[0][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][2]=byteBuffer.asFloatBuffer();
			textureBuffer[0][2].put(new float[]{0.0f,0.375f,0.0f,0.25f,0.25f,0.375f,0.25f,0.25f});
			textureBuffer[0][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][3]=byteBuffer.asFloatBuffer();
			textureBuffer[0][3].put(new float[]{0.0f,0.5f,0.0f,0.375f,0.25f,0.5f,0.25f,0.375f});
			textureBuffer[0][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][4]=byteBuffer.asFloatBuffer();
			textureBuffer[0][4].put(new float[]{0.0f,0.625f,0.0f,0.5f,0.25f,0.625f,0.25f,0.5f});
			textureBuffer[0][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][5]=byteBuffer.asFloatBuffer();
			textureBuffer[0][5].put(new float[]{0.0f,0.75f,0.0f,0.625f,0.25f,0.75f,0.25f,0.625f});
			textureBuffer[0][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][6]=byteBuffer.asFloatBuffer();
			textureBuffer[0][6].put(new float[]{0.0f,0.875f,0.0f,0.75f,0.25f,0.875f,0.25f,0.75f});
			textureBuffer[0][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][7]=byteBuffer.asFloatBuffer();
			textureBuffer[0][7].put(new float[]{0.0f,1.0f,0.0f,0.875f,0.25f,1.0f,0.25f,0.875f});
			textureBuffer[0][7].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][0]=byteBuffer.asFloatBuffer();
			textureBuffer[1][0].put(new float[]{0.25f,0.125f,0.25f,0.0f,0.5f,0.125f,0.5f,0.0f});
			textureBuffer[1][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][1]=byteBuffer.asFloatBuffer();
			textureBuffer[1][1].put(new float[]{0.25f,0.25f,0.25f,0.125f,0.5f,0.25f,0.5f,0.125f});
			textureBuffer[1][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][2]=byteBuffer.asFloatBuffer();
			textureBuffer[1][2].put(new float[]{0.25f,0.375f,0.25f,0.25f,0.5f,0.375f,0.5f,0.25f});
			textureBuffer[1][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][3]=byteBuffer.asFloatBuffer();
			textureBuffer[1][3].put(new float[]{0.25f,0.5f,0.25f,0.375f,0.5f,0.5f,0.5f,0.375f});
			textureBuffer[1][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][4]=byteBuffer.asFloatBuffer();
			textureBuffer[1][4].put(new float[]{0.25f,0.625f,0.25f,0.5f,0.5f,0.625f,0.5f,0.5f});
			textureBuffer[1][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][5]=byteBuffer.asFloatBuffer();
			textureBuffer[1][5].put(new float[]{0.25f,0.75f,0.25f,0.625f,0.5f,0.75f,0.5f,0.625f});
			textureBuffer[1][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][6]=byteBuffer.asFloatBuffer();
			textureBuffer[1][6].put(new float[]{0.25f,0.875f,0.25f,0.75f,0.5f,0.875f,0.5f,0.75f});
			textureBuffer[1][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[1][7]=byteBuffer.asFloatBuffer();
			textureBuffer[1][7].put(new float[]{0.25f,1.0f,0.25f,0.875f,0.5f,1.0f,0.5f,0.875f});
			textureBuffer[1][7].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][0]=byteBuffer.asFloatBuffer();
			textureBuffer[2][0].put(new float[]{0.5f,0.125f,0.5f,0.0f,0.75f,0.125f,0.75f,0.0f});
			textureBuffer[2][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][1]=byteBuffer.asFloatBuffer();
			textureBuffer[2][1].put(new float[]{0.5f,0.25f,0.5f,0.125f,0.75f,0.25f,0.75f,0.125f});
			textureBuffer[2][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][2]=byteBuffer.asFloatBuffer();
			textureBuffer[2][2].put(new float[]{0.5f,0.375f,0.5f,0.25f,0.75f,0.375f,0.75f,0.25f});
			textureBuffer[2][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][3]=byteBuffer.asFloatBuffer();
			textureBuffer[2][3].put(new float[]{0.5f,0.5f,0.5f,0.375f,0.75f,0.5f,0.75f,0.375f});
			textureBuffer[2][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][4]=byteBuffer.asFloatBuffer();
			textureBuffer[2][4].put(new float[]{0.5f,0.625f,0.5f,0.5f,0.75f,0.625f,0.75f,0.5f});
			textureBuffer[2][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][5]=byteBuffer.asFloatBuffer();
			textureBuffer[2][5].put(new float[]{0.5f,0.75f,0.5f,0.625f,0.75f,0.75f,0.75f,0.625f});
			textureBuffer[2][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][6]=byteBuffer.asFloatBuffer();
			textureBuffer[2][6].put(new float[]{0.5f,0.875f,0.5f,0.75f,0.75f,0.875f,0.75f,0.75f});
			textureBuffer[2][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[2][7]=byteBuffer.asFloatBuffer();
			textureBuffer[2][7].put(new float[]{0.5f,1.0f,0.5f,0.875f,0.75f,1.0f,0.75f,0.875f});
			textureBuffer[2][7].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][0]=byteBuffer.asFloatBuffer();
			textureBuffer[3][0].put(new float[]{0.75f,0.125f,0.75f,0.0f,1.0f,0.125f,1.0f,0.0f});
			textureBuffer[3][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][1]=byteBuffer.asFloatBuffer();
			textureBuffer[3][1].put(new float[]{0.75f,0.25f,0.75f,0.125f,1.0f,0.25f,1.0f,0.125f});
			textureBuffer[3][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][2]=byteBuffer.asFloatBuffer();
			textureBuffer[3][2].put(new float[]{0.75f,0.375f,0.75f,0.25f,1.0f,0.375f,1.0f,0.25f});
			textureBuffer[3][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][3]=byteBuffer.asFloatBuffer();
			textureBuffer[3][3].put(new float[]{0.75f,0.5f,0.75f,0.375f,1.0f,0.5f,1.0f,0.375f});
			textureBuffer[3][3].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][4]=byteBuffer.asFloatBuffer();
			textureBuffer[3][4].put(new float[]{0.75f,0.625f,0.75f,0.5f,1.0f,0.625f,1.0f,0.5f});
			textureBuffer[3][4].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][5]=byteBuffer.asFloatBuffer();
			textureBuffer[3][5].put(new float[]{0.75f,0.75f,0.75f,0.625f,1.0f,0.75f,1.0f,0.625f});
			textureBuffer[3][5].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][6]=byteBuffer.asFloatBuffer();
			textureBuffer[3][6].put(new float[]{0.75f,0.875f,0.75f,0.75f,1.0f,0.875f,1.0f,0.75f});
			textureBuffer[3][6].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[3][7]=byteBuffer.asFloatBuffer();
			textureBuffer[3][7].put(new float[]{0.75f,1.0f,0.75f,0.875f,1.0f,1.0f,1.0f,0.875f});
			textureBuffer[3][7].position(0);

		}
		public void attach(ImageTemplate... templates){
			try {
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Arrays.stream(templates).forEach(template->{
				template.setTextureBuffer(textureBuffer);
				template.setFlippedTextureBuffer(textureBuffer);
			});
		}
		public TextureBuffer4x8Loader (){
			super();
		}
	}
	public static class ImageData256x512Loader extends Thread implements DataLoader {
		protected String fileName = null;
		protected ByteBuffer pixelBuffer = null;
		protected ByteBuffer flippedPixelBuffer = null;
		protected Boolean failed = false;
		private ImageTemplate template;
		
		public ImageData256x512Loader (String initialFileName, ImageTemplate template){
			super();
			this.fileName=initialFileName;
			this.template = template;
			start();
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
			int[] pixels = new int[131072];
			PixelGrabber pixelGrabber = new PixelGrabber(bufferedImage,0,0,256,512,pixels,0,256);
			try{
				pixelGrabber.grabPixels();
			}
			catch(Exception e0){
				System.err.println("Pixel Grabbing interrupted!");
				failed=true;
				return ;
			}
			byte[] bytes = new byte[524288];
			byte[] flippedBytes = new byte[524288];
			int p = 0;
			int r = 0;
			int g = 0;
			int b = 0;
			int a = 0;
			int i = 0;
			int j = 0;
			int k = 0;
			for(int y = 0;y<512;++y){
				for(int x = 0;x<256;++x){
					i=(256*y)+x;
					j=i*4;
					k=4*(256*(y+1)-x-1);
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
			pixelBuffer=ByteBuffer.allocateDirect(524288).order(ByteOrder.nativeOrder());
			pixelBuffer.put(bytes).flip();
			flippedPixelBuffer=ByteBuffer.allocateDirect(524288).order(ByteOrder.nativeOrder());
			flippedPixelBuffer.put(flippedBytes).flip();
		}
		public void attach(){
			try{
				super.join();
			}
			catch(InterruptedException e0){
				e0.printStackTrace();
			}
			if(failed==true){
				template.setTexture(0);
				return ;
			}
			template.setTexture(getTextureFromPixelBuffer(pixelBuffer));
			template.setFlippedTexture(getTextureFromPixelBuffer(flippedPixelBuffer));
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
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA8,256,512,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
			GL11.glPopAttrib();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture);
			int result = GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D,GL11.GL_RGBA8,256,512,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
			if(result!=0){
				System.err.println("GLApp.makeTextureMipMap(): Error occured while building mip map, result="+result+" error="+GLU.gluErrorString(result));
			}
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
			GL11.glTexEnvf(GL11.GL_TEXTURE_ENV,GL11.GL_TEXTURE_ENV_MODE,GL11.GL_MODULATE);
			return texture;
		}
		public ImageData256x512Loader (String fileName,ByteBuffer pixelBuffer,ByteBuffer flippedPixelBuffer,Boolean failed){
			super();
			this.fileName=fileName;
			this.pixelBuffer=pixelBuffer;
			this.flippedPixelBuffer=flippedPixelBuffer;
			this.failed=failed;
		}
		public ImageData256x512Loader (){
			super();
		}
	}
}
