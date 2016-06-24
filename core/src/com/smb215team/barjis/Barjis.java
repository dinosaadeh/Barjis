package com.smb215team.barjis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;

import com.smb215team.barjis.game.GameController;
import com.smb215team.barjis.game.GameRenderer;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.game.objects.*;

public class Barjis extends ApplicationAdapter {
    private static final String TAG = Barjis.class.getName();

    private GameController gameController;
    private GameRenderer gameRenderer;
    
    private boolean paused;

    @Override
    public void create () {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Dino: initiate the dices
        Dices.instance.init();
        // Initialize controller and renderer
        gameController = new GameController();
        gameRenderer = new GameRenderer(gameController);
        
        // Game world is active on start
        paused = false;
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
        Gdx.gl.glClearColor(0x94/255.0f, 0xfe/255.0f, 0xe3/255.0f, 0xff/255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        gameRenderer.render();
    }
    
    @Override public void resize (int width, int height) {
        gameRenderer.resize(width, height);
    }
    
    @Override public void pause () {
        paused = true;
    }
    
    @Override public void resume () {
        Assets.instance.init(new AssetManager());
        paused = false;
    }
    
    @Override public void dispose () {
        gameRenderer.dispose();
        Assets.instance.dispose();
    }
}
