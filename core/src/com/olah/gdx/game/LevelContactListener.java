package com.olah.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.olah.gdx.game.GameObjects.AbstractGameObject;
import com.olah.gdx.game.GameObjects.Sardines;
import com.olah.gdx.game.GameObjects.ScoreObject;
import com.olah.gdx.game.util.Constants;

/**
 * A class that handles the collisions detecting for the given level.
 * @author Brad Olah
 */
public class LevelContactListener implements ContactListener
{
	public static final String TAG = LevelContactListener.class.getName();
	private WorldController worldController;
	private Level level;

	public LevelContactListener(WorldController worldController)
	{
		this.worldController = worldController;
		this.level = worldController.level;
	}

	/**
	 *
	 */
	@Override
	public void beginContact(Contact contact)
	{
		String fixtureAUserData = (String)contact.getFixtureA().getUserData();
		String fixtureBUserData = (String)contact.getFixtureB().getUserData();
		//If one of the fixtures colliding is the cats foot, increment numFootContacts.
		if(fixtureAUserData.equals("foot")||fixtureBUserData.equals("foot"))
		{
			level.cat.numFootContacts++;
		}
		//Checks if collision occurred between the player and a collidableObject.
		testCollidableObjectCollision(contact);
	}

	/**
	 * If one of the fixtures that lost contact is the cats foot, decrement numFootContacts.
	 */
	@Override
	public void endContact(Contact contact)
	{
		String fixtureAUserData = (String)contact.getFixtureA().getUserData();
		String fixtureBUserData = (String)contact.getFixtureB().getUserData();
		//If one of the fixtures that lost contact is the cats foot, decrement numFootContacts.
		if(fixtureAUserData.equals("foot")||fixtureBUserData.equals("foot"))
		{
			level.cat.numFootContacts--;
		}
	}

	/**
	 * Checks for and handles collision with collidableObjects.
	 * @param contact
	 */
	private void testCollidableObjectCollision(Contact contact)
	{
		AbstractGameObject objectA = (AbstractGameObject) contact.getFixtureA().getBody().getUserData();
		AbstractGameObject objectB= (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
		String objectAType = objectA.getType();
		String objectBType = objectB.getType();

		//If objectA is the collidableObject finds its type and performs appropriate logic.
		if(objectAType.equals("collidableObject") && objectBType.equals("player"))
		{
			//Changes the filter mask that the object uses.
			Filter filter = new Filter();
			filter.maskBits = Constants.MASK_SCOREOBJECT_DEAD;
			contact.getFixtureA().setFilterData(filter);
			//Updates the score based on what the collidableObject is
			updateScore(objectA);
		}
		//If objectB is the collidableObject finds its type and performs appropriate logic.
		if(objectBType.equals("collidableObject") && objectAType.equals("player"))
		{
			//Changes the filter mask that the object uses.
			Filter filter = new Filter();
			filter.maskBits = Constants.MASK_SCOREOBJECT_DEAD;
			contact.getFixtureB().setFilterData(filter);
			//Updates the score based on what the collidableObject is
			updateScore(objectB);
		}
	}

	/**
	 * Updates the score based on the type of object it is.
	 * @param object
	 */
	private void updateScore(AbstractGameObject object)
	{
		if(object.getClass().equals(Sardines.class))
		{
			Sardines obj = (Sardines) object;
			worldController.score += obj.getScore();
			level.cat.setSardinePowerup(true);
			worldController.time += obj.setSardineTime();
			Gdx.app.log(TAG, "Sardine collected");
		}
		if(object.getClass().equals(ScoreObject.class))
		{
			ScoreObject obj = (ScoreObject) object;
			worldController.score += obj.getScore();
			Gdx.app.log(TAG, "Score Object collected");
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// TODO Auto-generated method stub

	}
}
