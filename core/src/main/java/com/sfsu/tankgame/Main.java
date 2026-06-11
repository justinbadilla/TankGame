package com.sfsu.tankgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.sfsu.tankgame.Screens.MenuScreen;

public class Main extends Game {
    //game assets
    public Texture breakableWallTexture;
    public Texture bulletTexture;
    public Texture redTank;
    public Texture blueTank;
    public Texture greyTank;
    public Texture navyTank;
    public TiledMap mapOne;
    public TiledMap mapTwo;
    public TiledMap mapThree;
    public Texture mapOnePreview;
    public Texture mapTwoPreview;
    public Texture mapThreePreview;

    public SpriteBatch batch;

    //sound assets
    public Sound gunshotSound;
    public Sound clickSound;
    public Music menuMusic;
    public Music gameMusic;

    @Override
    public void create() {
        //game assets
        breakableWallTexture = new Texture("breakable.png");
        bulletTexture = new Texture("bullet.jpg");
        gunshotSound = Gdx.audio.newSound(Gdx.files.internal("shotfiring.wav"));
        redTank = new Texture("red tank.png");
        blueTank = new Texture("blue tank.png");
        greyTank = new Texture("grey tank.png");
        navyTank = new Texture("navy tank.png");
        mapOne = new TmxMapLoader().load("maps/map 1.tmx");
        mapTwo = new TmxMapLoader().load("maps/map 2.tmx");
        mapThree = new TmxMapLoader().load("maps/map 3.tmx");
        mapOnePreview = new Texture("maps/map 1.png");
        mapTwoPreview = new Texture("maps/map 2.png");
        mapThreePreview = new Texture("maps/map 3.png");

        batch = new SpriteBatch();
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.wav"));
        menuMusic.setLooping(true);
        //game music
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("8-bit When You Sleep.mp3"));
        gameMusic.setLooping(true);
        clickSound = Gdx.audio.newSound(
            Gdx.files.internal("ClickSound.mp3")
        );

        menuMusic.play();

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
        
    }

    @Override
    public void dispose() {

        if (getScreen() != null) {
            getScreen().dispose();
        }

        batch.dispose();
        clickSound.dispose();

        if(menuMusic != null){
            menuMusic.dispose();
        }
        if(gameMusic != null){
            gameMusic.dispose();
        }

        if (breakableWallTexture != null) {
            breakableWallTexture.dispose();
        }

        if (bulletTexture != null) {
            bulletTexture.dispose();
        }

        if (gunshotSound != null) {
            gunshotSound.dispose();
        }

        //tank assets
        redTank.dispose();
        blueTank.dispose();
        greyTank.dispose();
        navyTank.dispose();

        //map assets
        mapOne.dispose();
        mapTwo.dispose();
        mapThree.dispose();
        mapOnePreview.dispose();
        mapTwoPreview.dispose();
        mapThreePreview.dispose();
    }
}
