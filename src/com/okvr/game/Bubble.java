package com.okvr.game;
/* enemies move randomly[] flying enemy[]
 * bubbles stay at random position[]
 * scores[] highscores[]
 * names []
*/
import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.awt.Rectangle;

public class Bubble extends ApplicationAdapter {
	SpriteBatch batch; //need to draw
	Texture img;
	int xpos = 45; //starting position
	int ypos=27,ground = 27; //starting position
	int vy = 0; //velocity y
	//sec is for walking sprite, shootsec is how long after you shoot, score keeps track of score
	//jumpHeight measures how high the guy has jumped up
	int sec=0,level=1, shootsec=0,score=0,immunesec=0,jumpHeight=0;
	//jumpy is the y position of the guy when jumping, deltaTime is used for the jumping sprite gamerun is how
	//long the game has been running //loadsec is 
	int jumpy=2,deltaTime,gamerun,loadsec;
	int lives = 4,credsec=0;// credsec is for time frames for credits screen
	int from,present;//integer secs for cred 
	
	/*All the boolean values used throughout the game
	 * still: if guy is in motion
	 * jump: if the guy jumped
	 * jumped: if the guy is falling
	 * shoot: if it shoots
	 * load: boolean for startscreen
	 * made: if enemies(not the class) were made 
	 * jumpState: similar to jump
	 * start: run menu
	 * instr: show instructions
	 * cred: show credits
	 * jumpState2: for wing sprites
	 * pick: show which mode to play
	 * endless: plays endless mode
	 * game: plays game mode
	 * dead: the user is dead when true*/
	boolean still=true,jump=true,jumped = true,shoot=true,load=true,made=true,jumpState=false;
	boolean start=true, instr = false,cred=false, jumpState2 =true;
	boolean pick = false,endless=false,game=false,dead=false,introduction=false;
	String side = "right"; //which way the user is looking at
	BitmapFont font,menufont,menufont2,menufont3; //different fonts 
	FileHandle file; //import high scores
	
	//ArrayLists of different objects and classes
	private ArrayList<Bub> bubbles = new ArrayList<Bub>(); //add all the bubbles
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>(); //all the enemies
	private ArrayList<Flyer> flyers = new ArrayList<Flyer>(); // add all the flyers
	private ArrayList<Hat> hats = new ArrayList<Hat>(); //all the hats
	private ArrayList<Ghost> ghosts = new ArrayList<Ghost>(); //add all the ghosts
	private ArrayList<Special> specials = new ArrayList<Special>();//rewards that user collects
	private ArrayList<Texture[]> motion = new ArrayList<Texture[]>(); //all sprites for animation
	private ArrayList<Rectangle[]>platforms = new ArrayList<Rectangle[]>(); //platforms for flyers to bounce off of
	private ArrayList<Integer>  scores; //high scores 
	
	//Lists for Texture and Pixmap
	private Texture [] backs = new Texture[5]; //background pictures
	private Texture [] walkright = new Texture[5]; //first item
	private Texture [] walkleft = new Texture[5]; //second item
	private Texture [] jumpright = new Texture[3];//third
	private Texture [] jumpleft = new Texture[3];//fourth
	private Texture [] spitr = new Texture[2];//fifth
	private Texture [] spitl = new Texture[2];//sixth
	private Texture [] hitPics = new Texture[4];//7th

	private Texture [] titles = new Texture[4];//not in motion
	private Texture [] bubPics = new Texture[3];//not in motion
	private Texture [] soldRPics = new Texture[4]; //soldier right
	private Texture [] soldLPics = new Texture[4]; //soldier left
	private Texture [] flyRPics = new Texture[2];//first item in flyPics
	private Texture [] flyLPics = new Texture[2];//second item in flyPics
	private Texture [] menuPics = new Texture[7]; //pictures for menu
	private Pixmap [] maskList = new Pixmap[3];
	private Texture [] hatPics = new Texture[3]; //hat
	private Texture [] bulletPics = new Texture[2]; 
	private Texture [] ghostPics = new Texture[2];
	private Texture [] intro = new Texture[2]; //intro
	private Texture [] bubDude = new Texture[2];//pics
	
	public static final int RIGHT = 0, LEFT = 1, DEFAULTX = 45, DEFAULTY = 27;
	public static final boolean JUMPING = true, FALLING = false;
	private int walkDir = 0;
	Pixmap mask1,mask2,mask,mask3; //masks for different levels
	
