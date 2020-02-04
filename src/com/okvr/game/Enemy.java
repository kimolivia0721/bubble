package com.okvr.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Pixmap;

public class Enemy{
	int x,y,jsec,ecol;
	int gravity = 0;
	double evy=0;
	int eground=26;
	int plat = cnum(255,0,0,255);
	int wall = cnum(0,0,255,255);
	int background = cnum(0,0,0,255);
	Pixmap mask; //make this go in the parameter
	int initGround = 26;
	boolean motion;
	boolean reachGround=false;
	//private static final String noJump="NO", yesJump="YES";
	String side, jump =""; 	
	Random rand = new Random();

	public Enemy(int x, int y,Pixmap mask){
		this.x = x;
		this.y = y;
		side = "r";
		this.mask = mask;
		motion = false;
	}
	public void check(){  //basically acts as render()
		if(y<=initGround){
			y=initGround;
			 reachGround =  true;
		}
		if (y>initGround && checkIfGround(x,y,mask)==true && reachGround==false){
			fall();
		}
		
		for(int step=0; step<evy; step++){
			if(y<=550){
				y+=1;
			}
		}
		for(int step=0; step>evy; step--){
			if(checkIfClear(x+20,y-1,mask)==true && y>=initGround){
				y--;
			}
			else{
				eground = y;
			}
		}
		if(checkIfClear(x+20,y-2,mask)==true){
			evy-=1;
		}
	}
	public void move(){
		int ecol=mask.getPixel(x,y);
		check();
		int n = rand.nextInt(2); 
		//evy=0;
		if(reachGround){
			if(motion){
				if(jumpCheck()==false){ //just walking back and forth
					if(side == "r" && bound("r")){
						x+=3;
					}
					else if(side =="l" && bound("l")){
						x-=3;
					}
					else{
						motion = false;
					}
				}
	
				else{
					motion = false;
				}
			}
			else if(n==1){ //moving and jumping at same time??
				int i = rand.nextInt(3); //left right still????  //jump or not jump
				if(i==0 && bound("r")){ //right
					x+=3;
					side = "r";
					if(jumpCheck()==true && y==eground){
						if(y>0 || ecol == plat){
							evy=17;
						}
					}
					motion = true;
				}
				else if(i==1 && bound("l")){ //left
					x-=3;
					side = "l";
					if(jumpCheck()==true && y==eground){ //jump
						evy=17;
					}
					motion = true;
				}
				else{ //stand still
					motion = false;
				}
			}
			else{ //stand still
				motion = false;
			}
		}
	}
	public void fall(){
		evy-=0.003;
		for(int i=0; i> evy; i--){
			y-=1;
		}
	}
	public boolean bound(String s){
		if(x+5<=517 && s.equals("r")){
			return true;
		}
		else if(x-5>48 && s.equals("l")){
			return true;
		}
		return false;
	}
	
	public boolean checkIfGround(int x, int y, Pixmap m){
		if(x<0 || x>= m.getWidth() || y<0 || y>= m.getHeight()){
			return false;
		}
		int ecol = m.getPixel(x, m.getHeight()- y);
		return ecol != wall;
	}
	public boolean jumpCheck(){
		return Math.random()<0.125; //chances of jump is 1/8
	}	
	public void jump(){
		if(jsec==300){
			jsec = 0;
		}
		jsec++;
	
	}
	public int cnum(int r, int g, int b, int a){
		return (r<<24) + (g<<16) + (b<<8) + a;
	}
	public boolean checkIfClear(int x, int y, Pixmap m){
		if(x<0 || x>= m.getWidth() || y<0 || y>= m.getHeight()){
			return false;
		}
		int ecol = m.getPixel(x, m.getHeight()- y);
		return ecol != plat;
	}
}

