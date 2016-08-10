package com.smb215team.barjis.game.enums;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

/**
 * Created by ammar on 7/10/16.
 */
//Dino: TO VALIDATE
public enum DicesValueEnum {

    SHAKKI(6,"Shakki"), DEST(10,"Dest"), TWO(2,"Two"), THREE(3,"Three"), FOUR(4,"Four"), BANJ(25,"Banj"), BARA(12,"Bara"), KHAL(1,"Bonus");

    // value of movement for every possibility
    private int value;

    private String label;

    DicesValueEnum(int value,String label) {
        this.value = value;this.label=label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel(){return label;}

    public static DicesValueEnum getDiceValueEnumFromNumber(int number){
        switch(number) {
            case 0:
                return SHAKKI;
            case 1:
                return DEST;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return BANJ;
            case 6:
                return  BARA;
           default:
               return null;

        }
    }
}
