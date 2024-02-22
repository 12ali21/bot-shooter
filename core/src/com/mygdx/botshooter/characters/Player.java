package com.mygdx.botshooter.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.*;
import com.mygdx.botshooter.characters.car.Car;
import com.mygdx.botshooter.characters.car.ControlAction;
import com.mygdx.botshooter.characters.car.FourWheelCar;
import com.mygdx.botshooter.characters.car.TrackCar;
import com.mygdx.botshooter.characters.weapons.MiniGun;
import com.mygdx.botshooter.characters.weapons.Weapon;
import com.mygdx.botshooter.map.MapController;


public class Player implements InputProcessor, Drawable {
    private final float SIZE = 6;

    private Sprite sprite;
    private Weapon weaponR;
    private Weapon weaponL;

    private TrackCarAnimator animator;

    private boolean collision = true;

    private MapController map;

    OrthographicCamera camera;
    TrackCar body;

    Array<Body> walls = new Array<>(false, 16);
    Array<ControlAction> actions = new Array<>(false, 4);

    private Vector3 tmpVector3 = new Vector3();
    private Vector2 tmpVector2 = new Vector2();

    public Player(MapController map, OrthographicCamera camera) {
        this.map = map;
        this.camera = camera;

        int posX = 100, posY = 10;

        animator = new TrackCarAnimator("player/driller_default");

        sprite = new Sprite(animator.getIdle());
        // position in world
        sprite.setPosition(posX, posY);
        // center position with respect to the player
        sprite.setOrigin(SIZE / 2, SIZE / 2);
        sprite.setSize(SIZE, SIZE);

        assignRightWeapon(new MiniGun(camera, new Vector2(0.9f, 1.2f)));
        assignLeftWeapon(new MiniGun(camera, new Vector2(-0.9f, 1.2f)));

        body = new TrackCar(GameScreen.world, new Rectangle(posX, posY, SIZE - 3.4f, SIZE - 1.1f));
        TextureRegion region = new TextureRegion();
    }


    private void assignRightWeapon(Weapon weapon) {
        weaponR = weapon;
    }

    private void assignLeftWeapon(Weapon weapon) {
        weaponL = weapon;
    }

    public Vector2 getWorldCenter() {
        return body.getPosition();
    }

    @Override
    public void render(Batch batch) {
//        weaponL.render(batch);
//        weaponR.render(batch);
        sprite.draw(batch);
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


    @Override
    public void update(float delta) {
        Debug.log("Player X", "" + sprite.getX());
        Debug.log("Player Y", "" + sprite.getY());

        // clear actions
        actions.clear();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            actions.add(ControlAction.DRIVE_FORWARD);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            actions.add(ControlAction.DRIVE_BACKWARD);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            actions.add(ControlAction.TURN_LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            actions.add(ControlAction.TURN_RIGHT);
        }

        animator.animateLeftTrack(body.getLeftTrackSpeed());
        animator.animateRightTrack(body.getRightTrackSpeed());

//        sprite.setTexture(animator.getFrame(delta));
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.setRotation(body.getRotation() * MathUtils.radiansToDegrees);

        body.updateDrive(actions);

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
