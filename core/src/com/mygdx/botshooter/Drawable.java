package com.mygdx.botshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Drawable {
    void render(Batch batch);
    void update(float delta);
}
