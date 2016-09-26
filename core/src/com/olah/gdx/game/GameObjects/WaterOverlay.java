package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

/**
 * The class that handles the logic behind WaterOverlay Game objects.
 * @author Brad Olah
 */
public class WaterOverlay extends AbstractGameObject
{
	private TextureRegion regWaterOverlay;
	private float length;
	
	public WaterOverlay(float length)
	{
		this.length = length;
		init();
	}
	
	private void init()
	{
		dimension.set(length * 10, 3);
		
		regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;
		
		origin.x = -dimension.x/2;
	}
	
	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg = null;
		reg = regWaterOverlay;
		batch.draw(reg.getTexture(), position.x+origin.x, position.y+origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);		
	}
}
