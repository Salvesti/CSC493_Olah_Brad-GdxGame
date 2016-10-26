package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Filter;
import com.olah.gdx.game.Assets;
import com.olah.gdx.game.util.Constants;

/**
 * A class that handles the logic behind the sardine powerup
 * @author bo3040
 *
 */
public class Sardines extends AbstractGameObject
{
	private TextureRegion regSardine;

	public Boolean collected;

	public Sardines()
	{
		init();
	}

	/**
	 * Initializes the sardine.
	 */
	private void init()
	{
		dimension.set(1f,1f);

		regSardine = Assets.instance.sardines.sardines;

		//Set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);

		collected = false;
	}

	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regSardine;
		batch.draw(reg.getTexture(),position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),false, false);
	}

	/**
	 * Gives the score value of the sardine and changes the
	 * filter mask that the object uses.
	 * @return int
	 */
	public int getScore()
	{
		//Changes the filter mask that the object uses.
		Filter filter = new Filter();
		filter.maskBits = Constants.MASK_SCOREOBJECT_DEAD;
		body.getFixtureList().peek().setFilterData(filter);
		return 250;
	}

	/**
	 * Gives the time bonus for collecting a sardine.
	 * @return float
	 */
	public float setSardineTime()
	{
		return 10;
	}
}
