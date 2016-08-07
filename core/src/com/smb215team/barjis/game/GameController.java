/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.smb215team.barjis.game.objects.Dice;
import com.smb215team.barjis.game.objects.DiceContainer;
import com.smb215team.barjis.game.objects.Dices;
import com.smb215team.barjis.game.objects.Pawn;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
import com.smb215team.barjis.game.enums.GameState;
import com.smb215team.barjis.game.objects.Player;
import com.smb215team.barjis.screens.MenuScreen;
import com.smb215team.barjis.game.GameRenderer;

/**
 *
 * @author dinosaadeh
 */
public class GameController extends InputAdapter {
    private static final String TAG = GameController.class.getName();
    
    private Game game;
    private GameRenderer gameRenderer;///added by naji temporary
    GameState state;
    Player[] players;
    public int currentPlayerIndex;
    DiceContainer diceContainer;
    public float timerForThrowingDices = 0.0f;
    public float timerForPlayerTurn = 0.0f;

    
     Vector2 touchPosition = new Vector2(0,0);
    float widthText ;// contains the width of the current set text
    float heightText ;// contains the height of the current set text
     
    // <editor-fold desc="Dino: TO DELETE Dummy stuff">
    Array<Pawn> dummyPawnToFillMap = new Array<Pawn>();
    Pawn dummyPawn = new Pawn();
    // </editor-fold>
        
    public GameController (Game game) {
        this.game = game;
        init();
    }
    
    private void init () {
        state = GameState.gameStart;
        
        Dices.instance.init();
        timerForThrowingDices = 0.0f;
        ConfigurationController.initCells();

        // <editor-fold desc="Initialising players' pawns">
        players = new Player[2]; //TODO: account for variable number of players (1 (AI), 2, 4)
        currentPlayerIndex = 0;
        if(2 == players.length) {
            players[0] = new Player(3, 0, ConfigurationController.GetPawnInitialPlaceholder(0));
            players[1] = new Player(1, 1, ConfigurationController.GetPawnInitialPlaceholder(1));
        }
        else {
            for(int i = 0; i < players.length; i++) {
                players[i] = new Player(i, i, ConfigurationController.GetPawnInitialPlaceholder(i));
            }
        }
        // Placing them at starting point
        // </editor-fold>
        
        initTestObjects();
    }
    
    public void update (float deltaTime) {
        switch (state) {
            case gameStart:
                gameStart();
                break;
            case playerTurnThrowDice:
                playOneHand(deltaTime);
                break;
            case playerTurnPlayPawns:
                interpretPlayerMoves(deltaTime);
                break;
            case gameOver:
                break;
            default:
                throw new AssertionError(state.name());
        }
    }

    public void dispose() {
        Dices.instance.dispose();
    }

    private void backToMenu () {
        // switch to menu screen
        game.setScreen(new MenuScreen(game));
    }
    
    private void initTestObjects() {
        // <editor-fold desc="Dino: TO DELETE Dummy pawn/dice">
        // </editor-fold>
    }
    
    // <editor-fold desc="STATE: game starting">
    /**
     * TODO
     * play three dices on each side to decide who goes first
     */
    public void gameStart() {
        //After knowing who goes first, diceContainer needs to be re-initialised
        diceContainer = new DiceContainer("SIDE01");//TODO: this value is dummy till gameStart logic is full written
        //Once done knowing and setting who goes first, set the game state to playerTurnThrowDice
        this.state = GameState.playerTurnThrowDice;
    }
    // </editor-fold>
    
    // <editor-fold desc="STATE: dices rolling">
    public void playOneHand(float deltaTime) {
        testDicesCollisions ();
        // Play the dice
        playDices(deltaTime);

        // Once dices are fully rolled, let the player do his moves
        if(!Dices.instance.canPlayerThrowDices && Dices.instance.dicesReachedAFullStop())
            this.state = state.playerTurnPlayPawns;
    }
    
