package com.smb215team.barjis.game.interfaces;

/**
 * Created by ammar on 11/30/16.
 */
public interface NetworkListener {

    public void moveRemotePlayerPawn(int playerIndex, int pawnIndex, int selectedIndexFromTable);

    public void throwRemotePlayerDices(int value);

    public void play();

}
