/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.handlers;

/**
 *
 * @author dinosaadeh
 */
public interface PlayerHandler {
    void initiateGame();
    public boolean getReadiness();
    public void setReadiness(boolean readiness);

    public void dispose();
    /**
     * This is created to get the index of the first player in the game.
     * In case the first player index is set externally (at the time of this writing it is the case of
     * the network player), this method returns the index of the player.. Any other case, it simply returns -1
     * allowing the game to make the decision.
     * @return
     */
    public int getCurrentPlayerIndexPreference();

    public String getLeftLabel();

    public String getRightLabel();
}