/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Skin skin;
    private Image mainScreenLogo;
    
    public MenuScreen (Game game) {
        super(game);
        skin = new Skin(Gdx.files.internal("menuSkin.json"));

        OrthographicCamera cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(false); // flip y-axis
        cameraGUI.update();

        stage = new Stage(new ScreenViewport(cameraGUI));
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render (float deltaTime) {
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x94/255.0f, 0xfe/255.0f, 0xe3/255.0f, 0xff/255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(Gdx.input.isTouched())
            game.setScreen(new GameScreen(game));
        stage.act(deltaTime);
        stage.draw();
    }
    
    @Override public void resize (int width, int height) { }
    
    @Override public void show () {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(true);
        
        //First row is for Barjis name
        Table firstRow = new Table();
        firstRow.setHeight(104f);
        firstRow.setWidth(350f);
        firstRow.align(Align.bottomLeft);
        firstRow.debug();
        Label barjisLabel = new Label("Barjis", skin);
        firstRow.add(barjisLabel).padLeft(39.0625f);

        mainTable.addActor(firstRow);
        //mainTable.row();
        
        //Second row for the main image and the menu
        Table secondRow = new Table();
        secondRow.setWidth(426);
        secondRow.setHeight(433.75f);
        mainScreenLogo = new Image(Assets.instance.assetMainScreenLogo.assetMainScreenLogo);
        secondRow.add(mainScreenLogo).padLeft(10);
        
        mainTable.addActor(secondRow);
        mainTable.row();
        
        //Third row is for libgdx and sound configuration
        Table thirdRow = new Table();
        //table.setDebug(true);
        //table.setBounds(0, 0, 100, 100);
        thirdRow.setWidth(426);
        thirdRow.setHeight(433.75f);
        
        thirdRow.add(mainScreenLogo).padLeft(10);
        
        stage.addActor(mainTable);
    }
    
    @Override public void hide () { }
    
    @Override public void pause () { }

    @Override
    public InputProcessor getInputProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}