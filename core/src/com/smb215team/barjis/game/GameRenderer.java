/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.smb215team.barjis.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
/**
 *
 * @author dinosaadeh
 */
public class GameRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private GameController gameController;
    
    public GameRenderer (GameController gameController) {
        this.gameController = gameController;
        init();
    }
    private void init () {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
        Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
    }
    
    public void render () {
        renderTestObjects();
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
        for (Sprite sprite : gameController.dices) {
            sprite.draw(batch);
        }
        // </editor-fold>
        batch.end();
    }
    
    public void resize (int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) *
        width;
        camera.update();
    }
    
    @Override public void dispose () {
        batch.dispose();
    }
}