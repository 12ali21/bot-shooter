package com.mygdx.botshooter.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.botshooter.actors.weapons.MiniGun;
import com.mygdx.botshooter.actors.weapons.Weapon;


public class Player extends Group implements InputProcessor {
    private Image mainImg;
    private Weapon weaponR;
    private Weapon weaponL;

    private Rectangle rect = new Rectangle();
    private float movementSpeed = 20;

    private boolean collision = true;

    private Map map;

    private Vector3 tmpVector3 = new Vector3();
    private Vector2 tmpVector2 = new Vector2();

    public Player(Map map) {
        this.map = map;

        Texture mainTexture = new Texture("player.png");
        mainImg = new Image(mainTexture);
        mainImg.setSize(4,4);
        setSize(mainImg.getWidth(), mainImg.getHeight());
        addActor(mainImg);
        setBounds(2,2,4,4);
        setOrigin(getWidth()/2, getHeight()/2);

        assignRightWeapon(new MiniGun());
        assignLeftWeapon(new MiniGun());
    }

    private void assignRightWeapon(Weapon weapon){
        weaponR = weapon;

        weaponR.setPosition(2.4f,3);
        addActor(weaponR);
        weaponR.setZIndex(0);
    }
    private void assignLeftWeapon(Weapon weapon){
        weaponL = weapon;

        weaponL.setPosition(0.6f,3);
        addActor(weaponL);
        weaponL.setZIndex(0);
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public Vector2 getCenter(){
        return new Vector2(getX() + getWidth()/2, getY() + getHeight()/2);
    }

    public void faceTowardsMouse(int screenX, int screenY){
        tmpVector3.set(screenX, screenY, 0);
        tmpVector3 = getStage().getCamera().unproject(tmpVector3);
        tmpVector2.set(tmpVector3.x, tmpVector3.y);
        tmpVector2.sub(getCenter());
        setRotation(tmpVector2.angleDeg() - 90);
    }



    public boolean checkCollisionWithWalls(Rectangle rect){
        if(!collision)
            return false;

        Array<Rectangle> cells = map.getWalls(
                (int) rect.x-1,
                (int) (rect.x + rect.width) + 1,
                (int) rect.y-1,
                (int) (rect.y + rect.height) +1);

        for(Rectangle cell:cells){
            if(rect.overlaps(cell)) {
                return true;
            }
        }
        return false;
    }

    public Rectangle getRect() {
        float SIZE = 3;
        float size_offset = getHeight() - SIZE;
        return new Rectangle(getX() + size_offset/2, getY() + size_offset/2, SIZE, SIZE);
    }

    @Override
    public void moveBy(float x, float y) {
        Rectangle newPos = getRect();

        newPos.setPosition(newPos.x + x, newPos.y + y);
//        System.out.println(newPos);
        if(!checkCollisionWithWalls(newPos)) {
            super.moveBy(x, y);
        }
    }

    @Override
    public void act(float delta) {
        float DIAGONAL_SCALE = 0.7f;

        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveBy((movementSpeed * delta) * DIAGONAL_SCALE, (movementSpeed * delta) * DIAGONAL_SCALE);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveBy(-(movementSpeed * delta) * DIAGONAL_SCALE, (movementSpeed * delta) * DIAGONAL_SCALE);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveBy((movementSpeed * delta) * DIAGONAL_SCALE, -(movementSpeed * delta) * DIAGONAL_SCALE);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveBy(-(movementSpeed * delta) * DIAGONAL_SCALE, -(movementSpeed * delta) * DIAGONAL_SCALE);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveBy(0, (movementSpeed * delta));
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveBy(0, (-movementSpeed * delta));
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveBy((movementSpeed * delta), 0);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveBy((-movementSpeed * delta), 0);
        }

        super.act(delta);
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
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        faceTowardsMouse(screenX, screenY);
        return true;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //TODO: Only face mouse when shooting
        faceTowardsMouse(screenX, screenY);
        return true;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
