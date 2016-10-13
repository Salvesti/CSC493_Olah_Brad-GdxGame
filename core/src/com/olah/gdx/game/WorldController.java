package com.olah.gdx.game;

import com.badlogic.gdx.Game;
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
import com.olah.gdx.game.GameObjects.CollisionZone;
import com.olah.gdx.game.GameObjects.Sardines;
import com.olah.gdx.game.GameObjects.ScoreObject;
import com.olah.gdx.game.Screens.MenuScreen;
import com.olah.gdx.game.util.Constants;
import com.badlogic.gdx.InputAdapter;

/**
 * A class that handles the locations and movements of game objects, and the camera.
 * @author Brad Olah
 */
public class WorldController extends InputAdapter
{
	private static final String TAG = WorldController.class.getName();

	private Game game;

	public CameraHelper cameraHelper;
	public Level level;
	public float time;
	public int score;
	private Float timeLeftGameOverDelay;
	public World b2World;
	public LevelContactListener levelContactChecker;

	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	public WorldController(Game game)
	{
		this.game = game;
		init();
	}

	/**
	 * Initializes the game world.
	 */
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		time = Constants.START_TIME;
		timeLeftGameOverDelay = 0f;
		initLevel();
		levelContactChecker = new LevelContactListener(level);
		initPhysics();
	}

	/**
	 * Initializes the level.
	 */
	private void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.cat);
		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
	}

	/**
	 * Initializes the physics objects.
	 */
	private void initPhysics()
	{
		if(b2World != null)
		{
			b2World.dispose();
		}
		b2World = new World(new Vector2(0,-9.81f),true);

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
			body.createFixture(fixtureDef).setUserData("collisionZone");
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
		origin.y = (cat.bounds.height/2.0f)-.4f;
		polygonShape.setAsBox(cat.bounds.width/2.2f,0.6f,origin,0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 30;
		fixtureDef.friction = 0.5f;
		body.createFixture(fixtureDef).setUserData("cat");
		FixtureDef footFixture = new FixtureDef();
		polygonShape.setAsBox(0.6f, 0.1f, new Vector2(1,0), 0);
		footFixture.isSensor = true;
		footFixture.shape = polygonShape;
		body.createFixture(footFixture).setUserData("foot");
		polygonShape.dispose();

		b2World.setContactListener(levelContactChecker);
	}

	/**
	 * Updates the world, camera, and physics engine based on deltaTime.
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		if(isGameOver())
		{
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay < 0)
			{
				backToMenu();
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

	/**
	 * Returns back to the menu screen.
	 */
	private void backToMenu()
	{
		game.setScreen(new MenuScreen(game));
	}

	/**
	 * Returns if the player is out of time.
	 * @return boolean
	 */
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
			if(Gdx.input.isKeyPressed(Keys.LEFT) && (level.cat.velocity.x != level.cat.terminalVelocity.x))
			{
				level.cat.body.applyLinearImpulse(-10,0,level.cat.position.x,level.cat.position.y,true);
				level.cat.scale = new Vector2(1,1);
			}else if(Gdx.input.isKeyPressed(Keys.RIGHT) && (level.cat.velocity.x != level.cat.terminalVelocity.x))
			{
				level.cat.body.applyLinearImpulse(10,0,level.cat.position.x,level.cat.position.y,true);
				level.cat.scale = new Vector2(-1,1);
			}
		}

		//Cat Jump
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			//If the cat is not in contact with a surface it can jump
			if(level.cat.numFootContacts > 0)
			{
				level.cat.body.applyLinearImpulse(0,600,level.cat.position.x,level.cat.position.y,true);
			}
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

	/**
	 * Moves the camera.
	 * @param x
	 * @param y
	 */
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * Handles various key presses.
	 */
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
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
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

	/**
	 * Checks if the bunny head collides with any gameObject, and calls the appropriate method.
	 */
	private void testCollisions()
	{
		r1.set(level.cat.position.x, level.cat.position.y, level.cat.bounds.width, level.cat.bounds.height);

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
