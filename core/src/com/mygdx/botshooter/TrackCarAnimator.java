package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TrackCarAnimator {
    public static final String rightTrackRegion = "right_track";
    public static final String leftTrackRegion = "left_track";
    public static final String bodyRegion = "body";
    public static final String drillRegion = "drill";

    private class AnimatedPart {
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
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(assets).child("out.atlas"));

        body = atlas.findRegion(bodyRegion);
        TextureRegion[][] rightTrackFrames = atlas.findRegion(rightTrackRegion).split(frameSize, frameSize);
        TextureRegion[][] leftTrackFrames = atlas.findRegion(leftTrackRegion).split(frameSize, frameSize);
        TextureRegion[][] drillFrames = atlas.findRegion(drillRegion).split(frameSize, frameSize);

        atlas.dispose();

        rightTrack = new AnimatedPart(new Animation<>(.03f, rightTrackFrames[0]));
        leftTrack = new AnimatedPart(new Animation<>(.03f, leftTrackFrames[0]));
        drill = new AnimatedPart(new Animation<>(.03f, drillFrames[0]));

        wholePixmap = new Pixmap(frameSize, frameSize, Pixmap.Format.RGBA8888);

    }
    public Texture getIdle() {
        drawOnPixmap(rightTrack.animation.getKeyFrame(rightTrack.stateTime));
        drawOnPixmap(leftTrack.animation.getKeyFrame(leftTrack.stateTime));
        drawOnPixmap(drill.animation.getKeyFrame(drill.stateTime));
        drawOnPixmap(body);

        if (whole != null) whole.dispose();
        whole = new Texture(wholePixmap);

        return whole;
    }

    public Texture getFrame(float delta) {
        if (rightTrack.speed != 0) {
            rightTrack.addDelta(delta);

            drawOnPixmap(rightTrack.animation.getKeyFrame(rightTrack.stateTime));
//            setAnimationSpeed(rightTrack.animation, rightTrack.speed);

        }
        if (leftTrack.speed != 0) {
            leftTrack.addDelta(delta);

            drawOnPixmap(leftTrack.animation.getKeyFrame(leftTrack.stateTime));
//            setAnimationSpeed(leftTrack.animation, leftTrack.speed);

        }
        if (drill.speed > 0) {
            drill.addDelta(delta);
            drawOnPixmap(drill.animation.getKeyFrame(drill.stateTime));
        }
        drawOnPixmap(body);

        if (whole != null) whole.dispose();
        whole = new Texture(wholePixmap);

//        resetStateTimes();
        return whole;
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

    //TODO: cache pix-maps for increased performance
    private void drawOnPixmap(TextureRegion region) {
        tmpTexture = region.getTexture();
        tmpTexture.getTextureData().prepare();
        tmpPixmap = tmpTexture.getTextureData().consumePixmap();

        wholePixmap.drawPixmap(tmpPixmap, 0, 0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight());
        tmpPixmap.dispose();
    }
}
