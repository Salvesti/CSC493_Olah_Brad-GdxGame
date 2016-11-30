package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.olah.gdx.game.Assets;
import com.olah.gdx.game.AudioManager;
import com.olah.gdx.game.util.Constants;
import com.olah.gdx.game.util.GamePreferences;
import com.olah.gdx.game.util.CharacterSkin;

/**
 * A class that handles the logic behind the cat object.
 * @author Brad Olah
 */
public class Cat extends AbstractGameObject
{
	public static final String TAG = Cat.class.getName();

	public int numFootContacts;

	public enum VIEW_DIRECTION{LEFT, RIGHT}

	private Animation animIdle;
	private Animation animWalk;
	private Animation animRun;
	private Animation animJump;

	public VIEW_DIRECTION viewDirection;
	public boolean hasSardinePowerup;
	public boolean hitLaserPointer;
	public float timeLeftSardinePowerup;
	public float timeLeftLaserPointer;
	public ParticleEffect dustParticles = new ParticleEffect();

	public Cat()
	{
		init();
	}

	/**
	 * Initializes the cat.
	 */
	public void init()
	{
		dimension.set(2,2);
		animIdle = Assets.instance.cat.animIdle;
		animWalk = Assets.instance.cat.animWalk;
		animRun = Assets.instance.cat.animRun;
		animJump = Assets.instance.cat.animJump;

		setAnimation(animIdle);
		//Center image on game object
		origin.set(dimension.x/2,dimension.y/2);
		//Bounding box for collision detection
		bounds.set(0,0,dimension.x, dimension.y);
		//Set physics values
		terminalVelocity.set(5.0f,16.0f);
		friction.set(12.0f,0.0f);
		acceleration.set(0.0f, -25.0f);
		//View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		//Powerups
		hasSardinePowerup = false;
		timeLeftSardinePowerup = 0;
		hitLaserPointer = false;
		timeLeftLaserPointer = 0;
		numFootContacts = 0;
		//Particles
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"),Gdx.files.internal("particles"));
		type = "player";
	}

	/**
	 * Toggles the sardine Powerup
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
	 * Toggles the laser pointer
	 * @param pickedUp
	 */
	public void setLaserPointer(boolean pickedUp)
	{
		hitLaserPointer = pickedUp;
		if(pickedUp)
		{
			timeLeftLaserPointer = Constants.ITEM_LASER_POINTER_DURATION;
		}
	}

	/**
	 * Checks to see if the sardine powerup is active
	 * @return
	 */
	public boolean hasSardinePowerup()
	{
		return hasSardinePowerup && timeLeftSardinePowerup > 0;
	}

	/**
	 * Checks to see if the laser pointer was hit
	 * @return
	 */
	public boolean hitLaserPointer()
	{
		return hitLaserPointer && timeLeftLaserPointer > 0;
	}

	/**
	 * Updates the cats location based on deltaTime.
	 */
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);

		if(body.getLinearVelocity().y < 0)
		{
			body.setLinearVelocity(body.getLinearVelocity().x,body.getLinearVelocity().y -.2f);
		}

		//Counts down laser pointer time if there is any.
		if(timeLeftLaserPointer > 0)
		{
			timeLeftLaserPointer -= deltaTime;
			if(timeLeftLaserPointer < 0)
			{
				//disable laser pointer
				timeLeftLaserPointer = 0;
				setLaserPointer(false);
			}
		}

		//Counts down sardine time if there is any.
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

		//TODO fix flipping of the emitter when the Cat changes direction.
		if(numFootContacts != 0)
		{
			if(body.getLinearVelocity().x > 2f || body.getLinearVelocity().x < -2f)
			{
				dustParticles.setPosition(position.x + dimension.x / 2, position.y);
				dustParticles.scaleEffect(-1f);
				dustParticles.flipY();
				dustParticles.start();
			}else
			{
				dustParticles.allowCompletion();
			}
		}
		else
		{
			dustParticles.allowCompletion();
		}
		dustParticles.update(deltaTime);

		//Sets animations
		if(numFootContacts == 0)
		{
			setAnimation(animJump);
		}else
		{
			if(body.getLinearVelocity().x == 0)
			{
				setAnimation(animIdle);
			}else
			{
				if(animation != animWalk)
				{
					setAnimation(animWalk);
				}
			}
		}
	}

	/**
	 * Moves the cat based on the given direction.
	 * @param direction
	 */
	public void moveCat(String direction)
	{
		float speedMod = 1;
		float speed = 7;
		if(hasSardinePowerup == true)
		{
			speedMod = 2;
		}
		if(direction == "Left")
		{
			//Limits the speed
			if(body.getLinearVelocity().x > -speed)
			{
				body.applyLinearImpulse(-50*speedMod,0,position.x,position.y,true);
				//Changes the facing of the cat sprite depending on direction of movement
				scale = new Vector2(1,1);
			}
		}
		if(direction == "Right")
		{
			//Limits the speed
			if(body.getLinearVelocity().x < speed)
			{
				body.applyLinearImpulse(50*speedMod,0,position.x,position.y,true);
				//Changes the facing of the cat sprite depending on direction of movement
				scale = new Vector2(-1,1);
			}
		}
	}

	public void jump()
	{
		//If the cat is not in contact with a surface it can jump
		if(numFootContacts > 0)
		{
			AudioManager.instance.play(Assets.instance.sounds.jump);
			body.applyLinearImpulse(0,3200,position.x,position.y,true);
		}
	}

	/**
	 * Renders the cat graphic.
	 */
	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;

		//Draw Particles
		dustParticles.draw(batch);

		//Apply Skin Color
		 batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());

		//Set special color when game object has a feather powerup
		if(hasSardinePowerup)
		{
			batch.setColor(1.0f,0.8f,0.0f,1.0f);
		}
		//Draw image
		reg = animation.getKeyFrame(stateTime,true);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);

		//Resets the color to white
		batch.setColor(1,1,1,1);
	}

}
