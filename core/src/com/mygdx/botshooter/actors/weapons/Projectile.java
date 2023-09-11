package com.mygdx.botshooter.actors.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Projectile extends Image {
    float velocity;
    float direction;
    private float SIZE = 1f/32;

    public Projectile(Texture texture, float velocity, float direction, float startX, float startY, float sizeScale) {
        super(texture);
        SIZE *= sizeScale;

        this.velocity = velocity;
        this.direction = direction;
        setRotation(direction-90);
        setSize(SIZE*texture.getWidth(), SIZE*texture.getHeight());
        setPosition(startX, startY);
    }

    public Projectile(Texture texture, float velocity, float direction, float startX, float startY) {
        this(texture, velocity, direction, startX, startY, 1);
    }

    @Override
    public void act(float delta) {
        moveBy(MathUtils.cosDeg(direction)*velocity*delta,
                MathUtils.sinDeg(direction)*velocity*delta);
    }
}
