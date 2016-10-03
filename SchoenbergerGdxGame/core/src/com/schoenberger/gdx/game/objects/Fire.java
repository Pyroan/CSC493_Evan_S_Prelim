package com.schoenberger.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.schoenberger.gdx.game.Assets;

public class Fire extends AbstractGameObject{
	private TextureRegion regFire;
	public boolean collected;
	
	public Fire() {
		init();
	}
	
	private void init () {
		dimension.set(0.5f, 0.5f);
		
		regFire = Assets.instance.fire.flame;
		
		// Set bounding box for collision detection
		bounds.set(0,0,dimension.x, dimension.y);
		
		collected = false;
	}
	
	public void render (SpriteBatch batch) {
		if (collected) return;
		
		TextureRegion reg = null;
		reg = regFire;
		batch.draw(reg.getTexture(), position.x, position.y, 
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	// For now let's just have fire grant points instead of killing you horribly.
	public int getScore() {
		return 100;
	}

}