package com.olah.gdx.game.util;



/**
 * Constants that will be used elsewhere in the game.
 * @author Brad Olah
 */

public class Constants
{
	//Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 20.0f;

	//Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 20.0f;

	//GUI Width and Height
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 480.f;
	
	//Location of description file for texture atlas
	public static final String ITEM_ATLAS_OBJECTS = "images/items.pack.atlas";
	//Location of description file for time objects
	public static final String TILE_ATLAS_OBJECTS = "images/tiles.pack.atlas";
	//Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	
	//Starting time limit
	public static final int START_TIME = 300;

	//The time in seconds the Sardines lasts for
	public static final float ITEM_SARDINES_POWERUP_DURATION = 9;

	public static final float TIME_DELAY_GAME_OVER = 3;
}
