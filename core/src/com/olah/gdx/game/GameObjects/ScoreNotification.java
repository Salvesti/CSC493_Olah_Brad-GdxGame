package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.olah.gdx.game.Assets;

public class ScoreNotification extends AbstractGameObject
{
	private int score;

	public ScoreNotification(int score,Vector2 position)
	{
		this.score = score;
		this.position.x = position.x;
		this.position.y = position.y;
	}

	public void render(SpriteBatch batch)
	{
		Assets.instance.fonts.defaultTiny.draw(batch, "+" + score, position.x, position.y);
	}
}
