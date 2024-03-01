package com.mygdx.botshooter.characters.animator;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Animator {
    protected class AnimatedPart {
        private final int STATE_TIME_LIMIT = 1000000;

        Animation<TextureRegion> animation;
        float speed = 0f;
        float stateTime = 0f;

        public AnimatedPart(Animation<TextureRegion> animation) {
            this.animation = animation;
            animation.setPlayMode(Animation.PlayMode.LOOP);
        }

        public void addDelta(float delta) {
            stateTime += delta * speed;
            if(stateTime < 0) {
                stateTime += animation.getAnimationDuration() * 100;
            } else if (stateTime > STATE_TIME_LIMIT) {
                stateTime -= animation.getAnimationDuration() * 100;
            }
        }
    }

    protected float width;
    protected float height;

    protected float originX;
    protected float originY;

    protected float rotation;

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setOrigin(float x, float y) {
        originX = x;
        originY = y;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void render(Batch batch, Vector2 position) {

    }
}
