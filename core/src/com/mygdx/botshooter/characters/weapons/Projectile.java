package com.mygdx.botshooter.characters.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.botshooter.characters.Player;
import com.mygdx.botshooter.map.MapController;
import com.mygdx.botshooter.util.Timer;
import com.mygdx.botshooter.util.TimerAction;

public class Projectile {
    Sprite sprite;

    float velocity;
    float direction;
    private float SIZE = 1f/32;
    private float lifetime = 1f;

    private Timer lifeTimer;

    public Projectile(Texture texture, float velocity, float direction, Vector2 startPos, float sizeScale) {
        sprite = new Sprite(texture);
        SIZE *= sizeScale;

        this.velocity = velocity;
        this.direction = direction;

        sprite.setRotation(direction-90);
        sprite.setSize(SIZE*texture.getWidth(), SIZE*texture.getHeight());
        sprite.setPosition(startPos.x, startPos.y);
        sprite.setOrigin(0,0);

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
        Player.projectiles.removeValue(this, true);
    }

    public Projectile(Texture texture, float velocity, float direction, Vector2 startPos) {
        this(texture, velocity, direction, startPos, 1);
    }

    public void moveBy(float x, float y){
        sprite.setPosition(sprite.getX() + x, sprite.getY() + y);
    }

    public void update(float delta) {
        lifeTimer.tick(delta);
        moveBy(MathUtils.cosDeg(direction)*velocity*delta,
                MathUtils.sinDeg(direction)*velocity*delta);
    }

    public void draw(Batch batch){
        if(MapController.checkCollisionWithWalls(sprite.getBoundingRectangle())){
            destroy();
            return;
        }
        sprite.draw(batch);
    }
}
