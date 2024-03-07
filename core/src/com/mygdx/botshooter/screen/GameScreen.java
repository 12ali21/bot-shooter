package com.mygdx.botshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.botshooter.util.Debug;
import com.mygdx.botshooter.world.GameWorld;
import com.mygdx.botshooter.world.characters.Player;
import com.mygdx.botshooter.world.characters.weapons.ProjectilesUtil;

public class GameScreen implements Screen {

    OrthographicCamera camera;
    OrthographicCamera mapCamera;

    SpriteBatch mainBatch;
    GameWorld gameWorld;

    private Player player;

    public final float ASPECT_RATIO = 1920f / 1080f;


    ScreenInputAdapter inputAdapter;

    @Override
    public void show() {
        mainBatch = new SpriteBatch();

        mapCamera = new OrthographicCamera(2048, 2048);
        // create the main camera
        camera = new OrthographicCamera(10, 10 / ASPECT_RATIO);
        Debug.setCamera(camera);

        // create the physics world and the map
        gameWorld = new GameWorld(camera);
        gameWorld.doDebugging(true);

        // create the player
        player = new Player(gameWorld, camera);

        // set an input adapter to the player
        inputAdapter = new ScreenInputAdapter();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(inputAdapter, player);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void update(float delta) {
        // update entities
        ProjectilesUtil.updateProjectiles(delta);
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
        mainBatch.dispose();
        gameWorld.dispose();
    }
}
