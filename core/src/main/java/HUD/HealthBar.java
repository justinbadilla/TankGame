package HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sfsu.tankgame.gameobjects.Tank;

public class HealthBar {

    Tank tank;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public HealthBar (Tank tank){
        this.tank = tank;

    }
    

    public void render(SpriteBatch batch, int health, float x, float y){
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

    shapeRenderer.setColor(Color.GREEN);

    float width = 500;
    float fullHealth = 200;
    float healthPercent = fullHealth/health;

    shapeRenderer.rect(x, y, width/healthPercent, 20); 
    shapeRenderer.end();

    }
}
