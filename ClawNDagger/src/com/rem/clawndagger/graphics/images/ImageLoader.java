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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.io.File;
public class ImageLoader { 
	private static TextureBuffer4x8Loader loadTextureBuffer4x8Loader = new TextureBuffer4x8Loader();
	private static TextureBuffer1x4Loader loadTextureBuffer1x4Loader = new TextureBuffer1x4Loader();
	private static Stream<Supplier<Boolean>> mapTemplates(
			Function<ImageTemplate,Consumer<TextureBufferLoader>> supplier,
			TextureBufferLoader buffer,
			ImageTemplate...templates){
		return Stream.of(templates)
				.unordered()
				.map(supplier::apply)
				.map(T->()->{T.accept(buffer);return true;});
	}
	static {
		loadTextureBuffer4x8Loader.start();
		loadTextureBuffer1x4Loader.start();
	}
	private static interface TextureBufferLoader {
		public FloatBuffer[][] getTextureBuffer();
		public int getWidth();
		public int getHeight();
	}
	public void load(){
		long start = System.currentTimeMillis();
		//System.out.println(
				Stream.of(
				mapTemplates(
						I->ImageLoader.build(I,256,516),
						loadTextureBuffer4x8Loader,
						ImageTemplate.CLAWMENT_BASE,
						ImageTemplate.CLAWMENT_BASE_2,
						ImageTemplate.LIL_RAT,
						ImageTemplate.SQUARE
						),
				mapTemplates(
						I->ImageLoader.build(I,1024,1024),
						loadTextureBuffer1x4Loader,
						ImageTemplate.BLUE_SKY
						)
				).parallel().unordered().flatMap(S->S).collect(Collectors.toList())
				.stream().unordered().forEach(Supplier::get);
				/*.mapToInt(S->
				{
					long start2 = System.currentTimeMillis();
					S.get();
					return (int)(System.currentTimeMillis()-start2);
				}).sum())*/;
		System.out.println("ImageLoader:"+(System.currentTimeMillis()-start));
	}


	private static class TextureBuffer4x8Loader extends Thread implements TextureBufferLoader {
		private FloatBuffer[][] textureBuffer = new FloatBuffer[4][8];
		public FloatBuffer[][] getTextureBuffer(){
			return textureBuffer;
		}
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
		public TextureBuffer4x8Loader (){
			super();
		}
		@Override
		public int getWidth() {
			return 4;
		}
		@Override
		public int getHeight() {
			return 8;
		}
	}
	public static int getTextureFromPixelBuffer(ByteBuffer pixels, int width, int height){
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
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA8,width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
		GL11.glPopAttrib();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture);
		int result = GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D,GL11.GL_RGBA8,width,height,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,pixels);
		if(result!=0){
			System.err.println("GLApp.makeTextureMipMap(): Error occured while building mip map, result="+result+" error="+GLU.gluErrorString(result));
		}
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV,GL11.GL_TEXTURE_ENV_MODE,GL11.GL_MODULATE);
		return texture;
	}

	private static class TextureBuffer1x4Loader extends Thread implements TextureBufferLoader {
		private FloatBuffer[][] textureBuffer  = new FloatBuffer[1][4];
		public FloatBuffer[][] getTextureBuffer(){
			return textureBuffer;
		}
		public void run(){
			ByteBuffer byteBuffer = null;
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][0]=byteBuffer.asFloatBuffer();
			textureBuffer[0][0].put(new float[]{0.0f,0.25f,0.0f,0.0f,1.0f,0.25f,1.0f,0.0f});
			textureBuffer[0][0].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][1]=byteBuffer.asFloatBuffer();
			textureBuffer[0][1].put(new float[]{0.0f,0.5f,0.0f,0.25f,1.0f,0.5f,1.0f,0.25f});
			textureBuffer[0][1].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][2]=byteBuffer.asFloatBuffer();
			textureBuffer[0][2].put(new float[]{0.0f,0.75f,0.0f,0.5f,1.0f,0.75f,1.0f,0.5f});
			textureBuffer[0][2].position(0);
			byteBuffer=ByteBuffer.allocateDirect(2*4*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			textureBuffer[0][3]=byteBuffer.asFloatBuffer();
			textureBuffer[0][3].put(new float[]{0.0f,1.0f,0.0f,0.75f,1.0f,1.0f,1.0f,0.75f});
			textureBuffer[0][3].position(0);
		}
		@Override
		public int getWidth() {
			return 1;
		}
		@Override
		public int getHeight() {
			return 4;
		}
	}
	private static BufferedImage getBufferedImage(String fileName){
		try{
			return ImageIO.read(new File(fileName));
		}
		catch(Exception e0){
			e0.printStackTrace();
			throw new RuntimeException("Failed to load image:"+fileName);
		}
	}
	private static int[] getPixelGrabber(BufferedImage image, int width, int height){
		int[] pixels = new int[width*height];
		try{
			new PixelGrabber(image,0,0,width,height,pixels,0,width).grabPixels();
			return pixels;
		}
		catch(Exception e0){
			throw new RuntimeException("Failed to Grab Pixels");
		}
	}
	public static Consumer<TextureBufferLoader> build(ImageTemplate template,
			int width, int height){
			int[] pixels = getPixelGrabber(
					getBufferedImage(template.getFileName()),width,height);
			ByteBuffer pixelBuffer=ByteBuffer.allocateDirect(width*height*4).order(ByteOrder.nativeOrder());
			ByteBuffer flippedPixelBuffer=ByteBuffer.allocateDirect(width*height*4).order(ByteOrder.nativeOrder());
			IntStream.range(0,height).boxed().parallel().flatMap(
					Y->
					IntStream.range(0,width).boxed().map(X->new int[]{
								pixels[(width*Y)+X],
								X,
								Y}
					)
					).unordered().map(A->
					new int[]{
							(A[0]>>24)&0xFF,
							(A[0]>>16)&0xFF,
							(A[0]>>8)&0xFF,
							(A[0]>>0)&0xFF,
							4*((width*A[2])+A[1]),
							4*(width*(A[2]+1)-A[1]-1)
					}).peek(PAR->pixelBuffer.put(PAR[4],  (byte)PAR[1]))
					  .peek(PAR->pixelBuffer.put(PAR[4]+1,(byte)PAR[2]))
					  .peek(PAR->pixelBuffer.put(PAR[4]+2,(byte)PAR[3]))
					  .peek(PAR->pixelBuffer.put(PAR[4]+3,(byte)PAR[0]))
					  
					  .peek(PAR->flippedPixelBuffer.put(PAR[5],(byte)PAR[1]))
					  .peek(PAR->flippedPixelBuffer.put(PAR[5]+1,(byte)PAR[2]))
					  .peek(PAR->flippedPixelBuffer.put(PAR[5]+2,(byte)PAR[3]))
					  .forEach(PAR->flippedPixelBuffer.put(PAR[5]+3,(byte)PAR[0]));

			pixelBuffer.position(width*height*4);
			pixelBuffer.flip();
			flippedPixelBuffer.position(width*height*4);
			flippedPixelBuffer.flip();
			return TB->
			{
				template.setTexture(getTextureFromPixelBuffer(pixelBuffer,width,height));
				template.setFlippedTexture(getTextureFromPixelBuffer(flippedPixelBuffer,width,height));
				template.setTextureBuffer(TB.getTextureBuffer());
				template.setFlippedTextureBuffer(TB.getTextureBuffer());
				template.setVisualPixelDimensions(width/TB.getWidth(),height/TB.getHeight());
			};
		}
}
