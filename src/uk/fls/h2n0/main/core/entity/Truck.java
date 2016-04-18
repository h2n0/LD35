package uk.fls.h2n0.main.core.entity;

import java.util.List;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Truck extends Entity{

	public int[][] frame;
	
	private boolean taken;
	private int deliveryTime;
	
	public Truck(){
		this.pos = new Point(1600 - 64, (1200-64)/2);
		this.frame = new int[2][];
		this.frame[0] = Loader.getPixels(new SplitImage("/images/other/truck/truck.png").load(), 0, 0, 64);
		this.frame[1] = Loader.getPixels(new SplitImage("/images/other/truck/thruster.png").load(), 0, 0, 24);
		this.w = 64;
		this.collPoint = new Point(this.pos.getIX() + 16, this.pos.getIY());
		this.collW = 18;
		this.blocksMovment = true;
		this.type = machine;
		this.taken = false;
	}
	
	public void postInit(){
		List<Entity> ents = this.world.getEntitysAround(this, 128);
		for(Entity e: ents){
			if(e.type == tree){
				e.die();
			}
		}
	}
	
	float yOff = 0;
	
	@Override
	public void render(Renderer r, Point pos) {
		int dy = this.pos.getIY() + (int)(Math.sin(yOff+=0.125/2) * 3) - 38;
		

		r.shadeElipse(this.pos.getIX() + 45, dy + 55, 10 * 3, 4 * 3);
		r.renderSection(this.frame[0], this.pos.getIX(),dy, 64);
		r.renderSection(this.frame[1], this.pos.getIX() + 30, dy + 45, 24);
		
		//shadeColl(r);
	}

	@Override
	public void update() {
		if(this.world.currentContracts.size() > 0){
			this.taken = false;
		}
	}
	
	public void interact(Player p){
		if(p.crates > 0 && this.world.currentContracts.size() > 0){
			if(world.completeContract())p.crates--;
		}
	}

}
