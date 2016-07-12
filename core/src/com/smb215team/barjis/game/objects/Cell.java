package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.smb215team.barjis.game.enums.CellNameEnum;

/**
 * Created by ammar on 7/13/16.
 */
public class Cell {

    private CellNameEnum name;

    private Vector2 vector;

    private boolean special;

    public Cell(CellNameEnum name, Vector2 vector, boolean special) {
        this.name = name;
        this.vector = vector;
        this.special = special;
    }

    public Cell(CellNameEnum name, float x, float y, boolean special) {
        this.name = name;
        this.vector = new Vector2(x, y);
        this.special = special;
    }

    public Cell(CellNameEnum name, Vector2 vector) {
        this.name = name;
        this.vector = vector;

    }


    public Cell(CellNameEnum name, float x, float y) {
        this.name = name;
        this.vector = new Vector2(x, y);

    }

    public Cell() {
        // empty constructor
    }

    public Vector2 getVector() {
        return vector;
    }

    public void setVector(Vector2 vector) {
        this.vector = vector;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public CellNameEnum getName() {
        return name;
    }

    public void setName(CellNameEnum name) {
        this.name = name;
    }
}
