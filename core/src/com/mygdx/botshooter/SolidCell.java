package com.mygdx.botshooter;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class SolidCell extends TiledMapTileLayer.Cell implements Solid {
    private float maxHP;
    public SolidCell(float hp) {
        this.maxHP = hp;
    }
}
