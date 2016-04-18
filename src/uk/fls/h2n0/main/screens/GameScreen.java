package uk.fls.h2n0.main.screens;

import java.awt.Graphics;

import fls.engine.main.art.Art;
import fls.engine.main.input.CustomController;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.Camera;
import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.core.entity.Player;
import uk.fls.h2n0.main.core.entity.Shack;
import uk.fls.h2n0.main.core.entity.Truck;
import uk.fls.h2n0.main.util.Renderer;

public class GameScreen extends Screen {
	
	private Renderer r;
	private Player p;
	private World w;
	private Camera cam;
	private Shack shack;
	
	private boolean started;
	
	public GameScreen(Renderer r){
		this.r = r;
		this.p = new Player(this);

		this.cam = new Camera(0,0);
		this.cam.w = 400;
		this.cam.h = 300;
		this.cam.ww = 400 * 4;
		this.cam.wh = 300 * 4;
		this.w = new World(this.cam);
		this.w.addEntity(p);

		this.shack = new Shack(this.cam.ww/2,this.cam.wh/2);
		this.w.addEntity(shack);
		this.w.addEntity(new Truck());
		
		this.started = true;
	}

	@Override
	public void update() {
		w.update();
		
		boolean down = this.input.isKeyHeld(this.input.s) || this.input.isKeyHeld(this.input.down);
		boolean up = this.input.isKeyHeld(this.input.w) || this.input.isKeyHeld(this.input.up);
		boolean left = this.input.isKeyHeld(this.input.a) || this.input.isKeyHeld(this.input.left); 
		boolean right = this.input.isKeyHeld(this.input.d) || this.input.isKeyHeld(this.input.right);
		
		boolean action = this.input.isKeyPressed(this.input.c) || this.input.isKeyPressed(this.input.space) || this.input.isKeyPressed(this.input.x); // Thinking about the non-EU keyboards!!
		
		
		boolean controllerAction = false;//hasController && this.input.getController().isA();
		
		if(this.started == false){
			if(down){
				p.move(0, 1);
			}
			
			if(up){
				p.move(0, -1);
			}
			
			if(left){
				p.move(-1, 0);
			}
			
			if(right){
				p.move(1, 0);
			}
			
			if(action || controllerAction){
				p.interact();
			}
		}else{
			if(action || controllerAction){
				this.started = !this.started;
			}
		}
		
		if(this.input.isKeyPressed(this.input.q)){
			Art.saveScreenShot(game, true);
		}
		
		this.cam.center(this.p.getPos().getIX(), this.p.getPos().getIY());
		
		if(this.w.getStrikes() >= 3){
			setScreen(new GameOverScreen(this.r,this.w.getTotalCrates()));
		}
	}

	@Override
	public void render(Graphics g) {
		r.fill(r.makeRGB(64, 64, 64));
		r.setOffset(-this.cam.pos.getIX(), -this.cam.pos.getIY());
		r.checkerBoard(this.cam.pos.getIX(), this.cam.pos.getIY(), 20+1,60+1);
		w.render(r);
		shack.shade(r);
		r.setOffset(0, 0);
		if(this.p.trunks > 0)r.renderTextInFrame("TRUNKS "+this.p.trunks,7 + 5,6 + 5);
		if(this.p.logs > 0)r.renderTextInFrame("LOGS "+this.p.logs,7 + 5,6 + 5 + 20);
		if(this.p.planks > 0)r.renderTextInFrame("PLANKS "+this.p.planks,7 + 5,6 + 5 + 40);
		if(this.p.crates > 0)r.renderTextInFrame("CRATES "+this.p.crates,7 + 5,6 + 5 + 60);
		
		if(started){
			String[] lines = new String[]{"Welcome","Hello there, welcome to TreeLine!","This is a game made by h2n0","for Ludum Dare 35.","","Controls.","Up, W to move upward", "Down, S to move down", "Left, A to move left", "Right, D to move right", "Space, X, C, or A on a controler to interact", "", "How to play.", "You are the newest in crate building technology.","You need to gather wood from the trees","This will give you logs. you need to use the","machines in the shack behind you, to create", "crates. once you have done so, you need to","go to the truck on the far east of the map","to hand in the crates. You can only deliver"," crates when you have a contract. contracts", " appear on the left side of the screen, if", "you fail to meet 3 contracts you will be...", " terminated.", "", "Press the interact button to move on."};
			
			int ml = 0;
			for(String s : lines)if(s.length() > ml)ml = s.length();
			ml *= 8;
			int tx = 11;
			int ty = 35;
			r.renderFrame(tx, ty+0, ml/9, lines.length);
			for(int i = 0; i < lines.length; i++){
				r.renderString(lines[i], tx, ty + i * 9);
			}
		}
		r.finaliseRender();
	}
	
	public void startSawMiniGame(){
		//this.setScreen(new SawMiniGameScreen(this, this.r));
	}

}
