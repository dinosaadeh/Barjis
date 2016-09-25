package com.smb215team.barjis.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by ammar on 9/24/16.
 */
public class MultiplayerGameController extends GameController {

    public int myPlayerIndex ;/// 0 --> Player1      1 -->Player2


    UpdateServer updateServer;

    public MultiplayerGameController(Game game,boolean mute) {
        super(game,mute);
    }


    @Override
    protected void init() {
        super.init();
        updateServer = new UpdateServer(this);
    }

    @Override
    public void textClicked(ChangeListener.ChangeEvent e, Actor actor) {
        if(currentSelectedPawnForPlay!= null) {
            super.textClicked(e, actor);
            updateServer.updatePawns(currentPlayerIndex, pawnUpdateIndex, selectedIndexInTable);
        }
    }
}
