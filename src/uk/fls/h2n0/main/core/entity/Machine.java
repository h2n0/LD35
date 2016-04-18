package uk.fls.h2n0.main.core.entity;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Machine extends Entity{

	
	public int[] data;
	public boolean shouldShow;
	public int machineType;
	
	public Machine(int x, int y,int type){
		this.data = Loader.getPixels(new SplitImage("/images/other/tools.png").load(), type, 0, 16, 19);
		this.w = 16;
		this.collW = 11;
		this.shouldShow = false;
		this.blocksMovment = true;

		this.pos = new Point(x + (w/2),y + 19/2);
		this.collPoint = new Point(this.pos.getIX(), this.pos.getIY());
		this.type = machine;
		this.machineType = type;
	}

	@Override
	public void render(Renderer r, Point pos) {
		if(shouldShow){
			for(int xx = 0; xx < 16; xx++){
				for(int yy = 0; yy < 19; yy++){
					r.setPixel(this.pos.getIX() + xx - (this.w/2), this.pos.getIY() - (19/2) + yy, this.data[xx + yy * 16]);
				}
			}
		}
	}

	@Override
	public void update() {
		
	}
	
	public void interact(Player p){
		if(this.machineType == 0){//Saw
			if(p.trunks > 0){
				p.trunks--;
				p.logs += 2;
			}
		}
		
		if(this.machineType == 1){//Drill
			if(p.logs > 0){
				p.logs--;
				p.planks += 2;
			}
		}
		
		if(this.machineType == 2){// Sander?
			if(p.planks >= 8){
				p.planks -= 8;
				p.crates ++;
			}
		}
	}	
}
