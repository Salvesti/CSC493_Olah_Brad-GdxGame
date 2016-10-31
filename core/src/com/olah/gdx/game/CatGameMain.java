package com.olah.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.olah.gdx.game.Screens.MenuScreen;
import com.olah.gdx.game.util.GamePreferences;

/**
 * A class for handling what happens when the game starts.
 * @author Brad Olah
 */
public class CatGameMain extends Game
{
	private static final String TAG = CatGameMain.class.getName();
	/**
	 * Starts the game on the menu screen
	 */
	@Override
	public void create()
	{
		//Set LibGDX log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG); //TODO change log level to a more appropriate level when releasing.
		//Load assets
		Assets.instance.init(new AssetManager());
		//Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		//Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
