package com.mygdx.botshooter.actors.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.botshooter.BotShooter;

public class Weapon extends Image {

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
        float rotation = vector2.angleDeg()-90;
        if(rotation < 0)
            rotation += 360;

//        System.out.printf("Parent rotation: %f, Child rotation: %f raw vector angle %f\n ", parentRotation, rotation, vector2.angleDeg());
        setRotation(rotation - parentRotation);
    }
}
