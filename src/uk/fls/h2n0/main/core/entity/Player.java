package uk.fls.h2n0.main.core.entity;

import java.awt.image.BufferedImage;
import java.util.List;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.screens.GameScreen;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Player extends Entity{
	
	
	private int[][] frames;
	private int frame = 0;
	
	private int cutTime;
	private int[] cutFrames;
	private int cutFrame;
	
	
	private int baseFrame;
	private int fDel = 0;
	private float yOff;
	
	private GameScreen screen;
	
	
	public int trunks;
	public int logs;
	public int planks;
	public int crates;
	
	public int stikes;
	public Player(GameScreen screen){
		this.frames = new int[16][];
		BufferedImage b = new SplitImage("/images/player/player.png").load();
		for(int i = 0; i < 4 * 4; i ++){
			this.frames[i] = Loader.getPixels(b, i % 4, i / 4, 32);
		}
		
		this.pos = new Point(760,670);
		this.tPos = new Point(10,10);
		this.type = this.player;
		this.baseFrame = 0;
		
		this.trunks = 0;
		this.logs = 0;
		this.planks = 0;
		this.crates = 0;
		this.stikes = 0;
		this.cutFrames = new int[]{0,1,2,3,2,3,2,3,2,3,2,1,0,0};
		this.screen = screen;
	}


	int s = 10;
	@Override
	public void render(Renderer r, Point p) {
		int dy = this.pos.getIY() - 32;
		
		int shadeSize =  8 + (this.cutTime > 0?4:0) + (int)(Math.sin(yOff) * 3);
		for(int i = 0; i < shadeSize; i++){
			//r.shade(this.pos.getIX() + (32 - shadeSize)/2 + i - w/2, dy + 34);
			
		}
		r.shadeElipse(this.pos.getIX()-1, dy + 34, shadeSize/2 - 1, shadeSize/3);
		
		for(int i = 0; i < 32 * 32; i++){
			int x = i % 32;
			int y = i / 32;
			int c = this.frames[this.baseFrame + this.frame][this.getXFlag()?(w-1-x) + y * 32:x + y * 32];
			r.setPixel(this.pos.getIX() + x - w/2, dy + y + (int)(Math.sin(yOff) * 3), c);	
		}
	}

	@Override
	public void update() {
		yOff += 0.125f / 2f;
		if(this.cutTime > 0 && this.cutFrame < this.cutFrames.length){
			this.canMove = false;
			this.baseFrame = 4;
			if(this.fDel == 0){
				this.frame = this.cutFrames[this.cutFrame++];
				this.fDel = 9;
				
				if(this.frame > 1){
					List<Entity> trees = this.world.getEntitysAround(this, 14);
					for(Entity e : trees){
						if(e.type == this.tree){
							boolean r = this.dir == 1;// && !isLeft(e);
							boolean l = this.dir == 3;// && isLeft(e);
							if(l || r){
								if(!((Tree)e).hasBeenCut()){
									e.hurt();
									if(this.frame > 2){
										this.trunks++;
										this.world.startContracts();
									}
								}
								//TODO particle effects maybe?
							}
						}
					}
				}
			}else{
				this.fDel--;
			}
			this.cutTime--;
		}else{
			updateMove();
			if(fDel == 0){
				this.frame = ++this.frame % 4;
				this.fDel = 10;
			}else{
				fDel--;
			}
			
			if(this.dir == 0)this.baseFrame = 12;
			else if(this.dir == 2)this.baseFrame = 8;
			else if(this.dir == 1 || this.dir == 3)this.baseFrame = 0;
		}
	}
	
	public void interact(){
		List<Entity> ents = this.world.getEntitysAround(this, 14);
		int s = 13;
		for(int i = 0; i < ents.size(); i++){
			Entity e = ents.get(i);
			if(e.type == this.machine){
				int d = this.pos.dist(e.pos);
				System.out.println(d);
				if(d < s * s){;
					if(e instanceof Machine)((Machine)e).interact(this);
					if(e instanceof Truck){
						((Truck)e).interact(this);
					}
					return;
				}else continue;
			}
		}
		
		if(this.cutTime == 0){
			this.cutTime = (this.cutFrames.length - 1) * 10;
			this.canMove = false;
			this.cutFrame = 0;
			this.frame = 0;
			this.fDel = 0;
		}
	}

}
