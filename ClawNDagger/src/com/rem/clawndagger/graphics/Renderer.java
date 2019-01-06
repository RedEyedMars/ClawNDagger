package com.rem.clawndagger.graphics;
import java.util.ArrayList;
import org.lwjgl.util.glu.GLU;

import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.interfaces.Drawable;

import java.util.List;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.input.Keyboard;
import org.lwjgl.Sys;
import java.util.LinkedList;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
public class Renderer {
	protected float viewX = 0f;
	protected float viewY = 0f;
	protected float viewZ = 0f;
	protected float aspectRatio = 0f;
	protected int viewportX = 0;
	protected int viewportY = 0;
	protected int viewportWidth = 0;
	protected int viewportHeight = 0;
	protected long ticksPerSecond = 0L;
	protected long animationInterval = 180L;
	protected long previousAnimationTime = 0L;
	protected long lastFrameTime = 0L;
	protected double secondsSinceLastFrame = 0.0;
	protected double avgSecsPerFrame = 0.01;
	protected int frameCount = 0;
	public static Renderer.Layer baseLayer = new Renderer.Layer();
	public static Renderer.Layer botLayer = new Renderer.Layer();
	public static Renderer.Layer midLayer = new Renderer.Layer();
	public static Renderer.Layer topLayer = new Renderer.Layer();

	public static void add(Drawable drawable,Renderer.Layer layer){
		layer.add(drawable);
	}
	public static void remove(Drawable drawable,Renderer.Layer layer){
		layer.remove(drawable);
	}
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(0f,0f,1f,0f,0f,0f,0f,1f,0f);
		GL11.glTranslatef(-0.7521f+viewX,-0.565f+viewY,-1.107f+viewZ);
		GL11.glScalef(1.504f,1.12875f,1f);
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glPushMatrix();
		
		baseLayer.render();
		botLayer.render();
		midLayer.render();
		topLayer.render();

		GL11.glPopMatrix();
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		++frameCount;
		if(Sys.getTime()-lastFrameTime>ticksPerSecond){
			frameCount=0;
		}
	}
	public void load(DisplayMode displayMode){
		baseLayer.load();
		botLayer.load();
		midLayer.load();
		topLayer.load();
		try{
			ticksPerSecond=Sys.getTimerResolution();
			if(aspectRatio==0f){
				aspectRatio=(float)displayMode.getWidth()/(float)displayMode.getHeight();
			}
			viewportHeight=displayMode.getHeight();
			viewportWidth=(int)((float)displayMode.getHeight()*(float)aspectRatio);
			if(viewportWidth>displayMode.getWidth()){
				viewportWidth=displayMode.getWidth();
				viewportHeight=(int)(displayMode.getWidth()/aspectRatio);
			}
			viewportX=(displayMode.getWidth()-viewportWidth)/2;
			viewportY=(displayMode.getHeight()-viewportHeight)/2;
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glClearColor(0f,0f,0f,1f);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER,0f);
			GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL,GL12.GL_SEPARATE_SPECULAR_COLOR);
			GL11.glViewport(viewportX,viewportY,viewportWidth,viewportHeight);
			setPerspective();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			updateTimer();
			Display.update();
		}
		catch(Exception e0){
			e0.printStackTrace();
			System.err.println("Renderer Load Failed");
		}
	}
	public void end(){
		Keyboard.destroy();
		Display.destroy();
		baseLayer.end();
		botLayer.end();
		midLayer.end();
		topLayer.end();
	}
	public void setPerspective(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(40f,aspectRatio,1f,1000f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public void updateTimer(){
		double numToAvg = 50.0;
		secondsSinceLastFrame=(double)(Sys.getTime()-lastFrameTime)/(double)ticksPerSecond;
		lastFrameTime=Sys.getTime();
		if(secondsSinceLastFrame<1){
			avgSecsPerFrame=avgSecsPerFrame*numToAvg+secondsSinceLastFrame;
			avgSecsPerFrame/=numToAvg+1.0;
		}
	}
	public void moveView(float moveX,float moveY){
		viewX+=moveX;
		viewY+=moveY;
	}
	public Renderer (){
		super();
	}
	public static class Layer extends Thread {
		public static int UNSTARTED = -1;
		public static int WAITING_FOR_EVENT = 0;
		public static int PROCESSING_EVENTS = 1;
		protected List<Drawable> layer = new ArrayList<Drawable>();
		protected LinkedList<Supplier<Boolean>> events = new LinkedList<Supplier<Boolean>>();
		protected int status = UNSTARTED;
		public void add(Drawable toAdd){
			synchronized(events){
				System.out.println("Add?");
				events.push(new Layer.Addition(toAdd));
				if(status==WAITING_FOR_EVENT){
					events.notifyAll();
				}
			}
		}
		public void remove(Drawable toRemove){
			synchronized(events){
				events.push(new Layer.Removal(toRemove));
				if(status==WAITING_FOR_EVENT){
					events.notifyAll();
				}
			}
		}
		public void run(){
			try{
				while(Gui.isRunning){
					synchronized(events){
						while(Gui.isRunning&&events.isEmpty()){
							status=WAITING_FOR_EVENT;
							events.wait();
							status=PROCESSING_EVENTS;
						}
					}
					while(Gui.isRunning){
						synchronized(layer){
							synchronized(events){
								if(events.isEmpty()){
									break;
								}
								events.pollFirst().get();
							}
						}
					}
				}
			}
			catch(Exception e0){
				e0.printStackTrace();
			}
		}
		public void end(){
			if(status==WAITING_FOR_EVENT){
				synchronized(events){
					events.notifyAll();
				}
			}
		}
		public void load(){
			start();
		}
		public void render(){
			int previousTexture = -2;
			synchronized(layer){
				Events.Draw draw = new Events.Draw();
				for(Drawable drawable:layer){
					if(drawable.getTexture()!=previousTexture){
						previousTexture=drawable.getTexture();
						GL11.glBindTexture(GL11.GL_TEXTURE_2D,previousTexture);
					}
					drawable.on(draw);
				}
			}
		}
		public Layer (){
			super();
		}
		public class Addition implements Supplier<Boolean> {
			protected Drawable toAdd = null;
			public Addition (Drawable toAdd){
				System.out.println("Add:"+toAdd);
				this.toAdd=toAdd;
			}
			public Boolean get(){
				System.out.println("get:"+toAdd);
				return layer.add(toAdd);
			}
			public Addition (){
				super();
			}
		}
		public class Removal implements Supplier<Boolean> {
			protected Drawable toRemove = null;
			public Removal (Drawable toRemove){
				this.toRemove=toRemove;
			}
			public Boolean get(){
				return layer.remove(toRemove);
			}
			public Removal (){
				super();
			}
		}
	}
}
