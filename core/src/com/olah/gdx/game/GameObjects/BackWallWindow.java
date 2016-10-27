package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

/**
 * The class that handles the logic behind window Game objects.
 * @author Brad Olah
 */
public class BackWallWindow  extends AbstractGameObject
{
	private TextureRegion window;

	public BackWallWindow()
	{
		init();
	}

	public void init()
	{
		super.init();
		dimension.set(1,1);

		window = Assets.instance.levelDecoration.window;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		float relX = 0;
		float relY = 0;

		reg = window;
		batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);
	}
}
