package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.art.SplitImage;
import fls.engine.main.screen.Screen;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class GameOverScreen extends Screen {

	
	private Renderer r;
	
	private final int[] fin;
	private final int amt;
	public GameOverScreen(Renderer r, int amt){
		this.r = r;
		this.fin = Loader.getPixels(new SplitImage("/images/other/final.png").load(), 0, 0, 64);
		this.amt = amt;
	}
	
	@Override
	public void update() {
		if(this.input.isKeyPressed(this.input.space)){// ||  (this.input.getController() != null && this.input.getController().isB())){
			setScreen(new TitleScreen(this.r));
		}
	}

	@Override
	public void render(Graphics g) {
		int c = 167/4;
		int xo = 25;
		String[] s = new String[]{"You managed to build " + this.amt + " creates.","Sadly though, you failed to meet 3 contracts.","","And so you have been terminated."};
		//r.checkerBoard(0, 0, 19, 56, c, c, c/3);
		r.fill(0);
		r.renderTextInFrame("Game Over!", xo, 30);
		for(int i = 0; i < s.length; i++){
			if(s[i].equals("") || s[i].equals(" "))continue;
			r.renderString(s[i], xo, 60 + 10 * i);
		}
		
		int fx = (400-64)/2;
		int fy = (300-64)/2;
		r.renderSection(fin, fx, fy, 64);
		int yOff = 0;
		for(int xx = 0; xx < 32; xx++){
			for(int yy = 0; yy < 64; yy++){
				r.shade(fx + 16 + xx, fy + yy);
				r.shade(fx + 16 + xx, fy + yy);
			}
		}
		r.lightenElipse(fx + 30, fx, 16, 10);
		r.lightenElipse(fx + 30, fx-2, 14, 10);
		
		r.renderString("Press a or space to go to the main menu", xo + 20, 300 - 27);
		r.finaliseRender();
	}

}
