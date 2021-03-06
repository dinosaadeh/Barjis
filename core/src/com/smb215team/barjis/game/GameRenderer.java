/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.smb215team.barjis.game.objects.Dices;
import com.smb215team.barjis.game.objects.Pawn;
import com.smb215team.barjis.game.objects.Player;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class GameRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private GameController gameController;

    private ShapeRenderer dummyShapeRenderer;
    private Stage stage2;
    public  GlyphLayout layout = new GlyphLayout(); // Obviously stick this in a field to avoid allocation each frame.
    private Label.LabelStyle labelStyle;
    public Viewport extendedViewPort;

    public GameRenderer (GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init () {
        batch = new SpriteBatch();

        labelStyle = new Label.LabelStyle();
        labelStyle.font = Assets.instance.fonts.defaultBig;


        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        this.gameController.camera = camera;
        camera.update();

        extendedViewPort =new ExtendViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);
        gameController.stage = new Stage(extendedViewPort);
        stage2 = new Stage(extendedViewPort);
        Gdx.input.setInputProcessor(gameController.stage);
        gameController.stage.addListener(gameController.screenClickListener);

    }

    public void render () {
        renderGui(batch);
        renderObjects();
        renderPlayer(batch);
        gameController.stage.draw();
        stage2.draw();
    }

    public void resize (int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();

        extendedViewPort.getCamera().viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        extendedViewPort.getCamera().viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / height) * width;
        extendedViewPort.getCamera().position.set(extendedViewPort.getCamera().viewportWidth / 2, extendedViewPort.getCamera().viewportHeight / 2, 0);
        extendedViewPort.getCamera().update();
        gameController.stage.getViewport().update(width,height,true);
    }

    @Override public void dispose () {
        batch.dispose();
    }

    private void renderObjects() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Dices.instance.render(batch);
        batch.end();
        //renderDebug(batch);
    }

    private void renderPlayer (SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(null != gameController.players) {
            for (Player player : gameController.players) {
                player.render(batch);
            }
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


        player0LabelPlaceholder.setSize(2.79f, 1.11f);
        player0LabelPlaceholder.setCenter(-6.09f, 3.495f);
        player0LabelPlaceholder.draw(batch);


        Sprite player1LabelPlaceholder = (1 == gameController.currentPlayerIndex) ? new Sprite(Assets.instance.playerLabels.lblPlaceholderPlayerOn1) : new Sprite(Assets.instance.playerLabels.lblPlaceholderPlayerOff1);
        player1LabelPlaceholder.setSize(2.79f, 1.11f);
        player1LabelPlaceholder.setCenter(6.09f, 3.495f);
        player1LabelPlaceholder.draw(batch);

        stage2.clear();
        Label player0Label = new Label(gameController.playerHandler.getLeftLabel(), labelStyle);
        player0Label.setFontScale(0.45f);
        player0Label.setPosition(71, 395);

        Label player1Label = new Label(gameController.playerHandler.getRightLabel(), labelStyle);
        player1Label.setFontScale(0.45f);
        player1Label.setPosition(719, 395);

        stage2.addActor(player0Label);
        stage2.addActor(player1Label);

        // </editor-fold>

        batch.setProjectionMatrix(extendedViewPort.getCamera().combined);
//        renderGuiMovesToBePlayed(batch); removed by ammar
        // draw FPS text (anchored to bottom right edge)

//        renderGuiFpsCounter(batch);
        batch.end();
    }
  
    private void renderGuiFpsCounter (SpriteBatch batch) {
        float x = extendedViewPort.getCamera().viewportWidth - 110;
        float y =  35;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.rochesterScaled;
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
        dummyShapeRenderer = new ShapeRenderer();
        dummyShapeRenderer.setProjectionMatrix(camera.combined);

        dummyShapeRenderer.begin(ShapeType.Line);
        dummyShapeRenderer.setColor(1, 1, 0, 1);

        // <editor-fold desc="Dice container">
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.x, Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.y, Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.width, Constants.DICES_CONTAINER_BORDER_TOP_SIDE01.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.x, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.y, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.width, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE01.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.x, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.y, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.width, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE01.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.x, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.y, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.width, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE01.height);

        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.x, Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.y, Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.width, Constants.DICES_CONTAINER_BORDER_TOP_SIDE02.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.x, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.y, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.width, Constants.DICES_CONTAINER_BORDER_BOTTOM_SIDE02.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.x, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.y, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.width, Constants.DICES_CONTAINER_BORDER_LEFT_SIDE02.height);
        dummyShapeRenderer.rect(Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.x, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.y, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.width, Constants.DICES_CONTAINER_BORDER_RIGHT_SIDE02.height);
        // </editor-fold>

        // <editor-fold desc="pawns">
        for(Player player : gameController.players) {
            if(null == player) break;
            for(Pawn pawn : player.pawns) {
                if(null == pawn) break;
                dummyShapeRenderer.rect(pawn.bounds.x, pawn.bounds.y, pawn.bounds.width, pawn.bounds.height);
            }
        }
        // </editor-fold>

        dummyShapeRenderer.end();
    }
}