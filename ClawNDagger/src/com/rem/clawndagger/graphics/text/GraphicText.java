package com.rem.clawndagger.graphics.text;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.rem.clawndagger.entities.motion.Rectangle;
import com.rem.clawndagger.game.events.Events.Draw;
import com.rem.clawndagger.game.events.Events.Draw.Focus;
import com.rem.clawndagger.game.events.Events.Draw.Unfocus;
import com.rem.clawndagger.graphics.Renderer;
import com.rem.clawndagger.graphics.images.Image;
import com.rem.clawndagger.graphics.images.ImageTemplate;
import com.rem.clawndagger.graphics.images.animation.Animation;
import com.rem.clawndagger.interfaces.Drawable;

public class GraphicText extends Rectangle implements Drawable, Drawable.Focusable {

	public enum Justification {
		LEFT((T,L)->0.0),
		CENTER((T,L)->T.getWidth()/2f-L.getCharWidth()/2f),
		RIGHT((T,L)->T.getWidth()-L.getCharWidth());
		

		public final BiFunction<GraphicText,GraphicLine,Double> justifiedX;
		private Justification(BiFunction<GraphicText,GraphicLine,Double> justifiedX){
			this.justifiedX = justifiedX;
		}
	}
	public enum FontSize {
		TALLER(1.0f,1.4f),
		REGULAR(1.0f,1.0f),
		LARGE(1.4f,3.2f);
		public final float width;
		public final float height;
		private FontSize(float width, float height) {
			this.width = width;
			this.height = height;
		}
	}

	private float visualW=1f;
	private float visualH=1f;

	protected String text;
	protected List<GraphicLine> lines = new ArrayList<GraphicLine>();

	protected int charIndex=0;
	protected int lineIndex = 0;
	private GraphicText self = this;
	private ImageTemplate font;
	private Renderer.Layer layer = Renderer.topLayer;

	private Justification justified = Justification.LEFT;
	protected Animation blinker = null;
	public GraphicText addBlinker(){
		blinker = new Animation(Renderer.topLayer ,new Rectangle(0.0,0.0,0.005f,0.025f) {
			public double getX(){
				if(lineIndex<lines.size()&&lines.get(lineIndex).length()>0&&charIndex>1){
					int horizontalIndex = Math.min(charIndex-2, lines.get(lineIndex).length()-1);
					return	GraphicText.this.getX()+
							lines.get(lineIndex).chars.get(horizontalIndex).getX();

				}
				else if(lineIndex<lines.size()&&lines.get(lineIndex).length()>0&&charIndex==1){
					return GraphicText.this.getX()+
							lines.get(lineIndex).chars.get(0).getWidth()*
							lines.get(lineIndex).chars.get(0).getWidthValue()*visualW;
				}
				return GraphicText.this.getX();
			}
			public double getY(){
				return GraphicText.this.getY()+0.005f-lineIndex*0.025f;
			}
		},
				font,
				new int[]{0,0},
				new int[]{1,0}){
			
		};
		return this;
	}

	public GraphicText(ImageTemplate font, String text) {
		super(0,0,0,0);
		this.font = font;
		this.text = text;
		Arrays.stream(
				text.split("\r?\n"))
		.map(GraphicLine::new)
		.forEach(lines::add);
		this.setWidth(1f);
		this.setHeight(1f);
		this.setX(0f);
		this.setY(0.97f);

	}


	public void change(String text){
		if(text==null)text="";
		this.text = text;
		String[] lines = text.split("\r?\n");
		int size = this.lines.size();
		if(size<lines.length){
			IntStream.range(size,lines.length).boxed()
			.map(I->lines[I])
			.map(GraphicLine::new)
			.forEach(this.lines::add);
		}
		else if(size>lines.length){
			IntStream.range(lines.length,size).boxed()
			.map(I->GraphicText.this.lines.get(I))
			.forEach(L->L.change(""));
		}
		IntStream.range(0,lines.length).forEach(
				I->this.lines.get(I).change(lines[I]));
	}


