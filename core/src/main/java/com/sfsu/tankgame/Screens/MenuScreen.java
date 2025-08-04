package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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

public class MenuScreen implements Screen{
    
    private final Main game;
    private Sound clickSound;
    private SpriteBatch batch;

    private TiledMap background;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    //button textures and coords
    private Texture logo;
    private float logoX;
    private float logoY;
    private Texture playButton;
    private Texture currentPlayButton;
    private float playX;
    private float playY;
    private Texture playButtonClick;
    private Texture exitButton;
    private Texture currentExitButton;
    private float exitX;
    private float exitY;
    private Texture exitButtonClick;

    //button "hitboxes"
    private Rectangle playHitbox;
    private Rectangle exitHitbox;


    //animation fields
    private boolean showButtons = false;
    private float delayButtonTimer;
    private float delayLogoTimer;
    private float logoTargetY;
    private float logoSpeed = 200f;

    //mouse
    float mouseX;
    float mouseY;

    private Viewport viewport;
    

    public MenuScreen(Main game){
        this.game = game;
        viewport = new FitViewport(1600, 900, new OrthographicCamera());

        //background and buttons
        background = new TmxMapLoader().load("menu/menuBackground.tmx");
        logo = new Texture(Gdx.files.internal("menu/logoTrans.png"));

        playButton = new Texture("menu/play.png");
        playButtonClick = new Texture("menu/playclick.png");
        exitButton = new Texture("menu/exit.png");
        exitButtonClick = new Texture("menu/exitclick.png");

        MapObject logoObject = background.getLayers().get("Logo").getObjects().get(0);
        Rectangle logoRect = ((RectangleMapObject) logoObject).getRectangle();
        logoX = logoRect.x;
        logoY = logoRect.y;
        //animation
        logoTargetY = logoY;
        logoY -= 200;


        MapObject playObject = background.getLayers().get("Play").getObjects().get(0);
        Rectangle playRect = ((RectangleMapObject) playObject).getRectangle();
        playX = playRect.x;
        playY = playRect.y;
        playHitbox = new Rectangle(playX-60, playY-60, playRect.getWidth()+20, playRect.getHeight()+20);

        MapObject exitObject = background.getLayers().get("Exit").getObjects().get(0);
        Rectangle exitRect = ((RectangleMapObject) exitObject).getRectangle();
        exitX = exitRect.x;
        exitY = exitRect.y;
        exitHitbox = new Rectangle(exitX, exitY, exitButton.getWidth(), exitButton.getHeight());

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
        //frames (beginning logo panning idea)
        camera.update();
        renderer.setView(camera);
        renderer.render();

        //mouse things
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        //timer for button animations
        delayButtonTimer += delta;
        if (delayButtonTimer >= 5f){
            showButtons = true;
        }
        
        if(playHitbox.contains(mouseX, mouseY)){
            currentPlayButton = playButtonClick;
        }else{
            currentPlayButton = playButton;
        }

        if(exitHitbox.contains(mouseX, mouseY)){
            currentExitButton = exitButtonClick;
        }else{
            currentExitButton = exitButton;
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        delayLogoTimer += delta;
        if(delayLogoTimer >= 4f){
            if (logoY < logoTargetY){
                logoY += logoSpeed * delta;
                if(logoY > logoTargetY){
                    logoY = logoTargetY;
                }
            }
        }
        batch.draw(logo, logoX, logoY, logo.getWidth(),logo.getHeight());

        if(showButtons == true){
            batch.draw(currentPlayButton, playX, playY);
            batch.draw(currentExitButton, exitX, exitY);
        }

        if(showButtons && Gdx.input.justTouched()){
            if(playHitbox.contains(mouseX, mouseY)){
                game.setScreen(new TankScreen(game));
            }
            if(exitHitbox.contains(mouseX, mouseY)){
                Gdx.app.exit();
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
        logo.dispose();
        playButton.dispose();
        exitButton.dispose();
        playButtonClick.dispose();
        exitButtonClick.dispose();
        batch.dispose();
    }
}
