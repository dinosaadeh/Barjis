/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import static com.smb215team.barjis.util.Constants.*;

/**
 * This is the container that will hold the dices within its boundaries.
 * Box borders will be rectangles defined in Constants.
 * Game controller will choose which container to use. Also, the container defines the X, Y margins
 * between which dices randomly are placed on the screen (within the container).
 * 
 * @author dinosaadeh
 */
public class DiceContainer {
    private static final String TAG = Dice.class.getName();
 
    public Rectangle borderTop;
    public Rectangle borderBottom;
    public Rectangle borderLeft;
    public Rectangle borderRight;
    
    public float diceMarginFromX;
    public float diceMarginToX;
    public float diceMarginFromY;
    public float diceMarginToY;
    
    public DiceContainer() {
        init("SIDE01");
    }
    
    public DiceContainer(String side) {
        init(side);
    }

    private void init(String side) {
        if(side.equals("SIDE01")){
            borderTop = DICES_CONTAINER_BORDER_TOP_SIDE01;
            borderBottom = DICES_CONTAINER_BORDER_BOTTOM_SIDE01;
            borderLeft = DICES_CONTAINER_BORDER_LEFT_SIDE01;
            borderRight = DICES_CONTAINER_BORDER_RIGHT_SIDE01;
        }
        
        if(side.equals("SIDE02")){
            borderTop = DICES_CONTAINER_BORDER_TOP_SIDE02;
            borderBottom = DICES_CONTAINER_BORDER_BOTTOM_SIDE02;
            borderLeft = DICES_CONTAINER_BORDER_LEFT_SIDE02;
            borderRight = DICES_CONTAINER_BORDER_RIGHT_SIDE02;
        }
        
        diceMarginFromX = borderLeft.x + 2 * borderLeft.width;
        diceMarginToX = borderRight.x - 2 * borderRight.width;
        diceMarginFromY = borderBottom.y + 2 * borderBottom.height;
        diceMarginToY = borderTop.y - 2 * borderTop.height;
    }
}