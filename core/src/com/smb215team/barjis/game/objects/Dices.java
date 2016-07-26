package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.game.GameController;

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
    public int[] currentHandMoves;
    public boolean canPlayerThrowDices;  
    Sound diceSound = Gdx.audio.newSound(Gdx.files.internal("diceSound.mp3"));
    private SpriteBatch batch ;
    public GameController gamecontroller;     
    BitmapFont returnTextFont = Assets.instance.fonts.defaultNormal;///used from wrapping the returnText
    public  GlyphLayout layout = new GlyphLayout(); // Obviously stick this in a field to avoid allocation each frame. 
    
    
    private Dices() {
        init();
    }

    /**
     * Initialises the dice when the game starts. It sets the number of all dice
     * combinations to 0.
     */
    public void init() {
        dices = new Dice[6];

        currentHandMoves = new int[8];
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
            float randomX = MathUtils.random(diceMarginFromX, diceMarginToX);//(-7.0f, -5.0f);
            float randomY = MathUtils.random(diceMarginFromY, diceMarginToY);//(-4.0f, 0f);
            dices[i].position.set(randomX, randomY);
            dices[i].bounds.set(randomX, randomY, 0.45f, 0.45f);
            dices[i].dimension.set(0.45f, 0.45f);
            //Gdx.app.log(TAG, "Position: " + randomX + ", " + randomY + ", velocity: " + dices[i].velocity);
        }
        
        diceSound.play();

     
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
    ///added by naji 
    public void getValue(SpriteBatch batch) {  
 //   getArrayValues    0       1     2    3    4    5       6        7
        String pResult[] ={"Shakki","Dest","2", "3", "4","Banj", "Bara", "Bonus"};    
        String returnText =""; //text to be returned on screen 
        String plusText ="";  // "+" in text 
        

     
       for (int i=0; i < currentHandMoves.length ; i++) {
     
       ///////this is just for testing purposes
      
            if ( currentHandMoves[i] !=0)
            { 
                 if (returnText == "")  {plusText ="";}  else {plusText=" + ";} 
                 returnText = returnText 
                           +  plusText
                           + currentHandMoves[i]
                           + "x"  
                           + pResult[i]; 
    
       layout.setText(returnTextFont, returnText); 
       returnTextFont.draw(batch, returnText, 30, 80);            
             }
//        ///Remark we should change the Xpos when its the second player
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