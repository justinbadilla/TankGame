package com.sfsu.tankgame.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends GameObject {

    public Wall(Rectangle hitbox){
        super(hitbox.x, hitbox.y);
        this.hitbox = hitbox;
    }

    @Override
    public void update(float delta){
        update(delta, new ArrayList<>());
    }

    public void update(float delta, List<GameObject>allObjects){

    }

    @Override
    public void draw(SpriteBatch batch){
        
    }
}
