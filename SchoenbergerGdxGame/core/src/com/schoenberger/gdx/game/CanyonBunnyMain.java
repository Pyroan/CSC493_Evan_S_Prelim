package com.schoenberger.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.schoenberger.gdx.game.Assets;
import com.schoenberger.gdx.game.screens.MenuScreen;
import com.schoenberger.gdx.util.AudioManager;
import com.schoenberger.gdx.util.GamePreferences;

public class CanyonBunnyMain extends Game {
	@Override
	public void create () {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		
		// load preferences for audio settings and start playing music.
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		// Start game at menu screen
		setScreen (new MenuScreen(this));
	}
}