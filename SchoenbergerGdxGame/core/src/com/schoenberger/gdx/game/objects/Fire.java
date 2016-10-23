package com.schoenberger.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.schoenberger.gdx.game.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Fire extends AbstractGameObject{
	private TextureRegion regFire;
	public boolean collected;
	
	public ParticleEffect fireParticles = new ParticleEffect();
	
	public Fire() {
		init();
	}
	
	private void init () {
		dimension.set(0.5f, 0.5f);
		
		regFire = Assets.instance.fire.flame;
		
		// Set bounding box for collision detection
		bounds.set(0,0,dimension.x, dimension.y);
		
		// particles
		fireParticles.load(Gdx.files.internal("particles/fire.pfx"), Gdx.files.internal("particles"));
		collected = false;
	}
	
	@Override
	public void render (SpriteBatch batch) {
		if (collected) return;
		
		TextureRegion reg = null;
		
		// Draw Particles
		fireParticles.draw(batch);
		
		reg = regFire;
		batch.draw(reg.getTexture(), position.x, position.y, 
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	@Override
	public void update(float deltaTime) {
		fireParticles.update(deltaTime);
	}
	
	// For now let's just have fire grant points instead of killing you horribly.
	public int getScore() {
		return 100;
	}

}