package com.okvr.game;

import com.badlogic.gdx.Gdx;


public class Bub {
	int orix,oriy,x,y,type,side,time,mon; //type 0 is when it first shoots
	//type 1 is after few seconds bubble gets bigger
	//side 0 is left and side 1 is right
	//type 2 is soldier
	public Bub(int x,int y, int type, int side,int time,int mon){
		orix = x;
		oriy = y;
		this.x = x;
		this.y = y;
		this.type = type;
		this.side = side;
		this.mon = mon;
	}
	
	public String toString(){
		return String.format("X:%d, Y:%d", x,y);
	}
	public void goup(){
		y+=2;
	}
	public void gonext(){
		if(side==1){
			if(Gdx.graphics.getWidth()-75<x){
				while(Gdx.graphics.getWidth()-75<x){
					x++;
					type = 1; //because the distance between the user and the bubble can't be 80
					if(Gdx.graphics.getWidth()-65<=x){
						break;
					}
				}
			}
			else{
				x+=5;
			}
		}
		else if(side==0){
			if(50 > x){
				while(50 > x){
					x--;
					type = 1;//because the distance between the user and the bubble can't be 80
					if(50>=x){
						break;
					}
				}
			}
			else{
				x-=5;
			}
		}
	}
	public void center(){
		if(x>300){
			x--;
		}
		else if(x<250){
			x++;
		}
	}
	public void timeup(){
		time++;
	}
}
