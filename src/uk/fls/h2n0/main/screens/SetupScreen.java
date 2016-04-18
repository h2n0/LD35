package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import uk.fls.h2n0.main.util.Renderer;

public class SetupScreen extends Screen {
	
	public Renderer rend;
	private boolean ready;
	
	public SetupScreen(){
		this.ready = false;
	}
	
	public void postInit(){
		this.rend = new Renderer(this.game.image);
		this.ready = true;
	}
	
	@Override
	public void update() {
		if(this.ready){
			//setScreen(new GameScreen(this.rend));
			setScreen(new TitleScreen(this.rend));
		}
	}

	@Override
	public void render(Graphics gr) {
		this.rend.finaliseRender();
	}

}
