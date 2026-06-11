package com.sfsu.tankgame.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;



public class BreakableWall extends GameObject {

    private Texture breakableWallTexture;

    private int health = 10;
    private boolean destroyed = false;

    public BreakableWall(Rectangle hitbox, Texture breakableWallTexture){
        super(hitbox.x, hitbox.y);
        this.hitbox = hitbox;
        this.breakableWallTexture = breakableWallTexture;
    }

    @Override
    public void update(float delta, List<GameObject>allObjects){

    }

    public void takeDamage(int amount){
        health -= amount;
        if (health <= 0){
            destroyed = true;
        }
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(!destroyed){

            batch.draw(
            breakableWallTexture,
            hitbox.x, hitbox.y
            );
        }
        
    }   

}
