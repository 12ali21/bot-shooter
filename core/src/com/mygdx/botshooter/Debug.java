package com.mygdx.botshooter;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

public class Debug {
    private static Stage stage;
    private static Table table;
    private static Label.LabelStyle style;
    private static float timeBuffer = 0;
    private static GLProfiler profiler;
    private static FPSLogger logger;
    private static HashMap<String, Label> logs;
    private static HashMap<String, Rectangle> rects;
    private static Runtime runtime;

    private static final String FPS_TAG = "FPS";
    private static final String BINDS_TAG = "binds";
    private static final String DRAWS_TAG = "draws";
    private static Camera gameCamera;
    private static ShapeRenderer shapeRenderer;



    static {
        logger = new FPSLogger();
        runtime = Runtime.getRuntime();

        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        stage = new Stage(new ScreenViewport());
        style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        table = new Table();
        table.top().left();
        table.setFillParent(true);

        logs = new HashMap<>();
        rects = new HashMap<>();
        shapeRenderer = new ShapeRenderer();

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

    public static void drawRect(String tag, int x, int y, int width, int height) {
        drawRect(tag, new Rectangle(x, y, width, height));
    }
    public static void drawRect(String tag, Rectangle rectangle) {
        Rectangle rect = rects.get(tag);
        if(rect == null) {
            rect = new Rectangle(rectangle);
        } else {
            rect.set(rectangle);
        }
        rects.put(tag, rect);
    }

    public static void log(String tag, Object info) {
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

    public static void setCamera(Camera camera) {
        gameCamera = camera;
    }

    public static void render(float delta) {
        logger.log();
        timeBuffer += delta;
        if (timeBuffer >= 1) {
            timeBuffer = 0;
            log("Memory", "" + (runtime.totalMemory() - runtime.freeMemory()) / 1048576 + "MB / "+ runtime.maxMemory() / 1048576 + "MB");
            log(FPS_TAG, "" + (int) (1 / delta));
            log(BINDS_TAG, "" + profiler.getTextureBindings());
            log(DRAWS_TAG, "" + profiler.getDrawCalls());
            profiler.reset();
        }

        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
        stage.getBatch().end();

        if(gameCamera != null) {
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (String tag : rects.keySet()) {
                Rectangle rect = rects.get(tag);
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            shapeRenderer.end();

            rects.clear();
        }
    }

    public void dispose() {
        stage.dispose();
    }
}
