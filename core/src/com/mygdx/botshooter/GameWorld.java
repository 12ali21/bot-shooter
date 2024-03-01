package com.mygdx.botshooter;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.botshooter.map.MapController;

public class GameWorld {
    private boolean doDebugging = true;


    private World world;
    private MapController mapController;
    Box2DDebugRenderer box2DDebugRenderer;


    public GameWorld(OrthographicCamera camera) {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(GameContactListener.getInstance());
        box2DDebugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);

        mapController = new MapController(camera);
    }

    public void update() {
        mapController.update(world);
        world.step(Constants.WORLD_TIME_STEP, 6, 2);
    }

    public void debug(Camera camera, float delta) {
        box2DDebugRenderer.render(world, camera.combined);

        if (doDebugging) {
            Debug.render(delta);
        }
    }

    public World getWorld() {
        return world;
    }

    public MapController getController() {
        return mapController;
    }

    public void renderGround(Batch mainBatch) {
        mapController.renderGround(mainBatch);
    }

    public void renderMountain(Batch mainBatch) {
        mapController.renderMountain(mainBatch);
    }
}
