package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

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
    public boolean canPlayerThrowDices;
    Sound diceSound = Gdx.audio.newSound(Gdx.files.internal("diceSound.mp3"));

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

        canPlayerThrowDices = true;
    }

    public void reset() {
        //Reset the currentHandNumberOfMoves
        for (int i = 0; i < 8; i++) {
            currentHandMoves[i] = 0;
        }
        canPlayerThrowDices=true;
    }

    public void dispose() {
        diceSound.dispose();
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

    public void throwDices(float diceMarginFromX, float diceMarginToX, float diceMarginFromY, float diceMarginToY) {
        bringDicesToFullStop();//I used this method to stop dices when by mistake a dice leaves the container. This bug should be solved and with that solved we no longer need this line.
        int currentThrowValue = 0;
        for (int i = 0; i < dices.length; i++) {
            if (Math.random() < 0.5) {
                dices[i] = new Dice(true);
            } else {
                dices[i] = new Dice(false);
                currentThrowValue++;
            }

            // Position on the screen
            float randomX = MathUtils.random(diceMarginFromX, diceMarginToX);
            float randomY = MathUtils.random(diceMarginFromY, diceMarginToY);
            dices[i].position.set(randomX, randomY);
            dices[i].bounds.set(randomX, randomY, 0.45f, 0.45f);
            dices[i].dimension.set(0.45f, 0.45f);
            //Gdx.app.log(TAG, "Position: " + randomX + ", " + randomY + ", velocity: " + dices[i].velocity);
        }

        diceSound.play();

        // Setting the value of the current throw
        currentHandMoves[currentThrowValue]++;
        if (1 == currentThrowValue || 5 == currentThrowValue) // if dest or banj, add the bonus move
        {
            currentHandMoves[7]++;
        }
        // If a stopper combination (2, 3 or 4), do not allow the player to throw dices again
        if (2 == currentThrowValue || 3 == currentThrowValue || 4 == currentThrowValue) {
             canPlayerThrowDices = false;
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

    public boolean dicesReachedAFullStop() {
        for(Dice dice : dices) {
            if(dice.velocity.x != 0 || dice.velocity.y != 0)
                return false;
        }
        return (true);
    }
    
    public void bringDicesToFullStop() {
        for(Dice dice : dices) {
            if(null == dice)
                return;
            dice.velocity.x =0;
            dice.velocity.y = 0;
        }
    }
}