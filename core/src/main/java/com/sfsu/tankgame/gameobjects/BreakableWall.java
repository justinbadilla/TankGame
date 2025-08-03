package com.sfsu.tankgame.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;



public class BreakableWall extends GameObject {

    Texture breakableWall = new Texture("breakable.png");
    private int health = 10;
    private boolean destroyed = false;

    public BreakableWall(Rectangle hitbox){
        super(hitbox.x, hitbox.y);
        this.hitbox = hitbox;

    }

    @Override
    public void update(float delta) {
        update(delta, new ArrayList<>());
    }
    
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
            breakableWall,
            hitbox.x, hitbox.y
            );
        }
        
    }

    public void dispose(){
        breakableWall.dispose();
    }

    
    
}
