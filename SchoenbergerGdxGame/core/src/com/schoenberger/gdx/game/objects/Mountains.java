package com.schoenberger.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.schoenberger.gdx.game.Assets;
import com.badlogic.gdx.math.Vector2;

public class Mountains extends AbstractGameObject {

	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;

	private int length;

	public Mountains (int length) {
		this.length = length;
		init();
	}
	
	/**
	 * Parallax Scrolling
	 */
	public void updateScrollPosition (Vector2 camPosition) {
		position.set(camPosition.x, position.y);
	}

	private void init () {
		dimension.set(10, 2);

		regMountainLeft =
				Assets.instance.levelDecoration.mountainLeft;
		regMountainRight =
				Assets.instance.levelDecoration.mountainRight;

		// Shift mountain and extend length
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
	}

	/**
	 * Draws a mountain.
	 * @param batch
	 * @param offsetX
	 * @param offsetY
	 * @param tintColor
	 * @param parallaxSpeedX
	 */
	private void drawMountain (SpriteBatch batch, float offsetX,
			float offsetY, float tintColor, float parallaxSpeedX) {
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;

		// Mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2* dimension.x) * (1-parallaxSpeedX));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < mountainLength; i++) {
			//mountain left
			reg = regMountainLeft;
			batch.draw(reg.getTexture(), origin.x + xRel, position.y +
					origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;

			// mountain right
			reg = regMountainRight;
			batch.draw(reg.getTexture(), 
					origin.x + xRel + position.x * parallaxSpeedX, 
					origin.y + yRel + position.y,
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), 
					false, false);
			xRel += dimension.x;
		}
		// Reset color to white
		batch.setColor(1,1,1,1);
	}

	@Override
	public void render (SpriteBatch batch) {
		// distant mountains (dark grey)
		drawMountain(batch, 0.5f, 0.5f, 0.5f, .8f);
		// distant mountains (grey)
		drawMountain(batch, 0.25f, 0.25f, .7f, .5f);
		//distant mountanis (light grey)
		drawMountain(batch, 0.0f, 0.0f, 0.9f, .3f);
	}

}