    private void playDices(float deltaTime) {
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed

        timerForThrowingDices += deltaTime;
        if(timerForThrowingDices >= 5 && Dices.instance.canPlayerThrowDices) {
            Dices.instance.throwDices(diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
            timerForThrowingDices -= 5.0f; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
          
//            pValueReturned=Dices.instance.getValue();
          //  Gdx.app.debug(TAG, "The value of the dices: " + Dices.instance.getValue());
          
          Gdx.app.debug(TAG, "The value of the dices: " );
        //  Dices.instance.getValue();
        
        }
    }
    // </editor-fold>
    
    // <editor-fold desc="STATE: player moving his pawns">
    private void interpretPlayerMoves(float deltaTime) {
        handlePlayerInput(deltaTime);
        
        timerForPlayerTurn += deltaTime;
        // Once player is finished, switch to the next player
        if(timerForThrowingDices >= 5 && Dices.instance.canPlayerThrowDices) {
            switchToNextPlayer();
        }
    }
    
    private void handlePlayerInput(float deltaTime) {
        if (Gdx.input.isTouched()){
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            //Gdx.app.log(TAG, "I'm touched :) at " + x1 + ", " + y1);

            for(Pawn pawn : players[currentPlayerIndex].pawns) {
                if(pawn.bounds.contains(x1, y1))
                    Gdx.app.log(TAG, "I'm touched at " + pawn.position.x + ", " + pawn.position.y);
            }
        }
    }
    // </editor-fold>

    /////this function specify the touch position of the player 
    //// !!! having a problem with TextWidth
    public void touchTextResult( float widthText, float heightText  )
    {       if (Gdx.input.justTouched()){
                touchPosition.set(Gdx.input.getX(), Gdx.input.getY()); 
               //  widthText = gameRenderer.layout.width;   // contains the width of the current set text
                //  heightText = gameRenderer.layout.height; // contains the height of the current set text
                  Gdx.app.debug("just-touched-anywhere",
                               " x " + touchPosition.x  +   " y " + touchPosition.y  +
                               " w " + widthText    +   " h " + heightText     );
            if( touchPosition.x >= 30 +7.5f  
              && touchPosition.x <= 30+ 7.5f  + (widthText*1.22f)
               //1.22 is the ratio between the text width and the one on the screen
              && touchPosition.y >= 80+15 
              && touchPosition.y <= 80+15+(heightText*1.22f)
              && touchPosition.x!=0 
              && touchPosition.y!=0 ){
                  Gdx.app.debug("justtouched-inside-Text",
                               " x " + touchPosition.x  +   " y " + touchPosition.y  +
                               " w " + widthText    +   " h " + heightText     );}
                                     }
}
/////this function specify the touch position of the player (to be used later when   choosing moves)
    
    private void moveSelectedSprite(float x, float y) {
        //testSprites[selectedSprite].translate(x, y);
    }

    private void testDicesCollisions () {
        // <editor-fold desc="Test collision: Dice <-> Dice borders">
        for (Dice dice : Dices.instance.dices) {
            if(null == dice)
                return;
            if (!diceContainer.borderTop.overlaps(dice.bounds) && !diceContainer.borderBottom.overlaps(dice.bounds) && !diceContainer.borderLeft.overlaps(dice.bounds) && !diceContainer.borderRight.overlaps(dice.bounds)) {
                dice.canCollideBorderTop = true;
                dice.canCollideBorderBottom = true;
                dice.canCollideBorderLeft = true;
                dice.canCollideBorderRight = true;
                continue;
            }
            if (diceContainer.borderTop.overlaps(dice.bounds) && dice.canCollideBorderTop){
                dice.collideWithWall(false, 't');
            }
            if (diceContainer.borderBottom.overlaps(dice.bounds) && dice.canCollideBorderBottom){
                dice.collideWithWall(false, 'b');
            }
            if (diceContainer.borderLeft.overlaps(dice.bounds) && dice.canCollideBorderLeft){
                dice.collideWithWall(true, 'l');
            }
            if (diceContainer.borderRight.overlaps(dice.bounds) && dice.canCollideBorderRight){
                dice.collideWithWall(true, 'r');
            }
        }
        // </editor-fold>
    }
    
    private void switchToNextPlayer() {
        if(currentPlayerIndex == players.length-1) {
            currentPlayerIndex = 0;
            diceContainer.init("SIDE01");
            
        }
        else {
            currentPlayerIndex++;
            diceContainer.init("SIDE02");
        }
        Dices.instance.reset();
    }
}