package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A class that handles the logic behind collision zones.
 * @author bo3040
 *
 */
public class CollisionZone extends AbstractGameObject
{
	AbstractGameObject obj;
	public CollisionZone(AbstractGameObject obj)
	{
		this.obj = obj;
		init();
	}

	private void init()
	{
		dimension.set(obj.dimension.x,obj.dimension.y);
		bounds.set(0, 0, dimension.x,obj.dimension.y);
	}
	@Override
	public void render(SpriteBatch batch)
	{
		//Does not have graphics so does not need a render method.
	}

}
