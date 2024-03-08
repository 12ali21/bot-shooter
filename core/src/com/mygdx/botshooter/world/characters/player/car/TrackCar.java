package com.mygdx.botshooter.world.characters.player.car;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.utils.Array;
import com.mygdx.botshooter.util.Debug;
import com.mygdx.botshooter.world.GameWorld;

public class TrackCar extends Car {
    private float tmp;

    Drill drill;


    public TrackCar(GameWorld gameWorld, Rectangle rect) {
        super(gameWorld, rect, 10);
        tires = new Tire[2];

        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[]{
                new Vector2(-rect.width / 2 + 0.2f,
                        -rect.height / 2 - 0.2f),
                new Vector2(-rect.width / 2 + 0.2f, rect.height / 2 - 1.4f),
                new Vector2(-0.2f, rect.height / 2),
                new Vector2(0.2f, rect.height / 2),
                new Vector2(rect.width / 2 - 0.2f, rect.height / 2 - 1.4f),
                new Vector2(rect.width / 2 - 0.2f, -rect.height / 2 - 0.2f)};

        shape.set(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;
        fixtureDef.density = 5f;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();

        float tireX = rect.width / 2 - .5f;
        float tireY = rect.height / 2 - 3f;
        float tireWidth = 0.1f;
        float tireHeight = 1.8f;


        Rectangle leftTireRect = new Rectangle(body.getPosition().x - tireX, body.getPosition().y + tireY, tireWidth, tireHeight);
        Rectangle rightTireRect = new Rectangle(body.getPosition().x + tireX, body.getPosition().y + tireY, tireWidth, tireHeight);

        tires[0] = new Tire(gameWorld.getWorld(), body.getMass(), leftTireRect, 80);
        tires[1] = new Tire(gameWorld.getWorld(), body.getMass(), rightTireRect, 80);

        // front left joint
        frontLeftJoint = (RevoluteJoint) createRevoluteJoint(gameWorld.getWorld(), tires[0].body, -tireX, tireY);

        // front right joint
        frontRightJoint = (RevoluteJoint) createRevoluteJoint(gameWorld.getWorld(), tires[1].body, tireX, tireY);

        float friction = 0.5f;
        float airDrag = 0.2f;
        float angularFriction = 0.25f;
        float driveForce = 1200;
        float maxForwardSpeed = 20;
        float maxBackwardSpeed = -15;

        tires[0].setAirDrag(airDrag);
        tires[0].setFriction(friction);
        tires[0].setAngularFriction(angularFriction);
        tires[0].setMaxDriveForce(driveForce);
        tires[0].setMaxForwardSpeed(maxForwardSpeed);
        tires[0].setMaxBackwardSpeed(maxBackwardSpeed);

        tires[1].setAirDrag(airDrag);
        tires[1].setFriction(friction);
        tires[1].setAngularFriction(angularFriction);
        tires[1].setMaxDriveForce(driveForce);
        tires[1].setMaxForwardSpeed(maxForwardSpeed);
        tires[1].setMaxBackwardSpeed(maxBackwardSpeed);

        drill = new Drill(body, gameWorld.getController(), 4f, 3f, 0, 1.7f);
//
//        body.setFixedRotation(true);
//        body.setTransform(rect.x, rect.y, -30f * MathUtils.degreesToRadians);

    }

    @Override
    public void update() {
        super.update();
        Debug.log("Player Position: (", body.getPosition().x + " , " + body.getPosition().y + ")");
        Debug.log("Player Velocity: ", body.getLinearVelocity().len());
        updateFriction();

    }

    @Override
    public void updateDrive(Array<ControlAction> actions, float delta) {
        int right = 0, left = 0;
        boolean isDrilling = false;

        ControlAction rightAction = ControlAction.DO_NOTHING;
        ControlAction leftAction = ControlAction.DO_NOTHING;
        for (ControlAction action : actions) {
            switch (action) {
                case UP:
                    right++;
                    left++;
                    break;
                case DOWN:
                    right--;
                    left--;
                    break;
                case LEFT:
                    right++;
                    left--;
                    break;
                case RIGHT:
                    right--;
                    left++;
                    break;
                case SPACE:
                    isDrilling = true;
                    break;
            }
        }

        if (right > 0) rightAction = ControlAction.UP;
        else if (right < 0) rightAction = ControlAction.DOWN;

        if (left > 0) leftAction = ControlAction.UP;
        else if (left < 0) leftAction = ControlAction.DOWN;

        tires[1].update(rightAction);
        tires[0].update(leftAction);


        if (isDrilling) {
            drill.setDrilling(true);
        } else {
            drill.setDrilling(false);
        }
        drill.update(delta);
    }

    public float getRightTrackSpeed() {
        return tires[1].getForwardSpeed();
    }

    public float getLeftTrackSpeed() {
        return tires[0].getForwardSpeed();
    }
}
