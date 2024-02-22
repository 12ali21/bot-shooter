package com.mygdx.botshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class BotShooter extends Game {

	Screen mainScreen;
	private boolean DEBUG = true;
	Debug debug;
//	Color backgroundColor = new Color(145/255f, 117/255f, 93/255f , 0);
	Color backgroundColor = new Color(0, 0, 0 , 0);


	// gets called when the game starts
	@Override
	public void create () {
		mainScreen = new GameScreen();
		mainScreen.show();

		if(DEBUG) {
			debug = new Debug();
		}
	}

	//gets called every time application gets resized
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		mainScreen.resize(width, height);
		debug.resize(width, height);
	}

	// gets called every frame
	@Override
	public void render () {
		ScreenUtils.clear(backgroundColor);
		float delta = Gdx.graphics.getDeltaTime();
		mainScreen.render(delta);
		if(DEBUG) {
			debug.render(delta);
		}
//		System.out.printf("Draws: %d\n", profiler.getTextureBindings());

	}

	// gets called when application gets destroyed
	@Override
	public void dispose () {
		mainScreen.dispose();
	}
}
