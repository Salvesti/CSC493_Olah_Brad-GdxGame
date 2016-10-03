package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.olah.gdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * Manages the gathering and interactions with assets.
 * @author Brad Olah
 */
public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	//Singleton: Prevent instantiation from other classes.
	private Assets(){}

	//A copy of each local Asset class to be used elsewhere.
	public AssetCat cat;
	public AssetSardines sardines;
	public AssetLaser laserPointer;
	public AssetScoreObject scoreObject;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;

	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//Set asset manager error handler
		assetManager.setErrorListener(this);

		//Load texture atlas
		assetManager.load(Constants.ITEM_ATLAS_OBJECTS,TextureAtlas.class);
		assetManager.load(Constants.TILE_ATLAS_OBJECTS,TextureAtlas.class);
		//Start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "+assetManager.getAssetNames().size);
		for (String a: assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG, "asset: " + a);
		}

		//Gets the value for the Texture Atlas location from the constants class.
		TextureAtlas itemAtlas = assetManager.get(Constants.ITEM_ATLAS_OBJECTS);
		TextureAtlas tileAtlas = assetManager.get(Constants.TILE_ATLAS_OBJECTS);
		
		//Enable texture filtering for pixel smoothing
		for(Texture t : itemAtlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		for(Texture t : tileAtlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		//Create game resource objects
		fonts = new AssetFonts();
		cat = new AssetCat();
		sardines = new AssetSardines(itemAtlas);
		laserPointer = new AssetLaser(itemAtlas);
		scoreObject = new AssetScoreObject(itemAtlas);
		levelDecoration = new AssetLevelDecoration(tileAtlas);
		
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	//Was @Override in the book. Was this actually supposed to be overridden?
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'",(Exception)throwable);
	}


	/*---------------------------------------------------------------------------------------------------------------------------
	 * --------------------------------------------------------------------------------------------------------------------------
	 * Inner classes for the asset images from the Texture atlas.
	 */
	/**
	 * A class that holds the information about the font Assets
	 * @author Brad Olah
	 */
	public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts()
		{
			//Create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"),true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"),true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"),true);
			//Set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			//Enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}


	
	/**
	 * A class that holds the information about the cat Asset
	 * @author Brad Olah
	 */
	public class AssetCat
	{
		public final Texture cat;

		public AssetCat()
		{
			cat = new Texture(Gdx.files.internal("images/cat.png"));
		}
	}
	
	/**
	 * A class that holds the information about the Sardines Asset
	 * @author Brad Olah
	 */
	public class AssetSardines
	{
		public final AtlasRegion sardines;

		public AssetSardines (TextureAtlas atlas)
		{
			sardines = atlas.findRegion("sardines");
		}
	}

	/**
	 * A class that holds the information about the Laser Pointer Asset
	 * @author Brad Olah
	 */
	public class AssetLaser
	{
		public final AtlasRegion laser;

		public AssetLaser (TextureAtlas atlas)
		{
			laser = atlas.findRegion("laser_pointer");
		}
	}

	/**
	 * A class that holds the information about the Gold Coin Asset
	 * @author Brad Olah
	 */
	public class AssetScoreObject
	{
		public final AtlasRegion beerBottle;
		public final AtlasRegion can;
		public final AtlasRegion cardboardBox;
		public final AtlasRegion cup;
		public final AtlasRegion glassJar;
		public final AtlasRegion laundaryDetergent;
		public final AtlasRegion milkJug;
		public final AtlasRegion mug;
		public final AtlasRegion newspaper;
		public final AtlasRegion paperBag;
		public final AtlasRegion pizzaBox;
		public final AtlasRegion sodaBottle;
		public final AtlasRegion sodaCan;
		public final AtlasRegion waterBottle;
		public final AtlasRegion sprayPaint;

		public AssetScoreObject (TextureAtlas atlas)
		{
			beerBottle = atlas.findRegion("beer_bottle");
			can = atlas.findRegion("can");
			cardboardBox = atlas.findRegion("cardboard_box");
			cup = atlas.findRegion("cup");
			glassJar = atlas.findRegion("glass_jar");
			laundaryDetergent = atlas.findRegion("laundary_detergent");
			milkJug = atlas.findRegion("milk_jug");
			mug = atlas.findRegion("mug");
			newspaper = atlas.findRegion("newspaper");
			paperBag = atlas.findRegion("paper_bag");
			pizzaBox = atlas.findRegion("pizza_box");
			sodaBottle = atlas.findRegion("soda_bottle");
			sodaCan = atlas.findRegion("soda_can");
			waterBottle = atlas.findRegion("water_bottle");
			sprayPaint = atlas.findRegion("spray_paint");
		}

		public TextureRegion random() 
		{
			Array<TextureRegion> scoreObjects = new Array<TextureRegion>();
			scoreObjects.add(beerBottle);
			scoreObjects.add(can);
			scoreObjects.add(cardboardBox);
			scoreObjects.add(cup);
			scoreObjects.add(glassJar);
			scoreObjects.add(laundaryDetergent);
			scoreObjects.add(milkJug);
			scoreObjects.add(mug);
			scoreObjects.add(newspaper);
			scoreObjects.add(paperBag);
			scoreObjects.add(pizzaBox);
			scoreObjects.add(sodaBottle);
			scoreObjects.add(sodaCan);
			scoreObjects.add(waterBottle);
			scoreObjects.add(sprayPaint);
			
			return scoreObjects.random();
		}
	}

	/**
	 * A class that holds the information about the Decoration Assets
	 * @author Brad Olah
	 */
	public class AssetLevelDecoration
	{
		public final AtlasRegion woodFloor;
		public final AtlasRegion wallpaper;
		public final AtlasRegion table;
		public final AtlasRegion window;
		public final AtlasRegion blackSpace;
		public final AtlasRegion outsideWallGap;
		public final AtlasRegion grass;
		public AssetLevelDecoration (TextureAtlas atlas)
		{
			woodFloor = atlas.findRegion("floor_block");
			wallpaper = atlas.findRegion("wallpaper_block");
			table = atlas.findRegion("table_block");
			window = atlas.findRegion("window_block");
			blackSpace = atlas.findRegion("outside_wall");
			outsideWallGap =atlas.findRegion("outside_wall_gap");
			grass =atlas.findRegion("grass");
		}
	}

}




