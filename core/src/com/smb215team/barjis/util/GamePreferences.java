package com.smb215team.barjis.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.smb215team.barjis.game.enums.GameModes;

public class GamePreferences {
    public static final String TAG = GamePreferences.class.getName();
    public static final GamePreferences instance = new GamePreferences();
    public boolean soundMute;
    public GameModes gameMode;
    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load() {
        soundMute = prefs.getBoolean("soundMute", true);
    }

    public void save() {
        prefs.putBoolean("soundMute", soundMute);
//        prefs.putString("gameMode", gameMode.name());//TODO INITIALIZE THE GAME MODE
        prefs.flush();
    }
}