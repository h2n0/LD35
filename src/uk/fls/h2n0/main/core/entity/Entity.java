package uk.fls.h2n0.main.core.entity;

import java.util.List;

import fls.engine.main.util.Point;
import uk.fls.h2n0.main.core.World;
import uk.fls.h2n0.main.util.Renderer;

public abstract class Entity {

	
	protected final int player = 0;
	protected final int other = 1;
	protected final int tree = 2;
	protected final int machine = 3;
	
	protected int data = 0;
	
	protected Point pos;
	protected Point tPos;
	protected Point collPoint = Point.zero;
	public World world;
	protected boolean canMove = true;
	public boolean blocksMovment = true;
	public boolean dead = false;
	
	public int health = -1;
	
	public int type = other;
	
	protected int w = 32;
	protected int collW = 10;
	
	protected int dir = -1;
	
	public abstract void render(Renderer r, Point pos);
	
	public abstract void update();
	
	public void move(float x, float y){
		if(!canMove)return;
		float npx = pos.x + x;
		float npy = pos.y + y;
		
		remXFlag();
		
		if(x < 0){
			dir = 3;
		}
		else if(x > 0){
			dir = 1;
			setXFlag();
		}
		else if(y < 0)dir = 0;
		else if(y > 0)dir = 2;
		
		Point p = new Point(npx, npy);
		List<Entity> ents = world.getEntitysAround(this, 32);
		if(ents.size()  > 0){
			for(int i = 0; i < ents.size(); i++){
				Entity e = ents.get(i);
				
				if(!e.blocksMovment)continue;
				float d = p.dist(e.collPoint);
				int s = e.collW;
				
				
				
				if(d > s * s)continue;
				else return;
			}
		}
		this.tPos.setPos(npx, npy);
		this.canMove = false;
	}
	
	protected void updateMove(){// called to move the entity
		boolean moving = !this.canMove;
		if(!moving)return;
		
		float dist = (float) Math.sqrt(pos.dist(tPos));
		if(dist > 2){
			float speed = 0.25f;
			float vx = (tPos.x/dist) * speed;
			float vy = (tPos.y/dist) * speed;
			
			pos.x += vx;
			pos.y += vy;
		}else{
			this.canMove = true;
			this.pos.setPos(this.tPos.x, this.tPos.y);
		}
	}
	
	public Point getPos(){
		return this.pos;
	}
	
	protected void outline(Renderer r, Point p){
		
		int sx = this.pos.getIX();
		int sy = this.pos.getIY();
		
		if(dir == 0)sy -= w;
		else if(dir == 1){
			sy -= w / 2;
			sx += w;
		}
		else if(dir == 2)sy += w;
		else if(dir == 3){
			sy -= w / 2;
			sx -= w;
		}
		for(int x = sx; x < sx + w; x++){
			for(int y = sy; y < sy + w; y++){
				if(x == sx || y == sy || x == sx + w-1 || y == sy + w-1){
					r.setPixel(x, y, r.makeRGB(255, 0, 0));
				}
			}
		}
	}
	
	public void postInit(){
		
	}
	
	public boolean within(Point pos, int w2,int h2){
		float tl = this.pos.x;
		float tt = this.pos.y;
		float tr = tl + w;
		float tb = tt + w;
		
		float pl = pos.x;
		float pt = pos.y;
		float pr = pl + w2;
		float pb = pt + h2;
		
		return ! (pl > tr || pr < tl || pt > tb || pb < tb);
	}
	
	protected void setXFlag(){
		this.data |= 0x2;
	}
	
	protected void remXFlag(){
		if(getXFlag()){
			this.data ^= 0x2;
		}
	}
	
	protected boolean getXFlag(){
		return (this.data & 0x2) > 0;
	}
	
	public boolean isLeft(Entity e){
		if(this.pos.x <= e.pos.x - e.w/2)return false;
		else return true;
	}
	
	public void hurt(){
		this.health--;
	}
	
	protected boolean onScreen(int x, int y , Point p){
		x += p.getIX();
		y += p.getIY();
		if(x - w < 0 || y - w < 0 || x > 400 || y > 300)return false;
		return true;
	}
	
	protected void shadeColl(Renderer r){
		r.shadeCircle(this.collPoint.getIX(), this.collPoint.getIY(), this.collW);
	}
	
	public void die(){
		this.dead = true;
	}
}
