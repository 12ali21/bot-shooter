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

	private GLProfiler profiler;
	GameScreen gameScreen;
//	Color backgroundColor = new Color(145/255f, 117/255f, 93/255f , 0);
	Color backgroundColor = new Color(0, 0, 0 , 0);


	// gets called when the game starts
	@Override
	public void create () {
		profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();
		gameScreen = new GameScreen();
		gameScreen.show();
	}

	//gets called every time application gets resized
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		gameScreen.resize(width, height);
	}

	// gets called every frame
	@Override
	public void render () {
		profiler.reset();
		ScreenUtils.clear(backgroundColor);
		gameScreen.render(Gdx.graphics.getDeltaTime());

//		System.out.printf("Draws: %d\n", profiler.getTextureBindings());

	}

	// gets called when application gets destroyed
	@Override
	public void dispose () {
		gameScreen.dispose();
	}
}
