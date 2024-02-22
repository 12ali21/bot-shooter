package com.mygdx.botshooter.characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.botshooter.Debug;
import com.mygdx.botshooter.GameScreen;
import com.mygdx.botshooter.map.MapController;

import java.util.HashSet;

public class DynamicObject implements Disposable {
    protected static HashSet<Body> everyObstacle = new HashSet<>();

    private Array<Body> obstacles;

    protected Body body;

    private PolygonShape wallBox;
    private final Rectangle bounds = new Rectangle();
    private float boundsSize;


    public DynamicObject(float boundsSize) {
        this.boundsSize = boundsSize;
        obstacles = new Array<>(false, 16);

        wallBox = new PolygonShape();
        wallBox.setAsBox(.5f, .5f);
    }

    private void destroyObstacles() {
        for (Body obstacle : obstacles) {
            body.getWorld().destroyBody(obstacle);
            everyObstacle.remove(obstacle);
        }
        obstacles.clear();
    }

    private Array<Rectangle> getWallsInPath() {
        bounds.set(body.getWorldCenter().x - boundsSize, body.getWorldCenter().y - boundsSize, boundsSize * 2, boundsSize * 2);
        int sX = (int) bounds.x;
        int eX = (int) (Math.ceil(bounds.x + bounds.width));
        int sY = (int) bounds.y;
        int eY = (int) (Math.ceil(bounds.y + bounds.height));
        Debug.drawRect("Bounds", bounds);

        return MapController.getWalls(sX, eX, sY, eY);
    }
    private void addObstacle(Body body) {
        everyObstacle.add(body);
        obstacles.add(body);
    }

    private Array<Rectangle> wallsRect;
    private void recalculateObstacles() {
        wallsRect = getWallsInPath();
        Body t;
        for (Rectangle wallRect : wallsRect) {
            BodyDef wallDef = new BodyDef();
            wallDef.position.set(wallRect.x + .5f, wallRect.y + .5f);
            t = GameScreen.world.createBody(wallDef);
            t.createFixture(wallBox, 0.0f);
            addObstacle(t);
        }
    }
    //TODO: don't destroy obstacle every frame
    //TODO: keep lastBounds to check if recalculating walls is needed
    //
    // every time first destroy all assigned obstacles, then recalculate the required obstacles inside a certain area
    protected void update() {
        destroyObstacles();
        recalculateObstacles();
    }

    @Override
    public void dispose() {
        wallBox.dispose();

    }
}

