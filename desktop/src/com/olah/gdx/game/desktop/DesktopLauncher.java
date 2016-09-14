package com.olah.gdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.olah.gdx.game.CanyonBunnyMain;

/**
 * Auto-generated Desktop launcher from the LibGDX setup app.
 * @author Brad Olah
 */
public class DesktopLauncher
{
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg)
	{
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight= 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.processIfModified(settings, "assets-raw/images", "../core/assets/images","canyonbunny.pack");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Canyon Bunny - Olah";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
