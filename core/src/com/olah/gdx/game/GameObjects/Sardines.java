package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

/**
 * A class that handles the logic behind the sardine powerup
 * @author Brad Olah
 *
 */
public class Sardines extends AbstractGameObject
{
	private TextureRegion regSardine;

	public Sardines()
	{
		init();
	}

	/**
	 * Initializes the sardine.
	 */
	@Override
	public void init()
	{
		dimension.set(1f,1f);

		regSardine = Assets.instance.sardines.sardines;

		//Set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);
		type = "collidableObject";
	}

	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regSardine;
		batch.draw(reg.getTexture(),position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),false, false);
	}

	/**
	 * Gives the score value of the sardine.
	 * @return int
	 */
	public int getScore()
	{
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
