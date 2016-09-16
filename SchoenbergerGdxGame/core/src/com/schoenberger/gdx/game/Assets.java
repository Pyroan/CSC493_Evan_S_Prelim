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

	// singleton: prevent instantiation from other classes.
	private Assets () {}

	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
				TextureAtlas.class);

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
		player = new AssetPlayer(atlas);
		box = new AssetBox(atlas);
		fire = new AssetFire(atlas);
		speedBoost = new AssetSpeedBoost(atlas);
		pileOfGarbage = new AssetPileOfGarbage(atlas);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Wouldn't load asset '"
				+ asset.fileName + "'", (Exception) throwable);
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
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;

		public AssetPileOfGarbage (TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
		}
	}
}

