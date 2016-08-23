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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.util.Constants;
/**
 *
 * @author dinosaadeh
 */
public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    Stage stage;
    Table table;
    private Image mainScreenLogo;
    
    public MenuScreen (Game game) {
        super(game);
        
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
        stage = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        table = new Table();
        //table.setDebug(true);
        //table.setBounds(0, 0, 100, 100);
        table.row();
        mainScreenLogo = new Image(Assets.instance.assetMainScreenLogo.assetMainScreenLogo);
        table.setWidth(426);
        table.setHeight(433.75f);
        table.setDebug(true);
        
        table.add(mainScreenLogo).padLeft(10);
        //table.setPosition(117.76f, 130f);
        //table.setPosition(10f, 130f);
        stage.addActor(table);
    }
    
    @Override public void hide () { }
    
    @Override public void pause () { }

    @Override
    public InputProcessor getInputProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}