package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.olah.gdx.game.GameObjects.AbstractGameObject;
import com.olah.gdx.game.GameObjects.WallBlack;
import com.olah.gdx.game.GameObjects.BackWallGap;
import com.olah.gdx.game.GameObjects.FloorGrass;
import com.olah.gdx.game.GameObjects.FloorTable;
import com.olah.gdx.game.GameObjects.BackWallWallpaper;
import com.olah.gdx.game.GameObjects.BackWallWindow;
import com.olah.gdx.game.GameObjects.Cat;
import com.olah.gdx.game.GameObjects.CollisionZone;
import com.olah.gdx.game.GameObjects.FloorWood;
import com.olah.gdx.game.GameObjects.LaserPointer;
import com.olah.gdx.game.GameObjects.Sardines;
import com.olah.gdx.game.GameObjects.ScoreObject;

/**
 * A class that handles the rendering of levels based on a picture input.
 * @author Brad Olah
 */
public class Level
{
	public static final String TAG = Level.class.getName();

	/**
	 * An Enum holding the color values associated with block types.
	 * @author Brad Olah
	 */
	public enum BLOCK_TYPE
	{
		EMPTY(255,255,255), //White
		FLOOR_WOOD(0,0,0), //Black
		FLOOR_TABLE(127,51,0),//Brown
		FLOOR_GRASS(0,255,144), //Light Green
		BACK_WALL_WALLPAPER(128,128,128),//Gray
		BACK_WALL_WINDOW(192,192,192),//Light Gray
		WALL_BLACK(178,0,255),//Magenta
		WALL_GAP(87,0,127), //Purple
		PLAYER_SPAWNPOINT(34,177,76), //Green
		ITEM_SARDINE(237,28,36), //Red
		ITEM_SCORE_OBJECT(0,162,232), //Light blue
		ITEM_LASER_POINTER(255,128,0);//Orange

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
	public Cat cat;
	public Array<Sardines> sardines;
	public Array<ScoreObject> scoreObjects;
	public Array<LaserPointer> laserPointers;
	public Array<FloorTable> floorTables;
	public Array<FloorGrass> floorGrass;
	public Array<FloorWood> floorWood;
	public Array<WallBlack> wallBlack;
	public Array<BackWallGap>	wallGaps;
	public Array<BackWallWallpaper> backWallWallpaper;
	public Array<BackWallWindow> backWallWindows;
	public Array<CollisionZone>	collisionZones;


	//decoration


	public Level (String foreground, String background)
	{
		init(foreground,background);
	}

