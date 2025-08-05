package com.sfsu.tankgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;


public class Maps {

    private TiledMap map;
    private Texture texture;

    public Maps(TiledMap map, Texture texture){
        this.map = map;
        this.texture = texture;

    }

    public TiledMap getMap() {
        return map;
    }

    public Texture getTexture() {
        return texture;
    }
}
