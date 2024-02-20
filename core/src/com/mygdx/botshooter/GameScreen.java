package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.botshooter.characters.Biter;
import com.mygdx.botshooter.characters.Player;
import com.mygdx.botshooter.characters.weapons.ProjectilesUtil;
import com.mygdx.botshooter.map.MapController;

public class GameScreen implements Screen {

    OrthographicCamera camera;
    SpriteBatch mainBatch;
    private Player player;
    public static World world;
    Box2DDebugRenderer box2DDebugRenderer;

    private MapController map;

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

        world = new World(new Vector2(0, 0), true);
        box2DDebugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);

        map = new MapController(camera);
        player = new Player(map, camera);
        biter = new Biter(40, 40);

        inputAdapter = new ScreenInputAdapter();

        InputMultiplexer inputMultiplexer = new InputMultiplexer(inputAdapter, player);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void update(float delta) {

        ProjectilesUtil.updateProjectiles(delta);
        biter.update(delta);

        world.step(Constants.WORLD_TIME_STEP, 6, 2);
        player.update(delta);

        Vector2 playerPos = player.getWorldCenter();
        camera.position.set(playerPos.x, playerPos.y, 0);
        if (inputAdapter.isFOVChanged()) updateFOV();
        camera.update();
    }


    @Override
    public void render(float delta) {
        update(delta);

        mainBatch.begin();
        // Render map
        map.render();
        // Render game objects
        ProjectilesUtil.drawProjectiles(mainBatch);
        player.render(mainBatch);
        biter.render(mainBatch);
        mainBatch.end();
        box2DDebugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        updateFOV();
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
