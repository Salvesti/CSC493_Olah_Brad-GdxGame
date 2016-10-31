package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	public AssetFonts fonts;
	public AssetSounds sounds;
	public AssetMusic music;

	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//Set asset manager error handler
		assetManager.setErrorListener(this);
		//Load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class);
		//Load sounds
		assetManager.load("sounds/jump.wav",Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		//Load music
		assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);
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
		fonts = new AssetFonts();
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
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
			waterOverlay = atlas.findRegion("water_overlay");
		}
	}
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
	
	public class AssetSounds
	{
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;
		public AssetSounds (AssetManager am)
		{
			jump = am.get("sounds/jump.wav",Sound.class);
			jumpWithFeather = am.get("sounds/jump_with_feather.wav", Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("sounds/pickup_feather.wav", Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}
	
	public class AssetMusic
	{
		public final Music song01;
		
		public AssetMusic(AssetManager am)
		{
			song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
		}
	}

}




