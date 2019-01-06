package com.rem.clawndagger.graphics;
import java.util.ArrayList;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.PixelFormat;

import com.rem.clawndagger.game.Game;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
public class Gui {
	protected FloatBuffer placeHolder = null;
	public static Boolean isRunning = true;
	protected ImageLoader imageLoader = null;
	protected InputHandler inputHandler = null;
	public static Game game = null;
	public static Renderer renderer = null;
	public static StringBuilder _log = new StringBuilder();
	protected int finishedKey = Keyboard.KEY_ESCAPE;
	protected String window_title = "OpenGL Window";
	protected boolean VSyncEnabled = true;
	protected boolean useCurrentDisplay = false;
	protected boolean fullScreen = false;
	protected boolean showMessages = true;
	protected int displayWidth = 1024;
	protected int displayHeight = 786;
	protected int displayColorBits = -1;
	protected int displayFrequency = -1;
	protected int depthBufferBits = 24;
	protected DisplayMode originalDisplayMode = null;
	protected DisplayMode displayMode = null;
	public FloatBuffer getPlaceHolder(){
		return placeHolder;
	}
	public void setPlaceHolder(FloatBuffer newPlaceHolder){
		placeHolder=newPlaceHolder;
	}
	public ImageLoader getImageLoader(){
		return imageLoader;
	}
	public void setImageLoader(ImageLoader newImageLoader){
		imageLoader=newImageLoader;
	}
	public InputHandler getInputHandler(){
		return inputHandler;
	}
	public void setInputHandler(InputHandler newInputHandler){
		inputHandler=newInputHandler;
	}
	public Gui (){
		load();
		run();
	}
	public void load(){
		initializeDisplay();
		renderer=new Renderer();
		renderer.load(displayMode);
		inputHandler=new InputHandler();
		inputHandler.load(displayMode);
		imageLoader=new ImageLoader();
		imageLoader.load();
		game=new Game();
		game.load();
	}
	public static void log(String toLog){
		_log.append(toLog);
		_log.append("\n");
	}
	public int getFinishedKey(){
		return finishedKey;
	}
	public void setFinishedKey(int newFinishedKey){
		finishedKey=newFinishedKey;
	}
	public String getWindowTitle(){
		return window_title;
	}
	public void setWindowTitle(String newWindowTitle){
		window_title=newWindowTitle;
	}
	public boolean getVSyncEnabled(){
		return VSyncEnabled;
	}
	public void setVSyncEnabled(boolean newVSyncEnabled){
		VSyncEnabled=newVSyncEnabled;
	}
	public boolean getUseCurrentDisplay(){
		return useCurrentDisplay;
	}
	public void setUseCurrentDisplay(boolean newUseCurrentDisplay){
		useCurrentDisplay=newUseCurrentDisplay;
	}
	public boolean getFullScreen(){
		return fullScreen;
	}
	public void setFullScreen(boolean newFullScreen){
		fullScreen=newFullScreen;
	}
	public boolean getShowMessages(){
		return showMessages;
	}
	public void setShowMessages(boolean newShowMessages){
		showMessages=newShowMessages;
	}
	public int getDisplayWidth(){
		return displayWidth;
	}
	public void setDisplayWidth(int newDisplayWidth){
		displayWidth=newDisplayWidth;
	}
	public int getDisplayHeight(){
		return displayHeight;
	}
	public void setDisplayHeight(int newDisplayHeight){
		displayHeight=newDisplayHeight;
	}
	public int getDisplayColorBits(){
		return displayColorBits;
	}
	public void setDisplayColorBits(int newDisplayColorBits){
		displayColorBits=newDisplayColorBits;
	}
	public int getDisplayFrequency(){
		return displayFrequency;
	}
	public void setDisplayFrequency(int newDisplayFrequency){
		displayFrequency=newDisplayFrequency;
	}
	public int getDepthBufferBits(){
		return depthBufferBits;
	}
	public void setDepthBufferBits(int newDepthBufferBits){
		depthBufferBits=newDepthBufferBits;
	}
	public DisplayMode getOriginalDisplayMode(){
		return originalDisplayMode;
	}
	public void setOriginalDisplayMode(DisplayMode newOriginalDisplayMode){
		originalDisplayMode=newOriginalDisplayMode;
	}
	public DisplayMode getDisplayMode(){
		return displayMode;
	}
	public void setDisplayMode(DisplayMode newDisplayMode){
		displayMode=newDisplayMode;
	}
	public void initializeDisplay(){
		originalDisplayMode=Display.getDisplayMode();
		Gui.log("GLApp.initDisplay(): Current display mode is "+originalDisplayMode);
		if(displayHeight==-1){
			displayHeight=originalDisplayMode.getHeight();
		}
		if(displayWidth==-1){
			displayWidth=originalDisplayMode.getWidth();
		}
		if(displayColorBits==-1){
			displayColorBits=originalDisplayMode.getBitsPerPixel();
		}
		if(displayFrequency==-1){
			displayFrequency=originalDisplayMode.getFrequency();
		}
		try{
			if(useCurrentDisplay){
				displayMode=originalDisplayMode;
			}
			else{
				displayMode=getDisplayMode(displayWidth,displayHeight,displayColorBits,displayFrequency);
				if(displayMode==null){
					displayMode=getDisplayMode(1024,768,32,60);
					if(displayMode==null){
						displayMode=getDisplayMode(1024,768,16,60);
						if(displayMode==null){
							displayMode=getDisplayMode(originalDisplayMode.getWidth(),originalDisplayMode.getHeight(),originalDisplayMode.getBitsPerPixel(),originalDisplayMode.getFrequency());
							if(displayMode==null){
								System.err.println("Gui Load Display Cant find a compatible Display Mode!!!");
							}
						}
					}
				}
			}
			Gui.log("initializeDisplay: Setting display mode to "+displayMode+" with pixel depth = "+depthBufferBits);
			Display.setDisplayMode(displayMode);
			displayMode=displayMode;
			displayWidth=displayMode.getWidth();
			displayHeight=displayMode.getHeight();
			displayColorBits=displayMode.getBitsPerPixel();
			displayFrequency=displayMode.getFrequency();
		}
		catch(Exception e0){
			e0.printStackTrace();
			System.err.println("initializeDisplay: Failed to create display: ");
			System.exit(1);
		}
		try{
			Display.create(new PixelFormat(0,depthBufferBits,8));
			Display.setTitle(window_title);
			Display.setFullscreen(fullScreen);
			Display.setVSyncEnabled(VSyncEnabled);
		}
		catch(Exception e0){
			e0.printStackTrace();
			System.err.println("initializeDisplay: Failed to create OpenGL window");
			System.exit(1);
		}
	}
	public DisplayMode getDisplayMode(int width,int height,int colourBits,int frequency){
		try{
			DisplayMode[] allDisplayModes = Display.getAvailableDisplayModes();
			DisplayMode tempDisplayMode = null;
			for(int i = 0;i<allDisplayModes.length;++i){
				tempDisplayMode=allDisplayModes[i];
				if(tempDisplayMode.getWidth()==width&&tempDisplayMode.getHeight()==height&&tempDisplayMode.getBitsPerPixel()==colourBits&&tempDisplayMode.getFrequency()==frequency){
					return tempDisplayMode;
				}
			}
		}
		catch(Exception e0){
			e0.printStackTrace();
			System.err.println("getDisplayMode Failed");
		}
		return null;
	}
	public void run(){
		try{
			while(isRunning){
				if(Display.isVisible()==false){
					Thread.sleep(200L);
				}
				else if(Display.isCloseRequested()){
					isRunning=false;
				}
				else{
					Thread.sleep(1);
				}
				inputHandler.handle();
				game.update();
				renderer.render();
				Display.update();
			}
		}
		catch(InterruptedException e0){
			e0.printStackTrace();
		}
		inputHandler.end();
		renderer.end();
	}
	public static interface Renderable {
		public int getTexture();
		public void render();
		public void animate();
	}
	
	public static class Colour {
		public Float getRedF(){
			return null;
		}
		public Float getGreenF(){
			return null;
		}
		public Float getBlueF(){
			return null;
		}
		public Integer getRedI(){
			return null;
		}
		public Integer getGreenI(){
			return null;
		}
		public Integer getBlueI(){
			return null;
		}
		public Float[] asFloatArray(){
			return null;
		}
		public int[] asintArray(){
			return null;
		}
		public int getTexture(){
			return 0;
		}
		public void render(){
		}
		public void endRender(){
		}
		public Colour (){
			super();
		}
	}
}
