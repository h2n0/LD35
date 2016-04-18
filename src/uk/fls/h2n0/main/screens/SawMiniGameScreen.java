package uk.fls.h2n0.main.screens;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;
import fls.engine.main.screen.Screen;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class SawMiniGameScreen extends Screen{
	
	public int[][] bg;
	
	public int[][] logParts;
	public int[][] tools;
	
	private Renderer r;
	public SawMiniGameScreen(GameScreen s, Renderer r){
		this.bg = new int[1][];
		this.logParts = new int[4][];
		this.tools = new int[3][];
		BufferedImage b = new SplitImage("/images/other/mini/stands.png").load();
		this.bg[0] = Loader.getPixels(b, 0, 0, 180, 162);
		this.r = r;
		
		for(int i = 0; i < 4; i++)this.logParts[i] = Loader.getPixels(new SplitImage("/images/other/mini/wood.png").load(), i, 0, 6, 15);
		for(int i = 0; i < 3; i++)this.tools[i] = Loader.getPixels(new SplitImage("/images/other/mini/tools.png").load(), i, 0, 64, 64);
	}

	@Override
	public void update() {
		
	}

	
	int l = 10;
	int xOff = 0;
	int xDel = 0;
	int toolFrame = 0;
	int toolFrameDel = 0;
	@Override
	public void render(Graphics g) {
		
		int tx = 200 - (180/2);
		int ty = 300 - 200;
		r.fill(r.makeRGB(12, 12, 12));
		for(int xx = 0; xx < 180; xx++){
			for(int yy = 0; yy < 162; yy++){
				r.setPixel(tx + xx, ty + yy, this.bg[0][xx + yy * 180]);
			}
		}

		
		r.renderSection(this.tools[this.toolFrame], tx + 180/3, ty + 32/4, 64);
		
		l = 50/4;
		
		if(xDel == 0){
			xOff = ++xOff % 200;
			xDel = 10;
		}else{
			xDel--;
		}
		
		if(toolFrameDel == 0){
			this.toolFrame = ++this.toolFrame % 3;
			this.toolFrameDel = 10;
		}else{
			this.toolFrameDel--;
		}
		
		for(int xl = 0; xl < l; xl++){
			for(int xx = 0; xx < 6; xx++){
				for(int yy = 0; yy < 15; yy++){
					int dx = 10 + xx + xl*6 + xOff;
					r.setPixel(10 + xx + xl*6 + xOff, 150-13+yy, this.logParts[dx > 200?3:0 + xl==0?0:xl==l-1?3:xl%2==0?1:2][xx + yy * 6]);
				}
			}
		}
		
		r.finaliseRender();
	}
}
