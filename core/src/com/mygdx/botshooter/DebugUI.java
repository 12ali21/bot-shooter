package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

public class DebugUI {
    private static Stage stage;
    private static SpriteBatch batch;
    private static Label fpsLabel;
    private static Label bindsLabel;
    private static Label drawsLabel;
    private static Table table;
    private static Label.LabelStyle style;
    private static float timeBuffer = 0;
    private static GLProfiler profiler;
    private static FPSLogger logger;
    private static HashMap<String, Label> logs;

    private static final String FPS_TAG = "FPS";
    private static final String BINDS_TAG = "binds";
    private static final String DRAWS_TAG = "draws";



    static {
        logger = new FPSLogger();

        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        stage = new Stage(new ScreenViewport());
        style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        table = new Table();
        table.top().left();
        table.setFillParent(true);

        logs = new HashMap<>();

        log(FPS_TAG, "");
        log(BINDS_TAG, "");
        log(DRAWS_TAG, "");

        stage.addActor(table);
    }

    public static void drawPoint(Batch batch, Camera camera, int x, int y) {
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(x, y, 0.1f, 90);
        shapeRenderer.end();
        batch.begin();
    }

    public static void log(String tag, String info) {
        Label label = logs.get(tag);
        if(label == null) {
            label = new Label("", style);
            addLabel(tag, label);
        }
        label.setText(tag+": "+info);
    }

    private static void addLabel(String tag,Label label) {
        table.add(label).left();
        table.row();
        logs.put(tag, label);
    }

    public static void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public static void render(float delta) {
        logger.log();
        timeBuffer += delta;
        if (timeBuffer > 1) {
            timeBuffer = 0;
            log(FPS_TAG, "" + (int) (1 / delta));
            log(BINDS_TAG, "" + profiler.getTextureBindings());
            log(DRAWS_TAG, "" + profiler.getDrawCalls());
            profiler.reset();

        }
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
