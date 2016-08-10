package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.smb215team.barjis.game.enums.DicesValueEnum;

import java.util.EnumMap;
import java.util.Map;

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
    public EnumMap<DicesValueEnum,Integer> currentHandMoves;// enumeration map , key is diceEnum and value is the number of reputation

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

        currentHandMoves=new EnumMap<DicesValueEnum, Integer>(DicesValueEnum.class);

        canPlayerThrowDices = true;

    }

    public void reset() {
        //Reset the currentHandNumberOfMoves
        currentHandMoves=new EnumMap<DicesValueEnum, Integer>(DicesValueEnum.class);

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

        DicesValueEnum result= DicesValueEnum.getDiceValueEnumFromNumber(currentThrowValue);
        diceSound.play();

        // Setting the value of the current throw

        currentHandMoves.put(result,currentHandMoves.get(result)==null?1:currentHandMoves.get(result)+1);
        if ( DicesValueEnum.DEST.equals(result)|| DicesValueEnum.BANJ.equals(result)) // if dest or banj, add the bonus move
        {
            currentHandMoves.put(DicesValueEnum.KHAL,currentHandMoves.get(DicesValueEnum.KHAL)==null?1:currentHandMoves.get(DicesValueEnum.KHAL)+1);

        }
        // If a stopper combination (2, 3 or 4), do not allow the player to throw dices again
        if (DicesValueEnum.TWO.equals(result)|| DicesValueEnum.THREE.equals(result)|| DicesValueEnum.FOUR.equals(result)) {
             canPlayerThrowDices = false;
        }
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