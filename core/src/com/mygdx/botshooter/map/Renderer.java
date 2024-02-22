package com.mygdx.botshooter.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.botshooter.Debug;

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

    private Vector2[] getCameraBounds() {
        return new Vector2[]{
                new Vector2(
                        (int) (camera.position.x - camera.viewportWidth / 2 - 1),
                        (int) (camera.position.y - camera.viewportHeight / 2 - 1)),
                new Vector2(
                        (int) (camera.position.x + camera.viewportWidth / 2 + 1),
                        (int) (camera.position.y + camera.viewportHeight / 2 + 1))
        };
    }

    private void renderLayer(TiledMapTileLayer layer, Batch batch) {
        TiledMapTileLayer.Cell cell;
        batch.setProjectionMatrix(camera.combined);

        Vector2[] bounds = getCameraBounds();

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
        renderLayer(ground, batch);
    }

    public void renderMountain(Batch batch) {
        TiledMapTileLayer mountain = (TiledMapTileLayer) map.getLayers().get(MOUNTAIN_LAYER);
        renderLayer(mountain, batch);
    }
}
