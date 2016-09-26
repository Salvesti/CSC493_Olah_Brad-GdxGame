package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

/**
 * The class that handles the logic behind gap Game objects.
 * @author Brad Olah
 */
public class WallGap extends AbstractGameObject
{
	private TextureRegion gap;
	
	public WallGap()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(1,1);
		
		gap = Assets.instance.levelDecoration.outsideWallGap;
	}
	
	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		
		reg = gap;
		batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);	
	}
}
