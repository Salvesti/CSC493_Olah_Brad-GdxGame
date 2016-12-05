package com.olah.gdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.olah.gdx.game.Assets;
import com.olah.gdx.game.util.Constants;

public class HighScoreUpdateScreen extends AbstractGameScreen
{

	private static final String TAG = Assets.class.getName();
	private Stage stage;
	private Skin skinLibgdx;
	private Window winHighScore;
	private boolean updateScore;

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

		Gdx.app.debug(TAG, "Draw Stage");
		//Build all layers
		Table highScoreLayer = buildHighScoreLayer();
		//Assemble stage.
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(highScoreLayer);
	}

	/**
	 * Builds the high score layer info.
	 * @return
	 */
	private Table buildHighScoreLayer()
	{
		Gdx.app.debug(TAG, "Build High Score Layer");
		winHighScore = new Window("High Score",skinLibgdx);
		if(updateScore)
		{
			Label nameLabel = new Label("Enter Name:", skinLibgdx);
			TextField highScore = new TextField("adfasdf", skinLibgdx);
			final TextButton button = new TextButton("Click me!", skinLibgdx);
			winHighScore.add(nameLabel).row();
			winHighScore.add(highScore).row();
			winHighScore.add(button);
			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					//Insert a high score
					System.out.println("insert a high score");
					game.setScreen(new MenuScreen(game));
				}
			});
		}
		//Make high score window slightly transparent
		winHighScore.setColor(1, 1, 1, 0.8f);
		// Move options window to bottom right corner
		winHighScore.setPosition(Constants.VIEWPORT_GUI_WIDTH - winHighScore.getWidth() - 50, 50);
		return winHighScore;
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
