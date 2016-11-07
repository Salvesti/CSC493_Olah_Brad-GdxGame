package com.olah.gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.olah.gdx.game.GameObjects.AbstractGameObject;
import com.olah.gdx.game.GameObjects.Cat;
import com.olah.gdx.game.GameObjects.CollisionZone;
import com.olah.gdx.game.GameObjects.LaserPointer;
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
	public float scoreVisual;

	private Array objectsToRemove;

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
		levelContactChecker = new LevelContactListener(this);
		initPhysics();
	}

	/**
	 * Initializes the level.
	 */
	private void initLevel()
	{
		score = 0;
		scoreVisual = 0;
		level = new Level(Constants.LEVEL_01F,Constants.LEVEL_01B);
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

		Vector2 origin = new Vector2();
		//CollisionZones
		for(CollisionZone zone : level.collisionZones)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(zone.position);
			Body body = b2World.createBody(bodyDef);
			body.setUserData(zone);
			zone.body = body;
			EdgeShape edgeShape = new EdgeShape();
			edgeShape.set(0, zone.bounds.height, zone.bounds.width, zone.bounds.height);
			/**
			PolygonShape polygonShape = new PolygonShape();
			origin.x = zone.bounds.width/2.0f;
			origin.y = zone.bounds.height/2.0f;
			polygonShape.setAsBox(zone.bounds.height/2.0f,zone.bounds.width/2.0f,origin,0);
			**/
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = edgeShape;
			fixtureDef.friction = .1f;
			//Sets what the fixture can collide with.
			fixtureDef.filter.categoryBits = Constants.CATEGORY_SCENERY;
			fixtureDef.filter.maskBits = Constants.MASK_SCENERY;
			body.createFixture(fixtureDef).setUserData("collisionZone");
			edgeShape.set(0, 0, zone.bounds.width, 0);
			body.createFixture(fixtureDef).setUserData("collisionZone");
			edgeShape.dispose();
		}
		//Score Objects
		for(ScoreObject obj : level.scoreObjects)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(obj.position);
			Body body = b2World.createBody(bodyDef);
			body.setUserData(obj);
			obj.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = obj.bounds.width/2.0f;
			origin.y = obj.bounds.height/2.0f;
			polygonShape.setAsBox(obj.bounds.height/2.0f,obj.bounds.width/2.0f,origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 2;
			//Sets what the fixture can collide with.
			fixtureDef.filter.categoryBits = Constants.CATEGORY_SCOREOBJECT_LIVE;
			fixtureDef.filter.maskBits = Constants.MASK_SCOREOBJECT_LIVE;
			body.createFixture(fixtureDef).setUserData("collidableObject");
			polygonShape.dispose();
		}
		//Score Objects
		for(Sardines sardine : level.sardines)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(sardine.position);
			Body body = b2World.createBody(bodyDef);
			body.setUserData(sardine);
			sardine.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = sardine.bounds.width/2.0f;
			origin.y = sardine.bounds.height/2.0f;
			polygonShape.setAsBox(sardine.bounds.height/2.0f,sardine.bounds.width/2.0f,origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 2;
			//Sets what the fixture can collide with.
			fixtureDef.filter.categoryBits = Constants.CATEGORY_SCOREOBJECT_LIVE;
			fixtureDef.filter.maskBits = Constants.MASK_SCOREOBJECT_LIVE;
			body.createFixture(fixtureDef).setUserData("collidableObject");
			polygonShape.dispose();
		}
		//Laser Pointers
		for(LaserPointer laser : level.laserPointers)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(laser.position);
			Body body = b2World.createBody(bodyDef);
			body.setUserData(laser);
			laser.body = body;
			CircleShape circleShape = new CircleShape();
			circleShape.setPosition(origin);
			circleShape.setRadius(.2f);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circleShape;
			fixtureDef.density = 2;
			fixtureDef.restitution= 0.2f;
			//Sets what the fixture can collide with.
			fixtureDef.filter.categoryBits = Constants.CATEGORY_SCOREOBJECT_LIVE;
			fixtureDef.filter.maskBits = Constants.MASK_SCOREOBJECT_LIVE;
			body.createFixture(fixtureDef).setUserData("collidableObject");
			circleShape.dispose();
		}
		//Player Cat
		Cat cat = level.cat;
		BodyDef catBody = new BodyDef();
		catBody.type = BodyType.DynamicBody;
		catBody.position.set(cat.position);
		catBody.fixedRotation = true;
		catBody.bullet = true;
		Body body = b2World.createBody(catBody);
		body.setUserData(cat);
		cat.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = cat.bounds.width/2.0f;
		origin.y = (cat.bounds.height/2.0f)-.2f;
		polygonShape.setAsBox(cat.bounds.width/2f,.8f,origin,0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 20;
		fixtureDef.friction = 0.5f;
		//Sets what the fixture can collide with.
		fixtureDef.filter.categoryBits = Constants.CATEGORY_PLAYER;
		fixtureDef.filter.maskBits = Constants.MASK_PLAYER;
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
		//TODO add object cleanup.
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
		cameraHelper.update(deltaTime);
		if(scoreVisual < score)
		{
			scoreVisual = Math.min(score,  scoreVisual + 250 * deltaTime);
		}
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
		if(level.cat.hitLaserPointer == false)
		{
			if(cameraHelper.hasTarget(level.cat))
			{
				//Player movement
				if((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) && (level.cat.velocity.x != level.cat.terminalVelocity.x))
				{
					level.cat.moveCat("Left");
				}else if((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && (level.cat.velocity.x != level.cat.terminalVelocity.x))
				{
					level.cat.moveCat("Right");
				}
			}

			//Cat Jump
			if(Gdx.input.isKeyJustPressed(Keys.SPACE))
			{
				level.cat.jump();
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
}
