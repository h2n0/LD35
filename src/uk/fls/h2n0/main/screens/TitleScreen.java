package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.art.SplitImage;
import fls.engine.main.screen.Screen;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class TitleScreen extends Screen {

	
	private int[] data;
	private Renderer r;
	private boolean loading;
	
	public TitleScreen(Renderer r){
		this.data = Loader.getPixels(new SplitImage("/images/other/start.png").load(), 0, 0, 400,300);
		this.r = r;
		this.loading = false;
	}
	
	@Override
	public void update() {
		if(loading){
			setScreen(new GameScreen(this.r));
		}
		
		//this.input.setPrimaryController(CustomController.b);
		if(this.input.isKeyPressed(this.input.space)){
			this.loading = true;
		}
	}

	@Override
	public void render(Graphics g) {
		for(int xx = 0; xx < 400; xx++){
			for(int yy = 0; yy < 300; yy++){
				r.setPixel(xx, yy, this.data[xx + yy * 400]);
			}
		}
		
		if(!this.loading)r.renderString("Press space to start", 100, 300 - 20);
		else r.renderString("Loading...", 100, 300 - 20);
		r.finaliseRender();
	}

}
