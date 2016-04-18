package uk.fls.h2n0.main.core;

import fls.engine.main.art.SplitImage;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.util.Loader;
import uk.fls.h2n0.main.util.Renderer;

public class Contract {

	
	public static enum Type{
		Table,
		Chair,
		Create;
	}
	
	
	public final Type type;
	private int amt;
	
	public boolean complete;
	public boolean inPosition;
	private Point pos;
	
	private float time;
	private float maxTime;
	
	private int[] data;
	private int[] fData;
	public World world;
	
	private int yPos;
	public Contract(Type t, int amt){
		this.type = t;
		this.amt = amt;
		this.complete = false;
		this.inPosition = false;
		this.pos = new Point(400-36, 300);
		this.data = Loader.getPixels(new SplitImage("/images/other/contract.png").load(), 0, 0, 32);
		
		
		int a = -1;
		for(int i = 0; i < Type.values().length; i++){
			if(Type.values()[i] == t){
				a = i;
				break;
			}
		}
		this.fData = Loader.getPixels(new SplitImage("/images/other/items.png").load(), 2-a, 0, 11);
		this.time = 60 * 60 + ((60 * 15) * amt);
		this.maxTime = this.time;
		this.yPos = 0;
	}
	
	public void render(Renderer r, Point p){
		
		int dx = this.pos.getIX() + p.getIX();
		int dy = this.pos.getIY() + p.getIY();
		String w = ""+this.amt;
		r.renderSection(data, dx, dy, 32);
		r.renderSection(fData, dx + 11, dy + 3, 11);
		r.renderString(""+this.amt, dx + (32-w.length()*8)/2, dy + 20);
		
		
		float barAmt = (this.time/this.maxTime) * 13;
		for(int i = 0; i < 4; i++)
		r.setPixel(dx + i + 1, dy + 16 + 13-(int)barAmt, 0);
	}
	
	public void update(World w){
		if(!complete){
			if(!this.inPosition){
				if(this.pos.y > 10 + this.yPos){
					this.pos.y --;
				}else{
					this.inPosition = true;
				}
			}else{
				this.time --;
				
				if(this.time == 0){
					this.complete = true;
					this.world.addStrike();
				}
			}
		}else{
			if(!this.inPosition){
				if(this.pos.x < 460){
					this.pos.x++;
				}else{
					this.inPosition = true;
				}
			}
		}
	}
	
	public boolean shouldRemove(){
		return (this.inPosition && this.complete);
	}
	
	public void decrease(){
		if(this.amt > 0)this.amt --;
		if(this.amt == 0){
			this.complete = true;
			this.inPosition = false;
		}
	}
	
	public void updatePos(int yOff){
		this.yPos = 34 * yOff;
		this.inPosition = false;
		System.out.println(this.yPos);
	}
}
