package com.olah.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.olah.gdx.game.AudioManager;
import com.olah.gdx.game.GamePreferences;

/**
 * A class for handling what is displayed to the player.
 * @author Brad Olah
 */
public class CanyonBunnyMain extends Game
{
	private static final String TAG = CanyonBunnyMain.class.getName();
	private boolean paused;

	private WorldController worldController;
	private WorldRenderer worldRenderer;


	@Override
	public void create()
	{
		//Set LibGDX log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG); //TODO change log level to a more appropriate level when releasing.
		//Load assets
		Assets.instance.init(new AssetManager());
		//Load preferences for audio settings and start  playing musci
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		//Start game at menu screen
		setScreen(new MenuScreen(this));

		//Game world is active on start
		paused = false;
	}
}
