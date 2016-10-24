package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.olah.gdx.game.Assets;

/**
 * The class that handles the logic behind Mountain Game objects.
 * @author Brad Olah
 */
public class Mountains extends AbstractGameObject
{
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	
	public void updateScrollPosition(Vector2 camPosition)
	{
		position.set(camPosition.x,position.y);
	}
	
	private int length;
	
	public Mountains(int length)
	{
		this.length = length;
		init();
	}
	
	private void init()
	{
		dimension.set(10,2);
		
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;
		
		//Shift mountain and extend length
		origin.x = -dimension.x*2;
		length += dimension.x*2;
	}
	
	private void drawMountains(SpriteBatch batch, float offsetX,float offsetY,float tintColor, float parallaxSpeedX)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor,tintColor,tintColor,1);
		float relX = dimension.x*offsetX;
		float relY = dimension.y*offsetY;
		
		//Mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length/(2*dimension.x) * (1-parallaxSpeedX));
		mountainLength += MathUtils.ceil(.05f+offsetX);
		for(int i = 0; i < mountainLength; i++)
		{
			//Mountain left
			reg = regMountainLeft;
			batch.draw(reg.getTexture(), origin.x+relX+position.x * parallaxSpeedX, origin.y+relY+position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		
			//Mountain right
			reg = regMountainRight;
			batch.draw(reg.getTexture(), origin.x+relX+position.x * parallaxSpeedX, origin.y+relY+position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
		//Reset color to white
		batch.setColor(1,1,1,1);
	}
	
	@Override
	public void render(SpriteBatch batch) 
	{
		//Distant mountains(dark gray)
		drawMountains(batch, 0.5f, 0.5f, 0.5f, 0.8f);
		//Distant mountains(gray)
		drawMountains(batch, 0.25f, 0.25f, 0.7f, 0.5f);
		//Distant mountains(light gray)
		drawMountains(batch, 0.0f, 0.0f, 0.9f, 0.3f);
	}
}