	protected GraphicLine getLine(int i) {
		IntStream.range(i,lines.size())
		.boxed()
		.map(I->new GraphicLine(""))
		.forEach(this.lines::add);
		return lines.get(i);
	}
	public void setJustified(Justification justified){
		this.justified = justified;
		
	}
	public boolean isJustified(Justification justified){
		return this.justified == justified;
	}

	public void setFontSize(FontSize fontSize){
		this.visualW = fontSize.width;
		this.visualH = fontSize.height;
	}

	@Override
	public Draw on(Draw draw) {
		return lines.stream().reduce(draw,(D,L)->L.on(D),(P,N)->N);
	}
	private class GraphicChar extends Rectangle implements Drawable, Drawable.Focusable {
		private float value;
		private Image image;

		public GraphicChar(char c) {
			super(0,0,0.1,0.1);
			font.create(this,((int)c)-32,0);
			/*
			super("squares",layer);
			if(sid>15)sid=0;
			setFrame(sid++);
			if(sid>15)sid=0;*/
			setValue(c);
		}

		public float getWidthValue() {
			return this.value;
		}

		public void change(char c) {
			Renderer.remove(this, layer);
			setValue(c);
		}
		/*
		@Override
		public void setFrame(int frame){
			super.setFrame(frame%16);
		}*/

		private void setValue(char c){
			if(c=='\t'){
				//value = 4*Hub.renderer.letterWidths.get(font).get(' ')*14/16;
			}
			else {
				//value = Hub.renderer.letterWidths.get(font).get(c)*14/16;
			}
		}
		/*
		@Override
		public void resize(float x, float y){
			super.resize(0.025f*visualW,0.025f*visualH);
		}*/

		@Override
		public Draw on(Draw draw) {
			return image.on(draw);
		}

		@Override
		public Boolean on(Focus focus) {
			layer.add(this);
			return true;
		}

		@Override
		public Boolean on(Unfocus focus) {
			layer.remove(this);
			return false;
		}
	}

	protected class GraphicLine extends Rectangle implements Drawable, Drawable.Focusable {
		private String text;
		private float offset = 0f;
		private List<GraphicChar> chars = new ArrayList<GraphicChar>();
		private int length;
		private int index = GraphicText.this.lines.size();

		public double getY(){
			return GraphicText.this.getY()+0.025f*(-index)*visualH;
		}
		public GraphicLine(String text) {
			super(0,0,1f,1f);
			this.text = text;
			this.length = this.text.length();
			text.chars()
			.boxed()
			.map(I->((char)(int)I))
			.map(GraphicChar::new)
			.forEach(GraphicLine.this.chars::add);
		}
		public void change(String string) {
			this.text = string;
			this.length = this.text.length();
			int size = chars.size();
			if(size<string.length()){
				string.chars()
				.mapToObj(I->new GraphicChar((char)I))
				.forEach(this.chars::add);
			}
			IntStream.range(0, size)
			.takeWhile(I->I<string.length())
			.forEach(I->chars.get(I).change(string.charAt(I)));
			/*
			for(int i=0;i<size;++i){
				if(i<string.length()){
					this.chars.get(i).change(string.charAt(i));
					//this.chars.get(i).turnOn();
				}
				else {
					//this.chars.get(i).turnOff();
				}
			}*/
		}
		public double offsetX(int index){
			if(index<chars.size()){
				return chars.get(index-1).getWidth()*
						chars.get(index-1).getWidthValue()*visualW;
			}
			else {
				return -1.0;
			}
		}
		public int length() {
			return length;
		}
		public String getText() {
			return text;
		}
		public double getCharWidth(){
			return chars.stream()
					.reduce(0.0,
							(F,C)->F+C.getWidth()*C.getWidthValue()*visualW,
							(P,N)->N);
		}
		private class CharHolder {
			int i;
			double a;
			public CharHolder(int i, double prev, List<GraphicChar> list, double visualW) {
				this.i = i;
				if(i<list.size())
					this.a = prev+list.get(i).getWidth()*list.get(i).getWidthValue()*visualW;
			}
		}
		public String wrap(float max) {
			if(chars.size()==0) {
				return "";
			}
			int i = Stream.iterate(
					new CharHolder(0,0.0,chars,visualW),
					C->C.a<=max,
					C->new CharHolder(C.i+1,C.a,chars,visualW))
					.takeWhile(C->C.i<chars.size())
					.findFirst()
					.or(()->
					Optional.of(new CharHolder(chars.size(),0.0,chars,visualW))).get().i;
			if(i>=text.length()-1){
				return "";
			}
			else {
				String excess = text.substring(i+1);
				change(text.substring(0, i+1));
				return excess;
			}
		}
		@Override
		public Draw on(Draw draw) {
			return this.chars.stream().reduce(draw,(D,C)->C.on(draw),(P,N)->N);
		}

