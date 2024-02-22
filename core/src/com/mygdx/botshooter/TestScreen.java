package com.mygdx.botshooter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestScreen implements Screen {
    TrackCarAnimator animator;
    Camera camera;

    SpriteBatch batch;
    public final float ASPECT_RATIO = 1920f / 1080f;


    @Override
    public void show() {
        camera = new OrthographicCamera(100, 100 / ASPECT_RATIO);
        batch = new SpriteBatch();
        animator = new TrackCarAnimator("player/driller_default");

    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(animator.getFrame(delta), 0, 0);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 640;
        camera.viewportHeight = 640 / ASPECT_RATIO;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
