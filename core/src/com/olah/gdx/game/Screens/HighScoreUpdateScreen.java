package com.olah.gdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.olah.gdx.game.Assets;
import com.olah.gdx.game.util.Constants;
import com.olah.gdx.game.util.GamePreferences;
import com.olah.gdx.game.util.HighScores;

public class HighScoreUpdateScreen extends AbstractGameScreen
{

	private static final String TAG = Assets.class.getName();
	private Stage stage;
	private Skin skinLibgdx;
	private Window winHighScore;
	private boolean updateScore;
	private TextField playerName;
	private int newScore;
	private ScrollPane scores;

	/**
	 *
	 * @param game
	 * @param updateScore True if inserting, False if displaying
	 */
	public HighScoreUpdateScreen(Game game,Boolean updateScore)
	{
		super(game);
		Gdx.app.debug(TAG, "High Score Screen");
		this.updateScore = updateScore;
		// TODO Auto-generated constructor stub
	}

	public void drawStage()
	{
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		//Build all layers
		winHighScore = new Window("High Scores",skinLibgdx);
		Table highScoreAddition = buildHighScoreAdditionMenu();
		//Assemble stage.
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(highScoreAddition);
	}

	/**
	 * Builds a ScrollPane holding the saved score values.
	 * @return
	 */
	private ScrollPane buildHighScoreList()
	{
		HighScores highScores = HighScores.instance;
		int numScores = highScores.numScores;
		Table highScoreTable = new Table();
		String[][] highScoresValues = highScores.loadScores();

		for(int i = 0; i < numScores ; i++)
		{
			Label scoreLabel = new Label(highScoresValues[i][0] +" "+ highScoresValues[i][1], skinLibgdx);
			highScoreTable.add(scoreLabel).row();
		}
		scores = new ScrollPane(highScoreTable,skinLibgdx);
		return scores;
	}

	/**
	 * Builds the high score layer info.
	 * @return
	 */
	private Table buildHighScoreAdditionMenu()
	{
		ScrollPane highScores = buildHighScoreList();
		winHighScore.add(highScores).size(500, 100).colspan(4).padBottom(20).row();
		//If this window came from the game allow a new score to be added..
		if(updateScore)
		{
			Label newScoreLabel = new Label("Your score was: "+newScore+"    ",skinLibgdx);
			Label nameLabel = new Label("Enter Name: ", skinLibgdx);

			playerName = new TextField("", skinLibgdx);
			final TextButton button = new TextButton("Add Score", skinLibgdx);

			winHighScore.add(newScoreLabel).colspan(4).row();
			winHighScore.add(nameLabel).colspan(4).row();
			winHighScore.add(playerName).colspan(4).row();
			winHighScore.add(button).colspan(4).row();
			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					//Insert a high score
					System.out.println("insert a high score");
					saveScore();
					game.setScreen(new MenuScreen(game));
				}
			});
		}
		//Adds a back button to return to home without adding score.
		final TextButton backButton = new TextButton("Return to Main Menu", skinLibgdx);
		winHighScore.add(backButton).colspan(4);
		backButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new MenuScreen(game));
			}
		});
		//Make high score window slightly transparent
		winHighScore.setColor(1, 1, 1, 0.8f);
		// Move options window to bottom right corner
		winHighScore.setPosition(Constants.VIEWPORT_GUI_WIDTH - winHighScore.getWidth() - 50, 50);
		winHighScore.pack();
		return winHighScore;
	}

	/**
	 * Saves a highScore.
	 */
	protected void saveScore()
	{
		HighScores highScores = HighScores.instance;
		highScores.addScore(playerName.getText(), newScore + "");

	}

	/**
	 * Passes in a score from the game.
	 * @param score
	 */
	public void giveScore(int score)
	{
		this.newScore = score;
	}

	@Override
	public void render(float deltaTime)
	{
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void resize (int width, int height)
	{
		stage.getViewport().update(width, height,true);
	}
	@Override
	public void show()
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		drawStage();
	}

	@Override
	public void hide()
	{
		stage.dispose();
	}
	@Override
	public void pause()
	{

	}

}
