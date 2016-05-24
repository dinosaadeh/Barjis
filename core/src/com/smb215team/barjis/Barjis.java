package com.smb215team.barjis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.smb215team.barjis.game.GameController;
import com.smb215team.barjis.game.GameRenderer;

public class Barjis extends ApplicationAdapter {
    private static final String TAG = Barjis.class.getName();

    private GameController gameController;
    private GameRenderer gameRenderer;
    
    private boolean paused;

    SpriteBatch batch;
    Texture img;
    ShapeRenderer shapeRendererTest;

    @Override
    public void create () {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
        
        // Initialize controller and renderer
        gameController = new GameController();
        gameRenderer = new GameRenderer(gameController);
        
        // Game world is active on start
        paused = false;
        
        /// PLAYING AROUND
            batch = new SpriteBatch();
            img = new Texture("badlogic.jpg");
            shapeRendererTest = new ShapeRenderer();
            //shapeRendererTest.rect(0, 0, 20, 50);
        /// END PLAYING AROUND
    }

    @Override
    public void render () {
        // Do not update game world when paused.
        if (!paused) {
            // Update game world by the time that has passed
            // since last rendered frame.
            gameController.update(Gdx.graphics.getDeltaTime());
        }
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        gameRenderer.render();
        
            //Gdx.gl.glClearColor(0, 0, 0, 1);
            //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //batch.begin();
            //batch.draw(img, 0, 0);
            //batch.end();
            /*
            shapeRendererTest.setColor(1, 1, 1, (float)0.5);
            shapeRendererTest.begin(ShapeRenderer.ShapeType.Filled);
            shapeRendererTest.rect(10, 50, 50, 100);
            shapeRendererTest.end();
            */
    }
    
    @Override public void resize (int width, int height) {
        gameRenderer.resize(width, height);
    }
    
    @Override public void pause () {
        paused = true;
    }
    
    @Override public void resume () {
        paused = false;
    }
    
    @Override public void dispose () {
        gameRenderer.dispose();
    }
}
