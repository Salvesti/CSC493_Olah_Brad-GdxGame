package com.olah.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

/**
 * A class for handling what is displayed to the player.
 * @author Brad Olah
 */
public class CatGameMain implements ApplicationListener
{
	private static final String TAG = CatGameMain.class.getName();
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
		//Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);

		//Game world is active on start
		paused = false;
	}

	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width,height);
	}

	@Override
	public void render()
	{
		//Do not update the game if it is paused.
		if(!paused)
		{
			//Update game world by the time that has passed
			//since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
		}
			//Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		//Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Render game world to screen
		worldRenderer.render();
	}

	@Override
	public void pause()
	{
		paused = true;
	}

	@Override
	public void resume()
	{
		paused = false;
		//Load assets
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void dispose()
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
