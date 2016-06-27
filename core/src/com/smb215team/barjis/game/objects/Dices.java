package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.smb215team.barjis.game.Assets;

/**
 * This is a singleton class that takes care of throwing the dice over and over
 * and returns the total moves allowed for the current turn
 *
 * @author dinosaadeh
 */
public class Dices {

    private static final String TAG = Dices.class.getName();

    public static final Dices instance = new Dices();
    public Dice[] dices;
    public Integer[] currentHandMoves;

    private Dices() {
        init();
    }

    /**
     * Initialises the dice when the game starts. It sets the number of all dice
     * combinations to 0.
     */
    public void init() {
        dices = new Dice[6];

        currentHandMoves = new Integer[8];
        currentHandMoves[0] = 0; // shakki
        currentHandMoves[1] = 0; // dest
        currentHandMoves[2] = 0; // 2
        currentHandMoves[3] = 0; // 3
        currentHandMoves[4] = 0; // 4
        currentHandMoves[5] = 0; // banj
        currentHandMoves[6] = 0; // bara
        currentHandMoves[7] = 0; // khal
    }

    public void render(SpriteBatch batch) {
        for(Dice dice : dices){
            if(null == dice)
                return;
            dice.render(batch);
        }
    }
    
    public void update(float deltaTime) {
        for(Dice dice : dices) {
            if(null == dice)
                return;
            dice.update(deltaTime);
        }
    }

    public void throwDicesForOneTurn() {
        //Reset the currentHandNumberOfMoves
        for (int i = 0; i < 8; i++) {
            currentHandMoves[i] = 0;
        }
        throwDices();
    }

    private void throwDices() {
        int currentThrowValue = 0;
        for (int i = 0; i < dices.length; i++) {
            if (Math.random() < 0.5) {
                dices[i] = new Dice(true);
            } else {
                dices[i] = new Dice(false);
                currentThrowValue++;
            }

            // Position on the screen
            float randomX = MathUtils.random(-7.0f, -5.0f);
            float randomY = MathUtils.random(-4.0f, 0f);
            dices[i].position.set(randomX, randomY);
            dices[i].dimension.set(0.45f, 0.45f);
        }

        // Setting the value of the current throw
        currentHandMoves[currentThrowValue]++;
        if (1 == currentThrowValue || 5 == currentThrowValue) // if dest or banj, add the bonus move
        {
            currentHandMoves[7]++;
        }
        // If a special combination, throw dices again
        if (0 == currentThrowValue || 1 == currentThrowValue || 5 == currentThrowValue || 6 == currentThrowValue) {
            throwDices();
        }
    }

    public String getValue() {
        String strToReturn = "";

        if (0 != currentHandMoves[1]) {
            strToReturn += " " + currentHandMoves[1] + " Dest";
        }

        if (0 != currentHandMoves[5]) {
            strToReturn += " " + currentHandMoves[5] + " Banj";
        }

        if (0 != currentHandMoves[0]) {
            strToReturn += " " + currentHandMoves[0] + " Shakki";
        }

        if (0 != currentHandMoves[6]) {
            strToReturn += " " + currentHandMoves[1] + " Dest";
        }

        if (0 != currentHandMoves[2]) {
            strToReturn += " 2";
        }
        if (0 != currentHandMoves[3]) {
            strToReturn += " 3";
        }
        if (0 != currentHandMoves[4]) {
            strToReturn += " 4";
        }

        if (0 != currentHandMoves[7]) {
            strToReturn += " " + currentHandMoves[7] + " Bonus";
        }

        return strToReturn;
    }
}
