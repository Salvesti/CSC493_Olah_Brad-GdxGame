package com.olah.gdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * A singleton that handles the game settings.
 * @author Brad Olah
 */
public class HighScores
{
	public static final String TAG = HighScores.class.getName();

	public static final HighScores instance = new HighScores();

	public int numScores;

	private Preferences prefs;

	//Singleton: Prevent instantiation from other classes
	private HighScores()
	{
		prefs = Gdx.app.getPreferences(Constants.HIGHSCORES);
		numScores = prefs.getInteger("numScores");
	}

	public String[][] loadScores()
	{
		String[][] scores = new String[numScores][2];
		for(int i = 0; i <numScores; i++)
		{
			scores[i][0] = prefs.getString("name"+i);
			scores[i][1] = prefs.getString("score"+i);
		}
		return scores;
	}

	public void addScore(String name, String score)
	{
		numScores++;
		prefs.putString("name"+numScores, name);
		prefs.putString("score"+numScores, score);
		prefs.putInteger("numScores", numScores);
		prefs.flush();

	}
}