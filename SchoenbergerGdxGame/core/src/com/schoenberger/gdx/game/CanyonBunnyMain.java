package com.schoenberger.gdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.schoenberger.gdx.game.Assets;
import com.schoenberger.gdx.game.screens.MenuScreen;

public class CanyonBunnyMain extends Game {
	@Override
	public void create () {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen (new MenuScreen(this));
	}
}