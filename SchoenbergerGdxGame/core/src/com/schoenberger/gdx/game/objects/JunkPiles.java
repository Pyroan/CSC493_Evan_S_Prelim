package com.schoenberger.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.schoenberger.gdx.game.Assets;

// These aren't really meant to be congruous so I guess
// We'll just have to see if this works.
public class JunkPiles extends AbstractGameObject{

	private TextureRegion regJunk;
	
	private int length;
	
	public JunkPiles (int length) {
		this.length = length;
		init();
	}
	
	private void init() {
		dimension.set(5,5);
		
		regJunk = Assets.instance.pileOfGarbage.junkPile;
		
		// shift pile and extend length
		origin.x = -dimension.x*2;
		length += dimension.x *2;
	}
	
	private void drawPile(SpriteBatch batch, float offsetX, 
			float offsetY, float tintColor) {
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		// junk spans the whole level
		int junkLength = 0;
		junkLength += MathUtils.ceil(length / (2 * dimension.x));
		junkLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < junkLength; i++) {
			// junkPile
			reg = regJunk;
			batch.draw(reg.getTexture(), position.x + xRel, position.y 
					+ yRel, origin.x, origin.y,dimension.x, dimension.y, 
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		// reset color to white
		batch.setColor(1,1,1,1);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		//distant piles (dark grey)
		drawPile(batch, 0.5f, 0.5f, 0.5f);
		// distant piles (grey)
		drawPile (batch, 0.25f, 0.25f, 0.7f);
		// distant piles (light grey)
		drawPile(batch, 0.0f, 0.0f, 0.9f);
		
	}
}
