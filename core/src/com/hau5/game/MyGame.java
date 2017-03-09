package com.hau5.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hau5.game.Screens.PlayScreen;

public class MyGame extends Game {

	public static final int WIDTH = 860;
	public static final int HEIGHT = 540;
	public static final float PIXELSPERMETER = 100;
	public static final int TILESIZE = 64;

	// box2d filters ^2
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short SPRITE_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short DEAD_BIT = 512;
	public static final short END_BIT = 1024;

	public SpriteBatch spriteBatch;

	// change from static when porting to android

	
	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		spriteBatch.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
}
