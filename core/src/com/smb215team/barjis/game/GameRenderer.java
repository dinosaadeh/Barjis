/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType; 
 
import com.smb215team.barjis.util.Constants;
import com.smb215team.barjis.game.objects.*;   
/**
 *
 * @author dinosaadeh
 */
public class GameRenderer implements Disposable {
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch ;
    private GameController gameController;
    ShapeRenderer shapeRenderer;
    static private boolean projectionMatrixSet;
    private ShapeRenderer dummyShapeRenderer;
     
    BitmapFont returnTextFont = Assets.instance.fonts.defaultNormal;///used from wrapping the returnText
    public  GlyphLayout layout = new GlyphLayout(); // Obviously stick this in a field to avoid allocation each frame. 
   
    
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
        renderGui(batch);
        renderTestObjects();
        renderPlayer(batch);
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

        Dices.instance.render(batch);
        batch.end();
        //renderDebug(batch);
    }
    
    private void renderPlayer (SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Player player : gameController.players) {
            player.render(batch);
        }
        batch.end();
    }
    
    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // <editor-fold desc="Objects that don't move.">
        Sprite boardSprite = new Sprite(Assets.instance.board.board);
        boardSprite.setSize(9.915f, 9);
        boardSprite.setCenter(0, 0);
        boardSprite.draw(batch);
        
        float[] deadPawnPlaceholderXPositions = {-7.395f, -6.735f, -6.09f, -5.445f, 4.98f, 5.625f, 6.27f, 6.945f};
        Sprite[] deadPawnPlaceholders = new Sprite[8];
        for(int i = 0; i < deadPawnPlaceholders.length; i++) {
            deadPawnPlaceholders[i] = new Sprite(Assets.instance.deadPawnPlaceholder.deadPawnPlaceholder);
            deadPawnPlaceholders[i].setSize(0.540f, 0.540f);
            deadPawnPlaceholders[i].setPosition(deadPawnPlaceholderXPositions[i], 2.07f);
            deadPawnPlaceholders[i].draw(batch);
        }
        // </editor-fold>
        
        // <editor-fold desc="Displaying players labels based on which player's turn it is">
        Sprite player0LabelPlaceholder = (0 == gameController.currentPlayerIndex) ? new Sprite(Assets.instance.playerLabels.lblPlaceholderPlayerOn0) : new Sprite(Assets.instance.playerLabels.lblPlaceholderPlayerOff0);
        Sprite player0Label = (0 == gameController.currentPlayerIndex) ? new Sprite(Assets.instance.playerLabels.lblPlayerOn0) : new Sprite(Assets.instance.playerLabels.lblPlayerOff0);
        player0LabelPlaceholder.setSize(2.79f, 1.11f);
        player0LabelPlaceholder.setCenter(-6.09f, 3.495f);
        player0LabelPlaceholder.draw(batch);
        player0Label.setSize(1.11f, 0.465f);
        player0Label.setCenter(-5.69f, 3.52f);
        player0Label.draw(batch);

        Sprite player1LabelPlaceholder = (1 == gameController.currentPlayerIndex) ? new Sprite(Assets.instance.playerLabels.lblPlaceholderPlayerOn1) : new Sprite(Assets.instance.playerLabels.lblPlaceholderPlayerOff1);
        Sprite player1Label = (1 == gameController.currentPlayerIndex) ? new Sprite(Assets.instance.playerLabels.lblPlayerOn1) : new Sprite(Assets.instance.playerLabels.lblPlayerOff1);
        player1LabelPlaceholder.setSize(2.79f, 1.11f);
        player1LabelPlaceholder.setCenter(6.09f, 3.495f);
        player1LabelPlaceholder.draw(batch);
        player1Label.setSize(1.11f, 0.465f);
        player1Label.setCenter(6.49f, 3.52f);
        player1Label.draw(batch);
        // </editor-fold>
        
        batch.setProjectionMatrix(cameraGUI.combined);
   //renderGuiMovesToBePlayed(batch); was moved after batch.end();
        // draw FPS text (anchored to bottom right edge)
        
         renderGuiMovesToBePlayed(batch);
        renderGuiFpsCounter(batch);
        batch.end();
    }

    private void renderGuiMovesToBePlayed (SpriteBatch batch) {
        
        Dices.instance.getValue(batch);
    }
    
    
    /////this function is not yet final ///just for testing purposes
       public void draw(SpriteBatch batch , float width,float height){
       //batch.end();
       if(!projectionMatrixSet){
           shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
       }
       shapeRenderer.begin(ShapeType.Line); 
       shapeRenderer.rect(30, 200, width, height); 
       Gdx.gl.glEnable(GL20.GL_BLEND);
       Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
       Gdx.gl.glDisable(GL20.GL_BLEND);
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
    
    private void renderDebug(SpriteBatch batch) {
        dummyShapeRenderer.setProjectionMatrix(camera.combined);
 
        dummyShapeRenderer.begin(ShapeType.Line);
        dummyShapeRenderer.setColor(1, 1, 0, 1);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.x, Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.y, Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.width, Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.x, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.y, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.width, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.x, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.y, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.width, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.x, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.y, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.width, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.height);

        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.x, Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.y, Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.width, Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.x, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.y, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.width, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.x, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.y, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.width, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.x, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.y, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.width, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.height);
        
        dummyShapeRenderer.end();
    }
}
