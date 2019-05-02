package com.rem.clawndagger.graphics;
import java.util.ArrayList;
import org.lwjgl.util.glu.GLU;

import com.rem.clawndagger.game.events.Events;
import com.rem.clawndagger.interfaces.Drawable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.input.Keyboard;
import org.lwjgl.Sys;
import java.util.LinkedList;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
public class Renderer {
	public float viewX = 0f;
	public float viewY = 0f;
	public float viewZ = 0f;
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
		GL11.glTranslatef(-0.6075f+viewX,-0.45f+viewY,-.2475f+viewZ);
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
		private enum Status {
			UNSTARTED(E->{}),WAITING_FOR_EVENT(Object::notifyAll),PROCESSING_EVENTS(E->{});

			private final Consumer<LinkedList<Consumer<List<Drawable>>>> notify;
			private Status(Consumer<LinkedList<Consumer<List<Drawable>>>>
			notify) {
				this.notify = notify;
			}
		}
		protected List<Drawable> layer = new ArrayList<Drawable>();
		protected LinkedList<Consumer<List<Drawable>>> events = new LinkedList<Consumer<List<Drawable>>>();
		protected Status status = Status.UNSTARTED;
		private void change(Consumer<List<Drawable>> method) {
			synchronized(events){
				events.push(method);
				status.notify.accept(events);
			}
		}
		public void add(Drawable toAdd){
			change(L->L.add(toAdd));
		}
		public void remove(Drawable toRemove){
			change(L->L.remove(toRemove));
		}
		public void run(){
			try{
				while(Gui.isRunning){
					synchronized(events){
						status=Status.WAITING_FOR_EVENT;
						while(Gui.isRunning&&events.isEmpty()){
							events.wait();
						}
						status=Status.PROCESSING_EVENTS;
					}
					pollEvents();
				}
			}
			catch(Exception e0){
				e0.printStackTrace();
			}
		}
		private void pollEvents() {
			Stream.generate(()->{synchronized(layer) {synchronized(events) {
				return events.pollFirst();}}})
			.unordered()
			.takeWhile(S->Gui.isRunning)
			.takeWhile(Objects::nonNull)
			.forEach(C->C.accept(layer));
		}
		public void end(){
			synchronized(events){
				status.notify.accept(events);
			}
		}
		public void load(){
			start();
		}
		public void render(){
			synchronized(layer){
				layer.stream()
				.reduce(new Events.Draw(),(draw,drawable)->drawable.on(draw),(P,N)->N);
			}
		}
	}
}
