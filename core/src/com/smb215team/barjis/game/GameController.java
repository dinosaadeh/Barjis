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
import com.smb215team.barjis.game.enums.GameState;
import com.smb215team.barjis.game.objects.Player;
import com.smb215team.barjis.screens.MenuScreen;

/**
 *
 * @author dinosaadeh
 */
public class GameController {
    private static final String TAG = GameController.class.getName();
    
    private Game game;
    GameState state;
    Player[] players;
    int currentPlayerIndex;
    DiceContainer diceContainer;
    public float timerForThrowingDices = 0.0f;
    
    // <editor-fold desc="Dino: TO DELETE Dummy stuff">
    Array<Pawn> dummyPawnToFillMap=new Array<Pawn>();
    // </editor-fold>
        
    public GameController (Game game) {
        this.game = game;
        init();
    }
    
    private void init () {
        state = GameState.gameStart;
        Dices.instance.init();
        diceContainer = new DiceContainer();
        timerForThrowingDices = 0.0f;
        ConfigurationController.initCells();

        players = new Player[2]; //TODO: account for variable number of players (1, 2, 3, 4)
        currentPlayerIndex = 0;
        for(int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }
        
        initTestObjects();
    }
    
    private void initTestObjects() {
        // <editor-fold desc="Dino: TO DELETE Dummy pawn/dice">
        //TODO delete it "ammar" , test to see reading from xml file
        for(Vector2 cell:ConfigurationController.boardMap){
            dummyPawnToFillMap.add(new Pawn(cell.x,cell.y));

        }

        // </editor-fold>
    }
    
    public void update (float deltaTime) {
        switch (state) {
            case gameStart:
                gameStart();
                break;
            case playerTurn:
                playOneHand(deltaTime);
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
    
    /**
     * TODO
     * play three dices on each side to decide who goes first
     */
    public void gameStart() {
        //Once done knowing and setting who goes first, set the game state to playerTurn
        this.state = GameState.playerTurn;
    }
    
    public void playOneHand(float deltaTime) {
        //Based on which player index I know where to play the dice
        switch(currentPlayerIndex) {
            case 0: diceContainer.init("SIDE01");
                break;
            case 1: diceContainer.init("SIDE02");
                break;
            default: diceContainer.init("SIDE01");
        }
        
        // Play the dice
        playDices(deltaTime);
        
//        if(!Dices.instance.canPlayerThrowDices)
//            switchToNextPlayer();
        // Once player is finished, switch to the next player
    }
    
    private void playDices(float deltaTime) {
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed
        
        testDicesCollisions ();

        timerForThrowingDices += deltaTime;
        if(timerForThrowingDices >= 5 && Dices.instance.canPlayerThrowDices) {
            Dices.instance.throwDices(diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
            timerForThrowingDices -= 5.0f; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
            Gdx.app.log(TAG, Dices.instance.getValue());
        }
    }
    
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
        if(currentPlayerIndex == players.length-1)
            currentPlayerIndex = 0;
        else
            currentPlayerIndex++;
        Dices.instance.canPlayerThrowDices = true;
    }
}