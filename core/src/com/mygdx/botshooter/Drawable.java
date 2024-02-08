package com.mygdx.botshooter;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Drawable {
    void render(Batch batch);
    void update(float delta);
}
