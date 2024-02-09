package com.mygdx.botshooter.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.botshooter.DebugUI;
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
    private float movementSpeed = 280;
    private float rotationSpeed = 200;

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
        DebugUI.drawPoint(batch, camera, (int) sprite.getX(), (int) sprite.getY());
    }

    public void faceTowardsMouse(int screenX, int screenY) {
        tmpVector3.set(screenX, screenY, 0);
        tmpVector3 = camera.unproject(tmpVector3);
        tmpVector2.set(tmpVector3.x, tmpVector3.y);
        tmpVector2.sub(new Vector2(getWorldOriginX(), getWorldOriginY()));
        sprite.setRotation(tmpVector2.angleDeg() - 90);
    }

    public boolean checkCollisionWithWalls(Rectangle rect) {
        if (!collision)
            return false;
        return MapController.checkCollisionWithWalls(rect);
    }


    public Rectangle getColliderRect() {
        float COLLIDER_SIZE = 3;
        float size_offset = sprite.getHeight() - COLLIDER_SIZE;
        return new Rectangle(sprite.getX() + size_offset / 2, sprite.getY() + size_offset / 2, COLLIDER_SIZE, COLLIDER_SIZE);
    }

    public void moveBy(float dx, float dy) {
        sprite.translate(dx, dy);
        collider.translate(dx, dy);
    }

    @Override
    public void update(float delta) {
        DebugUI.log("Player X", "" + sprite.getX());
        DebugUI.log("Player Y", "" + sprite.getY());
        float DIAGONAL_SCALE = 0.7f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveBy((float) (MathUtils.cosDeg(direction) * movementSpeed * delta), (float) (MathUtils.sinDeg(direction) * movementSpeed * delta));
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                direction += rotationSpeed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                direction -= rotationSpeed * delta;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveBy((float) (-MathUtils.cosDeg(direction) * movementSpeed * delta), (float) (-MathUtils.sinDeg(direction) * movementSpeed * delta));
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                direction -= rotationSpeed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                direction += rotationSpeed * delta;
            }
        }

        sprite.setRotation(direction - 90);
        collider.setRotation(direction - 90);

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
