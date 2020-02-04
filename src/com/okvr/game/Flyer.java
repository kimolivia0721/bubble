package com.okvr.game;


import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Flyer{
	double x,y;
	int vx,vy;
	boolean hit = false;
	String side;
	int sec;
	static Texture []flyPics;
	static Texture []flyPicsL;

	public Flyer(double x, double y){
		this.x = x;
		this.y = y;
		vx = 2;
		vy = 4;
		side="right";

		if(flyPicsL==null){
			flyPicsL = new Texture[2];
			for(int i=1;i<3;i++){
				String mn = String.format("flyer/sam%d.png", i);
				flyPicsL[i-1] = new Texture(mn);
			
			}
		}
		if(flyPics==null){
			flyPics = new Texture[2];
			for(int i=1;i<3;i++){
				String ln = String.format("flyer/sam%df.png", i);
				flyPics[i-1] = new Texture(ln);
			
			}
		}
	}
	
	public void updateSide(){
		if(vx>0){
			side="right";
		}
		else{
			side = "left";
		}
	}
	public void move(ArrayList<Rectangle[]>walls,int pos){
		Rectangle r;
		updateSide();

		y+=vy;
		r = new Rectangle((int)x,(int)y, 30,28);//flyers rect
		for(Rectangle rect: walls.get(pos)){
			if(r.intersects(rect)){
				if(vy<0){
					y=rect.y+rect.height+1;
				}
				else{
					y=rect.y-r.height-1;
				}
			vy*=-1; //invert vy;	
			}
		}
		x+=vx;
		r = new Rectangle((int)x,(int)y, 30,28);//flyers rect
		for(Rectangle rect: walls.get(pos)){
			if(r.intersects(rect)){
				if(vx<0){
					x=rect.x+rect.width+1;
				}
				else{
					x=rect.x-r.width-1;
				}
			vx*=-1; //invert vx;	
			}
		}
	}

	public void bounceWall(){
		if(x>530 || x<45){
			vx*=-1;
		}
		if(y>550 || y<18){
			vy*=-1;
		}
	}
	
	public void drawFlyers(SpriteBatch batch){
		sec++;
		if(side == "right"){
			batch.draw(flyPics[sec/20%2],(int)x,(int)y);
		}
		else{
			batch.draw(flyPicsL[sec/20%2],(int)x,(int)y);
		}
	}


}

