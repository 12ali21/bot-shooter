package com.mygdx.botshooter.world.characters.car;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.utils.Array;
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
                new Vector2(-rect.width / 2 + 0.2f,rect.height / 2 - 1.4f),
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

        float wheelWidth = 0.1f;
        float wheelHeight = 1.8f;

        tires[0] = new Tire(gameWorld.getWorld(), body.getMass(), wheelWidth, wheelHeight, 80);
        tires[1] = new Tire(gameWorld.getWorld(), body.getMass(), wheelWidth, wheelHeight, 80);
        // front left joint
        frontLeftJoint = (RevoluteJoint) createRevoluteJoint(gameWorld.getWorld(), tires[0].body, -rect.width / 2 + .5f, rect.height / 2 - 3f);

        // front right joint
        frontRightJoint = (RevoluteJoint) createRevoluteJoint(gameWorld.getWorld(), tires[1].body, rect.width / 2 - .5f, rect.height / 2 - 3f);

        float friction = 0.7f;
        float airDrag = 0.2f;
        float angularFriction = 0.25f;

        tires[0].setAirDrag(airDrag);
        tires[0].setFriction(friction);
        tires[0].setAngularFriction(angularFriction);
        tires[0].setMaxDriveForce(2100);
        tires[0].setMaxForwardSpeed(35);
        tires[0].setMaxBackwardSpeed(-30);

        tires[1].setAirDrag(airDrag);
        tires[1].setFriction(friction);
        tires[1].setAngularFriction(angularFriction);
        tires[1].setMaxDriveForce(2100);
        tires[1].setMaxForwardSpeed(35);
        tires[1].setMaxBackwardSpeed(-30);

        drill = new Drill(body, gameWorld.getController(), 4f,3f, 0, 1.7f);
//
//        body.setFixedRotation(true);
//        body.setTransform(rect.x, rect.y, -30f * MathUtils.degreesToRadians);

    }

    private Vector2 getForwardVelocity() {
        // get the vector from the center to the top of the tire in world
        Vector2 upNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
        upNormal.nor();
        // projection of linear velocity on up facing vector
        upNormal.scl(upNormal.dot(body.getLinearVelocity()));
        return upNormal;
    }

    private void applyDrillForce() {
        body.applyForceToCenter(getForwardVelocity().scl(-2000f), true);
    }

    @Override
    public void updateDrive(Array<ControlAction> actions, float delta) {
        int right = 0, left = 0;
        boolean isDrilling = false;

        ControlAction rightAction = ControlAction.DO_NOTHING;
        ControlAction leftAction = ControlAction.DO_NOTHING;
        for (ControlAction action : actions) {
            switch (action) {
                case DRIVE_FORWARD:
                    right++;
                    left++;
                    break;
                case DRIVE_BACKWARD:
                    right--;
                    left--;
                    break;
                case TURN_LEFT:
                    right++;
                    left--;
                    break;
                case TURN_RIGHT:
                    right--;
                    left++;
                    break;
                case DRILL:
                    isDrilling = true;
                    break;
            }
        }

        if (right > 0) rightAction = ControlAction.DRIVE_FORWARD;
        else if (right < 0) rightAction = ControlAction.DRIVE_BACKWARD;

        if (left > 0) leftAction = ControlAction.DRIVE_FORWARD;
        else if (left < 0) leftAction = ControlAction.DRIVE_BACKWARD;

        tires[1].update(rightAction);
        tires[0].update(leftAction);


        if(isDrilling) {
            applyDrillForce();
            drill.setDrilling(true);
        } else {
            drill.setDrilling(false);
        }

        updateFriction();
        update();
    }

    public float getRightTrackSpeed() {
        return tires[1].getForwardSpeed();
    }

    public float getLeftTrackSpeed() {
        return tires[0].getForwardSpeed();
    }
}
