package com.schoenberger.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.schoenberger.gdx.game.Assets;
/**
 * Convenient that we need to make the feathers make you fast
 * because that was the plan anyway 
 */
public class SpeedBoost extends AbstractGameObject{
	
	private TextureRegion regBooster;
	
	public boolean collected;
	
	public SpeedBoost() {
		init ();
	}
	
	private void init() {
		dimension.set(0.5f, 0.5f);
		
		regBooster = Assets.instance.speedBoost.booster;
		
		// Set bounding box for collision detection
		bounds.set(0,0, dimension.x, dimension.y);
		
		collected = false;
	}
	
	public void render (SpriteBatch batch) {
		if (collected) return;
		
		TextureRegion reg = null;
		reg = regBooster;
		batch.draw(reg.getTexture(), position.x, position.y, 
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	public int getScore() {
		return 250;
	}

}