	/**
	 * Initializes the level based on the given foreground and background.
	 * @param foreground
	 */
	private void init(String foreground,String background)
	{
		//Player
		cat = null;
		//objects
		sardines = new Array<Sardines>();
		scoreObjects = new Array<ScoreObject>();
		laserPointers = new Array<LaserPointer>();
		floorTables = new Array<FloorTable>();
		floorGrass = new Array<FloorGrass>();
		wallBlack = new Array<WallBlack>();
		wallGaps = new Array<BackWallGap>();
		backWallWallpaper = new Array<BackWallWallpaper>();
		backWallWindows = new Array<BackWallWindow>();
		floorWood = new Array<FloorWood>();
		collisionZones = new Array<CollisionZone>();

		//Builds the two layers.
		buildFromLayer(background,0);
		buildFromLayer(foreground,0);
	}
	/**
	 * Draws objects based on the image passed into the method.
	 * @param layer
	 */
	public void buildFromLayer(String layer,float offset)
	{
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(layer));
		//scan pixels from top-left to bottom-right
		for(int pixelY = 0; pixelY < pixmap.getHeight();pixelY++)
		{
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				AbstractGameObject obj = null;
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
				else if(BLOCK_TYPE.FLOOR_TABLE.sameColor(currentPixel))
				{
					obj  = new FloorTable();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					floorTables.add((FloorTable)obj);

					obj = new CollisionZone(floorTables.peek());
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					collisionZones.add((CollisionZone)obj);
				}
				else if(BLOCK_TYPE.FLOOR_GRASS.sameColor(currentPixel))
				{
					obj  = new FloorGrass();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					floorGrass.add((FloorGrass)obj);

					obj = new CollisionZone(floorGrass.peek());
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					collisionZones.add((CollisionZone)obj);
				}
				else if(BLOCK_TYPE.WALL_BLACK.sameColor(currentPixel))
				{
					obj  = new WallBlack();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					wallBlack.add((WallBlack)obj);

					obj = new CollisionZone(wallBlack.peek());
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					collisionZones.add((CollisionZone)obj);
				}
				else if(BLOCK_TYPE.BACK_WALL_WALLPAPER.sameColor(currentPixel))
				{
					obj  = new BackWallWallpaper();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					backWallWallpaper.add((BackWallWallpaper)obj);
				}
				else if(BLOCK_TYPE.WALL_GAP.sameColor(currentPixel))
				{
					obj  = new BackWallGap();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					wallGaps.add((BackWallGap)obj);
				}
				else if(BLOCK_TYPE.BACK_WALL_WINDOW.sameColor(currentPixel))
				{
					obj  = new BackWallWindow();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					backWallWindows.add((BackWallWindow)obj);
				}
				else if(BLOCK_TYPE.FLOOR_WOOD.sameColor(currentPixel))
				{
					obj  = new FloorWood();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					floorWood.add((FloorWood)obj);

					obj = new CollisionZone(floorWood.peek());
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y);
					collisionZones.add((CollisionZone)obj);
				}
				//Player Spawn point
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
					obj = new Cat();
					obj.position.set(pixelX + offset,baseHeight*obj.dimension.y/2);
					cat = (Cat)obj;
				}
				//Sardine
				else if(BLOCK_TYPE.ITEM_SARDINE.sameColor(currentPixel))
				{
					obj = new Sardines();
					obj.position.set(pixelX + offset,baseHeight * obj.dimension.y);
					sardines.add((Sardines)obj);
				}
				//Score Object
				else if(BLOCK_TYPE.ITEM_SCORE_OBJECT.sameColor(currentPixel))
				{
					obj = new ScoreObject();
					obj.position.set(pixelX + offset,baseHeight * obj.dimension.y);
					scoreObjects.add((ScoreObject)obj);
				}else if(BLOCK_TYPE.ITEM_LASER_POINTER.sameColor(currentPixel))
				{
					obj = new LaserPointer();
					obj.position.set(pixelX + offset,baseHeight * obj.dimension.y);
					laserPointers.add((LaserPointer)obj);
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
		//Free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '"+layer+"' loaded");
	}

	/**
	 * Renders all of the game objects
	 * @param batch
	 */
	public void render(SpriteBatch batch)
	{

		//Draw grass
		for(FloorGrass grass : floorGrass)
		{
			grass.render(batch);
		}
		//Draw gaps
		for(BackWallGap gap : wallGaps)
		{
			gap.render(batch);
		}
		//Draw wallpaper
		for(BackWallWallpaper wall : backWallWallpaper)
		{
			wall.render(batch);
		}
		//Draw windows
		for(BackWallWindow window : backWallWindows)
		{
			window.render(batch);
		}
		//Draw windows
		for(FloorWood floor : floorWood)
		{
			floor.render(batch);
		}
		//Draw tables
		for(FloorTable table : floorTables)
		{
			table.render(batch);
		}
		//Draw BlackSpace
		for(WallBlack space : wallBlack)
		{
			space.render(batch);
		}
		//Draw Sardines
		for(Sardines sardine : sardines)
		{
			sardine.render(batch);
		}
		//Draw Score objects
		for(ScoreObject scoreObject : scoreObjects)
		{
			scoreObject.render(batch);
		}
		//Draw Laser Pointer objects
		for(LaserPointer laser : laserPointers)
		{
			laser.render(batch);
		}

		//Draw player
		cat.render(batch);
	}

	/**
	 * Updates all game objects in the level.
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		cat.update(deltaTime);
		for(Sardines sardine : sardines)
		{
			sardine.update(deltaTime);
		}
		for(ScoreObject scoreObject : scoreObjects)
		{
			scoreObject.update(deltaTime);
		}
		for(LaserPointer laser: laserPointers)
		{
			laser.update(deltaTime);
		}

	}

}

