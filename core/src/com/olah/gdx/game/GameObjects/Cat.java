package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.olah.gdx.game.Assets;
import com.olah.gdx.game.util.Constants;

public class Cat extends AbstractGameObject
{
	public static final String TAG = Cat.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;

	public enum VIEW_DIRECTION{LEFT, RIGHT}

	public enum JUMP_STATE
	{
		GROUNDED,
		FALLING,
		JUMP_RISING,
		JUMP_FALLING
	}

	private Texture regHead;

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasSardinePowerup;
	public float timeLeftSardinePowerup;

	public Cat()
	{
		init();
	}

	public void init()
	{
		dimension.set(2,2);
		regHead = Assets.instance.cat.cat;
		//Center image on game object
		origin.set(dimension.x/2,dimension.y/2);
		//Bounding box for collision detection
		bounds.set(0,0,dimension.x, dimension.y);
		//Set physics values
		terminalVelocity.set(8.0f,16.0f);
		friction.set(12.0f,0.0f);
		acceleration.set(0.0f, -25.0f);
		//View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		//Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		//Powerups
		hasSardinePowerup = false;
		timeLeftSardinePowerup = 0;
	}

	/**
	 * Makes the bunny jump. States handle if the bunny is already jumping.
	 * @param jumpKeyPressed
	 */
	public void setJumping(boolean jumpKeyPressed)
	{
		switch (jumpState)
		{
		case GROUNDED://Character is standing on a platform
			if(jumpKeyPressed)
			{
				//start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING: //Rising in the air
			if(!jumpKeyPressed)
			{
				jumpState = JUMP_STATE.JUMP_FALLING;
			}
			break;
		case FALLING: //Falling down
		case JUMP_FALLING: //Falling down after jump
			break;
		}
	}

	/**
	 * Toggles the feather Powerup
	 * @param pickedUp
	 */
	public void setSardinePowerup(boolean pickedUp)
	{
		hasSardinePowerup = pickedUp;
		if(pickedUp)
		{
			timeLeftSardinePowerup = Constants.ITEM_SARDINES_POWERUP_DURATION;
		}
	}

	/**
	 * Checks to see if the feather powerup is active
	 * @return
	 */
	public boolean hasFeatherPowerup()
	{
		return hasSardinePowerup && timeLeftSardinePowerup > 0;
	}

	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		//Changes the facing of the bunny sprite depending on direction of movement
		if(velocity.x !=0)
		{
			viewDirection = velocity.x <0 ? VIEW_DIRECTION.RIGHT : VIEW_DIRECTION.LEFT;
		}
		if(timeLeftSardinePowerup > 0)
		{
			timeLeftSardinePowerup -= deltaTime;
			if(timeLeftSardinePowerup < 0)
			{
				//disable Powerup
				timeLeftSardinePowerup = 0;
				setSardinePowerup(false);
			}
		}
	}

	@Override
	public void updateMotionY(float deltaTime)
	{
		//Handles the switching of states needed to enable jumping and falling.
		switch(jumpState)
		{
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;
		case JUMP_RISING:
			//Keep track of jump time
			timeJumping += deltaTime;
			//Jump time left
			if(timeJumping <= JUMP_TIME_MAX)
			{
				//Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			//Add delta times to track jump time
			timeJumping += deltaTime;
			//Jump to minimal height if jump key was pressed too short
			if(timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
			{
				//Still jumping
				velocity.y = terminalVelocity.y;
			}
		}
		if(jumpState != JUMP_STATE.GROUNDED)
			super.updateMotionY(deltaTime);
	}

	//TODO fix iceskating
	@Override
	public void updateMotionX(float deltaTime)
	{
		float speedMod = 1;
		if(hasSardinePowerup == true)
		{
			speedMod = 2;
		}
		if(velocity.x != 0)
		{
			//Apply friction
			if(velocity.x > 0)
			{
				velocity.x = Math.max(velocity.x*speedMod - friction.x * deltaTime,0);
			}
			else
			{
				velocity.x = Math.min(velocity.x*speedMod + friction.x * deltaTime, 0);
			}
		}
		//Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		//Make sure the object's velocity does not exceed the
		//positive or negative terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x*speedMod, terminalVelocity.x*speedMod);
	}

	@Override
	public void render (SpriteBatch batch)
	{
		Texture reg = null;

		//Set special color when game object has a feather powerup
		if(hasSardinePowerup)
		{
			batch.setColor(1.0f,0.8f,0.0f,1.0f);
		}
		//Draw image
		reg = regHead;
		batch.draw(regHead, position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 2, 2,reg.getWidth(), reg.getHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);
		//Resets the color to white
		batch.setColor(1,1,1,1);
	}
}
