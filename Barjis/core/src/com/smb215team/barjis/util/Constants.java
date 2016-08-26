/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.util;

import com.badlogic.gdx.math.Rectangle;

/**
 * @author dinosaadeh
 */
public class Constants {
    // Visible game world is 5 meters wide
    public static final float VIEWPORT_WIDTH = 16f;
    // Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = 9f;
    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "BarjisPackFile.atlas";

    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

    // <editor-fold desc="These are the boundaries of dice container for each side">
    /**
     * Left bottom side
     */
    public static final Rectangle DICES_CONTAINER_BORDER_TOP_SIDE01 = new Rectangle(-7.65f, 0f, 3f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_BOTTOM_SIDE01 = new Rectangle(-7.65f, -4.2f, 3f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_LEFT_SIDE01 = new Rectangle(-7.4f, -4.5f, 0.1f, 4.5f);
    public static final Rectangle DICES_CONTAINER_BORDER_RIGHT_SIDE01 = new Rectangle(-4.7f, -4.5f, 0.1f, 4.5f);

    /**
     * Right bottom side
     */
    public static final Rectangle DICES_CONTAINER_BORDER_TOP_SIDE02 = new Rectangle(4.6f, 0f, 3.1f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_BOTTOM_SIDE02 = new Rectangle(4.6f, -4.2f, 3.1f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_LEFT_SIDE02 = new Rectangle(4.6f, -4.5f, 0.1f, 4.5f);
    public static final Rectangle DICES_CONTAINER_BORDER_RIGHT_SIDE02 = new Rectangle(7.4f, -4.5f, 0.1f, 4.5f);
    // </editor-fold>
    
    public static final Integer[] SHIRE_INDEXES = {10, 21, 27, 38, 44, 55, 61, 72};
//TODO to check
    public static final Integer DiceNumber = 83;
    // <editor-fold desc="Timer limits">
    public static final float TIMER_LIMIT_FOR_THROWING_DICES_AT_GAME_START = 3f;//2 seconds
    public static final float TIMER_LIMIT_FOR_THROWING_DICES = 5f;//5 seconds
    public static final float TIMER_LIMIT_FOR_PLAYER_WITH_NO_MOVES = 2f;//2 seconds
    // </editor-fold>
}