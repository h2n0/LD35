package uk.fls.h2n0.main.core.entity;

import java.util.List;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Shack extends Entity{
	
	private int[][] frames;
	private Machine[] mechs;
	private boolean playerNear;
	
	public Shack(int x, int y){
		
		System.out.println(x + ":" + y);
		w = 80;
		this.pos = new Point(x - (w/2),y+3);
		
		this.frames = new int[2][];
		for(int i = 0; i < 2; i++){
			this.frames[i] = Loader.getPixels(new SplitImage("/images/other/shack.png").load(), i, 0, 80, 53);
		}
		this.playerNear = false;
		this.blocksMovment = true;
		this.type = other;
		this.mechs = new Machine[3];
		
		this.collPoint = new Point(x - (w/2), y - 3 - 5);
		this.collW = 10;
	}
	
	public void postInit(){
		reload();
	}

	@Override
	public void render(Renderer r, Point pos) {
		int dx = this.pos.getIX() - (80/2);
		int dy = this.pos.getIY() - 53/2;
		for(int xx = 0; xx < 80; xx++){
			for(int yy = 0; yy < 53; yy++){
				r.setPixel(dx + xx, dy + yy, this.frames[this.playerNear?1:0][xx + yy * 80]);
			}
		}

		for(int i = 0; i < 3; i++)this.mechs[i].render(r, Point.zero);
	}

	@Override
	public void update() {
		List<Entity> ents = this.world.getEntitysAround(this, w);
		if(ents.size() > 0){
			for(int i = 0; i < ents.size(); i++){
				Entity e = ents.get(i);
				if(e.type == player){
					if(pos.dist(e.pos) < 60 * 60){
						this.playerNear = true;
						showTools();
					}else{
						this.playerNear = false;
						hideTools();
					}
				}
			}
		}
	}
	
	public void shade(Renderer r){
		int dx = this.pos.getIX() - (80/2);
		int dy = this.pos.getIY() - 53/2;
		if(this.playerNear){
			for(int xx = 0; xx < 80; xx++){
				for(int yy = 0; yy < 53; yy++){
					r.shade(dx + xx, dy + 42 - yy);
				}
			}
			
		}else{
			for(int xx = 0; xx < 72; xx++){
				for(int yy = 0; yy < 10; yy++){
					r.shade(dx + 4 +  + xx, dy + 42 - yy);
				}
			}
			
			for(int xx = 0; xx < 80; xx++){
				for(int yy = 0; yy < 11; yy++){
					r.shade(dx + xx, dy - yy);
				}
			}
		}
	}
	
	private void showTools(){
		for(int i = 0; i < 3; i++)this.mechs[i].shouldShow = true;
	}
	
	private void hideTools(){
		for(int i = 0; i < 3; i++)this.mechs[i].shouldShow = false;
	}
	
	public void reload(){
		int dx = this.pos.getIX() - (80/2) + 9;
		int dy = this.pos.getIY() - 53/2 + 6;
		int xOff = 0;
		for(int i = 0; i < 3; i++){
			
			if(i == 1){
				xOff += 23;
			}else if(i == 2){
				xOff += 23;
			}
			this.mechs[i] = new Machine(dx + xOff,dy,i);
			this.world.addEntity(this.mechs[i]);
		}
		this.type = tree;
	}

}
