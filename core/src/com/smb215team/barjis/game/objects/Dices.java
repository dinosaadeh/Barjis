package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.smb215team.barjis.util.GamePreferences;

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
    public static final String[] movesLabels = {"Shakki", "Dest", "2", "3", "4", "Banj", "Bara", "Bonus"};
    public static final Integer[] movesValues = {6, 10, 2, 3, 4, 25, 12, 1}; 
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
        reset();
    }

    public void reset() {
        //Reset the currentHandNumberOfMoves
        for (int i = 0; i < 8; i++) {
            currentHandMoves[i] = 0;
        }

        canPlayerThrowDices = true;
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
          //dicesCollision(deltaTime);//Dino: TO Review
        }
    }

    /**
     * This method throws the dices, counts the dices that showed the closed side and based on that
     * increments the times a value showed in the table currentHandMoves
     * @param diceMarginFromX: the dice will animate from diceMarginFromX
     * @param diceMarginToX: -- to diceMarginToX
     * @param diceMarginFromY: -- and from diceMarginFromY
     * @param diceMarginToY: -- to diceMarginToY
     * @return 
     */
    public int throwDices(float diceMarginFromX, float diceMarginToX, float diceMarginFromY, float diceMarginToY) {
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
            dices[i].setSize(0.45f, 0.45f);
        }

        playSound();

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
        
        return currentThrowValue;
    }

    /**
     * This method throws the dices, counts the dices that showed the closed side and based on that
     * increments the times a value showed in the table currentHandMoves
     * @param diceMarginFromX: the dice will animate from diceMarginFromX
     * @param diceMarginToX: -- to diceMarginToX
     * @param diceMarginFromY: -- and from diceMarginFromY
     * @param diceMarginToY: -- to diceMarginToY
     * @return
     */
    public int throwDicesWithKnownValue(int value, float diceMarginFromX, float diceMarginToX, float diceMarginFromY, float diceMarginToY) {
        bringDicesToFullStop();//I used this method to stop dices when by mistake a dice leaves the container. This bug should be solved and with that solved we no longer need this line.
        int currentThrowValue = value;
        for (int i = 0; i < dices.length; i++) {
            dices[i] = new Dice(!(value > 0));
            value--;

            // Position on the screen
            float randomX = MathUtils.random(diceMarginFromX, diceMarginToX);
            float randomY = MathUtils.random(diceMarginFromY, diceMarginToY);
            dices[i].position.set(randomX, randomY);
            dices[i].bounds.set(randomX, randomY, 0.45f, 0.45f);
            dices[i].dimension.set(0.45f, 0.45f);
            dices[i].setSize(0.45f, 0.45f);
        }

        playSound();

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

        return currentThrowValue;
    }

    /**
     * Throws 3 dices on each side to see which player starts.
     * This accounts only for 2 player mode (not 4)
     * @return 0 for left, 1 for right and -1 if both sides have the same value
     */
    public int throwDicesOnEachSideToDecideFirstPlayer(DiceContainer diceContainerLeft, DiceContainer diceContainerRight) {
        int currentThrowValueLeft = 0;
        int currentThrowValueRight = 0;
        float randomX = 0.0f, randomY = 0.0f;
        for (int i = 0; i < dices.length; i++) {
            if (Math.random() < 0.5) {
                dices[i] = new Dice(true);
            } else {
                dices[i] = new Dice(false);
                if(i <= 2)
                    currentThrowValueLeft++;
                else
                    currentThrowValueRight++;
            }

            // Position on the screen
            if(i <= 2) {
                randomX = MathUtils.random(diceContainerLeft.diceMarginFromX, diceContainerLeft.diceMarginToX);
                randomY = MathUtils.random(diceContainerLeft.diceMarginFromY, diceContainerLeft.diceMarginToY);
            } else {
                randomX = MathUtils.random(diceContainerRight.diceMarginFromX, diceContainerRight.diceMarginToX);
                randomY = MathUtils.random(diceContainerRight.diceMarginFromY, diceContainerRight.diceMarginToY);
            }
            
            dices[i].position.set(randomX, randomY);
            dices[i].bounds.set(randomX, randomY, 0.45f, 0.45f);
            dices[i].dimension.set(0.45f, 0.45f);
        }
        playSound();
        if (currentThrowValueLeft == currentThrowValueRight) return -1;
        return (currentThrowValueLeft > currentThrowValueRight) ? 0 : 1;
    }

    /**
     * Throws 3 dices on each side knowing the values already.
     * This accounts only for 2 player mode (not 4)
     */
    public void throwDicesOnEachSide(DiceContainer diceContainerLeft, int leftInitialThreeDicesThrowValue, DiceContainer diceContainerRight, int rightInitialThreeDicesThrowValue) {
        int currentThrowValueLeft = leftInitialThreeDicesThrowValue;
        int currentThrowValueRight = rightInitialThreeDicesThrowValue;
        float randomX = 0.0f, randomY = 0.0f;
        for (int i = 0; i < dices.length; i++) {
            // Create dices based on known value & position on the screen
            if(i <= 2) {
                dices[i] = new Dice(!(currentThrowValueLeft > 0));
                currentThrowValueLeft--;
                randomX = MathUtils.random(diceContainerLeft.diceMarginFromX, diceContainerLeft.diceMarginToX);
                randomY = MathUtils.random(diceContainerLeft.diceMarginFromY, diceContainerLeft.diceMarginToY);
            } else {
                dices[i] = new Dice(!(currentThrowValueRight > 0));
                currentThrowValueRight--;
                randomX = MathUtils.random(diceContainerRight.diceMarginFromX, diceContainerRight.diceMarginToX);
                randomY = MathUtils.random(diceContainerRight.diceMarginFromY, diceContainerRight.diceMarginToY);
            }

            dices[i].position.set(randomX, randomY);
            dices[i].bounds.set(randomX, randomY, 0.45f, 0.45f);
            dices[i].dimension.set(0.45f, 0.45f);
        }
        playSound();
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
    
    public void dicesCollision(float deltaTime) {
        for (int j = 0; j < dices.length; j++) {
            for (int i = j + 1; i < dices.length; i++) {
          //  dices[i].bounds.x += dices[i].position.x * dices[i].velocity.x;
                // dices[i].bounds.y += dices[i].position.y * dices[i].velocity.y;
                dices[i].bounds.set(dices[i].position.x, dices[i].position.y, 0.45f, 0.45f);
                dices[j].bounds.set(dices[j].position.x, dices[j].position.y, 0.45f, 0.45f);

                if (dices[j].bounds.overlaps(dices[i].bounds)) {
                    //Gdx.app.log(TAG, "Collision between dice " + j + " et dice " + i);

                    ////reverting the way of the dice
                    dices[i].velocity.x = dices[i].velocity.x * -1;
                    dices[i].velocity.y = dices[i].velocity.y * -1;
                    dices[j].velocity.x = dices[j].velocity.x * -1;
                    dices[j].velocity.y = dices[j].velocity.y * -1;

                    //Increasing friction so that the dice comes to a stop shortly after collision with a wall
                    dices[i].friction.x = 0.85f * dices[i].velocity.x;
                    dices[i].friction.y = 0.85f * dices[i].velocity.y;
                    dices[j].friction.x = 0.85f * dices[j].velocity.x;
                    dices[j].friction.y = 0.85f * dices[j].velocity.y;
                }
            }
        }
    }
    
    public void playSound(){
        if(!GamePreferences.instance.soundMute) {
            diceSound.play();
        }
    }
}