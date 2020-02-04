package com.okvr.game;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class Hat{
	int x,y; //the hat dude's location
	int bx,by; //bullet location
	int vy,bvy; //velocity #physics
	boolean alive,backDown,backUp;
	public Hat(int x, int y){
		this.x=x;
		this.y=y;
		bx=x-5;
		by=y+18;
		backDown = false;
		backUp=false;
		alive=false;
	}
	 Rectangle createRange(){
		Rectangle rec = new Rectangle(x,y+10, 22,180);
		return rec;
	}
	//check if the guy is in the range of the hat
	public boolean checkInRange(int xpos, int ypos, ArrayList<Texture[]> motion){
		Rectangle m = new Rectangle(xpos,ypos,motion.get(0)[0].getWidth(), motion.get(0)[0].getHeight());
		return m.intersects(createRange());
	}
	
	public void checkUp(int xpos,int ypos, ArrayList<Texture[]> motion){  //basically acts as render()   
		if(backUp){ //going up
			by += bvy;
			if(by<y+200){
				bvy--;
			}
			if(by >= y+200){
				by = y+200; //reach the max jump point
				backDown = true; //start going down
				backUp = false;
			}
		}
	}
	public void checkDown(int xpos,int ypos, ArrayList<Texture[]> motion){
		if(backDown){ //going down
			by-= bvy;
			bvy++;
			if(by < y+18){ //once it reaches start location set it to start
				by = y + 18;
				 //no longer activated
				backDown = false; //not going down	
			}
		}
	}
}