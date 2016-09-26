package com.olah.gdx.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.olah.gdx.game.Assets;

public class Wallpaper extends AbstractGameObject{
	private TextureRegion wallpaper;
	
	public Wallpaper()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(1,1);
		
		wallpaper = Assets.instance.levelDecoration.wallpaper;
	}
	
	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		
		reg = wallpaper;
		batch.draw(reg.getTexture(), position.x+relX, position.y+relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,reg.getRegionX(),reg.getRegionY(),reg.getRegionWidth(),reg.getRegionHeight(),false,false);	
	}
}