package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.smb215team.barjis.game.GameController;
import com.smb215team.barjis.game.GameRenderer;
import com.smb215team.barjis.game.MultiplayerGameController;

/**
 * Created by ammar on 9/24/16.
 */
public class MultiplayerGameScreen extends GameScreen{



    public MultiplayerGameScreen(Game game,boolean mute) {
        super(game,mute);
    }

    @Override
    public void show() {
        gameController = new MultiplayerGameController(game,mute);
        gameRenderer = new GameRenderer(gameController);
        Gdx.input.setCatchBackKey(true);
    }

}
