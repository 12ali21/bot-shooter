package com.mygdx.botshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.ScreenUtils;

public class BotShooter extends ApplicationAdapter {

	private static Vector2 v;
	private GameStage stage;
	private GLProfiler profiler;
	OrthographicCamera camera;


	@Override
	public void create () {
		profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(10, 10*h/w);

		stage = new GameStage(camera);
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
		if(v != null){
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.circle(v.x, v.y, 0.1f, 90);
			shapeRenderer.end();
		}

	}

	public static void test(Vector2 v){
		BotShooter.v = v;
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
