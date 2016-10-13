package com.smb215team.barjis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.screens.MenuScreen;

public class Barjis extends Game {
    private static final String TAG = Barjis.class.getName();

    @Override
    public void create () {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }
}