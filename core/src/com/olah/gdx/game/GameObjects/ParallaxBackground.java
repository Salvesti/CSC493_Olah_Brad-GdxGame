package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.olah.gdx.game.Assets;

public class ParallaxBackground extends AbstractGameObject
{
	private Texture trees;
	private Texture buildingsFront;
	private Texture buildingsMiddle;
	private Texture buildingsBack;
	private Texture sky;
	private int length;
	
	public ParallaxBackground(int length)
	{
		this.length = length;
		init();
	}
	
	protected void init()
	{
		dimension.set(10,2);
		
		trees = Assets.instance.levelDecoration.trees;
		buildingsFront = Assets.instance.levelDecoration.buildingsFront;
		buildingsMiddle = Assets.instance.levelDecoration.buildingsMiddle;
		buildingsBack = Assets.instance.levelDecoration.buildingsBack;
		sky = Assets.instance.levelDecoration.sky;
	}
	
	public void updateScrollPosition(Vector2 camPosition)
	{
		position.set(camPosition.x, position.y);
	}
	
	private void drawLayer(SpriteBatch batch, Texture texture, float offsetX, float offsetY, float parallaxSpeedX)
	{
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		//Spans the whole level
		int layerLength = 0;
		layerLength += MathUtils.ceil(length / (2*dimension.x)*(1-parallaxSpeedX));
		layerLength += MathUtils.ceil(0.5f * offsetX);
		for (int i = 0; i < layerLength; i++) {
			
			batch.draw(texture, origin.x + xRel + position.x * parallaxSpeedX, origin.y + yRel + position.y, 
			texture.getWidth(), texture.getHeight());
			xRel += dimension.x;

			xRel += dimension.x;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) 
	{
		drawLayer(batch, sky, 0.5f, 0.5f, 0.8f);
		drawLayer(batch, buildingsBack, 0.25f, 0.25f, 0.5f);
		drawLayer(batch, buildingsMiddle, 0.15f, 0.15f, 0.3f);
		drawLayer(batch, buildingsFront, 0.05f, 0.05f, 0.2f);
		drawLayer(batch, trees, 0.0f, 0.0f, 0.1f);
		
	}

}
