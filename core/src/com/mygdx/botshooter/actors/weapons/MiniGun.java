package com.mygdx.botshooter.actors.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.botshooter.BotShooter;
import com.mygdx.botshooter.actors.Player;

public class MiniGun extends Weapon{
    private final float SIZE = 0.7f;
    public MiniGun() {
        Texture texture = new Texture(Gdx.files.internal("minigun/minigun.png"));
        setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        float ratio = (float)texture.getHeight()/texture.getWidth();
        setSize(SIZE, SIZE*ratio);
        setOrigin(getWidth()/2, 0);
    }

}
