package com.smb215team.barjis.game.handlers;

/**
 * Created by dinosaadeh on 2/18/17.
 */

public class AIPlayerHandler implements PlayerHandler {
    @Override
    public void initiateGame() {

    }

    @Override
    public boolean getReadiness() {
        return false;
    }

    @Override
    public void setReadiness(boolean readiness) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public int getCurrentPlayerIndexPreference() {
        return 0;
    }

    @Override
    public String getLeftLabel() {
        return null;
    }

    @Override
    public String getRightLabel() {
        return null;
    }
}