	int col,wall,plat,background; //color of pixel 
	Texture masky,endPic,gamewin;
	ShapeRenderer shaperender;  
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shaperender = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("krungthep32.fnt")); //font loaded
		menufont = new BitmapFont(Gdx.files.internal("arcade.fnt")); //arcade looking font
		menufont2 = new BitmapFont(Gdx.files.internal("menufont2.fnt")); //smaller size arcade font
		menufont3=new BitmapFont(Gdx.files.internal("menufont3.fnt")); //smallest size arcade font
		mask1 = new Pixmap(Gdx.files.internal("masks/mask1.png")); //level 1 mask
		mask2 = new Pixmap(Gdx.files.internal("masks/mask2.png")); //level 2 mask
		mask3 = new Pixmap(Gdx.files.internal("masks/mask3.png")); //level 3 mask
		maskList[0]=mask1; //first item in maskList
		maskList[1]=mask2; //second item
		maskList[2]=mask3; //third item
		
		plat = cnum(255,0,0,255); 
		wall = cnum(0,0,255,255);
		background = cnum(0,0,0,255);
		
		//import all the textures
		for(int i = 1; i<6; i++){
			String fn = String.format("walk/walk%d.png",i);
			String sn = String.format("walk/walk%df.png",i);
			String pn = String.format("backs/background%d.png", i);
			backs[i-1] = new Texture(pn);
			walkright[i-1] = new Texture(fn);
			walkleft[i-1] = new Texture(sn);
		}
		for(int i = 1; i<4; i++){
			String pn = String.format("spitoutbubble/bubs%d.png",i);
			bubPics[i-1] = new Texture(pn);
			String fn = String.format("jump/jump%d.png",i);
			String sn = String.format("jump/jump%df.png",i);
			jumpright[i-1] = new Texture(fn);
			jumpleft[i-1] = new Texture(sn);
			String rn = String.format("hat/hat%d.png",i);
			hatPics[i-1] = new Texture(rn);
		}
		for(int i = 1; i<3; i++){
			String fn = String.format("spitoutbubble/drag%d.png",i);
			String sn = String.format("spitoutbubble/drag%df.png",i);
			String pn = String.format("flyer/sam%df.png",i);
			String qn = String.format("flyer/sam%d.png",i);
			String in = String.format("intro/intro%d.png", i);
			flyRPics[i-1] = new Texture(pn);
			flyLPics[i-1] = new Texture(qn);
			spitr[i-1] = new Texture(fn);
			String bp = String.format("bullet/hatBullet%d.png", i);
			bulletPics[i-1] = new Texture(bp);
			spitl[i-1] = new Texture(sn);
			intro[i-1] = new Texture(in);
		}
		for(int i=1; i <5; i++){
			String fn = String.format("title/title%d.jpg", i);
			String pn = String.format("soldier/ed%d.png", i);
			String sn = String.format("soldier/ed%df.png", i);
			String qn = String.format("hits/hit%d.png", i);
			hitPics[i-1] = new Texture(qn);
			titles[i-1] = new Texture(fn);
			soldRPics[i-1] = new Texture(sn);
			soldLPics[i-1] = new Texture(pn);
		}
		
		//add all the user pictures in motion
		motion.add(walkright);
		motion.add(walkleft);
		motion.add(jumpright);
		motion.add(jumpleft);
		motion.add(spitr);
		motion.add(spitl);
		motion.add(hitPics);
		
		Rectangle[]level2={ //boundaries for level 2//flyers need them
				new Rectangle(130,230,345,1),
				new Rectangle(75,115,140,1),
				new Rectangle(245,115,115,1),
				new Rectangle(395,115,125,1),
				new Rectangle(187,345,101,1),
				new Rectangle(320,345,101,1),
				new Rectangle(245,460,120,1),
				new Rectangle(130,254,345,1),
				new Rectangle(75,139,140,1),
				new Rectangle(245,139,115,1),
				new Rectangle(395,139,125,1),
				new Rectangle(187,369,101,1),
				new Rectangle(320,369,101,1),
				new Rectangle(245,484,120,1)};
		//level2 rectangle is only for level it doesn't work for other levels
		platforms.add(level2);
		
		for(int i=1;i<8;i++){ //import pictures for menu
			String mn = String.format("menu/start%d.jpg", i);
			menuPics[i-1] = new Texture(mn);
		}
		
		gamewin = new Texture("win.png"); //picture when the user completed all the levels
		ghostPics[0]=new Texture("ghosts/ghost1.png");//right
		ghostPics[1]=new Texture("ghosts/ghost1f.png");//left side
		bubDude[0] = new Texture("intro/guy/one.png");//first pic
		bubDude[1] = new Texture("intro/guy/two.png"); //second pic
		endPic = new Texture("gameover.png"); //gameover picture
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(load){ //display loading screen when true
			batch.begin();	
			startScreen(); //call loading screen
			batch.end();
		}
		else if(start){ //user picks instruction, credits, or start button
			if(pick){ //picking which mode
				batch.begin();
				Rectangle rect = new Rectangle(118,452,359,96); //top rectangle normal game
				Rectangle recb = new Rectangle(118,49,359,96); //bottom rectangle endless game
				batch.draw(menuPics[4], 0, 0); //displays this if user's mouse isn't on one of the options
				if(rect.contains(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY())){
					batch.draw(menuPics[5], 0, 0); //draw a different picture if the mouse is on this option
					if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ //if user clicks
						start = false; //start ends 
						pick = false; //pick ends
						game = true; //game starts
						introduction = true; //start intro()
						endless = false;
					}
				}
				else if(recb.contains(Gdx.input.getX(),Gdx.graphics.getHeight()- Gdx.input.getY())){ 
					batch.draw(menuPics[6], 0, 0);
					if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
						start = false; 
						pick = false;
						endless = true;//endless mode starts
						game = false; //game doesn't start
					}
				}
				batch.end();
				if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){ //escape button allows the user to exit current screen
					pick = false;
				}
			}
			else if(instr){
				if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){ //exit currect screen
					instr = false;
				}
				img = new Texture("instruction.png"); //display instructions
				batch.begin();
				batch.draw(img, 0, 0); 
				batch.end();
			}
			else if(cred){
				batch.begin();
				if(credsec<650){ //after this many secs the font disappears
					from++;
					menufont2.draw(batch, "Credits", 200, from);
				}
				if(60<credsec&&credsec<1000){ //
					present++; //different fps varibale for each font because the time it disappears are different
					menufont3.draw(batch, "Orignal game:Bubble Bobble 2 \n\n\n\n"
							+ "Programmed by Olivia Kim\n\n              Victoria Ren\n\n\n"
							+ "Editor Victoria Ren\n\n\n"
							+ "Music by Olivia Kim\n\n\n"
							+ "Special thanks to Mr. McKenzie", 70,present); 	
				}
				if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)||credsec>1000){ //finish credit when cred ends
					cred = false;
					credsec = 0;
				}
				credsec++;
				batch.end();
			}
			else{
				batch.begin();
				startMenu(); //pick start, instruction or credits
				batch.end();
			}
		}
		else if(dead){ //displays highscores
			batch.begin();
			batch.draw(endPic,0,0);
		    for(int i=scores.size()-1;i>0;i--){ 
			    font.setColor(255, 255, 255, 255);
		        font.draw(batch, Integer.toString(scores.get(i)), 280
		        +new GlyphLayout(font,Integer.toString(scores.get(i))).width, 275+i*49); //display score in highest to lowest order
		    }
		    batch.end();
		}
		else if(endless){ //starts endless mode
			mask = maskList[level-1]; //use the right mask for the current level
			col=mask.getPixel(xpos, ypos); //color of the spot 
			mask.drawPixmap(mask, 0, 0); //draw pixel map to get pixel number for classes and user
			if(enemies.size()<1){ // if no opponents are alive 
				createene(mask); //make them again
			}
			if(flyers.size()<1){ 
				createflyer(mask);
			}
			//lives = -1;
			runMethods(); //look at runMethods for more information
		}
		else if(introduction){ //introduction animation before "game" starts
			intro(); //intro animation ends when user presses end
			if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
				introduction = false; //introduction ends
				game = true; //game starts
			}
		}
		else if(game){
			mask = maskList[level-1]; //use a mask for the current level
			col=mask.getPixel(xpos, ypos); //color of the spot 
			mask.drawPixmap(mask, 0, 0); //draw it to get the num for mask.getPixel
			if(made&&level==1){ //level1 has enemies only
				createene(mask); //make enemies
				made = false; //don't make infinite enemies
			}
			else if(made&&level==2){ //level2 has enemies and flyers
				createflyer(mask); //make flyers
				createene(mask);
				made = false; 
			}
			else if(made&&level==3){ //level3 has hats and enemies
				createHats(); // make hats
				createene(mask);
				made = false; 
			}
			runMethods(); //look at runMethods for more information
			moveon(); //move on to next level if opponents all died
		}
	}
	public void moveon(){ 
		if(level==2&&flyers.size()==0&&enemies.size()==0){ //when all the opponents died
			loading(); //wait for a sec then
			if(loadsec>200){ 
				immune(); //make the user immune to get ready 
				immunesec = 120; //start immune animation 120 is where it stops turning
				level = 3; //move on to next level
				loadsec = 0;  //reset
				made = true; //make opponents again
				specials = new ArrayList<Special>(); //empty specials
				lives+=1;
			}
		}
		else if(level==1&&enemies.size()==0){ 
			loading();
			if(loadsec>200){
				immune();
				immunesec = 120;
				level = 2;
				loadsec =0;
				made = true;
				lives+=1;
				specials = new ArrayList<Special>(); 
			}
		}
		else if(level==3&&hats.size()==0&&enemies.size()==0){
			batch.draw(gamewin,150,150); //display game won if the user finished everything
		}
	}
	public void loading(){
		loadsec++; //increase loadsec 
		gamerun = 0; //gamerun resets so ghosts disappear
	}
	public void ghostAction(){//starts this when the user doesn't end 
		if(gamerun==1500){ 
			Ghost g = new Ghost(0,0,0); //make a new ghost
			ghosts.add(g); //add to the list
		}
		if(gamerun>500){
			for(int i=0;i<ghosts.size();i++){
				Ghost g = ghosts.get(i);
				if(g.x<xpos){ //ghost follows
					g.x++;
					g.side = 0; //right
				}
				else if(g.x>xpos){
					g.x--;
					g.side = 1; //left
				}
				if(g.y<ypos){
					g.y++; //increase when user is higher than the ghost
				}
				else if(g.y>ypos){ //increase when user is lower than the ghost
					g.y--;
				}
				batch.draw(ghostPics[g.side],g.x,g.y); //change picture depending on the side
				Rectangle rec = new Rectangle(g.x,g.y,45,42);
				Rectangle userrec = new Rectangle(xpos,ypos,40,40);
				if(rec.intersects(userrec)){ //if user collides with the ghost
					if(lives>=0){ //reduce life 
						if(immunesec==0){ //user becomes immune
							immune();
							vy = 0;//reset
							jumpHeight = 0; //reset
						}
					}
				}
			}
		}
	}
	public void dealWithSpecials(){ //special is made when user kills an opponent
		if(specials.size()>0){ //when there is at least one special
			for(int i=0;i<specials.size();i++){
				Special s= specials.get(i);
				Rectangle r = new Rectangle(s.x,s.y,20,21);
				Rectangle p = new Rectangle(xpos,ypos,motion.get(0)[0].getWidth(),motion.get(0)[0].getHeight());
				if(r.intersects(p)){ //if user intersects with special 
					specials.remove(s); //remove special
					score+=100; //increase score
				}
			}
		}
	}
	public void runMethods(){
		batch.begin();
		gamerun++; //counter for ghosts
		if(Gdx.input.isKeyPressed(Keys.LEFT)&&checkImmune()){ //if user is alive 
			if(xpos>48){; //xpos has to be in the domain
				walk(side);  //change x position
			}
			walkDir = LEFT; //update walkDir for textures
			side = "left"; //update side
			still = false; //user is moving
		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)&&checkImmune()){
			if(xpos<517){
				walk(side);
			}
			walkDir = RIGHT;
			side = "right";
			still = false;
		}
		else{
			if(!jumpState){ //if the user is jumping 
				still = true; //user is not still
			}
			else{
				still = false; //if not it is false
			}
		}
		if(!still){ //sec is for walking sprite
			sec++;
			if(sec>19){  //reset so it doesn't go out of bound
				sec = 0;
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)&&ypos==ground&&checkImmune()){ /*&& jumpState!=JUMPING*/
			//can't jump again while jumping
			jumpState = JUMPING; //needed two jump booleans to get the textures working
			jumpState2 = false; //this is for jumping sprite (wings)
			vy=17; //vy set 17
			still=false; //user isn't still - for texture
		}
		if(Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT)&&checkImmune()){
			shootBub(side); //shoot a bubble when user presses control and when it's alive
		}
		for(int step=0; step<vy; step++){ //velocity
			if(ypos<=550){ 
				ypos++; //increase ypos accoridingly to vy
			}
		}
		for(int step=0; step>vy; step--){ //gravity
			if(checkIfClear(xpos+20,ypos-1,mask) && ypos>=27){ //if the user isn't on platform or ground
				ypos--; //the user falls
				jumpState2=true; //set it to true for wings textures to work
				if(deltaTime>2){ //flap wings
					deltaTime = 0; //reset
				}
				else{
					deltaTime++;
				}
			}
			else{
				jumpState = false; //user isn't jumping anymore
				ground = ypos; //reset ground
			}
		}
		jumpHeight+=vy; //increase jumpHeight by the amount of vy
		if(checkIfClear(xpos+20,ypos-1,mask)){ //if the user isn't on platform
			vy--; //decrease vy
		}
		if(jumpHeight>153){  //can't jump higher than this
			jumpState2 = true; //true when falling
			vy = 0; //reset
			jumpHeight = 0; //reset
		}
		//check how long it has passed after the user shoot
		drawBack(); //draw background
		if(shoot){ //w
			if(120>immunesec&&immunesec>0){
				immunesec++; //increase immunesec
				immuneDraw(); //the user turns - animation
			}
			else if(120<immunesec){ //
				immunesec++;
				if(immunesec%2==0){ //animation for immune
					drawWalk(sec); //shows the player that he is immune
				}
				if(immunesec>250){ //reset immunesec
					immunesec=0;
				}
			}
			else if(immunesec==120){ //reset x and y positions
				xpos = DEFAULTX;
				ypos = DEFAULTY;
				immunesec++;
				if(immunesec%2==0){ 
					drawWalk(sec);
				}
			}
			else{
				drawWalk(sec); //if user is alive just draw walk
			}
		}
		else{
			checkShoot(); //check if the user shoot any bubbles
		}
		for(Flyer f: flyers){
			f.move(platforms,0); //move all the flyers
		}
		for(Special s: specials){
			s.move(); //move all the specials
		}
		for(Special s: specials){
			s.draw(batch); //draw all the specials
		}
		for(Flyer f: flyers){
			f.drawFlyers(batch); //draw all the flyers
		}
		runHats(); //draw all the hats
		hatsAction(); //activate hats if user gets in the range
		dealWithSpecials(); //collect specials
		bubblemethod(); //draw all the bubbles and make them go up
		enemy(); //move enemies and draw
		flyAction(); //check if they bounce off walls and if they die or intersect with user
		drawScore(); //display score
		ghostAction(); //ghost appears later
		deadUser(); //check if the user died
		batch.end();
	}
	public void hatsAction(){//deals with all the moves for hat's bullet
		for(int i=0; i<hats.size(); i++){ 
			Hat hat = hats.get(i); 
			if(hat.checkInRange(xpos,ypos,motion)&&!hat.backDown&&!hat.backUp){ //if user is in range and hats are down
				hat.backUp = true;  //make the hat go up
				hat.checkUp(xpos,ypos,motion); //velocity
				hat.bvy = 8; //reset
			}
			else if(hat.backUp){ //keeps going up
				hat.checkUp(xpos,ypos,motion); //until reaches the maximum height
				hat.bvy = 8;
			}
			else if(hat.backDown){ //go down
				hat.checkDown(xpos,ypos,motion); //until it goes back to the original position
			}
			deathH(hat); //check if hit the user or if bubble hit it
		}
	}
	public void deathH(Hat h){ //check if hat died or user died
		Rectangle q = new Rectangle(xpos,ypos,motion.get(0)[0].getWidth(),motion.get(0)[0].getHeight()); //rectangle for user
		for(int i=0; i<bubbles.size();i++){
			Bub b = bubbles.get(i);
			Rectangle r = new Rectangle(b.x,b.y,bubPics[0].getWidth(),bubPics[0].getHeight()); //bubble box
			Rectangle p = new Rectangle(h.x,h.y,hatPics[0].getWidth(),hatPics[0].getHeight());
			if(r.intersects(p)&&b.type==0){	 //if it collides and bubble was just shot
				hats.remove(h); //remove hat
				Special s = new Special (h.x,h.y,"hats",mask); //make a special
				specials.add(s); //add it to the list
				s.move(); //move special
				b.type = 2; //type changes to 2 which means it's not empty
				score+=10; //increase score
			}
		}
		if(h.backUp||h.backDown){ //if it was attacking
			Rectangle p = new Rectangle(h.bx,h.by,bulletPics[0].getWidth(),bulletPics[0].getHeight()); //enemy box
			if(q.intersects(p)){ //and it intersects
				if(lives>=0){ //the user dies
					if(immunesec==0){
						immune();
						vy = 0; 
						jumpHeight = 0;
					}
				}
			}
		}
	}
	public void flyAction(){//deals with flyers motion
		for(int i=0;i<flyers.size();i++){
			Flyer f = flyers.get(i);
			f.bounceWall(); 
			deathF(f); //check if the flyer died or it killed the enemy
		}
	}
	public void deathF(Flyer f){ // check if flyer died or it killed the enemy
		Rectangle q = new Rectangle(xpos,ypos,motion.get(0)[0].getWidth(),motion.get(0)[0].getHeight()); //rectangle for user
		int x = (int)(f.x);
		int y = (int)(f.y);
		for(int i=0; i<bubbles.size();i++){ 
			Bub b = bubbles.get(i);
			Rectangle r = new Rectangle(b.x,b.y,bubPics[0].getWidth(),bubPics[0].getHeight()); //bubble box
			Rectangle p = new Rectangle(x,y,soldRPics[0].getWidth(),soldRPics[0].getHeight());
			if(r.intersects(p)&&b.type==0){	
				flyers.remove(f);
				Special s = new Special ((int)f.x,(int)f.y,"fly",mask); //different special 
				specials.add(s);
				b.type = 2;
				score+=10;
			}
		}
		Rectangle p = new Rectangle(x,y,flyRPics[0].getWidth(),flyRPics[0].getHeight()); //enemy box
		if(q.intersects(p)){
			if(lives>=0){
				if(immunesec==0){
					immune();
					vy = 0; 
					jumpHeight = 0;
				}
			}
		}
	}
	public void startMenu(){//starts menu user can choose its character and instructions
		batch.draw(menuPics[0],0,0); //default menu picture
		Rectangle rec2 = new Rectangle(92,288,425,122); //start
		Rectangle rec1 = new Rectangle(312,205,208,70); //credits 
		Rectangle rec3 = new Rectangle(92,205,208,70); //instructions
		if(rec1.contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())){ //if it collides
			batch.draw(menuPics[3],0,0); //draw the corresponding picture
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ //if user clicks
				cred= true; //start the option
			}
		}
		if(rec3.contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY())){ //same function for the rest
			batch.draw(menuPics[2],0,0); //of the code
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				instr = true;
			}
		}
		if(rec2.contains(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY())){
			batch.draw(menuPics[1],0,0);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				pick = true;
			}
		}
	}
	public boolean checkIfClear(int x, int y, Pixmap m){ //check if the position is on the platform
		if(x<0 || x>= m.getWidth() || y<0 || y>= m.getHeight()){ 
			return false;
		}
		int c = m.getPixel(x, m.getHeight()- y);
		return c != plat;
	}
	public void checkShoot(){ //draw bubble depending on which side it shoot
		if(side.equals("right")){
			drawBubRight();
		}
		else{
			drawBubLeft();
		}
	}
	public void drawScore(){ //display current score
		font.setColor(255, 255, 255, 255); 
		font.draw(batch, "Your Score:", 55, 550);
		font.draw(batch,Integer.toString(score),168+new GlyphLayout(font,Integer.toString(score)).width,550);
		font.draw(batch, "Lives:",450,550);
		font.draw(batch, Integer.toString(lives),510+new GlyphLayout(font,Integer.toString(lives)).width,550);
	}
	public void startScreen(){ //loading screen
		sec++;
		batch.draw(titles[(int)(sec/20)],0,0); //texture changes 
		if(sec>59){
			sec = 0;
		}
		if(Gdx.input.isKeyJustPressed(Keys.CONTROL_LEFT)||Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT)){
			load = false; //if user pressed control loading ends
			sec = 0;
		}
	}
	public void walk(String s){ //changes x positions
		sec++;
		if(s=="right"){
			xpos+=5;
		}
		else{
			xpos-=5;
		}
	}
	public void drawBack(){ //draw backgrounds
		batch.draw(backs[level-1],0,0);
	}
	public void drawWalk(int sec){ //draw walking and jumping 
		if(!still&&!jumpState&&jumpState2){ //depending on the boolean draw different textures
			batch.draw(motion.get(walkDir)[sec/4],xpos,ypos);
		}
		else if(!still&&jumpState&&!jumpState2){ //flap wings when it is falling
			batch.draw(motion.get(walkDir+2)[2],xpos,ypos);
		}
		else if(!still&&jumpState&&jumpState2){ 
			if(deltaTime<2){
				batch.draw(motion.get(walkDir+2)[0],xpos,ypos);
			}
			else{	
				batch.draw(motion.get(walkDir+2)[1],xpos,ypos);
			}
		}
		else{ 
			batch.draw(motion.get(walkDir)[0],xpos,ypos);
		}	
	}
	public void shootBub(String s){ 
		if(shoot==false){
			shootsec++;
			if(shootsec==20){
				shootsec = 0;
				shoot=true;
			}
		}
		else if(checkImmune()){ //the guy can't shoot when he is dead
			if(s == "right"){ //shooting direction varies from where the guy is pointing towards
				if(xpos+30>=Gdx.graphics.getWidth()-70){ //when the bubble hits the wall
					Bub bubble = new Bub(Gdx.graphics.getWidth()-70,ypos+15,0,1,0,0); //shooting starts farther from the user  
					bubbles.add(bubble); //modify the x value
				}
				else{
					Bub bubble = new Bub(xpos+50,ypos+15,0,1,0,0); 
					bubbles.add(bubble);
				}
			}
			else{ //if side is left shoot left if not shoot right
				if(xpos-30<=55){ //when the bubble hits the wall
					Bub bubble = new Bub(55,ypos+15,0,0,0,0);
					bubbles.add(bubble); //modify the x value
				}
				else{
					Bub bubble = new Bub(xpos-50,ypos+15,0,0,0,0);
					bubbles.add(bubble);
				}
			}
			shoot=false;
		}
	}
	public void drawBubRight(){
		if(shootsec==20){
			shootsec = 0; 
			shoot = true;
		}	
		else if(shootsec<5&&checkImmune()){
			batch.draw(motion.get(4)[0],xpos,ypos);
		}
		else{
			if(checkImmune()){
				batch.draw(motion.get(4)[1],xpos,ypos);
			}
		}
		shootsec++;
	}
	public void drawBubLeft(){
		if(shootsec==20){
			shootsec = 0; 
			shoot = true;
		}
		else if(shootsec<5&&checkImmune()){
			batch.draw(motion.get(5)[0],xpos,ypos);
		}
		else{
			if(checkImmune()){
				batch.draw(motion.get(5)[1],xpos,ypos);
			}
			
		}
		shootsec++;
	}
	public void createflyer(Pixmap m){
		for(int i=0; i<2;i++){
			Flyer f;
			if(i%2==0){
				f = new Flyer(300+i*80,410); //use the current mask to
			} //figure out the borders and platforms
			else{
				f = new Flyer(300-i*80,410); //summons different angle flyers
			}
			flyers.add(f);
		}
	}
	public void createene(Pixmap m){
	
		for(int i=0; i<6;i++){
			Enemy e = new Enemy(140+i*50,540,m); //use the current mask mask might differ depending
			enemies.add(e); // on the level so this is the most accurate method
			e.reachGround=false; //enemy drops from the ceiling so they fall until
			//they hit the group
		}
	
	}
	public void enemy(){ //runs every single soldier
		for(int i=0; i<enemies.size();i++){
			Enemy e = enemies.get(i);
			e.move(); //randomly moves soldier to left, right or jump
			if(e.jump=="j"){
				if(e.side=="r"){
					batch.draw(soldRPics[0],e.x,e.y);
				}
				else if(e.side=="l"){
					batch.draw(soldLPics[0],e.x,e.y);
				}
			}
			else if(e.side=="r"){
				batch.draw(soldRPics[0],e.x,e.y);
			}
			else if(e.side=="l"){
				batch.draw(soldLPics[0],e.x,e.y);
			}
			deathE(e); //check if it collided with a bubble or with user
		}
	}
	public void createHats(){
		Hat h1 = new Hat(292,26);
		Hat h2 = new Hat(52, 140);
		Hat h3 = new Hat(527, 140);
		Hat h4 = new Hat(223, 255);
		Hat h5 = new Hat(355, 255);
		hats.add(h1);
		hats.add(h2);
		hats.add(h3);
		hats.add(h4);
		hats.add(h5);
	}
	public void runHats(){
		for(int i = 0; i<hats.size(); i++){
			Hat hatty= hats.get(i);
			if(!hatty.backDown&&!hatty.backUp){
				batch.draw(hatPics[0], hatty.x, hatty.y);
			}
			else{
				if(hatty.backDown||hatty.backUp){
					batch.draw(hatPics[2], hatty.x, hatty.y);
					batch.draw(bulletPics[0],hatty.bx,hatty.by);
				}
			}
		}
	}
	public void deathE(Enemy e){
		Rectangle q = new Rectangle(xpos,ypos,motion.get(0)[0].getWidth(),motion.get(0)[0].getHeight()); //rectangle for user
		for(int i=0; i<bubbles.size();i++){
			Bub b = bubbles.get(i);
			Rectangle r = new Rectangle(b.x,b.y,bubPics[0].getWidth(),bubPics[0].getHeight()); //bubble box
			Rectangle p = new Rectangle(e.x,e.y,soldRPics[0].getWidth(),soldRPics[0].getHeight());
			if(r.intersects(p)&&b.type==0){	
				enemies.remove(e);
				Special s = new Special (e.x,e.y,"sold",mask);
				specials.add(s);
				b.type = 2;
				score+=10;
			}
		}
		Rectangle p = new Rectangle(e.x,e.y,soldRPics[0].getWidth(),soldRPics[0].getHeight()); //enemy box
		if(q.intersects(p)){
			if(lives>=0){
				if(immunesec==0){
					immune();
					vy = 0; 
					jumpHeight = 0;
				}
			}
		}
	}
	public boolean checkImmune(){
		if(immunesec<120&&immunesec>0){

			return false;
		}
		return true;
	}
	public void immune(){
		immunesec++;
		if(immunesec==1){
			lives-=1;
		}
	}
	public void immuneDraw(){
		if(immunesec>=60&&immunesec<120){
			batch.draw(motion.get(6)[(immunesec-60)/15],xpos,ypos);
		}
		else if(immunesec<60){
			batch.draw(motion.get(6)[immunesec/15],xpos,ypos);
		}
	}
	public void bubblemethod(){
		if(bubbles.size()>0){
			for(int i=0;i<bubbles.size();i++){
				Bub b = bubbles.get(i);
				batch.draw(bubPics[b.type], b.x,b.y);
				if(b.type==0){	
					b.gonext();
					if(b.x-b.orix>80 || b.orix-b.x>80){
						b.type = 1;
					}
				}
				else if(b.type==1 || b.type==2){
					if(b.y<400){
						b.goup();
					}
					else{
						if(270<b.x&&b.x<300){
							
						}
						else{
							b.center();
						}
					}
				}
				b.timeup();
				Rectangle r = new Rectangle(b.x,b.y,bubPics[1].getWidth(),bubPics[1].getHeight());
				Rectangle p = new Rectangle(xpos,ypos,motion.get(0)[0].getWidth(),motion.get(0)[0].getHeight());
				
				if(r.intersects(p) && b.type ==1){
					bubbles.remove(b);
				}
				if(r.intersects(p) && b.type == 2){
					bubbles.remove(b);	
				}
				if(b.time==600){
					bubbles.remove(b);
				}
			}
		}
	}
	public void jump(String s){
		if(jumped==false){ // jumped == falling
			if(120<=ypos-jumpy && ypos-jumpy<=150 && col!=background ){
				ypos-=5;
			}
		}
		else{
			if(ypos-jumpy>150){
				jumped = false;
			}
			else if(120<=ypos-jumpy&&ypos-jumpy<=150){
				ypos+=5;
			}
			else{ 
				ypos+=7;
			}
		}
	}
	public boolean clear(int x, int y){
		if(x>=mask.getWidth()||y>=mask.getHeight()){
			return false;
		}
		int c = mask.getPixel(x+15,y);
		return c != wall;
	}
	public int cnum(int r, int g, int b, int a){
		return (r<<24) + (g<<16) + (b<<8) + a;
	}	
	public void deadUser(){
		if(lives<0){
			dead = true;
			FileHandle file = Gdx.files.local("highscore.txt");
			String text = file.readString();
			String [] wordsList = text.split("\n");
			//ArrayList<Score> words=new ArrayList<Score>();
			scores = new ArrayList<Integer>();
			for(String w:wordsList){
				try{
					scores.add(Integer.parseInt(w));
				}
				catch(NumberFormatException ex){
					// no nothing, Macs are silly ... &d
				}
			}
			// skip over mystery &d
			scores.add(score);
			Collections.sort(scores);	
			file.writeString(String.format("%d\n%d\n%d",scores.get(3),scores.get(2),scores.get(1)), false);
		}
	}
	public void intro(){ //save this one
		int x=190;
		int y=100;
		batch.begin();
		sec++;
		batch.draw(intro[(int) sec/20%2],0,0);

		if(sec%29==0){
			x+=5;
		}
		if(sec%31==0){
			y-=5;
		}
		if(sec%37==0){
			x-=5;
		}
		if(sec%41==0){
			y+=5;
		}
		batch.draw(bubDude[(int)sec/10%2],x,y);
		batch.end();
	}
}
