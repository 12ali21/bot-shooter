package com.mygdx.botshooter.characters.car;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

public class TrackCar extends Car {
    public TrackCar(World world, Rectangle rect) {
        super(world, rect);
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
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        fixtureDef.density = 5f;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();

        float wheelWidth = 0.1f;
        float wheelHeight = 2f;


        // front left joint
        frontLeftJoint = (RevoluteJoint) createTireJoint(world, tires[0] = new Tire(world, body.getMass(), wheelWidth, wheelHeight, 80), -rect.width / 2 + .5f, rect.height / 2 - 3f);

        // front right joint
        frontRightJoint = (RevoluteJoint) createTireJoint(world, tires[1] = new Tire(world, body.getMass(), wheelWidth, wheelHeight, 80), rect.width / 2 - .5f, rect.height / 2 - 3f);

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
    }

    @Override
    public void updateDrive(Array<ControlAction> actions) {
        int right = 0, left = 0;
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
            }

            if (right > 0) tires[1].driveForward();
            else if (right < 0) tires[1].driveBackward();

            if (left > 0) tires[0].driveForward();
            else if (left < 0) tires[0].driveBackward();
        }

        updateFriction();
    }
}
