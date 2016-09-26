package com.olah.gdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	private void renderGui(SpriteBatch batch2) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		renderGuiTime(batch);
		batch.end();
	}
	private void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	private void renderGuiTime(SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		Assets.instance.fonts.defaultBig.draw(batch, "Time: "+worldController.time, x+75, y+37);
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
