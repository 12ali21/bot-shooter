package com.mygdx.botshooter.actors.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MiniGun extends Weapon{
    private final float SIZE = 0.7f;

    public MiniGun() {
        Texture texture = new Texture(Gdx.files.internal("minigun/minigun.png"));
        setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        float ratio = (float)texture.getHeight()/texture.getWidth();
        setSize(SIZE, SIZE*ratio);
        setOrigin(getWidth()/2, 0);

        bulletTexture = new Texture(Gdx.files.internal("minigun/minigun_bullet.png"));
        setFireRate(1000);
        setRecoil(4);
    }



}
