package com.mygdx.botshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BotShooter extends ApplicationAdapter {

	private GameStage stage;

	@Override
	public void create () {
		stage = new GameStage();
	}

	@Override
	public void resize(int width, int height) {
		stage.camera.viewportWidth = 64;
		stage.camera.viewportHeight = 64f * height/width;
		super.resize(width, height);
	}

	@Override
	public void render () {
		ScreenUtils.clear(145/255f, 117/255f, 93/255f , 0);
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
