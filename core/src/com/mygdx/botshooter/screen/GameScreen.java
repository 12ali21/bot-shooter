package com.mygdx.botshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.botshooter.Debug;
import com.mygdx.botshooter.GameWorld;
import com.mygdx.botshooter.ScreenInputAdapter;
import com.mygdx.botshooter.characters.Biter;
import com.mygdx.botshooter.characters.Player;
import com.mygdx.botshooter.characters.weapons.ProjectilesUtil;

public class GameScreen implements Screen {

    OrthographicCamera camera;
    SpriteBatch mainBatch;
    private Player player;
    GameWorld gameWorld;

    private static Vector2 v;
    private Biter biter;
    public final float ASPECT_RATIO = 1920f / 1080f;


    ScreenInputAdapter inputAdapter;

    @Override
    public void show() {
        mainBatch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(10, 10 * h / w);
        Debug.setCamera(camera);

        gameWorld = new GameWorld(camera);


        player = new Player(gameWorld, camera);
        biter = new Biter(40, 40);

        inputAdapter = new ScreenInputAdapter();

        InputMultiplexer inputMultiplexer = new InputMultiplexer(inputAdapter, player);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void update(float delta) {
        // update entities
        ProjectilesUtil.updateProjectiles(delta);
        biter.update(delta);
        player.update(delta);

        // update world physics
        gameWorld.update();

        // center camera on player
        Vector2 playerPos = player.getWorldCenter();
        camera.position.set(playerPos.x, playerPos.y, 0);
        if (inputAdapter.isFOVChanged()) updateFOV();
        camera.update();
    }


    @Override
    public void render(float delta) {
        update(delta);
        mainBatch.setProjectionMatrix(camera.combined);
        mainBatch.begin();
        // Render ground layer
        gameWorld.renderGround(mainBatch);

        // Render game objects
        ProjectilesUtil.drawProjectiles(mainBatch);
        player.render(mainBatch);
        biter.render(mainBatch);

        // Render mountain layer
        gameWorld.renderMountain(mainBatch);
        mainBatch.end();

        // render world logs and debugs
        gameWorld.debug(camera, delta);
    }

    @Override
    public void resize(int width, int height) {
        updateFOV();
        Debug.resize(width, height);
    }

    private void updateFOV() {
        camera.viewportWidth = inputAdapter.getFOV();
        camera.viewportHeight = inputAdapter.getFOV() / ASPECT_RATIO;
    }

    public static void test(Vector2 v) {
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
