package com.olah.gdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.olah.gdx.game.CanyonBunnyMain;

/**
 * Launches the Desktop version of the LibGDX game.
 * @author Brad Olah
 */
public class DesktopLauncher
{
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg)
	{
		//Creates a texture atlas
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight= 1024;
			settings.duplicatePadding = true;
			settings.debug = drawDebugOutline;
			TexturePacker.processIfModified(settings, "assets-raw/images", "../core/assets/images","canyonbunny.pack");
			//TexturePacker.processIfModified(settings, "assets-raw/images-ui", "../core/assets/images","canyonbunny-ui.pack");
		}

		//Sets the configurations for the desktop window.
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Canyon Bunny - Olah";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
