package com.mygdx.botshooter.world.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.botshooter.util.Debug;

import java.util.HashSet;


public class MapController implements Disposable {
    private final MapGenerator map;
    private final Renderer renderer;


    private TextureRegion mapTexture;
    private final HashSet<Body> obstacles;
    private final Array<Rectangle> registeredBounds;
    private final PolygonShape wallBox;

    public MapController(OrthographicCamera camera) {
        map = new MapGenerator(123, 2000, 2000, camera);
        renderer = new Renderer(map.map, 1, camera);

        obstacles = new HashSet<>();
        registeredBounds = new Array<>(false, 16);

        wallBox = new PolygonShape();
        wallBox.setAsBox(.5f, .5f);
    }

    public void renderGround(Batch batch) {
        renderer.renderGround(batch);
    }

    public void renderMountain(Batch batch) {
        renderer.renderMountain(batch);
    }

    public void renderMapTexture(Batch batch, Camera camera) {
        if(mapTexture == null) {
            mapTexture = renderer.renderMapTexture(batch, camera);
        }
        System.out.println(mapTexture.getRegionHeight());
//        batch.draw(mapTexture.getTexture(),-1000, -1000, 2000, 2000);
    }

    /**
     * Get all walls in the given bounds
     * @param startX start X coordinate
     * @param endX end X coordinate
     * @param startY start Y coordinate
     * @param endY end Y coordinate
     * @return an array of rectangles representing the walls in the given bounds
     */
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

    /**
     * Register bounds to be used for recalculating obstacles
     * @param bounds the bounds to register
     */
    public void registerBounds(Rectangle bounds) {
        registeredBounds.add(bounds);
    }

    /**
     * Destroy all obstacles
     * @param world the world to destroy the obstacles in
     */
    private void destroyObstacles(World world) {
        for (Body obstacle : obstacles) {
            world.destroyBody(obstacle);
        }
        obstacles.clear();
    }

    /**
     * Get all walls in the given bounds
     * @param bounds the bounds to get the walls in
     * @return an array of rectangles representing the walls in the given bounds
     */

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

    private void recalculateObstacles(World world, Rectangle bounds) {
        Array<Rectangle> wallsRect = getWallsInBounds(bounds);
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

    /**
     * Damage a wall at the given coordinates
     * @param x the X coordinate of the wall
     * @param y the Y coordinate of the wall
     * @return true if the wall was destroyed, false otherwise
     */
    public boolean damageWall(int x, int y) {
        Cell cell = map.mountainLayer.getCell(x, y);
        if(cell == map.mountainCells[0]) {
            map.mountainLayer.setCell(x, y, map.mountainCells[1]);
        } else if(cell == map.mountainCells[1]) {
            map.mountainLayer.setCell(x, y, map.mountainCells[2]);
        } else if(cell == map.mountainCells[2]) {
            map.mountainLayer.setCell(x, y, map.mountainCells[3]);
        } else if(cell == map.mountainCells[3]) {
            map.mountainLayer.setCell(x, y, null);
            map.groundLayer.setCell(x, y, map.getRandomCell(map.rockyGroundCells));
            return true;
        }
        return false;
    }


    @Override
    public void dispose() {
        wallBox.dispose();
    }
}
