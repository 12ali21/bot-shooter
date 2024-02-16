package com.mygdx.botshooter.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.Debug;
import com.mygdx.botshooter.Drawable;
import com.mygdx.botshooter.GameScreen;
import com.mygdx.botshooter.OrientedBox;
import com.mygdx.botshooter.characters.weapons.MiniGun;
import com.mygdx.botshooter.characters.weapons.Weapon;
import com.mygdx.botshooter.map.MapController;

import java.util.ArrayList;


public class Player implements InputProcessor, Drawable {
    private final float SIZE = 6;

    private Sprite sprite;
    private Weapon weaponR;
    private Weapon weaponL;

    private Rectangle rect = new Rectangle();
    private OrientedBox collider;

    private float velocity = 0;
    private float acceleration = 300;
    private float deceleration = acceleration * 2;
    private float maxMovementSpeed = 160;
    private float maxBackwardSpeed = maxMovementSpeed / 3f;

    private float rotationSpeed = 0;
    private float maxRotationSpeed = 200;

    private float friction = 0.7f;
    private float frictionWithWalls = 0.5f;

    private float direction = 90;

    private boolean collision = true;

    private MapController map;

    OrthographicCamera camera;
    Body body;


    private Vector3 tmpVector3 = new Vector3();
    private Vector2 tmpVector2 = new Vector2();

