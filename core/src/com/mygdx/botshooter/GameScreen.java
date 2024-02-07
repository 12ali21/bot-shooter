package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.botshooter.characters.Biter;
import com.mygdx.botshooter.characters.Player;
import com.mygdx.botshooter.characters.weapons.Projectile;
import com.mygdx.botshooter.characters.weapons.ProjectilesUtil;
import com.mygdx.botshooter.map.MapController;

public class GameScreen implements Screen {

    OrthographicCamera camera;
    SpriteBatch mainBatch;
    private Player player;
    private MapController map;

    private static Vector2 v;
    private Biter biter;
    public final float FOV = 128;
    public final float ASPECT_RATIO = 1920f/1080f;

    @Override
    public void show() {
        mainBatch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(10, 10*h/w);

        map = new MapController(camera);
        player = new Player(map, camera);
        biter = new Biter(40,40);

        Gdx.input.setInputProcessor(player);

    }
    private void update(float delta){
        ProjectilesUtil.updateProjectiles(delta);
        player.update(delta);
        biter.update(delta);

        camera.position.set(player.getWorldOriginX(), player.getWorldOriginY(), 0);
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
        camera.viewportWidth = FOV;
        camera.viewportHeight = FOV / ASPECT_RATIO;
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
