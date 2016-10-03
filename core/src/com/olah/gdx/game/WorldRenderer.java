package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.olah.gdx.game.util.Constants;

/**
 *
 * @author Brad Olah
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;

	public WorldRenderer (WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}
	private void init()
	{
		batch = new SpriteBatch();
		//Creates world camera
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		
		//Creates GUI camera
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true); //Flip y-axis
		cameraGUI.update();

	}

	public void render()
	{
		renderWorld(batch);
		renderGui(batch);
	}

	/**
	 * Renders all the elemtns of the gui
	 * @param batch2
	 */
	private void renderGui(SpriteBatch batch2) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		renderGuiTime(batch);
		renderGuiScore(batch);
		renderFpsCounter(batch);
		renderGuiGameOverMessage(batch);
		batch.end();
	}
	
	
	private void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	/**
	 * Renders the game over message
	 */
	private void renderGuiGameOverMessage(SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth/2;
		float y = cameraGUI.viewportHeight/2;
		if(worldController.isGameOver())
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1,0.75f,0.25f,1);
			fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, false);
		}
		
	}
	
	/**
	 * Renders the current fps
	 * @param batch
	 */
	private void renderFpsCounter(SpriteBatch batch) 
	{
		float x = cameraGUI.viewportWidth-55;
		float y = cameraGUI.viewportHeight-15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if(fps >=45)
		{
			//45 or more FPS shows up in green
			fpsFont.setColor(0,1,0,1);
		}else if(fps >=30)
		{
			//30 or more FPS show up in yellow
			fpsFont.setColor(1,1,0,1);
		}else
		{
			//Less than 30 FPS show up in red
			fpsFont.setColor(1,0,0,1);
		}
		fpsFont.draw(batch, "FPS: "+fps, x, y);
		fpsFont.setColor(1,1,1,1); //White
		
	}
	
	/**
	 * Renders the time left in the game
	 * @param batch
	 */
	private void renderGuiTime(SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		Assets.instance.fonts.defaultBig.draw(batch, "Time: "+worldController.time, x+75, y+37);
	}
	
	/**
	 * Renders the current score
	 * @param batch
	 */
	private void renderGuiScore(SpriteBatch batch)
	{
		float x = -15;
		float y = 20;
		Assets.instance.fonts.defaultBig.draw(batch, "Score: "+worldController.score, x+75, y+37);
		
	}

	
	public void resize(int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height)*width;
		camera.update();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}

}
