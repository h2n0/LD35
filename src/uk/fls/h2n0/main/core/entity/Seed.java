package uk.fls.h2n0.main.core.entity;

import java.util.Random;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Seed extends Entity {
	
	public int[] data;
	
	private int yOff;
	private int life;
	private int maxLife;
	
	private float xpow;
	private float ypow;
	
	public Seed(int x, int y){
		this.pos = new Point(x,y);
		this.data = Loader.getPixels(new SplitImage("/images/other/seed.png").load(), 0, 0, 8);
		this.yOff = 0;
		this.life = 0;
		
		Random r = new Random();
		this.maxLife = 60 * 60 + (60 * (int)Math.floor(Math.random() * 10));
		
		this.xpow = r.nextFloat() * 5;
		if(r.nextBoolean())this.xpow = -this.xpow;
		
		this.ypow = r.nextFloat() * 2;
	}

	@Override
	public void render(Renderer r, Point pos) {
		r.shadeElipse(this.pos.getIX(), this.pos.getIY() + 20, (14 - yOff/2)/2, (24 - yOff)/2);
		r.renderSection(data, this.pos.getIX()- 4, this.pos.getIY() - 4 + yOff, 8);
	}

	@Override
	public void update() {
		if(this.yOff < 20){
			this.yOff -= this.ypow;
			this.pos.x += this.xpow;
			
			this.xpow *= 0.97;
			this.ypow *= 0.98;
			
			this.ypow -= 0.15;
		}
		else{
			this.life++;
			
			if(this.life == this.maxLife){
				this.die();
				this.world.addEntity(new Tree(this.pos.getIX(), this.pos.getIY() + yOff));
			}
		}
	}

}