    public Player(MapController map, OrthographicCamera camera) {
        this.map = map;
        this.camera = camera;

        int posX = 100, posY = 2;

        Texture spriteTexture = new Texture("player_3.png");
        sprite = new Sprite(spriteTexture);
        // position in world
        sprite.setPosition(posX, posY);
        // center position with respect to the player
        sprite.setOrigin(SIZE / 2, SIZE / 2);
        sprite.setSize(SIZE, SIZE);

        // create the collider
        collider = new OrientedBox(posX + 1.5, posY,
                posX + 1.5, posY + SIZE - 1.5,
                posX + SIZE - 1.5, posY + SIZE - 1.5,
                posX + SIZE - 1.5, posY);
        collider.setPosition(posX, posY);
        collider.setOrigin(SIZE / 2, SIZE / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX, posY);

        body = GameScreen.world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(SIZE / 2, SIZE / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0.1f;

        Fixture fixture = body.createFixture(fixtureDef);
        body.setAngularDamping(6f);
        body.setLinearDamping(1f);

        shape.dispose();

        assignRightWeapon(new MiniGun(camera, new Vector2(0.9f, 1.2f)));
        assignLeftWeapon(new MiniGun(camera, new Vector2(-0.9f, 1.2f)));
    }


    private void assignRightWeapon(Weapon weapon) {
        weaponR = weapon;
    }

    private void assignLeftWeapon(Weapon weapon) {
        weaponL = weapon;
    }

    public float getWorldOriginX() {
        return sprite.getX() + sprite.getOriginX();
    }

    public float getWorldOriginY() {
        return sprite.getY() + sprite.getOriginY();
    }

    @Override
    public void render(Batch batch) {
//        weaponL.render(batch);
//        weaponR.render(batch);
        sprite.draw(batch);
//        collider.debugRender(batch, camera);
    }

    private Array<Rectangle> getWalls(Rectangle bounds) {
        int sX = (int) bounds.x;
        int eX = (int) (Math.ceil(bounds.x + bounds.width));
        int sY = (int) bounds.y;
        int eY = (int) (Math.ceil(bounds.y + bounds.height));
        Debug.drawRect("Bounds", bounds);

        return MapController.getWalls(sX, eX, sY, eY);
    }

    private Array<Rectangle> getWallsInPath() {
        Rectangle bounds = new Rectangle(body.getPosition().x - 6, body.getPosition().y - 6, SIZE * 2, SIZE * 2);
//        Debug.log("dx, dy", "" + dx + ", " + dy);

        return getWalls(bounds);
    }


    /*
    public void moveBy(float dx, float dy) {

        Debug.log("len", "" + Math.sqrt(dx * dx + dy * dy));
        OrientedBox newCollider = collider.copy();
        newCollider.translate(dx, dy);

        Batch batch = new SpriteBatch();
        batch.begin();
//        newCollider.debugRender(batch, camera);
        batch.end();


        Array<Rectangle> walls = getWallsInPath(dx, dy);

//        for (Rectangle wall : walls) {
//            Debug.drawRect("" + wall, wall);
//        }

        Debug.log("walls", "" + walls.size);
        double maxPenetration = 0;
        double penetration;
        for (Rectangle wall : walls) {
            if (newCollider.checkCollision(wall)) {
                if (velocity > 0)
                    penetration = newCollider.getPenetrationDepth(dx, dy, wall, true);
                else
                    penetration = newCollider.getPenetrationDepth(dx, dy, wall, false);

                if (penetration > maxPenetration) {
                    maxPenetration = penetration;
                }
            }
        }
        // if there is a collision, move the player to the edge of the wall
//        if (maxPenetration > 0) {
//            // a trick to avoid a bug that let player go through the wall when reversing
//            if (maxPenetration < SIZE) {
//                // get the direction from dx and dy
//                float angle = MathUtils.atan2Deg(dy, dx);
//                dx -= maxPenetration * MathUtils.cosDeg(angle);
//                dy -= maxPenetration * MathUtils.sinDeg(angle);
//            }
//
//            velocity = 0;
//        }
        sprite.translate(dx, dy);
        collider.translate(dx, dy);
    }
*/

    public void rotateBy(float rotation) {
//        OrientedBox newCollider = collider.copy();
//        newCollider.setDirection(direction + rotation - 90);
//        // check if by rotating collision with walls will happen
//        Array<Rectangle> walls = getWalls(newCollider.getAABB());
//        for(Rectangle wall : walls) {
//            if(newCollider.checkCollision(wall)) {
//                return;
//            }
//        }
//        System.out.println("rotation: " + rotation);
        direction += rotation;

        collider.setDirection(direction - 90);
        sprite.setRotation(direction + -90);

    }

    ArrayList<Body> walls = new ArrayList<>();

    @Override
    public void update(float delta) {
        Debug.log("Player X", "" + sprite.getX());
        Debug.log("Player Y", "" + sprite.getY());

        direction = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            body.applyForceToCenter(
                    new Vector2(0, acceleration), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            body.applyForceToCenter(
                    new Vector2(0, -deceleration), true);
        }
//        if (!Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
//            if (Math.abs(velocity) < 0.5) {
//                velocity = 0;
//            } else {
//                velocity -= (velocity > 0 ? deceleration : -deceleration) * friction * delta;
//            }
//        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.applyForceToCenter(new Vector2(-acceleration, 0), true);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.applyForceToCenter(new Vector2(acceleration, 0), true);

        }

        sprite.setPosition(body.getPosition().x - 3, body.getPosition().y - 3);
        Array<Rectangle> wallsR;
        wallsR = getWallsInPath();
        for (Body wall : walls) {
            GameScreen.world.destroyBody(wall);
        }
        walls.clear();

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(.5f, .5f);
        Body t;
        for (Rectangle wallRect : wallsR) {
            BodyDef wallDef = new BodyDef();
            wallDef.position.set(wallRect.x + .5f, wallRect.y + .5f);
            t = GameScreen.world.createBody(wallDef);
            t.createFixture(groundBox, 0.0f);
            walls.add(t);
        }
        groundBox.dispose();


        weaponL.update(delta,
                sprite.getX() + sprite.getOriginX(),
                sprite.getY() + sprite.getOriginY(),
                sprite.getRotation());

        weaponR.update(delta,
                sprite.getX() + sprite.getOriginX(),
                sprite.getY() + sprite.getOriginY(),
                sprite.getRotation());

    }

    @Override
    public boolean keyDown(int keyCode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        weaponL.setShooting(true);
        weaponR.setShooting(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        weaponL.setShooting(false);
        weaponR.setShooting(false);
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        faceTowardsMouse(screenX, screenY);

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
//        //TODO: Only face mouse when shooting
//        faceTowardsMouse(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
