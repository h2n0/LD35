package uk.fls.h2n0.main.core.entity;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Tree extends Entity{

	
	private int[][] frames = new int[9][];
	
	private int height;
	private boolean chopped;
	public Tree(int x,int y){
		
		this.w = 20;
		this.health = 7;
		
		BufferedImage img = new SplitImage("/images/trees/trees.png").load();
		for(int i = 0; i < 9; i++){
			int xx = i % 3;
			int yy = i / 3;
			this.frames[i] = Loader.getPixels(img, xx, yy, 20);
		}
		this.height = 1 + (int)Math.floor(Math.random() * 3);
		
		this.chopped = false;//Math.random() > 0.125;
		
		this.type = tree;

		this.pos = new Point(x - (w),y);
		this.collPoint = this.pos;
		this.collW = 6;
	}
	
	@Override
	public void render(Renderer r, Point p) {
		if(chopped){
			//r.shadeCircle(this.pos.getIX(), this.pos.getIY() + 6, 12);
			r.renderSection(this.frames[0],pos.getIX() - (w/2),this.pos.getIY()-8, 20);

			
			//if(!onScreen(this.pos.getIX() - (w/2),this.pos.getIY()-8, p))return;

			//outline(r,this.pos);
		}else{
			
			int tx = pos.getIX() + 1 - (w/2);
			int ty = pos.getIY() - 11;
			
			//if(!onScreen(tx,ty, p))return;
			

			
			
			int size = this.height * 2;
			r.shadeElipse(this.pos.getIX(), this.pos.getIY() + 4, 14 + size,6);

			//r.shadeCircle(this.pos.getIX() - (w/2), this.pos.getIY() + 5, 10 + (this.height * 2));
			r.renderSection(this.frames[1], tx, ty, 20);
			

			ty -= 20;
			for(int i = 0; i < this.height-1; i++){
				r.renderSection(this.frames[4], tx, ty, 20);
				ty-= 20;
			}
			
			r.renderSection(this.frames[2], tx, ty, 20);
			
			tx -= 20;
			r.renderSection(this.frames[3], tx, ty, 20);
			tx += 40;
			r.renderSection(this.frames[5], tx, ty, 20);
			tx-=40;
			ty-=20;
			for(int i = 0; i < 3; i++){
				r.renderSection(this.frames[6 + i], tx, ty, 20);
				tx+=20;
			}
			
			//outline(r,this.pos);
		}
	}

	@Override
	public void update() {
		if(this.chopped)this.blocksMovment = false;
		if(this.health == 0 && !chopped){
			this.chopped = true;
			for(int i = 0; i < 4; i++){
				world.addEntity(new Seed(this.pos.getIX() + w/2, this.pos.getIY() - (this.height-1) * 20));
			}
		}
	}
	
	public boolean hasBeenCut(){
		return this.chopped;
	}

}
