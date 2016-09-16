package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.olah.gdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
	public AssetBunny bunny;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;

	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//Set asset manager error handler
		assetManager.setErrorListener(this);
		//Load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class);
		//Start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "+assetManager.getAssetNames().size);
		for (String a: assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG, "asset: " + a);
		}

		//Gets the value for the Texture Atlas location from the constants class.
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		//Enable texture filtering for pixel smoothing
		for(Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		//Create game resource objects
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
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
	 * A class that holds the information about the Bunny Asset
	 * @author Brad Olah
	 */
	public class AssetBunny
	{
		public final AtlasRegion head;

		public AssetBunny (TextureAtlas atlas)
		{
			head = atlas.findRegion("bunny_head");
		}
	}

	/**
	 * A class that holds the information about the Bunny Asset
	 * @author Brad Olah
	 */
	public class AssetRock
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock (TextureAtlas atlas)
		{
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	/**
	 * A class that holds the information about the Gold Coin Asset
	 * @author Brad Olah
	 */
	public class AssetGoldCoin
	{
		public final AtlasRegion goldCoin;

		public AssetGoldCoin (TextureAtlas atlas)
		{
			goldCoin = atlas.findRegion("item_gold_coin");
		}
	}

	/**
	 * A class that holds the information about the Feather Asset
	 * @author Brad Olah
	 */
	public class AssetFeather
	{
		public final AtlasRegion feather;

		public AssetFeather (TextureAtlas atlas)
		{
			feather = atlas.findRegion("item_feather");
		}
	}

	/**
	 * A class that holds the information about the Decoration Assets
	 * @author Brad Olah
	 */
	public class AssetLevelDecoration
	{
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;

		public AssetLevelDecoration (TextureAtlas atlas)
		{
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay =atlas.findRegion("water_overlay");
		}
	}

}




