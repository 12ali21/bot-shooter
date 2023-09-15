package com.mygdx.botshooter.characters.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.botshooter.map.MapController;
import com.mygdx.botshooter.quad.Quad;
import com.mygdx.botshooter.util.Timer;
import com.mygdx.botshooter.util.TimerAction;

public class Projectile {
    private Sprite sprite;
    private float velocity;
    private float direction;
    private float SIZE = 1f/32;
    private float lifetime = 1f;

    private boolean alive = true;

    private Timer lifeTimer;
    private Quad.Point lastPos;

    public Projectile(Texture texture, float velocity, float direction, Vector2 startPos, float sizeScale) {
        sprite = new Sprite(texture);
        SIZE *= sizeScale;


        this.velocity = velocity;
        this.direction = direction;

        sprite.setRotation(direction-90);
        sprite.setSize(SIZE*texture.getWidth(), SIZE*texture.getHeight());
        sprite.setPosition(startPos.x, startPos.y);
        sprite.setOrigin(0,0);

        lastPos = new Quad.Point((int) startPos.x, (int) startPos.y);
        System.out.println("new projectile" + this);

        lifeTimer = new Timer(new TimerAction() {
            @Override
            public void act(float delta) {
                destroy();
                lifeTimer.stop();
            }
        }, lifetime);
        lifeTimer.start();
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
        lifeTimer.changeInterval(lifetime);
    }

    public void destroy(){
        boolean res = ProjectilesUtil.remove(this, lastPos);
        if(!res)
            System.out.println("NOT REMOVED " + this);
        alive = false;
        lifeTimer.reset();
    }

    public Projectile(Texture texture, float velocity, float direction, Vector2 startPos) {
        this(texture, velocity, direction, startPos, 1);
    }

    public void moveBy(float x, float y){
        sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
    }

    public void update(float delta) {
        if(!alive) return;
        lifeTimer.tick(delta);
        moveBy(MathUtils.cosDeg(direction)*velocity*delta,
                MathUtils.sinDeg(direction)*velocity*delta);

        Quad.Point newPos = new Quad.Point((int) sprite.getX(), (int) sprite.getY());
        if(!newPos.getChunk().equals(lastPos.getChunk())){
            ProjectilesUtil.updateChunk(this, lastPos, newPos);
        }
        lastPos.update(newPos.x, newPos.y);
    }

    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void draw(Batch batch){
        if(!alive) return;
        if(MapController.checkCollisionWithWalls(sprite.getBoundingRectangle())){
            destroy();
            return;
        }
        sprite.draw(batch);
    }
}
