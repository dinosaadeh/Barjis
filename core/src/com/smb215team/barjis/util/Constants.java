/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    public static final Vector2[] boardMap = new Vector2[]{
            //Bottom Branch
            //Bottom Branch - middle
            new Vector2(-0.2f, -1.4f),
            new Vector2(-0.2f, -1.8f),
            new Vector2(-0.2f, -2.2f),
            new Vector2(-0.2f, -2.6f),
            new Vector2(-0.2f, -3f),
            new Vector2(-0.2f, -3.4f),
            new Vector2(-0.2f, -3.8f),
            new Vector2(-0.2f, -4.2f),
            //Bottom Branch - right
            new Vector2(0.6f, -4.2f),
            new Vector2(0.6f, -3.8f),
            new Vector2(0.6f, -3.4f),
            new Vector2(0.6f, -3f),
            new Vector2(0.6f, -2.6f),
            new Vector2(0.6f, -2.2f),
            new Vector2(0.6f, -1.8f),
            new Vector2(0.6f, -1.4f),
            //Bottom Branch - left
            new Vector2(-1f, -1.4f),
            new Vector2(-1f, -1.8f),
            new Vector2(-1f, -2.2f),
            new Vector2(-1f, -2.6f),
            new Vector2(-1f, -3f),
            new Vector2(-1f, -3.4f),
            new Vector2(-1f, -3.8f),
            new Vector2(-1f, -4.2f),
            //Right Branch
            //Right Branch - middle
            new Vector2(0.9f, -0.2f),
            new Vector2(1.25f, -0.2f),
            new Vector2(1.6f, -0.2f),
            new Vector2(1.98f, -0.2f),
            new Vector2(2.35f, -0.2f),
            new Vector2(2.72f, -0.2f),
            new Vector2(3.08f, -0.2f),
            new Vector2(3.45f, -0.2f),

            //Right Branch - right
            new Vector2(0.9f, 0.6f),
            new Vector2(1.25f, 0.7f),
            new Vector2(1.6f, 0.7f),
            new Vector2(1.98f, 0.7f),
            new Vector2(2.35f, 0.7f),
            new Vector2(2.72f, 0.7f),
            new Vector2(3.08f, 0.7f),
            new Vector2(3.45f, 0.7f),
            //Right Branch - left
            new Vector2(0.9f, -1f),
            new Vector2(1.25f, -1.1f),
            new Vector2(1.6f, -1.1f),
            new Vector2(1.98f, -1.1f),
            new Vector2(2.35f, -1.1f),
            new Vector2(2.72f, -1.1f),
            new Vector2(3.08f, -1.1f),
            new Vector2(3.45f, -1.1f),

            //Top Branch
            //Top Branch - middle
            new Vector2(-0.18f, 0.98f),
            new Vector2(-0.18f, 1.38f),
            new Vector2(-0.18f, 1.78f),
            new Vector2(-0.18f, 2.2f),
            new Vector2(-0.18f, 2.6f),
            new Vector2(-0.18f, 3f),
            new Vector2(-0.18f, 3.4f),
            new Vector2(-0.18f, 3.8f),
            //Top Branch - right
            new Vector2(-0.84f, 0.98f),
            new Vector2(-0.9f, 1.38f),
            new Vector2(-0.9f, 1.78f),
            new Vector2(-0.9f, 2.2f),
            new Vector2(-0.9f, 2.6f),
            new Vector2(-0.9f, 3f),
            new Vector2(-0.9f, 3.4f),
            new Vector2(-0.9f, 3.8f),
            //Top Branch - left
            new Vector2(0.6f, 0.98f),
            new Vector2(0.65f, 1.38f),
            new Vector2(0.65f, 1.78f),
            new Vector2(0.65f, 2.2f),
            new Vector2(0.65f, 2.6f),
            new Vector2(0.65f, 3f),
            new Vector2(0.65f, 3.4f),
            new Vector2(0.65f, 3.8f),
            //Left Branch
            //Left Branch - middle
            new Vector2(-1.2f, -0.2f),
            new Vector2(-1.58f, -0.2f),
            new Vector2(-1.94f, -0.2f),
            new Vector2(-2.3f, -0.2f),
            new Vector2(-2.66f, -0.2f),
            new Vector2(-3.03f, -0.2f),
            new Vector2(-3.4f, -0.2f),
            new Vector2(-3.76f, -0.2f),
            //Left Branch - right
            new Vector2(-1.2f, -1f),
            new Vector2(-1.58f, -1.1f),
            new Vector2(-1.94f, -1.1f),
            new Vector2(-2.3f, -1.1f),
            new Vector2(-2.66f, -1.1f),
            new Vector2(-3.03f, -1.1f),
            new Vector2(-3.4f, -1.1f),
            new Vector2(-3.76f, -1.1f),
            //Left Branch - left
            new Vector2(-1.2f, 0.6f),
            new Vector2(-1.58f, 0.7f),
            new Vector2(-1.94f, 0.7f),
            new Vector2(-2.3f, 0.7f),
            new Vector2(-2.66f, 0.7f),
            new Vector2(-3.03f, 0.7f),
            new Vector2(-3.4f, 0.7f),
            new Vector2(-3.76f, 0.7f)

    };

    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;
    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

    // <editor-fold desc="These are the boundaries of dice container for each side">
    /**
     * Left bottom side
     */
    public static final Rectangle DICES_CONTAINER_BORDER_TOP_SIDE01 = new Rectangle(-7.65f, 0f, 3.5f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_BOTTOM_SIDE01 = new Rectangle(-7.65f, -4.5f, 3.5f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_LEFT_SIDE01 = new Rectangle(-7.65f, -4.5f, 0.1f, 4.5f);
    public static final Rectangle DICES_CONTAINER_BORDER_RIGHT_SIDE01 = new Rectangle(-4.2f, -4.5f, 0.1f, 4.5f);

    /**
     * Right bottom side
     */
    public static final Rectangle DICES_CONTAINER_BORDER_TOP_SIDE02 = new Rectangle(4.1f, 0f, 3.6f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_BOTTOM_SIDE02 = new Rectangle(4.1f, -4.5f, 3.6f, 0.1f);
    public static final Rectangle DICES_CONTAINER_BORDER_LEFT_SIDE02 = new Rectangle(4.1f, -4.5f, 0.1f, 4.5f);
    public static final Rectangle DICES_CONTAINER_BORDER_RIGHT_SIDE02 = new Rectangle(7.6f, -4.5f, 0.1f, 4.5f);
    // </editor-fold>
}