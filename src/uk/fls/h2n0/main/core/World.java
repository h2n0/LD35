package uk.fls.h2n0.main.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import fls.engine.main.util.Camera;
import fls.engine.main.util.Point;
import uk.fls.h2n0.main.core.entity.Entity;
import uk.fls.h2n0.main.core.entity.Tree;
import uk.fls.h2n0.main.util.Renderer;

public class World {

	
	public class EntitySorter implements Comparator<Entity>{

		@Override
		public int compare(Entity e1, Entity e2) {
			if(e1.getPos().y > e2.getPos().y)return 1;
			else if(e1.getPos().y < e2.getPos().y)return -1;
			else return 0;
		}
		
	}
	
	private EntitySorter sorter;
	private List<Entity> entitys;
	public List<Contract> currentContracts;
	private Camera cam;
	
	private final int w;
	private final int h;
	
	private int totalCrates;
	
	private int contractTime;
	
	private boolean acceptingContracts;
	
	private int stikes;
	
	public World(Camera cam){
		this.entitys = new ArrayList<Entity>();
		this.sorter = new EntitySorter();
		this.cam = cam;
		this.currentContracts = new ArrayList<Contract>();
		this.contractTime = 0;//60 * 60;
		
		this.w = cam.ww;
		this.h = cam.wh;
		Random r = new Random();
		
		Point center = new Point(800,600);
		int dist = 200;
		for(int i = 0; i < 200 * 3; i++){
			int tx = r.nextInt(w);
			int ty = r.nextInt(h);
			Point p = new Point(tx,ty);
			if(center.dist(p) < dist * dist)continue;
			this.addEntity(new Tree(tx,ty));
		}
		
		this.acceptingContracts = false;
		this.stikes = 0;
		this.totalCrates = 0;
	}
	
	public void addEntity(Entity e){
		e.world = this;
		this.entitys.add(e);
		e.postInit();
	}
	
	public void addContract(Contract e){
		if(!this.acceptingContracts)return;
		if(this.currentContracts.size() < 4)this.currentContracts.add(e);
		organiseContracts();
		e.world = this;
	}
	
	public void update(){
		for(int i = 0; i < this.entitys.size(); i++){
			Entity e = this.entitys.get(i);
			e.update();
			if(e.dead)this.entitys.remove(e);
		}
		
		
		boolean removed = false;
		for(int i = 0; i < this.currentContracts.size(); i++){
			this.currentContracts.get(i).update(this);
			if(this.currentContracts.get(i).shouldRemove()){
				this.currentContracts.remove(i);
				removed = true;
			}
		}
		
		if(removed){
			organiseContracts();
		}
		
		if(this.contractTime == 0){
			int amt = 1 + (int)Math.floor(Math.random() * 14);
			this.addContract(new Contract(Contract.Type.Create, amt));
			this.contractTime = 60 * 30 * (int)Math.floor(Math.random() * 6);
		}else{
			this.contractTime--;
		}
	}
	
	public void render(Renderer r){
		Collections.sort(this.entitys, this.sorter);
		for(int i = 0; i < this.entitys.size(); i++){
			Entity e = this.entitys.get(i);
			e.render(r, this.cam.pos);
		}
		
		for(int i = 0; i < this.currentContracts.size(); i++){
			this.currentContracts.get(i).render(r, this.cam.pos);
		}
		
		//r.shadeCircle(800 - 40, 600, 150 * 2);
	}
	
	public List<Entity> getEntitysAround(Entity e, float dist){
		List<Entity> res = new ArrayList<Entity>();
		
		for(int i = 0; i < this.entitys.size(); i++){
			Entity en = this.entitys.get(i);
			float d = e.getPos().dist(en.getPos());
			if(d <= dist * dist){
				if(e.type == en.type)continue;
				res.add(en);
			}
		}
		
		return res;
	}
	
	public void startContracts(){
		this.acceptingContracts = true;
	}
	
	public boolean completeContract(){
		if(this.currentContracts.size() > 0){
			for(int i = 0; i < currentContracts.size(); i++){
				Contract c = this.currentContracts.get(i);
				if(!c.complete){
					c.decrease();
					this.totalCrates++;
					return true;
				}
			}
		}
		return false;
	}
	
	private void organiseContracts(){
		for(int i = 0; i < this.currentContracts.size(); i++){
			this.currentContracts.get(i).updatePos(i);
			//System.out.println(this.currentContracts.get(i).yPos);
		}
	}
	
	public void addStrike(){
		this.stikes++;
	}
	
	public int getStrikes(){
		return this.stikes;
	}
	
	public int getTotalCrates(){
		return this.totalCrates;
	}
		
}
