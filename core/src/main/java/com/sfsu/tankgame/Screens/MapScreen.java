package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sfsu.tankgame.Main;
import com.sfsu.tankgame.Maps;


public class MapScreen implements Screen{

    final Main game;

    private SpriteBatch batch;

    private TiledMap background;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    //parameters of player tanks (pass on to gamescreen)
    Texture playerOneTank;
    Texture playerTwoTank;

    //button textures and coords
    //(next)
    private Texture nextButton;
    private float nextX;
    private float nextY;
    //(prev)
    private Texture previousButton;
    private float prevX;
    private float prevY;
    //(continue)
    private Texture continueButton;
    private float continueX;
    private float continueY;
    private Rectangle continueHitBox;

    //map list
    TiledMap mapOne;
    TiledMap mapTwo;
    TiledMap mapThree;
    int mapChoice;
    Maps[] mapList;
    //display map png
    private float mapX;
    private float mapY;

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

        //maplist logic
        mapOne = new TmxMapLoader().load("maps/map 1.tmx");
        mapTwo = new TmxMapLoader().load("maps/map 2.tmx");
        mapThree = new TmxMapLoader().load("maps/map 3.tmx");
        mapList = new Maps[3];
        mapList [0] = new Maps(mapOne, new Texture("maps/map 1.png"));
        mapList [1] = new Maps(mapTwo, new Texture("maps/map 2.png"));
        mapList [2] = new Maps (mapThree, new Texture("maps/map 3.png"));
        //hovering choice
        mapChoice = 0;

        //buttons
        //next buttion
        nextButton = new Texture("menu/next.png");
        MapObject nextButtonObject = background.getLayers().get("nextMap").getObjects().get(0);
        Rectangle nextButtonRect = ((RectangleMapObject) nextButtonObject).getRectangle();
        nextX = nextButtonRect.x;
        nextY = nextButtonRect.y;
        //previous button
        previousButton = new Texture("menu/previous.png");
        MapObject prevButtonObject = background.getLayers().get("previousMap").getObjects().get(0);
        Rectangle prevButtonRect = ((RectangleMapObject) prevButtonObject).getRectangle();
        prevX = prevButtonRect.x;
        prevY = prevButtonRect.y;
        //continue button
        continueButton = new Texture("menu/continue.png");
        MapObject continueObject = background.getLayers().get("Continue").getObjects().get(0);
        Rectangle continueRect = ((RectangleMapObject) continueObject).getRectangle();
        continueX = continueRect.x;
        continueY = continueRect.y;
        continueHitBox = new Rectangle(continueX, continueY, continueRect.getWidth(), continueRect.getHeight());

        //display map
        MapObject mapObject = background.getLayers().get("mapImage").getObjects().get(0);
        Rectangle mapRect = ((RectangleMapObject) mapObject).getRectangle();
        mapX = mapRect.x;
        mapY = mapRect.y;

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
        batch.draw(nextButton, nextX-100, nextY);
        batch.draw(previousButton, prevX-100, prevY);
        batch.draw(continueButton, continueX+60, continueY);

        //initial map
        batch.draw(mapList[mapChoice].getTexture(), mapX-70, mapY, mapList[mapChoice].getTexture().getWidth()/2, mapList[mapChoice].getTexture().getHeight()/2);

        //buttons
        if(Gdx.input.isKeyJustPressed(Keys.RIGHT)){
            mapChoice++;
            if(mapChoice >= mapList.length){
                    mapChoice = 0;
            }
        }
        if(Gdx.input.isKeyJustPressed(Keys.LEFT)){
            mapChoice --;
            if(mapChoice < 0){
                mapChoice = mapList.length - 1;
            }
        }
        if(Gdx.input.justTouched()){
            if(continueHitBox.contains(mouseX, mouseY)){
                game.setScreen(new GameScreen(game, mapList[mapChoice].getMap(), playerOneTank, playerTwoTank));
            }
        }

        batch.end();
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
