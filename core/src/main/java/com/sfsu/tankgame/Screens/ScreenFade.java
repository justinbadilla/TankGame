package com.sfsu.tankgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScreenFade {

    private ShapeRenderer shapeRenderer;
    private float timer = 0f;
    private float duration;

    public ScreenFade(float duration) {
        this.duration = duration;
        shapeRenderer = new ShapeRenderer();
    }

    public void renderFadeIn(float delta, OrthographicCamera camera, float worldWidth, float worldHeight) {
        if (timer >= duration) {
            return;
        }

        timer += delta;

        float alpha = 1f - timer / duration;
        alpha = Math.max(0f, alpha);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, alpha);
        shapeRenderer.rect(0, 0, worldWidth, worldHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}