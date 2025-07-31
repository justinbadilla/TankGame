package com.sfsu.tankgame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sfsu.tankgame.gameobjects.GameObject;
import com.sfsu.tankgame.gameobjects.Tank;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture image;
    private Tank tankOne;
    private Tank tankTwo;

    private List<GameObject>allObjects;

    private ControlScheme playerOneControls = new ControlScheme(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE);
    private ControlScheme playerTwoControls = new ControlScheme(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.ENTER);

    @Override
    public void create() {

        allObjects = new ArrayList<>();
        batch = new SpriteBatch();
        image = new Texture("red tank.png");

        tankOne = new Tank(image, 0, 0, playerOneControls);
        tankTwo = new Tank (image, 100, 100, playerTwoControls);

        allObjects.add(tankOne);
        allObjects.add(tankTwo);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();

        tankOne.draw(batch);
        tankTwo.draw(batch);

        tankOne.update(delta, allObjects);
        tankTwo.update(delta, allObjects);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
