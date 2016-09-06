/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class CreditsScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Skin skin;
    private Image mainScreenName;
    private Image mainScreenLogo;

    public CreditsScreen (Game game) {
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
        stage.act(deltaTime);
        stage.draw();
    }
    
    @Override public void resize (int width, int height) { }
    
    @Override public void show () {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        //mainTable.setDebug(true, true);
        mainTable.align(Align.topLeft);
        
        // <editor-fold desc="First row is for Barjis name">
        Table firstRow = new Table();
        firstRow.align(Align.bottomLeft);
        mainScreenName = new Image(Assets.instance.mainScreenLogo.assetMainScreenName);
        firstRow.add(mainScreenName).padLeft(39.0625f).padTop(24f);

        mainTable.add(firstRow).left();//.size(Constants.VIEWPORT_GUI_WIDTH, 144f);
        mainTable.row();
        // </editor-fold>

        // <editor-fold desc="Second row for the main image and the text">
        Table secondRow = new Table();
        mainScreenLogo = new Image(Assets.instance.mainScreenLogo.assetMainScreenLogo);
        secondRow.add(mainScreenLogo).minWidth(332.03125f).padLeft(115f);//.size(327.34375f, 277.6f);
        //secondRow.debug();

        mainTable.add(secondRow).expandX().left().top();//.size(Constants.VIEWPORT_GUI_WIDTH, 308f);
        mainTable.row();
        // </editor-fold>
        
        stage.addActor(mainTable);
    }
    
    @Override public void hide () { }
    
    @Override public void pause () { }

    @Override
    public InputProcessor getInputProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}