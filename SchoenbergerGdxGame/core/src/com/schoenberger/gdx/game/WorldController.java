package com.schoenberger.gdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.schoenberger.gdx.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.schoenberger.gdx.util.Constants;
import com.schoenberger.gdx.game.objects.Box;
import com.badlogic.gdx.math.Rectangle;
import com.schoenberger.gdx.game.objects.Tire;
import com.schoenberger.gdx.game.objects.Tire.JUMP_STATE;
import com.schoenberger.gdx.game.objects.SpeedBoost;
import com.schoenberger.gdx.game.objects.Fire;
import com.schoenberger.gdx.game.objects.Box;

public class WorldController extends InputAdapter{
	// Tags are required for all debug messages
	private static final String TAG = WorldController.class.getName();
	
	public CameraHelper cameraHelper;
	
	public Level level;
	public int lives;
	public int score;
	
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	private void onCollisionPlayerWithBox (Box box) {
		Tire player = level.player;
		float heightDifference = Math.abs(player.position.y 
				- (box.position.y + box.bounds.height));
		if (heightDifference > .25f) {
			boolean hitRightEdge = player.position.x > (
					box.position.x + box.bounds.width /2.0f);
			if (hitRightEdge) {
				player.position.x = box.position.x + box.bounds.width;
			} else {
				player.position.x = box.position.x - 
						player.bounds.width;
			}
			return;
		}
		
		switch (player.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			player.position.y = box.position.y +
			player.bounds.height + player.origin.y;
			player.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			player.position.y = box.position.y +
			player.bounds.height + player.origin.y;
			break;
		}
	}
	
	private void onCollisionPlayerWithFire (Fire myMixtape /*It's lit*/) {
		myMixtape.collected = true;
		// score += myMixtape.getScore() maybe not update score for getting hit.
		Gdx.app.log(TAG, "Fire hit");
	}
	
	private void onCollisionPlayerWithBoost (SpeedBoost speedBoost) {
		speedBoost.collected = true;
		score += speedBoost.getScore();
		level.player.setSpeedBoost(true);
		Gdx.app.log(TAG, "Speed Boosted");
	}
	
	private void testCollisions () {
		r1.set(level.player.position.x, level.player.position.y,
				level.player.bounds.width, level.player.bounds.height);
		
		// Test Collision: Player <-> Boxes
		for (Box box : level.boxes) {
			r2.set(box.position.x, box.position.y, box.bounds.width,
					+box.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPlayerWithBox(box);
			// IMPORTANT: must do all collision for valid
			// edge testing on boxes
		}
		
		// Test Collision: Player <-> Fire
		for (Fire flame: level.flames) {
			if (flame.collected) continue;
			r2.set(flame.position.x, flame.position.y,
					flame.bounds.width, flame.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPlayerWithFire(flame);
		}
		
		// Test collision: Player <-> speedBoosts
		for (SpeedBoost boost: level.speedBoosts) {
			if (boost.collected) continue;
			r2.set(boost.position.x, boost.position.y,
					boost.bounds.width, boost.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPlayerWithBoost(boost);
			break;
		}
	}
	
	public WorldController() {
		init();
	}
	
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeLeftGameOverDelay = 0;
		initLevel();
	}
	
	private void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.player);
	}
	
	/**
	 * Draws a fancy test asset (a cyan box filled with red and with a yellow x)
	 * @param width
	 * @param height
	 * @return
	 */
	private Pixmap createProceduralPixmap (int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow-colored X shape on the square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0,  0, width, height);
		return pixmap;
	}
	
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if (isGameOver()) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay <0) init();
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
	}
	
	private void handleDebugInput (float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		
		if (!cameraHelper.hasTarget(level.player)){
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=
					camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera (0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
		}
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *=
				camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}
	
	private void handleInputGame(float deltaTime) {
		if(cameraHelper.hasTarget(level.player)) {
			// player movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.player.velocity.x = -level.player.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.player.velocity.x =
						level.player.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType()!= ApplicationType.Desktop) {
					level.player.velocity.x = 
							level.player.terminalVelocity.x;
				}
			}
			
			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.player.setJumping(true);
			} else {
				level.player.setJumping(false);
			}
		}
	}
	
	private void moveCamera (float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	@Override
	public boolean keyUp (int keycode) {
		//Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world was reset"); // "Resetted" isn't a word, so I changed the log message a bit.
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.player);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}
	
	private float timeLeftGameOverDelay;
	
	public boolean isGameOver() {
		return lives < 0;
	}
	
	public boolean isPlayerInWater() {
		return level.player.position.y < -5;
	}
}
