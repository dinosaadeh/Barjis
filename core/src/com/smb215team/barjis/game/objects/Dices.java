/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Dices extends AbstractGameObject {

    private static final String TAG = Dices.class.getName();

    public static final Dices instance = new Dices();
    public Sprite[] dices;
    public Integer[] currentHandMoves;

    private Dices() {
        init();
    }

    /**
     * Initialises the dice when the game starts. It sets the number of all dice
     * combinations to 0.
     */
    public void init() {
        dimension.set(0.5f, 0.5f);
        // Set physics values
        velocity.set(3.0f, 4.0f);
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        dices = new Sprite[6];

        currentHandMoves = new Integer[8];
        currentHandMoves[0] = 0; // shakki
        currentHandMoves[1] = 0; // dest
        currentHandMoves[2] = 0; // 2
        currentHandMoves[3] = 0; // 3
        currentHandMoves[4] = 0; // 4
        currentHandMoves[5] = 0; // banj
        currentHandMoves[6] = 0; // bara
        currentHandMoves[7] = 0; // khal

        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }

    public void render(SpriteBatch batch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                dices[i] = new Sprite(Assets.instance.dice.diceUp);
            } else {
                dices[i] = new Sprite(Assets.instance.dice.diceDown);
                currentThrowValue++;
            }

            // Position on the screen
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            dices[i].setPosition(randomX, randomY);
            dices[i].setSize(0.75f, 0.75f);
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
    
    @Override
    public void update (float deltaTime) {
        super.update(deltaTime);
        Gdx.app.log(TAG, ": fitna");
    }
}
