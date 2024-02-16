package com.mygdx.botshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

public class BotShooter extends Game {

	GameScreen gameScreen;
	private boolean DEBUG = true;
	Debug debug;
//	Color backgroundColor = new Color(145/255f, 117/255f, 93/255f , 0);
	Color backgroundColor = new Color(0, 0, 0 , 0);


	// gets called when the game starts
	@Override
	public void create () {
		gameScreen = new GameScreen();
		gameScreen.show();

		if(DEBUG) {
			debug = new Debug();
		}
	}

	//gets called every time application gets resized
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		gameScreen.resize(width, height);
		debug.resize(width, height);
	}

	// gets called every frame
	@Override
	public void render () {
		ScreenUtils.clear(backgroundColor);
		float delta = Gdx.graphics.getDeltaTime();
		gameScreen.render(delta);
		if(DEBUG) {
			debug.render(delta);
		}
//		System.out.printf("Draws: %d\n", profiler.getTextureBindings());

	}

	// gets called when application gets destroyed
	@Override
	public void dispose () {
		gameScreen.dispose();
	}
}
