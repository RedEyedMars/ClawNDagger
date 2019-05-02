package com.rem.clawndagger.graphics;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.nio.ByteOrder;
import org.lwjgl.input.Cursor;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.lwjgl.input.Keyboard;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Mouse;
import java.nio.IntBuffer;
public class InputHandler extends Thread {
	private enum Status {
		UNSTARTED(()->{throw new RuntimeException("Unable to wait for UNSTARTED status!");}
				 ,()->true),
		WAITING_FOR_INPUT(
				Status::waitingForInput,
				Status::notifyOfInput),
		PROCESSING(()->true,()->true)
		;
		public static Status status = UNSTARTED;
		public final Supplier<Boolean> waiting;
		public final Supplier<Boolean> notifying;
		private Status(Supplier<Boolean> waiting,
				Supplier<Boolean> notifying) {
			this.waiting = waiting;
			this.notifying = notifying;
		}
		public static boolean notifyOfInput() {
			synchronized(status) {
			status.notifyAll();
			status=PROCESSING;
			}
			return true;
		}
		public static boolean waitingForInput() {
			try {
				synchronized(status) {
					while(Gui.isRunning&&status==WAITING_FOR_INPUT){
						status.wait();	
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return Gui.isRunning;
		}
	}
	public static class Listeners{
		public static List<InputHandler.Events.KeyboardEvent.Listener> keyboard =
				new CopyOnWriteArrayList<InputHandler.Events.KeyboardEvent.Listener>();
		public static List<InputHandler.Events.MouseWheelEvent.Listener> mousewheel =
				new CopyOnWriteArrayList<InputHandler.Events.MouseWheelEvent.Listener>();
		public static List<InputHandler.Events.MouseEvent.Listener> mouse =
				new CopyOnWriteArrayList<InputHandler.Events.MouseEvent.Listener>();
	}
	protected static int cursorX = 0;
	protected static int cursorY = 0;
	protected boolean firstMove = true;
	protected static boolean continuousKeyboard = false;
	protected boolean hideNativeCursor = false;
	protected boolean disableNativeCursor = false;

	public void run(){
		Status.status=Status.WAITING_FOR_INPUT;
		Stream.generate(InputHandler::generateEvents)
		.takeWhile(S->Status.status.waiting.get())
		.flatMap(S->S.get())
		.takeWhile(S->Gui.isRunning)
		.flatMap(E->E)
		.takeWhile(S->Gui.isRunning)
		.forEach(E->E.get().handle());
	}
	public void load(DisplayMode displayMode){
		try{
			Keyboard.create();
			if(disableNativeCursor){
				disableNativeCursor(true);
				cursorX=(int)displayMode.getWidth()/2;
				cursorY=(int)displayMode.getHeight()/2;
			}
			if(hideNativeCursor){
				hideNativeCursor(true);
			}
		}
		catch(Exception e0){
			e0.printStackTrace();
			System.err.println("InputHandler.load: "+e0);
		}
		start();
	}
	public static Supplier<Stream<Stream<Supplier<Event>>>> generateEvents(){
		return  ()->
				Stream.of(
						InputHandler.Events.MouseWheelEvent.stream(),
						InputHandler.Events.MouseEvent.stream(),
						InputHandler.Events.MouseEvent.movementStreams(),
						//Keyboard.next()?
						InputHandler.Events.KeyboardEvent.stream()//:
						
						/*Stream.of((Supplier<Event>)()->new InputHandler.Events.KeyboardEvent.Press(Keyboard.getEventKey(),Keyboard.getEventCharacter()))
							  .filter(S->continuousKeyboard&&Keyboard.getEventKeyState())
						*/
						);
	}
	public void handle(){
		if(Status.status==Status.UNSTARTED){
			return;
		}
		Status.status.notifying.get();
	}
	public void end(){
		Status.status.notifying.get();
	}
	public void disableNativeCursor(boolean newNativeCursorValue){
		disableNativeCursor=newNativeCursorValue;
		Mouse.setGrabbed(newNativeCursorValue);
	}
	public void hideNativeCursor(boolean hide){
		hideNativeCursor=hide;
		int canHide = Cursor.getCapabilities()&Cursor.CURSOR_ONE_BIT_TRANSPARENCY;
		if(canHide==0){
			System.err.println("Renderer hideHardwareCursor(): No hardwared cursor support!");
			return ;
		}
		try{
			if(hide){
				Cursor cursor = null;
				int cursorImageCount = 1;
				int cursorWidth = Cursor.getMaxCursorSize();
				int cursorHeight = cursorWidth;
				IntBuffer cursorDelays = null;
				IntBuffer cursorImages=ByteBuffer.allocateDirect(cursorWidth*cursorHeight*cursorImageCount*4).order(ByteOrder.nativeOrder()).asIntBuffer();
				IntStream.range(0,cursorWidth*cursorHeight).forEach(I->cursorImages.put(0x00000000));
				cursorImages.flip();
				cursor=new Cursor(Cursor.getMaxCursorSize(),Cursor.getMaxCursorSize(),Cursor.getMaxCursorSize()/2,Cursor.getMaxCursorSize()/2,cursorImageCount,cursorImages,cursorDelays);
				Mouse.setNativeCursor(cursor);
			}
			else{
				Mouse.setNativeCursor(null);
			}
		}
		catch(Exception e0){
			e0.printStackTrace();
			System.err.println("Renderer hideHardwareCursor(): error");
		}
	}

	public InputHandler (){
		super();
	}
	public static class Events {
		public Events (){
			super();
		}
		public static class MouseWheelEvent extends InputHandler.Event {
			protected int amount = 0;
			public int getAmount(){
				return amount;
			}
			public static Stream<Supplier<Event>> stream() {
				return Stream.of((Supplier<Event>)()->new InputHandler.Events.MouseWheelEvent(Mouse.getDWheel()))
							    .filter(S->Mouse.getDWheel()!=0);
			}
			public void setAmount(int newAmount){
				amount=newAmount;
			}
			public MouseWheelEvent (int amount){
				this.amount=amount;
			}
			public void handle(){
				InputHandler.Listeners.mousewheel
				.forEach(L->L.listen(MouseWheelEvent.this));
			}
			public static interface Listener {
				public void listen(InputHandler.Events.MouseWheelEvent event);
			}
		}
		public static class MouseEvent extends Event{
			public enum Type {
				Move(false,false,InputHandler.Events.MouseEvent.Listener::mouseMove),

				Drag_Left(true,false,InputHandler.Events.MouseEvent.Listener::mouseDrag),
				Drag_Right(false,true,InputHandler.Events.MouseEvent.Listener::mouseDrag),

				Press_Left(true,false,InputHandler.Events.MouseEvent.Listener::mousePress),
				Press_Right(false,true,InputHandler.Events.MouseEvent.Listener::mousePress),

				Release_Left(false,false,InputHandler.Events.MouseEvent.Listener::mouseRelease),
				Release_Right(false,false,InputHandler.Events.MouseEvent.Listener::mouseRelease)
				;
				public final boolean isLeftButton;
				public final boolean isRightButton;
				public final BiConsumer<InputHandler.Events.MouseEvent.Listener,
				InputHandler.Events.MouseEvent> handle;
				private Type(boolean isLeftButton,
						boolean isRightButton,
						BiConsumer<InputHandler.Events.MouseEvent.Listener,
						InputHandler.Events.MouseEvent> handle) {
					this.isLeftButton = isLeftButton;
					this.isRightButton = isRightButton;
					this.handle = handle;
				}
			}


			protected final int cursorX;
			protected final int cursorY;
			private Type type;

			public MouseEvent (
					int cursorX,int cursorY,
					MouseEvent.Type type){
				this.cursorX=cursorX;
				this.cursorY=cursorY;
				this.type = type;
			}
			public static Stream<Supplier<Event>> movementStreams() {
				return
						Stream.of(
						Stream.of((Supplier<Event>)()->InputHandler.Events.MouseEvent.move(Mouse.getX(),Mouse.getY()))
							  .filter(S->Mouse.getDX()!=0||Mouse.getDY()!=0||Mouse.getDWheel()!=0),
						Stream.of((Supplier<Event>)()->InputHandler.Events.MouseEvent.drag(0,Mouse.getX(),Mouse.getY()))
							  .filter(S->Mouse.isButtonDown(0)),
					    Stream.of((Supplier<Event>)()->InputHandler.Events.MouseEvent.drag(1,Mouse.getX(),Mouse.getY()))
					    	  .filter(S->Mouse.isButtonDown(1)))
						.flatMap(S->S);
			}
			public static Stream<Supplier<Event>> stream() {
				return Stream.generate(()->Mouse.next())
						.takeWhile(B->B)
						.map(B->Mouse.getEventButtonState())
						.map(S->S?
								()->InputHandler.Events.MouseEvent.press(Mouse.getEventButton(),Mouse.getX(),Mouse.getY()):
								()->InputHandler.Events.MouseEvent.release(Mouse.getEventButton(),Mouse.getX(),Mouse.getY()));
			}
			public static interface Listener {
				public void mouseDrag(InputHandler.Events.MouseEvent event);
				public void mousePress(InputHandler.Events.MouseEvent event);
				public void mouseRelease(InputHandler.Events.MouseEvent event);
				public void mouseMove(InputHandler.Events.MouseEvent event);
			}

			public void handle(){
				InputHandler.Listeners.mouse.forEach(
						L->type.handle.accept(L,MouseEvent.this));
			}
			public static InputHandler.Events.MouseEvent move(int cursorX,int cursorY){
				return new InputHandler.Events.MouseEvent(cursorX,cursorY,
						MouseEvent.Type.Move
						);
			}
			public static InputHandler.Events.MouseEvent drag(
					int button,int cursorX,int cursorY){
				return new InputHandler.Events.MouseEvent(cursorX,cursorY,
						button==0?MouseEvent.Type.Drag_Left:
							MouseEvent.Type.Drag_Right
						);
			}
			public static InputHandler.Events.MouseEvent press(
					int button,int cursorX,int cursorY){
				return new InputHandler.Events.MouseEvent(cursorX,cursorY,
						button==0?MouseEvent.Type.Press_Left:
							MouseEvent.Type.Press_Right
						);
			}
			public static InputHandler.Events.MouseEvent release(
					int button,int cursorX,int cursorY){
				return new InputHandler.Events.MouseEvent(cursorX,cursorY,
						button==0?MouseEvent.Type.Release_Left:
							MouseEvent.Type.Release_Right
						);
			}
		}
		public static abstract class KeyboardEvent extends InputHandler.Event {
			protected int keyInt = 0;
			protected char keyChar = 0;
			public int getKeyInt(){
				return keyInt;
			}
			public static Stream<Supplier<Event>> stream() {
				return Stream.generate(()->Keyboard.next())
						.takeWhile(B->B)
						.map(B->Keyboard.getEventKeyState())
						.map(S->S?
						()->new InputHandler.Events.KeyboardEvent.Press(Keyboard.getEventKey(),Keyboard.getEventCharacter())
						:
						()->new InputHandler.Events.KeyboardEvent.Release(Keyboard.getEventKey())
						);
			}
			public void setKeyInt(int newKeyInt){
				keyInt=newKeyInt;
			}
			public char getKeyChar(){
				return keyChar;
			}
			public void setKeyChar(char newKeyChar){
				keyChar=newKeyChar;
			}
			public KeyboardEvent (int keyInt,char keyChar){
				this.keyInt=keyInt;
				this.keyChar=keyChar;
			}
			public boolean isPress(){
				return false;
			}
			public boolean isRelease(){
				return false;
			}
			public KeyboardEvent (){
				super();
			}
			public static interface Listener {
				public void listen(InputHandler.Events.KeyboardEvent.Press event);
				public void listen(InputHandler.Events.KeyboardEvent.Release event);
			}
			public static class Press extends InputHandler.Events.KeyboardEvent {
				public Press (int keyInt,char keyChar){
					super(keyInt,keyChar);
				}
				public boolean isPress(){
					return true;
				}
				public void handle(){
					InputHandler.Listeners.keyboard.forEach(L->L.listen(Press.this));
				}
				public Press (){
					super();
				}
			}
			public static class Release extends InputHandler.Events.KeyboardEvent {
				public Release (int keyInt,char keyChar){
					super(keyInt,keyChar);
				}
				public Release (int keyInt){
					super(keyInt,(char)0);
				}
				public boolean isRelease(){
					return true;
				}
				public void handle(){
					InputHandler.Listeners.keyboard.forEach(L->L.listen(Release.this));
				}
				public Release (){
					super();
				}
			}
		}
	}
	public static abstract class Event {
		public abstract void handle();
	}
}
