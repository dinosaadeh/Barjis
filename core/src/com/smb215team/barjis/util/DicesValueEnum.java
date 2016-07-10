package com.smb215team.barjis.util;

/**
 * Created by ammar on 7/10/16.
 */
public enum DicesValueEnum {

    SHAKKI(6), DEST(10), TWO(2), THREE(3), FOUR(4), BANJ(25), BARA(12), KHAL(1);

    // value of movement for every possibility
    private int value;


    DicesValueEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
