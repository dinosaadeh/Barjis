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
public class LocalPlayerHandler implements PlayerHandler {
    private static final String TAG = LocalPlayerHandler.class.getName();
    private boolean isReady = false;
    public int initialThreeDicesThrowValue = -1;

    @Override
    public void initiateGame() {
        setReadiness(true);
    }
    
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getReadiness() {
        return isReady;
    }

    @Override
    public void setReadiness(boolean readiness) {
        isReady = readiness;
    }
}