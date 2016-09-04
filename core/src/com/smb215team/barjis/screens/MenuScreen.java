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
    private Image mainScreenName;
    private Image mainScreenLogo;
    private ImageButton btnPlaySolo;
    private ImageButton btnPvp;
    private Image mainScreenButtonsSeparator;
    private ImageButton btnHowToPlay;
    private ImageButton btnCredits;
    
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
        stage.act(deltaTime);
        stage.draw();
    }
    
    @Override public void resize (int width, int height) { }
    
    @Override public void show () {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(true, true);
        mainTable.align(Align.topLeft);
        
        // <editor-fold desc="First row is for Barjis name">
        Table firstRow = new Table();
        firstRow.align(Align.bottomLeft);
        mainScreenName = new Image(Assets.instance.mainScreenLogo.assetMainScreenName);
        mainScreenName.setSize(157.03125f, 83.2f);
//        mainScreenName.setAlignment(Align.left);
        firstRow.add(mainScreenName).padLeft(39.0625f).padTop(24f);

        mainTable.add(firstRow).size(Constants.VIEWPORT_GUI_WIDTH, 144f);
        mainTable.row();
        // </editor-fold>

        // <editor-fold desc="Second row for the main image and the menu">
        Table secondRow = new Table();
        secondRow.align(Align.bottomLeft);
        mainScreenLogo = new Image(Assets.instance.mainScreenLogo.assetMainScreenLogo);
        secondRow.add(mainScreenLogo).size(327.34375f, 272.8f).padLeft(69.53125f);
        
        // <editor-fold desc="Buttons">
        Table buttonsTable = new Table();
        
        btnPlaySolo = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnPlaySolo));
        btnPlaySolo.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new GameScreen(game));
                }
            });
        buttonsTable.add(btnPlaySolo);
        buttonsTable.row();
        
        btnPvp = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnPvp));
        btnPvp.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new GameScreen(game));
                }
            });
        buttonsTable.add(btnPvp);
        buttonsTable.row();
        
        mainScreenButtonsSeparator = new Image(Assets.instance.mainScreenButtons.btnsSeparator);
        mainScreenButtonsSeparator.debug();
        mainScreenButtonsSeparator.setSize(175.78125f, 1);
        buttonsTable.add(mainScreenButtonsSeparator).size(175.78125f, 28f);
        buttonsTable.row();

        btnHowToPlay = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnHowToPlay));
        btnHowToPlay.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new GameScreen(game));
                }
            });
        buttonsTable.add(btnHowToPlay);
        buttonsTable.row();
        
        btnCredits = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnCredits));
        btnCredits.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new GameScreen(game));
                }
            });
        buttonsTable.add(btnCredits);
        buttonsTable.row();
        
        secondRow.add(buttonsTable).size(400f, 272.8f);
        // </editor-fold>
        
        mainTable.add(secondRow).size(Constants.VIEWPORT_GUI_WIDTH, 308f);
        mainTable.row();
        // </editor-fold>
        
        // <editor-fold desc="Third row is for libgdx and sound configuration">
//        Table thirdRow = new Table();
//        //table.setBounds(0, 0, 100, 100);
//        thirdRow.setWidth(426);
//        thirdRow.setHeight(98.25f);
//        
//        //thirdRow.add(mainScreenLogo).padLeft(10);
//        Label barjisLabel1 = new Label("Barjis 2", skin);
//        thirdRow.add(barjisLabel1);
//        mainTable.add(thirdRow).size(Constants.VIEWPORT_GUI_WIDTH, 67.2f);
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