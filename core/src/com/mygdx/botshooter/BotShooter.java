package com.mygdx.botshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.ScreenUtils;

public class BotShooter extends ApplicationAdapter {

	private GameStage stage;
	private GLProfiler profiler;

	@Override
	public void create () {
		profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();

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
		profiler.reset();

		ScreenUtils.clear(145/255f, 117/255f, 93/255f , 0);
		stage.act();
		stage.draw();
//		System.out.printf("Draws: %d\n", profiler.getTextureBindings());


	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
