package com.nico.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nico.GameObjects.Level;

public class Alpha extends Game {
	SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		setScreen(new Splash(this));
	}

}
