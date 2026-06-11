package com.sfsu.tankgame.gameobjects;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected float x, y;
    protected float angle;
    protected Rectangle hitbox;
    
    public GameObject(float x, float y){
        this.x = x;
        this.y = y;
        this.angle = 0;
        this.hitbox = new Rectangle(x, y, 0, 0);

    }

    public abstract void update(float delta, List<GameObject>allObjects);
    public abstract void draw(SpriteBatch batch);

    public Rectangle getHitBox(){
        return hitbox;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
