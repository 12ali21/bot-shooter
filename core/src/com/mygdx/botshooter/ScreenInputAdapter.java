package com.mygdx.botshooter;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;

public class ScreenInputAdapter extends InputAdapter {

    public static final float FOVLimitLow = 50;
    public static final float FOVLimitHigh = 128;
    public static final float FOVChangeSpeed = 5f;

    private float FOV = FOVLimitLow;
    private boolean FOVChanged = false;

    @Override
    public boolean scrolled(float amountX, float amountY) {
        FOV += amountY * FOVChangeSpeed;
        FOV = MathUtils.clamp(FOV, FOVLimitLow, FOVLimitHigh);
        FOVChanged = true;
        return true;
    }

    public boolean isFOVChanged() {
        return FOVChanged;
    }

    public float getFOV() {
        FOVChanged = false;
        return FOV;
    }
}
