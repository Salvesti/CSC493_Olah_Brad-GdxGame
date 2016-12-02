package com.olah.gdx.game.util;

import com.badlogic.gdx.graphics.Color;

/**
 * An enum that handles skin color options.
 * @author Brad Olah
 */
public enum CharacterSkin
{
	WHITE("Gray", 1.0f, 1.0f, 1.0f),
	GRAY("Black", 0.5f, 0.5f, 0.5f),
	BROWN("Brown", 0.7f, 0.5f, 0.3f);

	private String name;
	private final Color color = new Color();

	private CharacterSkin(String name, float r, float g, float b)
	{
		this.name = name;
		color.set(r, g, b, 1.0f);
	}

	@Override
	public String toString()
	{
		return name;
	}

	public Color getColor()
	{
		return color;
	}
}