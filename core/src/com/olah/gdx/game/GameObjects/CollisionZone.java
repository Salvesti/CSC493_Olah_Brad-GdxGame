package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A class that handles the logic behind collision zones.
 * @author Brad Olah
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

	public void init()
	{
		dimension.set(obj.dimension.x,obj.dimension.y);
		bounds.set(0, 0, dimension.x,obj.dimension.y);
		type = "collisionZone";
	}

	/**
	 * Does not have graphics so does not draw anything.
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		//Does not have graphics so does not need a render method.
	}

}
