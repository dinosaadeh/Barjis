/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.smb215team.barjis.game.enums.CellNameEnum;
import com.smb215team.barjis.game.objects.Cell;

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
            new Vector2(5, 0),
            new Vector2(6, 3),
            new Vector2(0, 4)
            //etc.
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

    public static final Cell[] CELLS = {new Cell(CellNameEnum.A21, -0.2f, -1.4f), new Cell(CellNameEnum.A22, -0.2f, -1.8f),
            new Cell(CellNameEnum.A23, -0.2f, -2.2f), new Cell(CellNameEnum.A24, -0.2f, -2.6f),
            new Cell(CellNameEnum.A25, -0.2f, -3f), new Cell(CellNameEnum.A26, -0.2f, -3.4f),
            new Cell(CellNameEnum.A27, -0.2f, -3.8f), new Cell(CellNameEnum.A28, -0.2f, -4.2f),
            new Cell(CellNameEnum.A38, 0.6f, -4.2f), new Cell(CellNameEnum.A37, 0.6f, -3.8f),
            new Cell(CellNameEnum.A36, 0.6f, -3.4f, true), new Cell(CellNameEnum.A35, 0.6f, -3f),
            new Cell(CellNameEnum.A34, 0.6f, -2.6f), new Cell(CellNameEnum.A33, 0.6f, -2.2f),
            new Cell(CellNameEnum.A32, 0.6f, -1.8f), new Cell(CellNameEnum.A31, 0.6f, -1.4f),
            new Cell(CellNameEnum.A11, -1f, -1.4f), new Cell(CellNameEnum.A12, -1f, -1.8f),
            new Cell(CellNameEnum.A13, -1f, -2.2f), new Cell(CellNameEnum.A14, -1f, -2.6f),
            new Cell(CellNameEnum.A15, -1f, -3f), new Cell(CellNameEnum.A16, -1f, -3.4f, true),
            new Cell(CellNameEnum.A17, -1f, -3.8f), new Cell(CellNameEnum.A18, -1f, -4.2f)};
}