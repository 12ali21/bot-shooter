package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.botshooter.characters.Player;
import com.mygdx.botshooter.map.MapController;

public class GameScreen implements Screen {

    OrthographicCamera camera;
    SpriteBatch mainBatch;
    private Player player;
    private MapController map;

    private static Vector2 v;



    @Override
    public void show() {
        mainBatch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(10, 10*h/w);

        map = new MapController(camera);
        player = new Player(map, camera);
        Gdx.input.setInputProcessor(player);

    }

    private void update(float delta){
        player.update(delta);

        camera.position.set(player.getWorldOriginX(), player.getWorldOriginY(), 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        mainBatch.begin();
        map.render();
        player.render(mainBatch);
        mainBatch.end();

        if(v != null){
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(v.x, v.y, 0.1f, 90);
            shapeRenderer.end();
        }


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 64;
        camera.viewportHeight = 64f * height/width;
    }

    public static void test(Vector2 v){
        GameScreen.v = v;
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
