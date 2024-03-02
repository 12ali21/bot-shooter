package com.mygdx.botshooter.world.characters.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TrackCarAnimator extends Animator {
    public static final String rightTrackRegion = "right_track";
    public static final String leftTrackRegion = "left_track";
    public static final String bodyRegion = "body";
    public static final String drillRegion = "drill";
    private float size;


    private static final int frameSize = 128;

    private AnimatedPart leftTrack;
    private AnimatedPart rightTrack;
    private AnimatedPart drill;

    private TextureRegion body;

    private Texture tmpTexture;
    private Pixmap tmpPixmap;
    private Texture whole;
    private Pixmap wholePixmap;

    private Vector2 exhaustPos;

    ParticleEffect smokeEffect;

    public TrackCarAnimator(String assets) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(assets).child("driller.atlas"));

        body = atlas.findRegion(bodyRegion);
        TextureRegion[][] rightTrackFrames = atlas.findRegion(rightTrackRegion).split(frameSize, frameSize);
        TextureRegion[][] leftTrackFrames = atlas.findRegion(leftTrackRegion).split(frameSize, frameSize);
        TextureRegion[][] drillFrames = atlas.findRegion(drillRegion).split(frameSize, frameSize);

        rightTrack = new AnimatedPart(new Animation<>(.03f, rightTrackFrames[0]));
        leftTrack = new AnimatedPart(new Animation<>(.03f, leftTrackFrames[0]));
        drill = new AnimatedPart(new Animation<>(.05f, drillFrames[0]));

        wholePixmap = new Pixmap(frameSize, frameSize, Pixmap.Format.RGBA8888);

        smokeEffect = new ParticleEffect();
        smokeEffect.load(Gdx.files.internal("particles/smoke.p"), Gdx.files.internal("particles/"));
        smokeEffect.start();
        smokeEffect.scaleEffect(0.03f);
        exhaustPos = new Vector2(-.5f, -2.9f);

    }

    public void render(Batch batch, Vector2 position) {

        batch.draw(rightTrack.animation.getKeyFrame(rightTrack.stateTime),
                position.x - originX, position.y - originY,
                originX, originY,
                width, height,
                1, 1, rotation);
        batch.draw(leftTrack.animation.getKeyFrame(leftTrack.stateTime),
                position.x - originX, position.y - originY,
                originX, originY,
                width, height,
                1, 1, rotation);


        batch.draw(drill.animation.getKeyFrame(drill.stateTime),
                position.x - originX, position.y - originY,
                originX, originY,
                width, height,
                1, 1, rotation);

        batch.draw(body, position.x - originX, position.y - originY,
                originX, originY,
                width, height,
                1, 1,
                rotation);

        Vector2 pos = exhaustPos.cpy().rotateDeg(rotation);
        smokeEffect.setPosition(position.x + pos.x, position.y + pos.y);

        ParticleEmitter.ScaledNumericValue angle = smokeEffect.getEmitters().first().getAngle();
        angle.setHigh(rotation - 80, rotation - 100);
        smokeEffect.draw(batch, 0.01f);

    }

    public void update(float delta) {
        if (rightTrack.speed != 0) {
            rightTrack.addDelta(delta);
        }
        if (leftTrack.speed != 0) {
            leftTrack.addDelta(delta);

        }
        if (drill.speed > 0) {
            drill.addDelta(delta);
        }
    }

    public void animateRightTrack(float speed) {
        rightTrack.speed = speed;
    }


    public void animateLeftTrack(float speed) {
        leftTrack.speed = speed;
    }

    public void animateDrilling(float speed) {
        drill.speed = speed;
    }
}
