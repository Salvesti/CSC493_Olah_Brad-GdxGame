package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CollisionZone extends AbstractGameObject
{
	public CollisionZone()
	{
		init();
	}

	private void init()
	{
		dimension.set(1,1);
		bounds.set(0, 0, 1, 1);
	}
	@Override
	public void render(SpriteBatch batch)
	{
		//Does not have graphics so does not need a render method.
	}

}
