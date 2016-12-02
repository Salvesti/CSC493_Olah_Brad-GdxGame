package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A type of AbstractGameObject that defines its type string as "surface"
 * @author Brad Olah
 */
public class Surface extends AbstractGameObject
{

	@Override
	protected void init()
	{
		this.type = "surface";
	}

	@Override
	public void render(SpriteBatch batch)
	{
		// TODO Auto-generated method stub
	}

}
