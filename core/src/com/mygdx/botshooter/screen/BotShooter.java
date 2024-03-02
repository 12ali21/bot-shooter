package com.mygdx.botshooter.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.botshooter.screen.GameScreen;

public class BotShooter extends Game {

	Screen mainScreen;
//	Color backgroundColor = new Color(145/255f, 117/255f, 93/255f , 0);
	Color backgroundColor = Color.BLACK;


	// gets called when the game starts
	@Override
	public void create () {
		mainScreen = new GameScreen();
		mainScreen.show();
	}

	//gets called every time application gets resized
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		mainScreen.resize(width, height);
	}

	// gets called every frame
	@Override
	public void render () {
		ScreenUtils.clear(backgroundColor);
		float delta = Gdx.graphics.getDeltaTime();
		mainScreen.render(delta);
//		System.out.printf("Draws: %d\n", profiler.getTextureBindings());

	}

	// gets called when application gets destroyed
	@Override
	public void dispose () {
		mainScreen.dispose();
	}
}
