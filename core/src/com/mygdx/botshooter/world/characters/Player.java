package com.mygdx.botshooter.world.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.world.GameWorld;
import com.mygdx.botshooter.world.characters.animator.TrackCarAnimator;
import com.mygdx.botshooter.world.characters.car.ControlAction;
import com.mygdx.botshooter.world.characters.car.TrackCar;


public class Player implements InputProcessor, Drawable {

//    private Weapon weaponR;
//    private Weapon weaponL;

    private final TrackCarAnimator animator;

    OrthographicCamera camera;
    TrackCar body;

    Array<ControlAction> actions = new Array<>(false, 4);

    public Player(GameWorld gameWorld, OrthographicCamera camera) {
        float SIZE = 6f;

        this.camera = camera;

        int posX = 100, posY = 10;

        animator = new TrackCarAnimator("player/driller_default");
        animator.setSize(SIZE, SIZE);
        animator.setOrigin(SIZE /2, SIZE /2);

//        weaponL = new MiniGun(camera, new Vector2(0.9f, 1.2f));
//        weaponL = new MiniGun(camera, new Vector2(-0.9f, 1.2f));

        body = new TrackCar(gameWorld, new Rectangle(posX, posY, SIZE - 3.4f, SIZE - 1.1f));
    }



    public Vector2 getWorldCenter() {
        return body.getPosition();
    }

    @Override
    public void render(Batch batch) {
        animator.render(batch, getWorldCenter());
    }



    @Override
    public void update(float delta) {
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

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            animator.animateDrilling(1);
            actions.add(ControlAction.DRILL);
        } else {
            animator.animateDrilling(0);
        }

        animator.animateLeftTrack(body.getLeftTrackSpeed());
        animator.animateRightTrack(body.getRightTrackSpeed());
        animator.setRotation(body.getRotation() * MathUtils.radiansToDegrees);

        animator.update(delta);

        body.updateDrive(actions, delta);

//        weaponL.update(delta,
//                sprite.getX() + sprite.getOriginX(),
//                sprite.getY() + sprite.getOriginY(),
//                sprite.getRotation());
//
//        weaponR.update(delta,
//                sprite.getX() + sprite.getOriginX(),
//                sprite.getY() + sprite.getOriginY(),
//                sprite.getRotation());

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
//        weaponL.setShooting(true);
//        weaponR.setShooting(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        weaponL.setShooting(false);
//        weaponR.setShooting(false);
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
