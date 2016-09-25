package com.schoenberger.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.schoenberger.gdx.game.Assets;

// The "ground"
public class Box extends AbstractGameObject{
	private TextureRegion regBox;
	
	public Box() {
		init();
	}
	
	private void init() {
		dimension.set(1,1);
		
		regBox = Assets.instance.box.middle;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		
		// Draw middle (as that is the only part because boxes are simple)
		reg = regBox;
		batch.draw(reg.getTexture(), position.x + relX, position.y 
				+ relY, origin.x, origin.y,dimension.x, dimension.y, 
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}