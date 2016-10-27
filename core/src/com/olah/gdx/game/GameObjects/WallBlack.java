package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

/**
 * The class that handles the logic behind black space Game objects.
 * @author Brad Olah
 */
public class WallBlack extends Surface
{
	private TextureRegion blackSpace;

	public WallBlack()
	{
		init();
	}

	protected void init()
	{
		super.init();
		dimension.set(1,1);

		blackSpace = Assets.instance.levelDecoration.blackSpace;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		float relX = 0;
		float relY = 0;

		reg = blackSpace;
		batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
	}
}
