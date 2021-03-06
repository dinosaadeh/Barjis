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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.game.enums.GameModes;
import com.smb215team.barjis.util.Constants;
import com.smb215team.barjis.util.GamePreferences;

/**
 *
 * @author dinosaadeh
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Image mainScreenName;
    private Image mainScreenLogo;
    private ImageButton btnPlaySolo;
    private ImageButton btnPvp;
    private Image mainScreenButtonsSeparator;
    private ImageButton btnHowToPlay;
    private ImageButton btnCredits;
    private Image poweredByLibgdx;
    private ImageButton btnMuteSound;
    private FitViewport fitViewport;// in case we need it or the camera GUI
    
    public MenuScreen (Game game) {
        super(game);
        fitViewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);
        stage = new Stage(fitViewport);
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
    
    @Override public void resize (int width, int height) {
        
    }
    
    @Override public void show () {
        GamePreferences.instance.load();
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.topLeft);
        
        // <editor-fold desc="First row is for Barjis name">
        Table firstRow = new Table();
        firstRow.align(Align.bottomLeft);
        mainScreenName = new Image(Assets.instance.menuScreenImages.assetMainScreenName);
        firstRow.add(mainScreenName).padLeft(39.0625f).padTop(24f);

        mainTable.add(firstRow).left();//.size(Constants.VIEWPORT_GUI_WIDTH, 144f);
        mainTable.row();
        // </editor-fold>

        // <editor-fold desc="Second row for the main image and the menu">
        Table secondRow = new Table();
        secondRow.setWidth(Gdx.graphics.getWidth());
        mainScreenLogo = new Image(Assets.instance.menuScreenImages.assetMainScreenLogo);
        secondRow.add(mainScreenLogo).padLeft(115f).size(327.34375f, 277.6f);
        //secondRow.debug();
        // <editor-fold desc="Buttons">
        Table buttonsTable = new Table();
        
        btnPlaySolo = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnPlaySolo));
        btnPlaySolo.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    GamePreferences.instance.gameMode = GameModes.solo;
                    GamePreferences.instance.save();
                    game.setScreen(new GameScreen(game));
                }
            });
//        buttonsTable.add(btnPlaySolo).size(250,70);
//        buttonsTable.row();
        
        btnPvp = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnPvp));
        btnPvp.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    GamePreferences.instance.gameMode = GameModes.pvpLocal;
                    //TODO when I launch the networking feature, this mode should be restored to pvpNetwork.. Graphics are ready to have PVP local and PVP Network
                    GamePreferences.instance.save();
                    game.setScreen(new GameScreen(game));
                }
            });
        buttonsTable.add(btnPvp).size(250,70);
        buttonsTable.row();
        
        mainScreenButtonsSeparator = new Image(Assets.instance.mainScreenButtons.btnsSeparator);
//        mainScreenButtonsSeparator.setSize(175.78125f, 1);
        buttonsTable.add(mainScreenButtonsSeparator).padBottom(20).padTop(10);//.size(175.78125f, 28f);
        buttonsTable.row();

        btnHowToPlay = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnHowToPlay));
        btnHowToPlay.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new HowToScreen(game));
                }
            });
        buttonsTable.add(btnHowToPlay).size(250,70);
        buttonsTable.row();
        
        btnCredits = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnCredits));
        btnCredits.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new CreditsScreen(game));
                }
            });
        buttonsTable.add(btnCredits).size(250,70);
        buttonsTable.row();
        
        secondRow.add(buttonsTable).right().padLeft(60);//.size(400f, 272.8f);
        // </editor-fold>

        mainTable.add(secondRow).expand().left().top();
        mainTable.row();
        // </editor-fold>
        
        // <editor-fold desc="Third row is for libgdx and sound configuration">
        Table thirdRow = new Table();
        poweredByLibgdx = new Image(Assets.instance.menuScreenImages.assetPoweredByLibgdx);
        thirdRow.add(poweredByLibgdx).padLeft(39.0625f).padBottom(43);

        btnMuteSound = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnSoundOn),new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnSoundOn),new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnSoundOff));

        // read the value from the setting maps (GamePreferences)
        btnMuteSound.setChecked(GamePreferences.instance.soundMute);
        // set listener to save settings when click on button and the sound settings changed
        btnMuteSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GamePreferences.instance.soundMute = btnMuteSound.isChecked();
                GamePreferences.instance.save();
            }
        } );

        thirdRow.add().width(510);
        thirdRow.add(btnMuteSound).padRight(60);

        mainTable.add(thirdRow).expandX().left().bottom();
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