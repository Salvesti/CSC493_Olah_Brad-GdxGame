package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.olah.gdx.game.GameObjects.Cat;
import com.olah.gdx.game.GameObjects.Cat.JUMP_STATE;
import com.olah.gdx.game.GameObjects.CollisionZone;
import com.olah.gdx.game.GameObjects.Sardines;
import com.olah.gdx.game.GameObjects.ScoreObject;
import com.olah.gdx.game.util.Constants;
import com.badlogic.gdx.InputAdapter;

/**
 * A class that handles the locations and movements of game objects, and the camera.
 * @author Brad Olah
 */
public class WorldController extends InputAdapter 
{
	private static final String TAG = WorldController.class.getName();

	public CameraHelper cameraHelper;
	public Level level;
	public float time;
	public int score;
	private Float timeLeftGameOverDelay;
	public World b2World;

	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	public WorldController()
	{
		init();
	}

	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		time = Constants.START_TIME;
		timeLeftGameOverDelay = 0f;
		initLevel();
		initPhysics();
	}

	private void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.cat);
		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
	}

	private void initPhysics()
	{
		if(b2World != null)
		{
			b2World.dispose();
		}
		b2World = new World(new Vector2(0,-9.81f),true);
		b2World.setContactListener(new myContactListener());
		//CollisionZones
		Vector2 origin = new Vector2();
		for(CollisionZone zone : level.collisionZones)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(zone.position);
			Body body = b2World.createBody(bodyDef);
			zone.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = zone.bounds.width/2.0f;
			origin.y = zone.bounds.height/2.0f;
			polygonShape.setAsBox(zone.bounds.height/2.0f,zone.bounds.width/2.0f,origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
		//Cat
		Cat cat = level.cat;
		BodyDef catBody = new BodyDef();
		catBody.type = BodyType.DynamicBody;
		catBody.position.set(cat.position);
		catBody.fixedRotation = true;
		catBody.bullet = true;
		Body body = b2World.createBody(catBody);
		cat.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = cat.bounds.width/2.0f;
		origin.y = (cat.bounds.height/2.0f)-.2f;
		polygonShape.setAsBox(cat.bounds.width/2.0f,0.8f,origin,0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 10;
		fixtureDef.friction = 0.5f;
		body.createFixture(fixtureDef);
		
		polygonShape.setAsBox(0.6f, 0.1f, new Vector2(1,0), 0);
		fixtureDef.isSensor = true;
		fixtureDef.shape = polygonShape;
		body.createFixture(fixtureDef);
		polygonShape.dispose();
		
	}

	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		if(isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay < 0)
			{
				init();
			}
		}else
		{
			handleInputGame(deltaTime);
			time -= deltaTime;
		}

		level.update(deltaTime);
		b2World.step(deltaTime, 10,20);
		testCollisions();
		cameraHelper.update(deltaTime);
	}

	//Returns if the player is out of lives
	public boolean isGameOver()
	{
		return time <= .99f;
	}

	/**
	 * Controls movement of character
	 * @param deltaTime
	 */
	private void handleInputGame(float deltaTime)
	{
		if(cameraHelper.hasTarget(level.cat))
		{
			//Player movement
			if(Gdx.input.isKeyPressed(Keys.LEFT))
			{
				//level.cat.velocity.x = -level.cat.terminalVelocity.x;
				level.cat.body.applyLinearImpulse(-10,0,level.cat.position.x,level.cat.position.y,true);
			}else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				//level.cat.velocity.x = level.cat.terminalVelocity.x;
				level.cat.body.applyLinearImpulse(10,0,level.cat.position.x,level.cat.position.y,true);
			}
		}

		//Cat Jump
		if(Gdx.input.isKeyPressed(Keys.SPACE))
		{
			level.cat.body.applyLinearImpulse(0,40,level.cat.position.x,level.cat.position.y,true);
			level.cat.setJumping(true);
		}else
		{
			level.cat.setJumping(false);
		}
	}

	/**
	 * Controls for moving the test objects.
	 * @param deltaTime
	 */
	private void handleDebugInput (float deltaTime)
	{
		if(!cameraHelper.hasTarget(level.cat))
		{
			/*Camera Controls (Movement)
			 * Left Shift = Increase Camera Movement Speed
			 * Left = Move camera left
			 * Right = Move camera right
			 * Up = Move camera up
			 * Down = Move camera down
			 * Backspace = Reset camera position
			 */
			float camMoveSpeed = 5* deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0,camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,-camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);

			/*Camera Controls (Zoom)
			 *  Left Shift = Increase zoom speed
			 *  Comma = Zoom in
			 *  Period = Zoom out
			 *  Slash = Reset zoom
			 */
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
		}
	}

	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	@Override
	public boolean keyUp(int keycode)
	{
		//Reset game world
		if(keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		else if(keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget()?null:level.cat);
			Gdx.app.debug(TAG, "Camera follow enabled: "+ cameraHelper.hasTarget());
		}
		return false;
	}

	/**
	 * Handles collisions between the Bunny Head and Gold Coins
	 * @param goldCoin
	 */
	private void onCollisionPlayerWithScoreObject(ScoreObject scoreObject)
	{
		scoreObject.collected = true;
		score += scoreObject.getScore();
		Gdx.app.log(TAG, "Score Object collected");
	}
	/**
	 * Handles collisions between the Bunny Head and Feathers
	 * @param feather
	 */
	private void onCollisionPlayerWithSardine(Sardines sardine)
	{
		sardine.collected = true;
		score += sardine.getScore();
		level.cat.setSardinePowerup(true);
		time += sardine.setSardineTime();
		Gdx.app.log(TAG, "Sardine collected");
	}
	/*private void onCollisionPlayerWithCollisionZone(CollisionZone zone)
	{
		Cat cat = level.cat;
		float heightDifference = Math.abs(cat.position.y - (zone.position.y + 1));
		if(heightDifference > 0.1f)
		{
			boolean hitRightEdge = cat.position.x > (zone.position.x +1 /2.0f);
			if(hitRightEdge)
			{
				cat.position.x = zone.position.x + 1;
			}
			else
			{
				cat.position.x = zone.position.x - 1;
			}
		}
		switch (cat.jumpState){
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			cat.position.y = zone.position.y + cat.bounds.height/2;
			cat.jumpState =  JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			cat.position.y = zone.position.y + cat.bounds.height/2;
			break;
		}

	}*/


	/**
	 * Checks if the bunny head collides with any gameObject, and calls the appropriate method.
	 */
	private void testCollisions()
	{
		r1.set(level.cat.position.x, level.cat.position.y, level.cat.bounds.width, level.cat.bounds.height);

		//Test collision: Cat <--> CollisionZones
		for(CollisionZone zone : level.collisionZones)
		{
			r2.set(zone.position.x, zone.position.y, 1, 1);
			if(!r1.overlaps(r2)) continue;
			//onCollisionPlayerWithCollisionZone(zone);
			//IMPORTANT: must do all collisions for valid edge testing on rocks.
		}

		//Test collision: Bunny Head <--> Sardine
		for(ScoreObject scoreObject : level.scoreObjects)
		{
			if(scoreObject.collected) continue;
			r2.set(scoreObject.position.x, scoreObject.position.y, scoreObject.bounds.width, scoreObject.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionPlayerWithScoreObject(scoreObject);
			break;
		}

		//Test collision: Bunny Head <--> Feather
		for(Sardines sardine : level.sardines)
		{
			if(sardine.collected) continue;
			r2.set(sardine.position.x, sardine.position.y, sardine.bounds.width, sardine.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionPlayerWithSardine(sardine);
			break;
		}
	}


}
