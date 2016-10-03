package com.schoenberger.gdx.util;
// Keeps track of constants to be accessible by everyone

public class Constants {
	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;

	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;

	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	
	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;
	
	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS =
			"images/tireswing.pack.atlas";

	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/mygame-level-01.png";
	
	// Amount of extra lives at level start
	public static final int LIVES_START = 3;
	
	// Duration of speed boost in seconds
	public static final float ITEM_SPEEDBOOST_DURATION = 3;
	
	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;
}
