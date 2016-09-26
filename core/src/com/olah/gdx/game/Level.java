package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.olah.gdx.game.GameObjects.AbstractGameObject;
import com.olah.gdx.game.GameObjects.BlackSpace;
import com.olah.gdx.game.GameObjects.Gap;
import com.olah.gdx.game.GameObjects.Grass;
import com.olah.gdx.game.GameObjects.Table;
import com.olah.gdx.game.GameObjects.TableTile;
import com.olah.gdx.game.GameObjects.Wallpaper;
import com.olah.gdx.game.GameObjects.WindowTile;
import com.olah.gdx.game.GameObjects.WoodFloor;

public class Level 
{
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE
	{
		EMPTY(255,255,255), //White
		WOODFLOOR(0,0,0), //Black
		SURFACE(127,51,0),//Brown
		WALLPAPER(128,128,128),//Gray
		BLACKTILE(178,0,255),//Magenta
		WALLGAP(87,0,127), //Purple
		WINDOW(192,192,192),//Light Gray
		GRASS(0,255,144), //Light Green
		PLAYER_SPAWNPOINT(34,177,76), //Green
		ITEM_SARDINE(237,28,36), //Red
		ITEM_SCORE_OBJECT(0,162,232); //Light blue
	
		private int color;
		private BLOCK_TYPE (int r, int g, int b)
		{
			color = r << 24 | g<<16 | b<< 8 | 0xff;
		}

		public boolean sameColor (int color)
		{
			return this.color == color;
		}
	
		public int getColor()
		{
			return color;
		}	
	}
	
	//objects
	public Array<TableTile> tables;
	public Array<Grass> grass;
	public Array<BlackSpace> blackSpaces;
	public Array<Gap>	gaps;
	public Array<Wallpaper> wallpaper;
	public Array<WindowTile> windows;
	public Array<WoodFloor> woodFloor;

	//decoration

	
	public Level (String filename)
	{
		init(filename);
	}
	
	private void init(String filename)
	{
		//objects
		tables = new Array<TableTile>();
		grass = new Array<Grass>();
		blackSpaces = new Array<BlackSpace>();
		gaps = new Array<Gap>();
		wallpaper = new Array<Wallpaper>();
		windows = new Array<WindowTile>();
		woodFloor = new Array<WoodFloor>();
		
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		//scan pixels from top-left to bottom-right
		for(int pixelY = 0; pixelY < pixmap.getHeight();pixelY++)
		{
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				//Height grows from bottom to top
				float baseHeight = pixmap.getHeight()-pixelY;
				//Get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				/* Find matching color value to identify block type at (x,y)
				 * point and create the corresponding game object if there is
				 * a match.
				 */
				//Empty space
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					//Do Nothing
				}
				//Rock
				else if(BLOCK_TYPE.SURFACE.sameColor(currentPixel))
				{
					obj  = new TableTile();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					tables.add((TableTile)obj);
				}
				else if(BLOCK_TYPE.GRASS.sameColor(currentPixel))
				{
					obj  = new Grass();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					grass.add((Grass)obj);
				}
				else if(BLOCK_TYPE.BLACKTILE.sameColor(currentPixel))
				{
					obj  = new BlackSpace();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					blackSpaces.add((BlackSpace)obj);
				}
				else if(BLOCK_TYPE.WALLPAPER.sameColor(currentPixel))
				{
					obj  = new Wallpaper();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					wallpaper.add((Wallpaper)obj);
				}
				else if(BLOCK_TYPE.WALLGAP.sameColor(currentPixel))
				{
					obj  = new Gap();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					gaps.add((Gap)obj);
				}
				else if(BLOCK_TYPE.WINDOW.sameColor(currentPixel))
				{
					obj  = new WindowTile();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					windows.add((WindowTile)obj);
				}
				else if(BLOCK_TYPE.WOODFLOOR.sameColor(currentPixel))
				{
					obj  = new WoodFloor();
					obj.position.set(pixelX,baseHeight*obj.dimension.y+offsetHeight);
					woodFloor.add((WoodFloor)obj);
				}
				//Player Spawn point
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
					//TODO Implement
				}
				//Feather
				else if(BLOCK_TYPE.ITEM_SARDINE.sameColor(currentPixel))
				{
					//TODO Implement
				}
				//Gold coin
				else if(BLOCK_TYPE.ITEM_SCORE_OBJECT.sameColor(currentPixel))
				{
					//TODO Implement
				}
				//Unknown object/pixel color
				else
				{
					int r = 0xff & (currentPixel >>> 24); //Red color channel
					int g = 0xff & (currentPixel >>> 16); //Red color channel
					int b = 0xff & (currentPixel >>> 8); //Red color channel
					int a = 0xff & (currentPixel); //Red color channel
					Gdx.app.error(TAG, "Unknown object at x<"+pixelX+">, y<"+pixelY+">: r<"+r+"> g<"+g+"> b<"+b+"> a<"+a+">");
				}
			}
		}
		/*
		//Decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0,2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1,-1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0,-3.75f);
		*/
		//Free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '"+filename+"' loaded");
	}
	
	public void render(SpriteBatch batch)
	{
		
		//Draw grass
		for(Grass grass : grass)
		{
			grass.render(batch);
		}
		//Draw BlackSpace
		for(BlackSpace space : blackSpaces)
		{
			space.render(batch);
		}
		//Draw gaps
		for(Gap gap : gaps)
		{
			gap.render(batch);
		}
		//Draw wallpaper
		for(Wallpaper wall : wallpaper)
		{
			wall.render(batch);
		}
		//Draw windows
		for(WindowTile window : windows)
		{
			window.render(batch);
		}
		//Draw windows
		for(WoodFloor floor : woodFloor)
		{
			floor.render(batch);
		}
		//Draw tables
		for(TableTile table : tables)
		{
			table.render(batch);
		}
		
	}
}

