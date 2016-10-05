package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.smb215team.barjis.game.MultiplayerGameController;
import com.smb215team.barjis.game.MultiplayerGameRenderer;

/**
 * Created by ammar on 9/24/16.
 */
public class MultiplayerGameScreen extends GameScreen{



    public MultiplayerGameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        gameController = new MultiplayerGameController(game);
        gameRenderer = new MultiplayerGameRenderer((MultiplayerGameController) gameController);
        Gdx.input.setCatchBackKey(true);
    }

}
