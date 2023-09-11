package com.mygdx.botshooter.characters.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class MiniGun extends Weapon{
    private final float SIZE = 0.7f;

    public MiniGun(OrthographicCamera camera, Vector2 offsetFromParentCenter) {
        super(camera, offsetFromParentCenter);
        Texture texture = new Texture(Gdx.files.internal("minigun/minigun.png"));
        setSprite(new Sprite(texture));
        float ratio = (float)texture.getHeight()/texture.getWidth();
        sprite.setSize(SIZE, SIZE*ratio);
        sprite.setOrigin(sprite.getWidth()/2, 0);

        bulletTexture = new Texture(Gdx.files.internal("minigun/minigun_bullet.png"));
        setFireRate(1000);
        setRecoil(4);
    }



}
