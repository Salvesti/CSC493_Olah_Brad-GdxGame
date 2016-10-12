package com.olah.gdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.olah.gdx.game.GameObjects.Cat;

/**
 * A class that handles the logic for if the cat can jump or not.
 * @author Brad Olah
 */
public class JumpContactListener implements ContactListener
{
	public static final String TAG = JumpContactListener.class.getName();
	private Cat cat;

	public JumpContactListener(Cat inputCat)
	{
		cat = inputCat;
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
			cat.numFootContacts++;
		}
		fixtureUserData = (String)contact.getFixtureB().getUserData();
		if(fixtureUserData.equals("foot"))
		{
			cat.numFootContacts++;
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
			cat.numFootContacts--;
		}
		fixtureUserData = (String)contact.getFixtureB().getUserData();
		if(fixtureUserData.equals("foot"))
		{
			cat.numFootContacts--;
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
