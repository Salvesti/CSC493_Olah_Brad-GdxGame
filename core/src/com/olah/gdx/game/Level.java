package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.olah.gdx.game.GameObjects.AbstractGameObject;
import com.olah.gdx.game.GameObjects.BunnyHead;
import com.olah.gdx.game.GameObjects.Clouds;
import com.olah.gdx.game.GameObjects.Feather;
import com.olah.gdx.game.GameObjects.GoldCoin;
import com.olah.gdx.game.GameObjects.Mountains;
import com.olah.gdx.game.GameObjects.Rock;
import com.olah.gdx.game.GameObjects.WaterOverlay;

public class Level 
{
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE
	{
		EMPTY(255,255,255), //White
		ROCK(0,0,0), //Black
		PLAYER_SPAWNPOINT(34,177,76), //Green
		ITEM_FEATHER(237,28,36), //Red
		ITEM_GOLD_COIN(0,162,232); //Light blue
	
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
	public Array<Rock> rocks;
	public BunnyHead bunnyHead;
	public Array<GoldCoin> goldCoins;
	public Array<Feather> feathers;

	//decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	
	public Level (String filename)
	{
		init(filename);
	}
	
	private void init(String filename)
	{
		//player character
		bunnyHead = null;
		//objects
		rocks = new Array<Rock>();
		goldCoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		//scan pixels from top-left to bottom-right
		int lastPixel = -1;
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
				else if(BLOCK_TYPE.ROCK.sameColor(currentPixel))
				{
					if(lastPixel != currentPixel)
					{
						obj  = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX,baseHeight*obj.dimension.y*heightIncreaseFactor+offsetHeight);
						rocks.add((Rock)obj);
					}else
					{
						rocks.get(rocks.size-1).increaseLength(1);
					}
				}
				//Player Spawn point
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					bunnyHead = (BunnyHead)obj;
				}
				//Feather
				else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel))
				{
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					feathers.add((Feather)obj);
				}
				//Gold coin
				else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
				{
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight);
					goldCoins.add((GoldCoin)obj);
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
				lastPixel = currentPixel;
			}
		}
		//Decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0,2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1,-1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0,-3.75f);
		
		//Free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '"+filename+"' loaded");
	}
	
	/**
	 * Renders all of the game objects to the screen.
	 * @param batch
	 */
	public void render(SpriteBatch batch)
	{
		//Draw mountains
		mountains.render(batch);
		//Draw rocks
		for(Rock rock : rocks)
		{
			rock.render(batch);
		}
		//Draw Gold Coins
		for(GoldCoin goldCoin:goldCoins)
		{
			goldCoin.render(batch);
		}
		//Draw Feathers
		for(Feather feather : feathers)
		{
			feather.render(batch);
		}
		//Draw player character
		bunnyHead.render(batch);
		
		//Draw water overlay
		waterOverlay.render(batch);
		
		//Draw clouds
		clouds.render(batch);
	}
	
	/**
	 * Updates all game objects in the level.
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		bunnyHead.update(deltaTime);
		for(Rock rock: rocks)
		{
			rock.update(deltaTime);
		}
		for(GoldCoin goldCoin : goldCoins)
		{
			goldCoin.update(deltaTime);
		}
		for(Feather feather : feathers)
		{
			feather.update(deltaTime);
		}
		clouds.update(deltaTime);
	}
}

