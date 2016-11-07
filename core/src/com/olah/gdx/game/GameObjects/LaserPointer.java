package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Filter;
import com.olah.gdx.game.Assets;
import com.olah.gdx.game.util.Constants;

/**
 * A class that handles the logic behind the laser pointer object.
 * @author Brad Olah
 *
 */
public class LaserPointer extends AbstractGameObject
{
	private TextureRegion regLaserPointer;
	private Boolean disabled;
	private float timeLeftDisabled;

	public LaserPointer()
	{
		init();
	}

	/**
	 * Initializes the score object.
	 */
	public void init()
	{
		dimension.set(1f,1f);

		regLaserPointer = Assets.instance.laserPointer.laser;

		//Set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);
		type = "laserPointer";
		disabled = false;
		timeLeftDisabled = 0;
	}

	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regLaserPointer;
		if(timeLeftDisabled > 0)
		{
			if(((int)timeLeftDisabled*1)%2 != 0)
			{
				batch.setColor(1,1,1,.5f);
			}
		}
		batch.draw(reg.getTexture(),position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),false, false);
		batch.setColor(1,1,1,1);
	}

	public void setDisabled(Boolean bool)
	{
		disabled = bool;
		if(disabled)
		{
			timeLeftDisabled = Constants.ITEM_LASER_POINTER_TIME_LEFT_DISABLED;
		}
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		//Counts down disabled time if there is any.
				if(timeLeftDisabled > 0)
				{
					timeLeftDisabled -= deltaTime;
					if(timeLeftDisabled < 0)
					{
						//disable laser pointer
						timeLeftDisabled = 0;
						setDisabled(false);
						Filter filter = new Filter();
						filter.categoryBits = Constants.CATEGORY_SCOREOBJECT_LIVE;
						filter.maskBits = Constants.MASK_SCOREOBJECT_LIVE;
						body.getFixtureList().peek().setFilterData(filter);
					}
				}
	}
}
