package com.mygdx.botshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class BotShooter extends Game {

	GameScreen gameScreen;
	private boolean DEBUG = true;
	DebugUI debugUI;
//	Color backgroundColor = new Color(145/255f, 117/255f, 93/255f , 0);
	Color backgroundColor = new Color(0, 0, 0 , 0);


	// gets called when the game starts
	@Override
	public void create () {
		gameScreen = new GameScreen();
		gameScreen.show();

		if(DEBUG) {
			debugUI = new DebugUI();
		}
	}

	//gets called every time application gets resized
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		gameScreen.resize(width, height);
		debugUI.resize(width, height);
	}

	// gets called every frame
	@Override
	public void render () {
		ScreenUtils.clear(backgroundColor);
		float delta = Gdx.graphics.getDeltaTime();
		gameScreen.render(delta);
		if(DEBUG) {
			debugUI.render(delta);
		}
//		System.out.printf("Draws: %d\n", profiler.getTextureBindings());

	}

	// gets called when application gets destroyed
	@Override
	public void dispose () {
		gameScreen.dispose();
	}
}
