package com.smb215team.barjis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.screens.MenuScreen;
import com.smb215team.barjis.game.objects.*;

public class Barjis extends Game {
    private static final String TAG = Barjis.class.getName();

    @Override
    public void create () {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Dino TODO (check if the below should be moved to GameScreen): initiate the dices
        Dices.instance.init();
        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }
}