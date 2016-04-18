package uk.fls.h2n0.main.util;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;

public class Renderer {

	
	private BufferedImage img;
	
	private int[] pixles;
	private boolean[] dirty;
	private int w,h;
	
	
	private int[][] font;
	private final String letters = "abcdefghijklmnopqrstuvwxyz0123456789.,?!";
	private int[][] frame;
	
	private int xOff, yOff;
	
	public Renderer(BufferedImage img){
		this.img = img;
		this.w = img.getWidth();
		this.h = img.getHeight();
		
		this.pixles = new int[w * h];
		this.dirty = new boolean[h];
		
		this.font = new int[letters.length()][];
		BufferedImage fontImage = new SplitImage("/images/other/font.png").load();
		for(int i = 0; i < letters.length(); i++){
			int x = i % 26;
			int y = i / 26;
			this.font[i] = Loader.getPixels(fontImage, x, y, 7);
		}
		
		this.frame = new int[9][];
		BufferedImage frameImage = new SplitImage("/images/other/frame.png").load();
		for(int i = 0; i < 9; i++){
			int x = i % 3;
			int y = i / 3;
			this.frame[i] = Loader.getPixels(frameImage, x, y, 9);
		}
		
		setOffset(0, 0);
	}
	
	public void finaliseRender(){
		for(int y = 0; y < this.h; y++){
			if(this.dirty[y]){
				int[] row = getPixels(y);
				img.setRGB(0, y, w, 1, row, 0, w);
			}
		}
		
		reset();
	}
	
	private int[] getPixels(int y){
		int[] res = new int[this.w];
		for(int i = 0; i < this.w; i++){
			res[i] = this.pixles[i + y * this.w];
		}
		return res;
	}
	
	public void setPixel(int x,int y,int c){
		x += this.xOff;
		y += this.yOff;
		if(!isValid(x, y))return;
		if(this.pixles[x + y * this.w] == c || c < 0 || c == makeRGB(125,0,125) || c == makeRGB(255,0,255)) return;
		this.pixles[x + y * this.w] = c;
		this.dirty[y] = true;
	}
	
	public void renderSection(int[] data,int x,int y, int xs){
		for(int i = 0; i < xs * xs; i++){
			int xx = i % xs;
			int yy = i / xs;
			setPixel(x + xx,y + yy, data[xx + yy * xs]);
		}
	}
	
	public void shade(int x,int y){
		x += this.xOff;
		y += this.yOff;
		if(!isValid(x, y))return;
		int c = this.pixles[x + y * this.w];
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c) & 0xFF;
		int rgb = getShadedColor(r, g, b, 0.7f);
		setPixel(x - this.xOff, y - this.yOff, rgb);
	}
	
	public void lighten(int x, int y){
		x += this.xOff;
		y += this.yOff;
		if(!isValid(x, y))return;
		int c = this.pixles[x + y * this.w];
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c) & 0xFF;
		int rgb = getShadedColor(r, g, b, 1/0.7f);
		setPixel(x - this.xOff, y - this.yOff, rgb);
	}
	
	public int getShadedColor(int r,int g,int b, float amt){
		r *= amt;
		g *= amt;
		b *= amt;
		return makeRGB(r,g,b);
	}
	
	private boolean isValid(int x, int y){
		if(x < 0 || y < 0 || x >= w || y >= h)return false;
		else return true;
	}
	
	public int makeRGB(int r, int g, int b){
		int rgb =  (r << 16) | (g << 8) | b;
		return rgb;
	}
	
	private void reset(){
		for(int i = 0; i < this.dirty.length; i++){
			this.dirty[i] = false;
		}
	}
	
	public void fill(int c){
		for(int i = 0; i < w * h; i++){
			this.setPixel(i % w, i / w, c);
		}
	}
	
	public void checkerBoard(int x, int y, int w, int h){
		int r = 117;
		int g = 163;
		int b = 0;
		checkerBoard(x,y,w,h,r,g,b);
	}
	
	public void checkerBoard(int x,int y,int w, int h, int r, int g, int b){
		int rgb = makeRGB(r,g,b);
		int shade = getShadedColor(r,g,b, 0.7f);
		int size = 20;
		
		x = x/size;
		y = y/size*2;
		for(int xx = x; xx <= x + w + 1; xx ++){
			for(int yy = y; yy <= y + h/2 + 1; yy ++){
				boolean sh = (xx + yy) % 2 == 1;
				for(int i = 0; i < size * (size/2); i++){
					int dx = (xx * size) + (i % size);
					int dy = (yy * size/2) + (i / size);
					setPixel(dx, dy, sh?shade:rgb);
				}
			}
		}
	}
	
	public void shadeCircle(int x,int y,int r){
		
		int top = y - r;
		int left = x - r;
		
		for(int dx = 0; dx < r * 2; dx++){
			for(int dy = 0; dy < r * 2; dy++){
				
				int px = left + dx;
				int py = top + dy;
				
				int tx = px - x;
				int ty = py - y;
				
				if((tx * tx) + (ty * ty) < (r * r)-16)shade(px,py);
			}
		}
		
	}
	
	public void shadeElipse(int x,int y, int w, int h){
		w += 3;
		x ++;
		for(int yy = -h+1; yy < h; yy++){
			for(int xx = -w ; xx <= w; xx++){
				if(xx * xx * h * h + yy * yy * w * w <= h * h * w * w){
					int dx = (x) + xx;
					shade(dx, y + yy);
				}
			}
		}
	}
	
	public void lightenElipse(int x,int y, int w, int h){
		w += 3;
		x ++;
		for(int yy = -h+1; yy < h; yy++){
			for(int xx = -w ; xx <= w; xx++){
				if(xx * xx * h * h + yy * yy * w * w <= h * h * w * w){
					int dx = (x) + xx;
					lighten(dx, y + yy);
				}
			}
		}
	}
	
	public void renderString(String words, int x, int y){
		words = words.trim().toLowerCase();
		for(int i = 0; i < words.length(); i++){
			String c = words.substring(i,i+1);
			if(c.equals(" "))continue;
			renderSection(this.font[letters.indexOf(c)], x + i * 8, y, 7);
		}
	}
	
	public void renderFrame(int x,int y,int w, int h){
		for(int xx = -1; xx < w + 1; xx++){
			for(int yy = -1; yy < h + 1; yy++){
				int dx = 1;
				int dy = 1;
				if(xx == -1)dx --;
				if(yy == -1)dy--;
				
				if(xx == w)dx++;
				if(yy == h)dy++;
				renderSection(this.frame[dx + dy * 3],x + (xx * 9), y + (yy * 9),9);
			}
		}
	}
	
	public void setOffset(int xo,int yo){
		this.xOff = xo;
		this.yOff = yo;
	}
	
	public void renderTextInFrame(String msg, int x, int y){
		int xl  = (msg.length() * 7)/9 + 1;
		renderFrame(x + ((xl)/2),y + 3,xl,0);
		renderString(msg,x,y);
	}
}
