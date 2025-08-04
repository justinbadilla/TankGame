package com.sfsu.tankgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sfsu.tankgame.Screens.GameScreen;
import com.sfsu.tankgame.Screens.MenuScreen;

public class Main extends Game {
    public SpriteBatch batch;
    private Music music;

    @Override
    public void create() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("menu.wav"));
        music.play();
        music.setLooping(true);
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
        
    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
    }
}
