package com.schoenberger.gdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.schoenberger.gdx.game.CanyonBunnyMain;


public class DesktopLauncher {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false; // Shows the pink debugging outlines when true

	public static void main (String[] arg) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets_raw/images",
					"../core/assets/images", "canyonbunny.pack");
			TexturePacker.process(settings, "assets_raw/images-ui",
					"../core/assets/images", "canyonbunny-ui.pack");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
		config.width = 800;
		config.height = 480;
	}
}
