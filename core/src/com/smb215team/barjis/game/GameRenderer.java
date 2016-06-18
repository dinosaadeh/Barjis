/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.smb215team.barjis.util.Constants;
import com.smb215team.barjis.game.objects.*;
/**
 *
 * @author dinosaadeh
 */
public class GameRenderer implements Disposable {
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private GameController gameController;
    
    public GameRenderer (GameController gameController) {
        this.gameController = gameController;
        init();
    }
    
    private void init () {
        batch = new SpriteBatch();
        
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }
    
    public void render () {
        renderTestObjects();
        renderGui(batch);
    }
    
    public void resize (int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
        
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }
    
    @Override public void dispose () {
        batch.dispose();
    }
   
    private void renderTestObjects() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // <editor-fold desc="Dino: This is my trial to layout the board">
        gameController.middleBlockSprite.draw(batch);
        for (Sprite sprite : gameController.branch1Cells) {
            sprite.draw(batch);
        }
        // </editor-fold>
        // <editor-fold desc="Dino: Showing the dices on the board">
        for (Sprite sprite : Dices.instance.dices) {
            if(null != sprite)
                sprite.draw(batch);
        }
        // </editor-fold>
        batch.end();
    }
    
    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        renderGuiMovesToBePlayed(batch);
        // draw FPS text (anchored to bottom right edge)
        renderGuiFpsCounter(batch);
        batch.end();
    }

    private void renderGuiMovesToBePlayed (SpriteBatch batch) {
        float x = -15;
        float y = -15;
        Assets.instance.fonts.defaultNormal.draw(batch, "" + Dices.instance.getValue(), x + 75, y + 37);
    }
    
    private void renderGuiFpsCounter (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }
}