package com.sfsu.tankgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sfsu.tankgame.Screens.EndScreen;
import com.sfsu.tankgame.Screens.GameScreen;
import com.sfsu.tankgame.Screens.MenuScreen;
import com.sfsu.tankgame.Screens.TankScreen;

public class Main extends Game {
    public SpriteBatch batch;
    public Music menuMusic;

    @Override
    public void create() {
        batch = new SpriteBatch();
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu.wav"));
        menuMusic.setLooping(true);
        if (!menuMusic.isPlaying()) {
            menuMusic.play();
        }

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
        
    }

    @Override
    public void dispose() {
        batch.dispose();
        if(menuMusic != null){
            menuMusic.dispose();
        }
    }
}
