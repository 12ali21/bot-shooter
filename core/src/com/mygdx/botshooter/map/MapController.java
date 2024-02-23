package com.mygdx.botshooter.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.botshooter.Debug;

import java.util.HashSet;


public class MapController implements Disposable {
    private final MapGenerator map;
    private final HashSet<Body> obstacles;
    private final Array<Rectangle> registeredBounds;
    private final PolygonShape wallBox;

    public MapController(OrthographicCamera camera) {
        map = new MapGenerator(123, 2000, 2000, camera);

        obstacles = new HashSet<>();
        registeredBounds = new Array<>(false, 16);

        wallBox = new PolygonShape();
        wallBox.setAsBox(.5f, .5f);
    }

    public void renderGround(Batch batch) {
        map.renderer.renderGround(batch);
    }

    public void renderMountain(Batch batch) {
        map.renderer.renderMountain(batch);
    }

    public Array<Rectangle> getWalls(int startX, int endX, int startY, int endY) {
        Array<Rectangle> cells = new Array<>();
        Cell cell;
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                cell = map.mountainLayer.getCell(x, y);
                if (cell != null) {
                    cells.add(new Rectangle(x, y, 1, 1));
                }
            }
        }
        return cells;
    }

    public void registerBounds(Rectangle bounds) {
        registeredBounds.add(bounds);
    }

    private void destroyObstacles(World world) {
        for (Body obstacle : obstacles) {
            world.destroyBody(obstacle);
        }
        obstacles.clear();
    }

    private Array<Rectangle> getWallsInBounds(Rectangle bounds) {
        int sX = (int) bounds.x;
        int eX = (int) (Math.ceil(bounds.x + bounds.width));
        int sY = (int) bounds.y;
        int eY = (int) (Math.ceil(bounds.y + bounds.height));
        Debug.drawRect("Bounds", bounds);

        return getWalls(sX, eX, sY, eY);
    }
    private void addObstacle(Body body) {
        obstacles.add(body);
    }

    private Array<Rectangle> wallsRect;
    private void recalculateObstacles(World world, Rectangle bounds) {
        wallsRect = getWallsInBounds(bounds);
        Body t;
        for (Rectangle wallRect : wallsRect) {
            BodyDef wallDef = new BodyDef();
            wallDef.position.set(wallRect.x + .5f, wallRect.y + .5f);
            t = world.createBody(wallDef);
            t.createFixture(wallBox, 0.0f).setUserData("wall");
            addObstacle(t);
        }
    }

    //TODO: don't destroy obstacle every frame (cache them?)
    //TODO: keep lastBounds to check if recalculating walls is needed
    public void update(World world) {
        destroyObstacles(world);
        for(Rectangle bounds : registeredBounds) {
            recalculateObstacles(world, bounds);
        }
    }


    @Override
    public void dispose() {
        wallBox.dispose();
    }
}
