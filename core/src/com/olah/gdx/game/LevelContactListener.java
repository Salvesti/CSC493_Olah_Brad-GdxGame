package com.olah.gdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * A class that handles the collisions detecting for the given level.
 * @author Brad Olah
 */
public class LevelContactListener implements ContactListener
{
	public static final String TAG = LevelContactListener.class.getName();
	private Level level;

	public LevelContactListener(Level level)
	{
		this.level = level;
	}

	/**
	 * If one of the fixtures colliding is the cats foot, increment numFootContacts.
	 */
	@Override
	public void beginContact(Contact contact)
	{
		String fixtureUserData = (String)contact.getFixtureA().getUserData();
		if(fixtureUserData.equals("foot"))
		{
			level.cat.numFootContacts++;
		}
		fixtureUserData = (String)contact.getFixtureB().getUserData();
		if(fixtureUserData.equals("foot"))
		{
			level.cat.numFootContacts++;
		}
	}

	/**
	 * If one of the fixtures that lost contact is the cats foot, decrement numFootContacts.
	 */
	@Override
	public void endContact(Contact contact)
	{
		String fixtureUserData = (String) contact.getFixtureA().getUserData();
		if(fixtureUserData.equals("foot"))
		{
			level.cat.numFootContacts--;
		}
		fixtureUserData = (String)contact.getFixtureB().getUserData();
		if(fixtureUserData.equals("foot"))
		{
			level.cat.numFootContacts--;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
