package com.sfsu.tankgame;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Maps {

    private TiledMap map;
    private Image image;

    public Maps(TiledMap map, Image image){
        this.map = map;
        this.image = image;

    }

    public TiledMap getMap() {
        return map;
    }

    public Image getImage() {
        return image;
    }
}
