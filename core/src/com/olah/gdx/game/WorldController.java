package com.olah.gdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.olah.gdx.game.GameObjects.BunnyHead;
import com.olah.gdx.game.GameObjects.BunnyHead.JUMP_STATE;
import com.olah.gdx.game.GameObjects.Carrot;
import com.olah.gdx.game.GameObjects.Feather;
import com.olah.gdx.game.GameObjects.GoldCoin;
import com.olah.gdx.game.GameObjects.Rock;
import com.olah.gdx.game.util.Constants;

/**
 * A class that handles the locations and movements of game objects, and the camera.
 * @author Brad Olah
 */
public class WorldController extends InputAdapter implements Disposable
{
	private static final String TAG = WorldController.class.getName();
	
	private Game game;
	
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int score;
	private float timeLeftGameOverDelay;
	public float livesVisual;
	public float scoreVisual;

	//Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	private boolean goalReached;
	public World b2world;

	public WorldController(Game game)
	{
		this.game = game;
		init();
	}
	
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay=0;
		initLevel();
	}

	private void initLevel()
	{
		score = 0;
		scoreVisual = score;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
		initPhysics();
	}
	
	private void initPhysics()
	{
		if(b2world != null) b2world.dispose();
		b2world = new World(new Vector2(0,-9.81f), true);
		//Rocks
		Vector2 origin = new Vector2();
		for(Rock rock : level.rocks)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width/2.0f;
			origin.y = rock.bounds.height/2.0f;
			polygonShape.setAsBox(rock.bounds.width/2.0f, rock.bounds.height/2.0f,origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
	}

	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		if(isGameOver() || goalReached)
		{
			timeLeftGameOverDelay -= deltaTime;
			if(timeLeftGameOverDelay <0) backToMenu();
		}else
		{
		handleInputGame(deltaTime);
		}
		b2world.step(deltaTime, 8, 3);
		level.update(deltaTime);
		testCollisions();
		
		cameraHelper.update(deltaTime);
		if(!isGameOver()&&isPlayerInWater())
		{
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives --;
			if (isGameOver())
			{
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			}else
			{
				initLevel();
			}
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if(livesVisual > lives)
		{
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		}
		if(scoreVisual < score)
		{
			scoreVisual = Math.min(score, scoreVisual +250 * deltaTime); 
		}
	}
	
	/**
	 * Spawns carrots
	 * @param pos
	 * @param numCarrots
	 * @param radius
	 */
	public void spawnCarrots(Vector2 pos, int numCarrots,float radius)
	{
		float carrotShapeScale = 0.5f;
		//Create carrots with box2d body and fixture.
		for(int i = 0; i < numCarrots;i++)
		{
			Carrot carrot = new Carrot();
			//calculate random spawn position,rotation, and scale
			float x = MathUtils.random(-radius,radius);
			float y = MathUtils.random(5.0f,15.0f);
			float rotation = MathUtils.random(0.0f,360.0f) * MathUtils.degreesToRadians;
			float carrotScale = MathUtils.random(0.5f,1.5f);
			carrot.scale.set(carrotScale, carrotScale);
			//Create box3d body for carrot with start position
			//and angle of rotation
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pos);
			bodyDef.position.add(x,y);
			bodyDef.angle = rotation;
			Body body = b2world.createBody(bodyDef);
			body.setType(BodyType.DynamicBody);
			carrot.body = body;
			//Create rectangular shape for carrot to allow
			//interactions(collisions) with other objects.
			PolygonShape polygonShape = new PolygonShape();
			float halfWidth = carrot.bounds.width/2.0f*carrotShapeScale;
			float halfHeight = carrot.bounds.height/2.0f*carrotShapeScale;
			polygonShape.setAsBox(halfWidth*carrotShapeScale, halfHeight*carrotShapeScale);
			//Set physics atttributes
			FixtureDef fixtureDef  = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 50;
			fixtureDef.restitution = 0.5f;
			fixtureDef.friction = 0.5f;
			body.createFixture(fixtureDef);
			//Finally, add new carrot to list for updating/rendering
			level.carrots.add(carrot);
			polygonShape.dispose();
		}
	}
	
	/**
	 * Switches back to menu screen
	 */
	private void backToMenu()
	{
		game.setScreen(new MenuScreen(game));
	}
	
	//Returns if the player is out of lives
	public boolean isGameOver()
	{
		return lives <0;
	}
	
	/**
	 * Returns if the player is in the water
	 * @return
	 */
	public boolean isPlayerInWater()
	{
		return level.bunnyHead.position.y<-5;
	}
	
	/**
	 * Controls movement of character
	 * @param deltaTime
	 */
	private void handleInputGame(float deltaTime)
	{
		if(cameraHelper.hasTarget(level.bunnyHead))
		{
			//Player movement
			if(Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			}else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			}
		}
		
		//Bunny Jump
		if(Gdx.input.isKeyPressed(Keys.SPACE))
		{
			level.bunnyHead.setJumping(true);
		}else
		{
			level.bunnyHead.setJumping(false);
		}
	}
	
	/**
	 * Controls for moving the test objects.
	 * @param deltaTime
	 */
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop)
		{
			return;
		}
		if(!cameraHelper.hasTarget(level.bunnyHead))
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
		//Toggle camera follow
		else if(keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget()?null:level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: "+ cameraHelper.hasTarget());
		}
		//Back to menu
		else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
			
		return false;
	}
	
	/**
	 * Handles collisions between the Bunny Head and Rocks
	 * @param rock
	 */
	private void onCollisionBunnyHeadWithRock(Rock rock)
	{
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if(heightDifference > 0.25f)
		{
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if(hitRightEdge)
			{
				bunnyHead.position.x = rock.position.x +rock.bounds.width;
			}else
			{
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}
		switch (bunnyHead.jumpState){
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState =  JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}
	/**
	 * Handles collisions between the Bunny Head and Gold Coins
	 * @param goldCoin
	 */
	private void onCollisionBunnyHeadWithGoldCoin(GoldCoin goldCoin)
	{
		goldCoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += goldCoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}
	/**
	 * Handles collisions between the Bunny Head and Feathers
	 * @param feather
	 */
	private void onCollisionBunnyHeadWithFeather(Feather feather)
	{
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}
	/**
	 * Handles collisions between the Bunny Head and the Goal
	 */
	private void onCollisionBunnyWithGoal()
	{
		goalReached = true;
		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead = new Vector2(level.bunnyHead.position);
		spawnCarrots(centerPosBunnyHead,Constants.CARROTS_SPAWN_MAX,Constants.CARROTS_SPAWN_RADIUS);
	}
	
	/**
	 * Checks if the bunny head collides with any gameObject, and calls the appropriate method.
	 */
	private void testCollisions()
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		
		//Test collision: Bunny Head <--> Rocks
		for(Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			//IMPORTANT: must do all collisions for valid edge testing on rocks.
		}
		
		//Test collision: Bunny Head <--> Gold Coins
		for(GoldCoin goldCoin : level.goldCoins)
		{
			if(goldCoin.collected) continue;
			r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithGoldCoin(goldCoin);
			break;
		}
		
		//Test collision: Bunny Head <--> Feather
		for(Feather feather : level.feathers)
		{
			if(feather.collected) continue;
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if(!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithFeather(feather);
			break;
		}
		
		//Test collision: Bunny Head <--> Goal
		if(!goalReached)
		{
			r2.set(level.goal.bounds);
			r2.x = level.goal.position.x;
			r2.y = level.goal.position.y;
			if(r1.overlaps(r2))
			{
				onCollisionBunnyWithGoal();
			}
		}
	}

	@Override
	public void dispose() 
	{
		if(b2world != null)
		{
			b2world.dispose();
		}
	}
}
