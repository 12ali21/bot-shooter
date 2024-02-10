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
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.Debug;
import com.mygdx.botshooter.Drawable;
import com.mygdx.botshooter.OrientedBox;
import com.mygdx.botshooter.characters.weapons.MiniGun;
import com.mygdx.botshooter.characters.weapons.Weapon;
import com.mygdx.botshooter.map.MapController;


public class Player implements InputProcessor, Drawable {
    private final float SIZE = 4;

    private Sprite sprite;
    private Weapon weaponR;
    private Weapon weaponL;

    private Rectangle rect = new Rectangle();
    private OrientedBox collider;

    private float velocity = 0;
    private float acceleration = 30;
    private float deceleration = acceleration * 2;
    private float maxMovementSpeed = 160;
    private float maxBackwardSpeed = maxMovementSpeed / 3f;

    private float rotationSpeed = 200;

    private float frictionWithWalls = 0.5f;

    private float direction = 90;

    private boolean collision = true;

    private MapController map;

    OrthographicCamera camera;

    private Vector3 tmpVector3 = new Vector3();
    private Vector2 tmpVector2 = new Vector2();

    public Player(MapController map, OrthographicCamera camera) {
        this.map = map;
        this.camera = camera;

        int posX = 100, posY = 2;

        Texture spriteTexture = new Texture("player.png");
        sprite = new Sprite(spriteTexture);
        // position in world
        sprite.setPosition(posX, posY);
        // center position with respect to the player
        sprite.setOrigin(SIZE / 2, SIZE / 2);
        sprite.setSize(SIZE, SIZE);

        // create the collider
        collider = new OrientedBox(posX + 1, posY,
                posX + 1, posY + SIZE,
                posX + SIZE - 1, posY + SIZE,
                posX + SIZE - 1, posY);
        collider.setPosition(posX, posY);
        collider.setOrigin(SIZE / 2, SIZE / 2);


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
        weaponL.render(batch);
        weaponR.render(batch);
        sprite.draw(batch);
        collider.debugRender(batch, camera);
    }

    private Array<Rectangle> getWallsInPath(float dx, float dy) {
        Rectangle bounds = collider.getAABB();
        Debug.log("dx, dy", "" + dx + ", " + dy);
        if (dx <= 0) {
            bounds.x += dx;
            bounds.width -= dx;
        } else {
            bounds.width += dx;
        }

        if (dy <= 0) {
            bounds.y += dy;
            bounds.height -= dy;
        } else {
            bounds.height += dy;
        }

        int sX = (int) bounds.x;
        int eX = (int) (Math.ceil(bounds.x + bounds.width));
        int sY = (int) bounds.y;
        int eY = (int) (Math.ceil(bounds.y + bounds.height));
        Debug.drawRect("Bounds", bounds);

        return MapController.getWalls(sX, eX, sY, eY);
    }


    public void moveBy(float dx, float dy) {
        Debug.log("len", "" + Math.sqrt(dx * dx + dy * dy));
        OrientedBox newCollider = collider.copy();
        newCollider.translate(dx, dy);

        Batch batch = new SpriteBatch();
        batch.begin();
        newCollider.debugRender(batch, camera);
        batch.end();


        Array<Rectangle> walls = getWallsInPath(dx, dy);

        for (Rectangle wall : walls) {
            Debug.drawRect("" + wall, wall);
        }

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
        Debug.log("Penetration", "" + maxPenetration);
        // if there is a collision, move the player to the edge of the wall
        if (maxPenetration > 0) {
            // a trick to avoid a bug that let player go through the wall when reversing
            if (maxPenetration < SIZE) {
                // get the direction from dx and dy
                float angle = MathUtils.atan2Deg(dy, dx);
                dx -= maxPenetration * MathUtils.cosDeg(angle);
                dy -= maxPenetration * MathUtils.sinDeg(angle);
            }

            velocity = 0;
        }
        sprite.translate(dx, dy);
        collider.translate(dx, dy);
    }

    @Override
    public void update(float delta) {
        Debug.log("Player X", "" + sprite.getX());
        Debug.log("Player Y", "" + sprite.getY());

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity += acceleration * delta;
            if (velocity > maxMovementSpeed) {
                velocity = maxMovementSpeed;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity -= deceleration * delta;
            if (velocity < -maxBackwardSpeed) {
                velocity = -maxBackwardSpeed;
            }
//            moveBy(-MathUtils.cosDeg(direction) * velocity * delta,
//                    -MathUtils.sinDeg(direction) * velocity * delta);


        }
        moveBy(MathUtils.cosDeg(direction) * velocity * delta,
                MathUtils.sinDeg(direction) * velocity * delta);

        if (velocity != 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                direction += rotationSpeed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                direction -= rotationSpeed * delta;
            }
        }

        sprite.setRotation(direction - 90);
        collider.setDirection(direction - 90);

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
