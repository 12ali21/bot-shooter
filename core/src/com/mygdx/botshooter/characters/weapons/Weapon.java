package com.mygdx.botshooter.characters.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.botshooter.characters.Player;
import com.mygdx.botshooter.util.Timer;
import com.mygdx.botshooter.util.TimerAction;
import com.mygdx.botshooter.util.Utils;

public class Weapon {

    protected boolean shooting = false;
    private boolean shootingStarted = false;

    private float worldDirection;

    private int fireRate;
    protected Texture bulletTexture;
    private float recoil = 0;

    private OrthographicCamera camera;

    private Timer shootingTimer;

    protected Sprite sprite;

    private Vector2 defaultOffset;
    private Vector2 offset;

    public Weapon(OrthographicCamera camera, Vector2 offsetFromParentCenter) {
        this.camera = camera;
        this.defaultOffset = offsetFromParentCenter;
        offset = new Vector2();
        offset.set(defaultOffset);
    }
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

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
        Vector2 vector2 = new Vector2(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY());
//        GameScreen.test(vector2);
        return vector2;
    }

    public void update(float delta, float parentCenterX, float parentCenterY, float parentRotation) {
        offset.set(defaultOffset);
        offset.rotateDeg(parentRotation);
        sprite.setOriginBasedPosition(parentCenterX + offset.x, parentCenterY + offset.y);

        Vector3 vector3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        vector3 = camera.unproject(vector3);
        Vector2 vector2 = new Vector2(vector3.x, vector3.y);

        vector2.sub(getOriginInWorld());

        worldDirection = vector2.angleDeg()-90;
        if(worldDirection < 0)
            worldDirection += 360;

        sprite.setRotation(worldDirection);

//        System.out.printf("Parent rotation: %f, Child rotation: %f raw vector angle %f\n ", parentRotation, rotation, vector2.angleDeg());
        shootingTimer.tick(delta);
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }


    private void fire(float delta){

        float randomFloat = Utils.random.nextFloat();
        float randomSize = randomFloat/2 + 0.5f;
        float randomRecoil = recoil * (randomFloat - 0.5f);
        float direction = worldDirection + randomRecoil;
        sprite.rotate(randomRecoil);

        Vector2 positionVector = getOriginInWorld();
        Vector2 offsetVector = new Vector2(0, sprite.getHeight());
        offsetVector.rotateDeg(direction);

        positionVector.add(offsetVector);
//        GameScreen.test(positionVector);

        Player.projectiles.add(new Projectile(bulletTexture,
                100,
                direction + 90,
                positionVector,
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
