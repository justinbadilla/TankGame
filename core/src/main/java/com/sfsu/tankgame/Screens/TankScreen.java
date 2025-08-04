package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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


public class TankScreen implements Screen{

    final Main game;
    private SpriteBatch batch;

    private TiledMap background;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    //array of tanks
    private Texture[] tankTextures;
    //player choice (using array)
    private int playerOneChoice;
    private int playerTwoChoice;

    //button textures and coords
    private Texture nextButton;
    private Texture previewButton;
    private Texture continueButton;
    private float continueX;
    private float continueY;
    private Rectangle continueHitBox;
    //Player One buttons
    private float nextOneX;
    private float nextOneY;
    private float prevOneX;
    private float prevOneY;
    //Player Two Buttons
    private float nextTwoX;
    private float nextTwoY;
    private float prevTwoX;
    private float prevTwoY;
    //tank one display
    private float tankOneDisplayX;
    private float tankOneDisplayY;
    //tank two display
    private float tankTwoDisplayX;
    private float tankTwoDisplayY;

    //button hitboxes
    //for P1
    Rectangle nextOneHitBox;
    Rectangle prevOneHitBox;
    //forP2
    Rectangle nextTwoHitBox;
    Rectangle prevTwoHitBox;

    //mouse
    float mouseX;
    float mouseY;

    private Viewport viewport;

    public TankScreen(Main game){
        this.game = game;
        viewport = new FitViewport(1600, 900, new OrthographicCamera());

        //background and buttons
        background = new TmxMapLoader().load("menu/menuBackground.tmx");
        nextButton = new Texture("menu/next.png");
        previewButton = new Texture("menu/previous.png");
        continueButton = new Texture("menu/continue.png");

        //button "hitboxes"
        //next button for player one
        MapObject nextOneObject = background.getLayers().get("nextOne").getObjects().get(0);
        Rectangle nextRectOne = ((RectangleMapObject) nextOneObject).getRectangle();
        nextOneX = nextRectOne.x;
        nextOneY = nextRectOne.y;
        nextOneHitBox = new Rectangle(nextOneX-100, nextOneY-75, nextRectOne.getWidth(), nextRectOne.getHeight());
        //prev button for player one
        MapObject prevOneObject = background.getLayers().get("prevOne").getObjects().get(0);
        Rectangle prevRectOne = ((RectangleMapObject) prevOneObject).getRectangle();
        prevOneX = prevRectOne.x;
        prevOneY = prevRectOne.y;
        prevOneHitBox = new Rectangle(prevOneX-70, prevOneY-70, prevRectOne.getWidth(), prevRectOne.getHeight());
        //next button for player two
        MapObject nextTwoObject = background.getLayers().get("nextTwo").getObjects().get(0);
        Rectangle nextRectTwo = ((RectangleMapObject) nextTwoObject).getRectangle();
        nextTwoX = nextRectTwo.x;
        nextTwoY = nextRectTwo.y;
        nextTwoHitBox = new Rectangle(nextTwoX-100, nextTwoY-30, nextRectTwo.getWidth(), nextRectTwo.getHeight());
        //prev button for player two
        MapObject prevTwoObject = background.getLayers().get("prevTwo").getObjects().get(0);
        Rectangle prevRectTwo = ((RectangleMapObject) prevTwoObject).getRectangle();
        prevTwoX = prevRectTwo.x;
        prevTwoY = prevRectTwo.y;
        prevTwoHitBox = new Rectangle(prevTwoX-70, prevTwoY-30, prevRectTwo.getWidth(), prevRectTwo.getHeight());
        //tank one display
        MapObject tankOneObject = background.getLayers().get("TankOne").getObjects().get(0);
        Rectangle tankOneRect = ((RectangleMapObject) tankOneObject).getRectangle();
        tankOneDisplayX = tankOneRect.x;
        tankOneDisplayY = tankOneRect.y;
        //tank two display
        MapObject tankTwoObject = background.getLayers().get("TankTwo").getObjects().get(0);
        Rectangle tankTwoRect = ((RectangleMapObject) tankTwoObject).getRectangle();
        tankTwoDisplayX = tankTwoRect.x;
        tankTwoDisplayY = tankTwoRect.y;
        //continue
        MapObject continueObject = background.getLayers().get("Continue").getObjects().get(0);
        Rectangle continueRect = ((RectangleMapObject) continueObject).getRectangle();
        continueX = continueRect.x;
        continueY = continueRect.y;
        continueHitBox = new Rectangle(continueX, continueY, continueRect.getWidth(), continueRect.getHeight());

        //tank array (with texture and image)
        tankTextures = new Texture[4];
        tankTextures[0] = new Texture("red tank.png");
        tankTextures[1] = new Texture("blue tank.png");
        tankTextures[2] =new Texture("grey tank.png");
        tankTextures[3] = new Texture("navy tank.png");
        //choice
        playerOneChoice = 0;
        playerTwoChoice = 0;

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

        //mouse
        mouseX = Gdx.input.getX();
        mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        //button print
        batch.draw(nextButton, nextOneX - 100, nextOneY);
        batch.draw(previewButton, prevOneX-100, prevOneY);
        batch.draw(nextButton, nextTwoX-100, nextTwoY);
        batch.draw(previewButton, prevTwoX-100, prevTwoY);
        batch.draw(continueButton, continueX+60, continueY);

        //show initial tanks
        batch.draw(tankTextures[playerOneChoice], tankOneDisplayX+40, tankOneDisplayY+45);
        batch.draw(tankTextures[playerTwoChoice], tankTwoDisplayX+40, tankTwoDisplayY+45);

        //button logic
        if(Gdx.input.justTouched()){
            //player1
            if(nextOneHitBox.contains(mouseX, mouseY)){
                playerOneChoice ++;
                if(playerOneChoice >= tankTextures.length){
                    playerOneChoice = 0;
                }
            }
            if(prevOneHitBox.contains(mouseX, mouseY)){
                playerOneChoice--;
                if(playerOneChoice <0){
                    playerOneChoice = tankTextures.length-1;
                }
            }
            //player2
            if(nextTwoHitBox.contains(mouseX, mouseY)){
                playerTwoChoice ++;
                if(playerTwoChoice >= tankTextures.length){
                    playerTwoChoice = 0;
                }
            }
            if(prevTwoHitBox.contains(mouseX, mouseY)){
                playerTwoChoice--;
                if(playerTwoChoice <0){
                    playerTwoChoice = tankTextures.length-1;
                }
            }
            if(continueHitBox.contains(mouseX, mouseY)){
                game.setScreen(new MapScreen(tankTextures[playerOneChoice], tankTextures[playerTwoChoice], game));
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
        batch.dispose();
        background.dispose();;
        nextButton.dispose();
        previewButton.dispose();

    }
    
}
