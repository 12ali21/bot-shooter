package com.mygdx.botshooter.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

class Renderer {
    public static final String MOUNTAIN_LAYER = "mountain.layer";
    public static final String GROUND_LAYER = "ground.layer";

    private TiledMap map;
    private float unitScale;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TextureRegion backgroundTexture;

    public Renderer(TiledMap map, float unitScale, OrthographicCamera camera) {
        this.map = map;
        this.unitScale = unitScale;
        this.camera = camera;

        backgroundTexture = TextureRegion.split(new Texture(Gdx.files.internal("tiles.png")), 32,32)[0][1];

        batch = new SpriteBatch();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    }

    public void setView(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void render(){

        int counter = 0;
        Frustum frustum = camera.frustum;
        MapLayer ground = map.getLayers().get(GROUND_LAYER);
        TiledMapTileLayer mountain = (TiledMapTileLayer) map.getLayers().get(MOUNTAIN_LAYER);
        TiledMapTileLayer.Cell cell;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        int y1 = (int)(camera.position.y - camera.viewportHeight/2 - 1);
        int y2 = (int)(camera.position.y + camera.viewportHeight/2 + 1);
        int x1 = (int)(camera.position.x - camera.viewportWidth/2 - 1);
        int x2 = (int)(camera.position.x + camera.viewportWidth/2 + 1);


        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++){
                cell = mountain.getCell(x, y);
                if (cell != null) {
                    counter++;
                    batch.draw(cell.getTile().getTextureRegion(), x, y, 1, 1);
                } else {
                    batch.draw(backgroundTexture, x, y, 1, 1);
                }
            }
        }
        System.out.println(counter);
        batch.end();
    }
}
