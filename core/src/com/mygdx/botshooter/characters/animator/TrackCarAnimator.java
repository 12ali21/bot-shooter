package com.mygdx.botshooter.characters.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

public class TrackCarAnimator extends Animator{
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

    public TrackCarAnimator(String assets) {
        this.size = size;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(assets).child("driller.atlas"));

        body = atlas.findRegion(bodyRegion);
        TextureRegion[][] rightTrackFrames = atlas.findRegion(rightTrackRegion).split(frameSize, frameSize);
        TextureRegion[][] leftTrackFrames = atlas.findRegion(leftTrackRegion).split(frameSize, frameSize);
        TextureRegion[][] drillFrames = atlas.findRegion(drillRegion).split(frameSize, frameSize);

        rightTrack = new AnimatedPart(new Animation<>(.03f, rightTrackFrames[0]));
        leftTrack = new AnimatedPart(new Animation<>(.03f, leftTrackFrames[0]));
        drill = new AnimatedPart(new Animation<>(.03f, drillFrames[0]));

        wholePixmap = new Pixmap(frameSize, frameSize, Pixmap.Format.RGBA8888);

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
