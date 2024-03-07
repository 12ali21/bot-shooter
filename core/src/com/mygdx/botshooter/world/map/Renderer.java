package com.mygdx.botshooter.world.map;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.logging.FileHandler;

class Renderer {
    public static final String MOUNTAIN_LAYER = "mountain.layer";
    public static final String GROUND_LAYER = "ground.layer";

    private TiledMap map;
    private float unitScale;
    private OrthographicCamera camera;
//    private TextureRegion backgroundTexture;

    public Renderer(TiledMap map, float unitScale, OrthographicCamera camera) {
        this.map = map;
        this.unitScale = unitScale;
        this.camera = camera;

//        backgroundTexture = TextureRegion.split(new Texture(Gdx.files.internal("tiles.png")), 32,32)[0][1];

//        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }

    public void setView(OrthographicCamera camera) {
        this.camera = camera;
    }

    private Vector2[] getCameraBounds(Camera camera) {
        return new Vector2[]{
                new Vector2(
                        (int) (camera.position.x - camera.viewportWidth / 2 - 5),
                        (int) (camera.position.y - camera.viewportHeight / 2 - 5)),
                new Vector2(
                        (int) (camera.position.x + camera.viewportWidth / 2 + 5),
                        (int) (camera.position.y + camera.viewportHeight / 2 + 5))
        };
    }

    private void renderLayer(TiledMapTileLayer layer, Batch batch, Vector2[] bounds) {
        TiledMapTileLayer.Cell cell;


        for (int y = (int) bounds[0].y; y < bounds[1].y; y++) {
            for (int x = (int) bounds[0].x; x < bounds[1].x; x++) {
                cell = layer.getCell(x, y);
                if (cell != null) {
                    batch.draw(cell.getTile().getTextureRegion(), x, y, unitScale, unitScale);
                }
            }
        }
    }

    public void renderGround(Batch batch) {
        TiledMapTileLayer ground = (TiledMapTileLayer) map.getLayers().get(GROUND_LAYER);
        renderLayer(ground, batch, getCameraBounds(camera));
    }

    public void renderMountain(Batch batch) {
        TiledMapTileLayer mountain = (TiledMapTileLayer) map.getLayers().get(MOUNTAIN_LAYER);
        renderLayer(mountain, batch, getCameraBounds(camera));
    }
    public TextureRegion renderMapTexture(Batch batch, Camera mapCamera) {
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, 2048, 2048, false);
        Vector2[] bounds = getCameraBounds(mapCamera);
        buffer.begin();
        renderLayer((TiledMapTileLayer) map.getLayers().get(GROUND_LAYER), batch, bounds);
        renderLayer((TiledMapTileLayer) map.getLayers().get(MOUNTAIN_LAYER), batch, bounds);
        buffer.end();

        Texture texture = buffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);

        return region;
    }
}
