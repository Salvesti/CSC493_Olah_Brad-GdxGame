package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

public class Sardines extends AbstractGameObject
{
	private TextureRegion regSardine;

	public Boolean collected;

	public Sardines()
	{
		init();
	}

	private void init()
	{
		dimension.set(1f,1f);

		regSardine = Assets.instance.sardines.sardines;

		//Set bounding box for collision detection
		bounds.set(0,0,dimension.x,dimension.y);

		collected = false;
	}

	public void render (SpriteBatch batch)
	{
		if(collected) return;

		TextureRegion reg = null;
		reg = regSardine;
		batch.draw(reg.getTexture(),position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),false, false);
	}

	public int getScore()
	{
		return 250;
	}

	public float setSardineTime()
	{
		return 10;
	}
}
