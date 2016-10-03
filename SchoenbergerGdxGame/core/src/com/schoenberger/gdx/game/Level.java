package com.schoenberger.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.schoenberger.gdx.game.objects.AbstractGameObject;
import com.schoenberger.gdx.game.objects.Box;
import com.schoenberger.gdx.game.objects.JunkPiles;
import com.schoenberger.gdx.game.objects.Tire;
import com.schoenberger.gdx.game.objects.SpeedBoost;
import com.schoenberger.gdx.game.objects.Fire;

public class Level {
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE {
		EMPTY (0,0,0), // black
		BOX (0,255,0), // green
		PLAYER_SPAWNPOINT (255,255, 255), // white
		SPEEDBOOST (255, 0, 255), // purple
		ITEM_GOLD_COIN (255, 255, 0); // yellow
		
		private int color;
		
		private BLOCK_TYPE (int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		public boolean sameColor(int color) {
			return this.color == color;
		}
		
		public int getColor () {
			return color;
		}
	}
	
	public Tire player;
	public Array<Fire> flames;
	public Array<SpeedBoost> speedBoosts;
	
	// objects
	public Array<Box> boxes;
	
	// decoration
	public JunkPiles junkPiles;
	
	public Level (String filename) {
		init(filename);
	}
	
	private void init (String filename) {
		// player character
		player = null;
		
		// objects
		boxes = new Array<Box>();
		flames = new Array<Fire>();
		speedBoosts = new Array<SpeedBoost>();
		
		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal (filename));
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight=pixmap.getHeight()-pixelY;
				//get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x, y)
				// point and create the corresponding game object if there is
				// a match.
				
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// do nothing
				}
				// box
				else if (BLOCK_TYPE.BOX.sameColor(currentPixel)) {
					obj = new Box();
					float heightIncreaseFactor = 1.0f;
					offsetHeight = -5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y 
							* heightIncreaseFactor + offsetHeight);
					boxes.add((Box) obj);
				}
				// player spawn point
				else if 
					(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					obj = new Tire();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					player = (Tire) obj;
				}
				// speedboost
				else if (BLOCK_TYPE.SPEEDBOOST.sameColor(currentPixel)) {
					obj = new SpeedBoost();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight *obj.dimension.y + offsetHeight);
					speedBoosts.add((SpeedBoost) obj);
				}
				// fire
				else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
					obj = new Fire();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					flames.add((Fire) obj);
				}
				// unknown object/pixel color
				else {
					int r = 0xff & (currentPixel >>> 24); // red color channel
					int g = 0xff & (currentPixel >>> 16); // green color channel
					int b = 0xff & (currentPixel >>> 8); // blue color channel
					int a = 0xff & currentPixel; // alpha channel.
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
							+ pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		
		// decoration
		junkPiles = new JunkPiles(pixmap.getWidth());
		junkPiles.position.set(-1,-1);
		
		//free memory 
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}
	
	public void render (SpriteBatch batch) {
		// Draw junkPiles
		junkPiles.render(batch);
		
		// Draw Boxes
		for (Box box : boxes) {
			box.render(batch);
		}
		
		// Draw Flames
		for (Fire flame: flames)
			flame.render(batch);
		
		// Draw Speed Boosts
		for (SpeedBoost boost: speedBoosts)
			boost.render(batch);
		
		// Draw player character
		player.render(batch);
	}
	
	public void update(float deltaTime) {
		player.update(deltaTime);
		for (Box box: boxes)
			box.update(deltaTime);
		for (Fire flame: flames)
			flame.update(deltaTime);
		for (SpeedBoost boost: speedBoosts)
			boost.update(deltaTime);
	}
}
