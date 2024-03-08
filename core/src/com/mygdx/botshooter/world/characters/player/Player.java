package com.mygdx.botshooter.world.characters.player;

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
import com.mygdx.botshooter.world.characters.Drawable;
import com.mygdx.botshooter.world.characters.animator.TrackCarAnimator;
import com.mygdx.botshooter.world.characters.player.car.ControlAction;
import com.mygdx.botshooter.world.characters.player.car.TrackCar;


public class Player implements InputProcessor, Drawable {

//    private Weapon weaponR;
//    private Weapon weaponL;

    private final TrackCarAnimator animator;

    OrthographicCamera camera;
    TrackCar drillerBody;
    PlayerBot bot;

    Array<ControlAction> actions = new Array<>(false, 4);

    public Player(GameWorld gameWorld, OrthographicCamera camera) {
        float SIZE = 6f;

        this.camera = camera;

        int posX = 100, posY = 10;

        bot = new PlayerBot(gameWorld, posX+10, posY);

        animator = new TrackCarAnimator("player/driller_default");
        animator.setSize(SIZE, SIZE);
        animator.setOrigin(SIZE /2, SIZE /2);

//        weaponL = new MiniGun(camera, new Vector2(0.9f, 1.2f));
//        weaponL = new MiniGun(camera, new Vector2(-0.9f, 1.2f));

        drillerBody = new TrackCar(gameWorld, new Rectangle(posX, posY, SIZE - 3.4f, SIZE - 1.1f));
    }



    public Vector2 getWorldCenter() {
        return bot.getPosition();
    }

    @Override
    public void render(Batch batch) {
        animator.render(batch, drillerBody.getPosition());
        bot.render(batch);
    }



    @Override
    public void update(float delta) {
        // clear actions
        actions.clear();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            actions.add(ControlAction.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            actions.add(ControlAction.DOWN);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            actions.add(ControlAction.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            actions.add(ControlAction.RIGHT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            animator.animateDrilling(1);
            actions.add(ControlAction.SPACE);
        } else {
            animator.animateDrilling(0);
        }

        animator.animateLeftTrack(drillerBody.getLeftTrackSpeed());
        animator.animateRightTrack(drillerBody.getRightTrackSpeed());
        animator.setRotation(drillerBody.getRotation() * MathUtils.radiansToDegrees);

        animator.update(delta);

        bot.updateMovement(actions);
        bot.update();
//        drillerBody.updateDrive(actions, delta);

        drillerBody.update();
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
