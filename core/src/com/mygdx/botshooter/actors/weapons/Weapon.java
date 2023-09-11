package com.mygdx.botshooter.actors.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.BotShooter;
import com.mygdx.botshooter.actors.Player;
import com.mygdx.botshooter.util.Timer;
import com.mygdx.botshooter.util.TimerAction;
import com.mygdx.botshooter.util.Utils;
import org.graalvm.compiler.core.common.util.Util;

public class Weapon extends Image {

    protected boolean shooting = false;
    private boolean shootingStarted = false;

    private float worldDirection;

    private int fireRate;
    protected Texture bulletTexture;
    private float recoil = 0;


    private Timer shootingTimer;

    public void setFireRate(int fireRate){
        this.fireRate = fireRate;
        shootingTimer = new Timer(new TimerAction() {
            @Override
            public void act(float delta) {
                fire(delta);
            }
        }, 60f/fireRate, true);
    }

    public void setRecoil(float recoil){
        this.recoil = recoil;
    }

    public Vector2 getOriginInWorld(){
        float parentWidth = getParent().getWidth();
        float parentHeight = getParent().getHeight();
        // position of origin but from center
        Vector2 vector2 = new Vector2(getX()-parentWidth/2 + getOriginX(), getY()-parentHeight/2 + getOriginY());
        vector2.rotateDeg(getParent().getRotation());

        // position of origin in world system
        vector2.add(getParent().getX() + parentWidth/2, getParent().getY() + parentHeight/2);
        BotShooter.test(vector2);
        return vector2;
    }

    @Override
    public void act(float delta) {
        Vector3 vector3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        vector3 = getStage().getCamera().unproject(vector3);
        Vector2 vector2 = new Vector2(vector3.x, vector3.y);

        vector2.sub(getOriginInWorld());

        float parentRotation = getParent().getRotation();
        if(parentRotation < 0)
            parentRotation += 360;
        worldDirection = vector2.angleDeg()-90;
        if(worldDirection < 0)
            worldDirection += 360;

        setRotation(worldDirection - parentRotation);

//        System.out.printf("Parent rotation: %f, Child rotation: %f raw vector angle %f\n ", parentRotation, rotation, vector2.angleDeg());
        shootingTimer.tick(delta);
    }


    private void fire(float delta){

        Texture bulletTexture = new Texture(Gdx.files.internal("minigun/minigun_bullet.png"));

        float randomFloat = Utils.random.nextFloat();
        float randomSize = randomFloat/2 + 0.5f;
        float randomRecoil = recoil * (randomFloat - 0.5f);
        float direction = worldDirection + randomRecoil;
        rotateBy(randomRecoil);

        Vector2 positionVector = getOriginInWorld();
        Vector2 offsetVector = new Vector2(0, getHeight());
        offsetVector.rotateDeg(direction);

        Player.projectiles.add(new Projectile(bulletTexture,
                100,
                direction + 90,
                positionVector.x + offsetVector.x,
                positionVector.y + offsetVector.y,
                randomSize));
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
        if(shooting && !shootingStarted) {
            shootingStarted = true;
            shootingTimer.start();
        }
        else if(!shooting) {
            shootingStarted = false;
            shootingTimer.stop();
        }
    }
}
