package com.schoenberger.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.schoenberger.gdx.util.Constants;

public class Assets implements Disposable, AssetErrorListener{

	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	public AssetPlayer player;
	public AssetBox box;
	public AssetFire fire;
	public AssetSpeedBoost speedBoost;
	public AssetPileOfGarbage pileOfGarbage;
	
	public AssetSounds sounds;
	public AssetMusic music;

	public AssetFonts fonts;

	// singleton: prevent instantiation from other classes.
	private Assets () {}

	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
				TextureAtlas.class);
		
		// load sounds.
		assetManager.load("sounds/tireJump.wav", Sound.class);
		assetManager.load("sounds/tireDie.wav", Sound.class);
		// load music
		assetManager.load("music/larry-awesome.mp3", Music.class);

		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG,  "# of assets loaded: "
				+ assetManager.getAssetNames().size);
		for (String a: assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		TextureAtlas atlas =
				assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// Enable texture filtering for pixel smoothing.
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		// create game resource objects
		fonts = new AssetFonts();
		player = new AssetPlayer(atlas);
		box = new AssetBox(atlas);
		fire = new AssetFire(atlas);
		speedBoost = new AssetSpeedBoost(atlas);
		pileOfGarbage = new AssetPileOfGarbage(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Wouldn't load asset '"
				+ asset.fileName + "'", (Exception) throwable);
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont (
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont (
					Gdx.files.internal("images/arial-15.fnt"), true);

			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public class AssetPlayer {
		public final AtlasRegion tire;

		public AssetPlayer (TextureAtlas atlas) {
			tire = atlas.findRegion("Player");
		}
	}

	public class AssetBox {
		public final AtlasRegion middle;

		public AssetBox (TextureAtlas atlas) {
			middle = atlas.findRegion("Box");
		}
	}

	public class AssetFire {
		public final AtlasRegion flame;

		public AssetFire (TextureAtlas atlas) {
			flame = atlas.findRegion("Fire");
		}
	}

	public class AssetSpeedBoost {
		public final AtlasRegion booster;

		public AssetSpeedBoost (TextureAtlas atlas)
		{
			booster = atlas.findRegion("Speedboost");
		}
	}

	public class AssetPileOfGarbage {
		public final AtlasRegion junkPile;

		public AssetPileOfGarbage (TextureAtlas atlas) {
			junkPile = atlas.findRegion("Pile o garbage");
		}
	}
	
	public class AssetSounds
	{
		public final Sound jump;
		public final Sound die;
		public AssetSounds (AssetManager am) 
		{
			jump = am.get("sounds/tireJump.wav", Sound.class);
			die = am.get("sounds/tireDie.wav", Sound.class);
		}
	}
	
	public class AssetMusic
	{
		public final Music song01;
		public AssetMusic (AssetManager am)
		{
			song01 = am.get("music/larry-awesome.mp3", Music.class);
		}
	}
}

