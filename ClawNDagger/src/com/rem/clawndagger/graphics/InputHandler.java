package com.rem.clawndagger.graphics;
import java.util.ArrayList;
import java.nio.ByteOrder;
import org.lwjgl.input.Cursor;
import java.util.List;
import org.lwjgl.input.Keyboard;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Mouse;
import java.nio.IntBuffer;
public class InputHandler extends Thread {
	public static int UNSTARTED = -1;
	public static int WAITING_FOR_EVENTS = 0;
	public static int PROCESSING_EVENTS = 1;
	protected int status = UNSTARTED;
	protected List<InputHandler.Event> events = new ArrayList<InputHandler.Event>();
	protected int cursorX = 0;
	protected int cursorY = 0;
	protected boolean firstMove = true;
	protected boolean continuousKeyboard = false;
	protected boolean hideNativeCursor = false;
	protected boolean disableNativeCursor = false;
	public int getStatus(){
		return status;
	}
	public void setStatus(int newStatus){
		status=newStatus;
	}
	public List<InputHandler.Event> getEvents(){
		return events;
	}
	public void setEvents(List<InputHandler.Event> newEvents){
		events=newEvents;
	}
	public int getCursorX(){
		return cursorX;
	}
	public void setCursorX(int newCursorX){
		cursorX=newCursorX;
	}
	public int getCursorY(){
		return cursorY;
	}
	public void setCursorY(int newCursorY){
		cursorY=newCursorY;
	}
	public boolean getFirstMove(){
		return firstMove;
	}
	public void setFirstMove(boolean newFirstMove){
		firstMove=newFirstMove;
	}
	public boolean getContinuousKeyboard(){
		return continuousKeyboard;
	}
	public void setContinuousKeyboard(boolean newContinuousKeyboard){
		continuousKeyboard=newContinuousKeyboard;
	}
	public boolean getHideNativeCursor(){
		return hideNativeCursor;
	}
	public void setHideNativeCursor(boolean newHideNativeCursor){
		hideNativeCursor=newHideNativeCursor;
	}
	public boolean getDisableNativeCursor(){
		return disableNativeCursor;
	}
	public void setDisableNativeCursor(boolean newDisableNativeCursor){
		disableNativeCursor=newDisableNativeCursor;
	}
	public static void addMouseListener(InputHandler.Events.MouseEvent.Listener toAdd){
		synchronized(InputHandler.Events.MouseEvent.listeners){
			InputHandler.Events.MouseEvent.listeners.add(toAdd);
		}
	}
	public static void removeMouseListener(InputHandler.Events.MouseEvent.Listener toRemove){
		synchronized(InputHandler.Events.MouseEvent.listeners){
			InputHandler.Events.MouseEvent.listeners.remove(toRemove);
		}
	}
	public static void addMouseWheelListener(InputHandler.Events.MouseWheelEvent.Listener toAdd){
		synchronized(InputHandler.Events.MouseWheelEvent.listeners){
			InputHandler.Events.MouseWheelEvent.listeners.add(toAdd);
		}
	}
	public static void removeWheelMouseListener(InputHandler.Events.MouseWheelEvent.Listener toRemove){
		synchronized(InputHandler.Events.MouseWheelEvent.listeners){
			InputHandler.Events.MouseWheelEvent.listeners.remove(toRemove);
		}
	}
	public static void addKeyboardListener(InputHandler.Events.KeyboardEvent.Listener toAdd){
		synchronized(InputHandler.Events.KeyboardEvent.listeners){
			InputHandler.Events.KeyboardEvent.listeners.add(toAdd);
		}
	}
	public static void removeKeyboardListener(InputHandler.Events.KeyboardEvent.Listener toRemove){
		synchronized(InputHandler.Events.KeyboardEvent.listeners){
			InputHandler.Events.KeyboardEvent.listeners.remove(toRemove);
		}
	}
	public void run(){
		try{
			while(Gui.isRunning){
				status=WAITING_FOR_EVENTS;
				synchronized(events){
					while(Gui.isRunning&&events.isEmpty()){
						events.wait();
					}
				}
				status=PROCESSING_EVENTS;
				while(events.isEmpty()==false){
					events.remove(0).handle();
				}
			}
		}
		catch(InterruptedException e0){
			e0.printStackTrace();
		}
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
	public void handle(){
		int mouseDX = Mouse.getDX();
		int mouseDY = Mouse.getDY();
		int mouseDW = Mouse.getDWheel();
		synchronized(events){
			if(mouseDX!=0||mouseDY!=0||mouseDW!=0){
				cursorX=Mouse.getX();
				cursorY=Mouse.getY();
				events.add(new InputHandler.Events.MouseEvent.Move(cursorX,cursorY));
			}
			if(mouseDW!=0){
				events.add(new InputHandler.Events.MouseWheelEvent(mouseDW));
			}
			while(Mouse.next()){
				if(Mouse.getEventButtonState()){
					events.add(new InputHandler.Events.MouseEvent.Press(Mouse.getEventButton(),cursorX,cursorY));
				}
				else{
					events.add(new InputHandler.Events.MouseEvent.Release(Mouse.getEventButton(),cursorX,cursorY));
				}
			}
			if(Mouse.isButtonDown(0)){
				events.add(new InputHandler.Events.MouseEvent.Drag(0,cursorX,cursorY));
			}
			if(Mouse.isButtonDown(1)){
				events.add(new InputHandler.Events.MouseEvent.Drag(1,cursorX,cursorY));
			}
			if(Keyboard.next()){
				if(Keyboard.getEventKeyState()){
					events.add(new InputHandler.Events.KeyboardEvent.Press(Keyboard.getEventKey(),Keyboard.getEventCharacter()));
				}
				else{
					events.add(new InputHandler.Events.KeyboardEvent.Release(Keyboard.getEventKey()));
				}
				while(Keyboard.next()){
					if(Keyboard.getEventKeyState()){
						events.add(new InputHandler.Events.KeyboardEvent.Press(Keyboard.getEventKey(),Keyboard.getEventCharacter()));
					}
					else{
						events.add(new InputHandler.Events.KeyboardEvent.Release(Keyboard.getEventKey()));
					}
				}
			}
			else if(continuousKeyboard&&Keyboard.getEventKeyState()){
				events.add(new InputHandler.Events.KeyboardEvent.Press(Keyboard.getEventKey(),Keyboard.getEventCharacter()));
			}
			if(status==WAITING_FOR_EVENTS){
				events.notifyAll();
			}
		}
	}
	public void end(){
		if(status==WAITING_FOR_EVENTS){
			synchronized(events){
				events.notifyAll();
			}
		}
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
				IntBuffer cursorImages = null;
				IntBuffer cursorDelays = null;
				cursorImages=ByteBuffer.allocateDirect(cursorWidth*cursorHeight*cursorImageCount*4).order(ByteOrder.nativeOrder()).asIntBuffer();
				for(int i = 0;i<cursorWidth;++i){
					for(int j = 0;j<cursorHeight;++j){
						cursorImages.put(0x00000000);
					}
				}
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
			public static List<InputHandler.Events.MouseWheelEvent.Listener> listeners = new ArrayList<InputHandler.Events.MouseWheelEvent.Listener>();
			protected int amount = 0;
			public int getAmount(){
				return amount;
			}
			public void setAmount(int newAmount){
				amount=newAmount;
			}
			public MouseWheelEvent (int amount){
				this.amount=amount;
			}
			public void handle(){
				synchronized(InputHandler.Events.MouseWheelEvent.listeners){
					for(InputHandler.Events.MouseWheelEvent.Listener listener:InputHandler.Events.MouseWheelEvent.listeners){
						listener.listen(this);
					}
				}
			}
			public MouseWheelEvent (){
				super();
			}
			public static interface Listener {
				public void listen(InputHandler.Events.MouseWheelEvent event);
			}
		}
		public static class MouseEvent extends InputHandler.Event {
			public static List<InputHandler.Events.MouseEvent.Listener> listeners = new ArrayList<InputHandler.Events.MouseEvent.Listener>();
			protected int cursorX = 0;
			protected int cursorY = 0;
			protected boolean isLeftButton = false;
			protected boolean isRightButton = false;
			public int getCursorX(){
				return cursorX;
			}
			public void setCursorX(int newCursorX){
				cursorX=newCursorX;
			}
			public int getCursorY(){
				return cursorY;
			}
			public void setCursorY(int newCursorY){
				cursorY=newCursorY;
			}
			public boolean getIsLeftButton(){
				return isLeftButton;
			}
			public void setIsLeftButton(boolean newIsLeftButton){
				isLeftButton=newIsLeftButton;
			}
			public boolean getIsRightButton(){
				return isRightButton;
			}
			public void setIsRightButton(boolean newIsRightButton){
				isRightButton=newIsRightButton;
			}
			public MouseEvent (int cursorX,int cursorY){
				this.cursorX=cursorX;
				this.cursorY=cursorY;
			}
			public boolean isDrag(){
				return false;
			}
			public boolean isMove(){
				return false;
			}
			public boolean isPress(){
				return false;
			}
			public boolean isRelease(){
				return false;
			}
			public MouseEvent (int cursorX,int cursorY,boolean isLeftButton,boolean isRightButton){
				super();
				this.cursorX=cursorX;
				this.cursorY=cursorY;
				this.isLeftButton=isLeftButton;
				this.isRightButton=isRightButton;
			}
			public MouseEvent (){
				super();
			}
			public static interface Listener {
				public void listen(InputHandler.Events.MouseEvent.Drag event);
				public void listen(InputHandler.Events.MouseEvent.Press event);
				public void listen(InputHandler.Events.MouseEvent.Release event);
				public void listen(InputHandler.Events.MouseEvent.Move event);
			}
			public static class Move extends InputHandler.Events.MouseEvent {
				public Move (int cursorX,int cursorY){
					super(cursorX,cursorY);
				}
				public boolean isMove(){
					return true;
				}
				public void handle(){
					synchronized(InputHandler.Events.MouseEvent.listeners){
						for(InputHandler.Events.MouseEvent.Listener listener:InputHandler.Events.MouseEvent.listeners){
							listener.listen(this);
						}
					}
				}
				public Move (){
					super();
				}
				public Move (int cursorX,int cursorY,boolean isLeftButton,boolean isRightButton){
					super(cursorX,cursorY,isLeftButton,isRightButton);
				}
			}
			public static class Drag extends InputHandler.Events.MouseEvent {
				public Drag (int cursorX,int cursorY){
					super(cursorX,cursorY);
				}
				public Drag (int button,int cursorX,int cursorY){
					super(cursorX,cursorY);
					setIsLeftButton(button==0);
					setIsRightButton(button==1);
				}
				public boolean isDrag(){
					return true;
				}
				public void handle(){
					synchronized(InputHandler.Events.MouseEvent.listeners){
						for(InputHandler.Events.MouseEvent.Listener listener:InputHandler.Events.MouseEvent.listeners){
							listener.listen(this);
						}
					}
				}
				public Drag (){
					super();
				}
				public Drag (int cursorX,int cursorY,boolean isLeftButton,boolean isRightButton){
					super(cursorX,cursorY,isLeftButton,isRightButton);
				}
			}
			public static class Press extends InputHandler.Events.MouseEvent {
				public Press (int button,int cursorX,int cursorY){
					super(cursorX,cursorY);
					setIsLeftButton(button==0);
					setIsRightButton(button==1);
				}
				public boolean isPress(){
					return true;
				}
				public void handle(){
					synchronized(InputHandler.Events.MouseEvent.listeners){
						for(InputHandler.Events.MouseEvent.Listener listener:InputHandler.Events.MouseEvent.listeners){
							listener.listen(this);
						}
					}
				}
				public Press (){
					super();
				}
				public Press (int cursorX,int cursorY,boolean isLeftButton,boolean isRightButton){
					super(cursorX,cursorY,isLeftButton,isRightButton);
				}
			}
			public static class Release extends InputHandler.Events.MouseEvent {
				public Release (int button,int cursorX,int cursorY){
					super(cursorX,cursorY);
					setIsLeftButton(button==0);
					setIsRightButton(button==1);
				}
				public boolean isRelease(){
					return true;
				}
				public void handle(){
					synchronized(InputHandler.Events.MouseEvent.listeners){
						for(InputHandler.Events.MouseEvent.Listener listener:InputHandler.Events.MouseEvent.listeners){
							listener.listen(this);
						}
					}
				}
				public Release (){
					super();
				}
				public Release (int cursorX,int cursorY,boolean isLeftButton,boolean isRightButton){
					super(cursorX,cursorY,isLeftButton,isRightButton);
				}
			}
		}
		public static class KeyboardEvent extends InputHandler.Event {
			public static List<InputHandler.Events.KeyboardEvent.Listener> listeners = new ArrayList<InputHandler.Events.KeyboardEvent.Listener>();
			protected int keyInt = 0;
			protected char keyChar = 0;
			public int getKeyInt(){
				return keyInt;
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
					synchronized(InputHandler.Events.KeyboardEvent.listeners){
						for(InputHandler.Events.KeyboardEvent.Listener listener:InputHandler.Events.KeyboardEvent.listeners){
							listener.listen(this);
						}
					}
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
					synchronized(InputHandler.Events.KeyboardEvent.listeners){
						for(InputHandler.Events.KeyboardEvent.Listener listener:InputHandler.Events.KeyboardEvent.listeners){
							listener.listen(this);
						}
					}
				}
				public Release (){
					super();
				}
			}
		}
	}
	public static class Event {
		public void handle(){
		}
		public Event (){
			super();
		}
	}
}
