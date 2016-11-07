package com.olah.gdx.game.util;



/**
 * Constants that will be used elsewhere in the game.
 * @author Brad Olah
 */

public class Constants
{
	//Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 10.0f;

	//Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 10.0f;

	//GUI Width and Height
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 480.f;

	//Location of description file for texture atlas
	public static final String ITEM_ATLAS_OBJECTS = "images/items.pack.atlas";
	//Location of description file for time objects
	public static final String TILE_ATLAS_OBJECTS = "images/tiles.pack.atlas";
	//Location of image files for level 01
	public static final String LEVEL_01F = "levels/level-01.png";
	public static final String LEVEL_01B = "levels/level-01b.png";
	//Starting time limit
	public static final int START_TIME = 21;

	//The time in seconds the Sardines lasts for
	public static final float ITEM_SARDINES_POWERUP_DURATION = 9;
	//The time in seconds the laser pointer lasts for
	public static final float ITEM_LASER_POINTER_DURATION = 5;
	//The time is seconds lasers are disabled for after collision.
	public static final float ITEM_LASER_POINTER_TIME_LEFT_DISABLED = 8;

	public static final float TIME_DELAY_GAME_OVER = 3;

	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack.atlas";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

	//Location of description file for skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
	public static final String PREFERENCES = "canyonbunny.prefs";

	//Collision categories.
	public static final short CATEGORY_PLAYER = 0x0001;
	public static final short CATEGORY_SCOREOBJECT_LIVE = 0x0002;
	public static final short CATEGORY_SCOREOBJECT_DEAD = 0x0004;
	public static final short CATEGORY_SCENERY = 0x0008;
	//Collision masks.
	public static final short MASK_PLAYER = CATEGORY_SCOREOBJECT_LIVE|CATEGORY_SCENERY;
	public static final short MASK_SCOREOBJECT_LIVE = CATEGORY_PLAYER|CATEGORY_SCOREOBJECT_LIVE|CATEGORY_SCENERY;
	public static final short MASK_SCOREOBJECT_DEAD = 0; //Collides with nothing.
	public static final short MASK_SCENERY = -1; //Collides with everything.






}