		@Override
		public Boolean on(Focus focus) {
			this.chars.stream().forEach(C->C.on(focus));
			return true;
		}

		@Override
		public Boolean on(Unfocus focus) {
			this.chars.stream().forEach(C->C.on(focus));
			return true;
		}
	}





	@Override
	public Boolean on(Focus focus) {
		lines.forEach(L->L.on(focus));
		return true;
	}

	@Override
	public Boolean on(Unfocus focus) {
		lines.forEach(L->L.on(focus));
		return true;
	}
	
	public Map<String,List<Float>> letterWidths= new HashMap<String,List<Float>>();
	public BufferedImage createCharImage(String fontName, Font font, int size, float[] fgColor, float[] bgColor) {
		Color bg = bgColor==null? new Color(0,0,0,0) : (bgColor.length==3? new Color(bgColor[0],bgColor[1],bgColor[2],1) : new Color(bgColor[0],bgColor[1],bgColor[2],bgColor[3]));
		Color fg = fgColor==null? new Color(1,1,1,1) : (fgColor.length==3? new Color(fgColor[0],fgColor[1],fgColor[2],1) : new Color(fgColor[0],fgColor[1],fgColor[2],fgColor[3]));
		boolean isAntiAliased = true;
		boolean usesFractionalMetrics = false;

		// get size of texture image needed to hold largest character of this font
		//int maxCharSize = getFontSize(font);
		int imgSizeW = size*16;
		int imgSizeH = size*16;
		if (imgSizeW > 2048) {
			System.err.println("GLFont.createCharImage(): texture size will be too big (" + imgSizeW + ") Make the font size smaller.");
			return null;
		}

		// we'll draw text into this image
		BufferedImage image = new BufferedImage(imgSizeW, imgSizeH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		// Clear image with background color (make transparent if color has alpha value)
		if (bg.getAlpha() < 255) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, (float)bg.getAlpha()/255f));
		}
		g.setColor(bg);
		g.fillRect(0,0,imgSizeW,imgSizeH);

		// prepare to draw character in foreground color
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.setColor(fg);
		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, isAntiAliased? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, usesFractionalMetrics? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// place the character (on baseline, centered horizontally)
		FontMetrics fm = g.getFontMetrics();
		int cwidth = fm.charWidth('M');
		int height = fm.getHeight();
		int ascent = fm.getAscent();
		int hborder = 2;
		int vborder = height-ascent/2-1;

		char[] data = new char[128];
		for(int i=0;i<128;++i){
			data[i]=((char)i);
		}
		int index = 0;
		for(int y=0;y<8;++y){
			for(int x=0;x<16;++x){
				letterWidths.get(fontName).add((float) (fm.charWidth(data[index]))/cwidth);
				g.drawChars(data, index, 1, hborder+x*size, vborder+y*size+1);
				++index;
			}
		}

		g.dispose();
		return image;
	}
}
