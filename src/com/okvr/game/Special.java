package com.okvr.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Special {
	int x,y;
	String type;
	Random rand;
	static Texture []pics;
	int plat,sec,ground;
	Pixmap mask;
	
	public Special(int x, int y, String t,Pixmap mask){
		this.x=x;
		this.y=y;
		this.mask = mask;
		type = t;
		plat = cnum(255,0,0,255);
		ground = cnum(0,0,255,255);
		if(pics==null){
			pics = new Texture[3];
			for(int i=1;i<4;i++){
				String s = String.format("ice.png");
				String f = String.format("corn.png");
				String h = String.format("butt.png");
				pics[0] = new Texture(s);
				pics[1] = new Texture(f);
				pics[2] = new Texture(h);
			}
		}
	}
	public void move(){
		int col = mask.getPixel(x, y);
		if(checkIfClear(x+10,y,mask)){
			if(col!=0&&y>28){
				y--;
			}
			else if(col!=255){
				y--;
			}
		}
	}
	
	public void draw(SpriteBatch batch){
		if(type == "sold"){
			batch.draw(pics[0],x,y);
		}
		else if(type == "fly"){
			batch.draw(pics[1],x,y);
		}
		else{
			batch.draw(pics[2],x,y);
			
		}
	}
	public int cnum(int r, int g, int b, int a){
		return (r<<24) + (g<<16) + (b<<8) + a;
	}
	public boolean checkIfClear(int x, int y, Pixmap m){
		if(x<0 || x>= m.getWidth() || y<0 || y>= m.getHeight()){
			return false;
		}
		int c = m.getPixel(x, m.getHeight()- y);
		return c != plat;
	}
	public boolean checkIfGround(int x, int y, Pixmap m){
		if(x<0 || x>= m.getWidth() || y<0 || y>= m.getHeight()){
			return false;
		}
		int c = m.getPixel(x, m.getHeight()- y);
		return c != ground;
	}
}