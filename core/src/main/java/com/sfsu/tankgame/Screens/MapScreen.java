package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sfsu.tankgame.Main;


public class MapScreen implements Screen{

    final Main game;

    private SpriteBatch batch;

    private TiledMap background;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    Texture playerOneTank;
    Texture playerTwoTank;

    float mouseX;
    float mouseY;

    private Viewport viewport;

    public MapScreen(Texture playerOneTank, Texture playerTwoTank, Main game){
        this.playerOneTank = playerOneTank;
        this.playerTwoTank = playerTwoTank;
        this.game = game;
        viewport = new FitViewport(1600, 900, new OrthographicCamera());

        background = new TmxMapLoader().load("menu/menuBackground.tmx");
        
        renderer = new OrthogonalTiledMapRenderer(background);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        renderer.setView(camera);

        batch = new SpriteBatch();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        camera.update();
        renderer.setView(camera);
        renderer.render();

        //mouse things
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.end();;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.setToOrtho(false, viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
;
    }
}
