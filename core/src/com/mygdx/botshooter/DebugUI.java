package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DebugUI {
    private Stage stage;
    private SpriteBatch batch;
    private Label fpsLabel;
    private Label bindsLabel;
    private Label drawsLabel;
    private Table table;
    private Label.LabelStyle style;
    private float timeBuffer = 0;
    private GLProfiler profiler;
    private FPSLogger logger;

    public DebugUI(Graphics graphics) {
        logger = new FPSLogger();

        profiler = new GLProfiler(graphics);
        profiler.enable();

        stage = new Stage(new ScreenViewport());
        style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        fpsLabel = new Label("", style);
        bindsLabel = new Label("", style);
        drawsLabel = new Label("", style);

        table = new Table();
        table.top().left();
        table.setFillParent(true);

        addLabel(fpsLabel);
        addLabel(bindsLabel);
        addLabel(drawsLabel);

        stage.addActor(table);
    }

    private void addLabel(Label label) {
        table.add(label).left();
        table.row();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        logger.log();
        timeBuffer += delta;
        if (timeBuffer > 1) {
            timeBuffer = 0;
            fpsLabel.setText("FPS: " + (int) (1 / delta));
            bindsLabel.setText("Bindings: " + profiler.getTextureBindings());
            drawsLabel.setText("Draws: " + profiler.getDrawCalls());
            profiler.reset();

        }
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
