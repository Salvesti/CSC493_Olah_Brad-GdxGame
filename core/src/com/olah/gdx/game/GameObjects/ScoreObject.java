package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

/**
 * A class that handles the logic behind the score object.
 * @author bo3040
 *
 */
public class ScoreObject extends AbstractGameObject
{
	private TextureRegion regScoreObject;

	public boolean collected;

	public ScoreObject()
	{
		init();
	}

	/**
	 * Initializes the score object.
	 */
	private void init()
	{
		dimension.set(1f,1f);

		regScoreObject = Assets.instance.scoreObject.random();

		//Set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);

		collected = false;
	}

	@Override
	public void render (SpriteBatch batch)
	{
		if(collected) return;

		TextureRegion reg = null;
		reg = regScoreObject;
		batch.draw(reg.getTexture(),position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),false, false);
	}

	/**
	 * Gives the score value of the score object.
	 * @return int
	 */
	public int getScore()
	{
		return 100;
	}
}
