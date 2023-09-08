package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.*;

public class PlayerActor extends Group {
    private Image mainImg;
    private Rectangle rect = new Rectangle();
    private float movementSpeed = 200;

    private Vector3 tmpVector3 = new Vector3();
    private Vector2 tmpVector2 = new Vector2();

    public PlayerActor() {
        super();
        setBounds(0,0,0,0);

        Texture bot = new Texture("player.png");
        mainImg = new Image(bot);
        mainImg.setPosition(0,0);
        mainImg.setSize(10,10);
        setSize(mainImg.getWidth(), mainImg.getHeight());
        setOrigin(getWidth()/2, getHeight()/2);
        addActor(mainImg);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public Vector2 getCenter(){
        return new Vector2(getX() + getWidth()/2, getY() + getHeight()/2);
    }

    public void faceTowardsMouse(){
        tmpVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        tmpVector3 = getStage().getCamera().unproject(tmpVector3);
        tmpVector2.set(tmpVector3.x, tmpVector3.y);

        System.out.println("mouse: "+tmpVector2);
        System.out.println("center:" + getCenter());
        tmpVector2.sub(getCenter());
        setRotation(tmpVector2.angleDeg() - 90);
    }

    @Override
    public void act(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            moveBy(0,movementSpeed*delta);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            moveBy(0,-movementSpeed*delta);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            moveBy(movementSpeed*delta, 0);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            moveBy(-movementSpeed*delta, 0);

        faceTowardsMouse();


        super.act(delta);
    }
}
