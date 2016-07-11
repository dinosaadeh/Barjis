/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;//for testing
import com.badlogic.gdx.audio.Sound;
import com.smb215team.barjis.game.objects.Dice;
import com.smb215team.barjis.game.objects.DiceContainer;
import com.smb215team.barjis.game.objects.Dices;
import com.smb215team.barjis.game.objects.Pawn;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class GameController {
    private static final String TAG = GameController.class.getName();
    
    public float dummyTimerForThrowingDices = 0.0f;
    Pawn dummyPawn;
    Pawn[] dummyPawnToFillMap;
    Dice dummyDice;
    
    DiceContainer diceContainer;
    Sound diceSound = Gdx.audio.newSound(Gdx.files.internal("diceSound.mp3"));

    
    public GameController () {
        init();
    }
    
    private void init () {
        initTestObjects();
    }
    
    private void initTestObjects() {
        // <editor-fold desc="Dino: TO DELETE Dummy pawn/dice">
        dummyPawn = new Pawn();
        dummyPawnToFillMap = new Pawn[Constants.boardMap.length];
        for(int i = 0; i < dummyPawnToFillMap.length; i++) {
            dummyPawnToFillMap[i] = new Pawn();
            dummyPawnToFillMap[i].position.set(Constants.boardMap[i].x, Constants.boardMap[i].y);

        }
        dummyDice = new Dice();
        float randomX = MathUtils.random(-7.0f, -5.0f);
        float randomY = MathUtils.random(-4.0f, 0f);
        dummyDice.position.set(randomX, randomY);
        dummyDice.bounds.set(randomX, randomY, 0.45f, 0.45f);
        dummyDice.dimension.set(0.45f, 0.45f);
        // </editor-fold>

        diceContainer = new DiceContainer();  
    }
    
    public void update (float deltaTime) {
        handleDebugInput(deltaTime);
        dummyPawn.update(deltaTime);
        dummyDice.update(deltaTime);
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed
        
        testCollisions ();

        // <editor-fold desc="Dino: Dummy timer to throw dices">
        dummyTimerForThrowingDices += deltaTime;
        if(dummyTimerForThrowingDices >= 5 && Dices.instance.canPlayerThrowDices) {
            Dices.instance.throwDices(diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
            diceSound.play();
            dummyTimerForThrowingDices -= 5.0f; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
            Gdx.app.log(TAG, Dices.instance.getValue());
        }
        // </editor-fold>
    }

    public void dispose() {
        diceSound.dispose();
    }
    
    private void handleDebugInput(float deltaTime) {
        if (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Desktop) {
            return;
        }
// Selected Sprite Controls
        float sprMoveSpeed = 5 * deltaTime;
        if (Gdx.input.isKeyPressed(Keys.A)) {
            moveSelectedSprite(
                    -sprMoveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            moveSelectedSprite(sprMoveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            moveSelectedSprite(0,
                    sprMoveSpeed);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            moveSelectedSprite(0,
                    -sprMoveSpeed);
        }
    }

    private void moveSelectedSprite(float x, float y) {
        //testSprites[selectedSprite].translate(x, y);
    }
    
    private void testCollisions () {
        // <editor-fold desc="TO DELETE: Test collision dummy dice with borders">
//        if (!diceContainer.borderTop.overlaps(dummyDice.bounds) && !diceContainer.borderBottom.overlaps(dummyDice.bounds) && !diceContainer.borderLeft.overlaps(dummyDice.bounds) && !diceContainer.borderRight.overlaps(dummyDice.bounds)) {
//            dummyDice.canCollideBorderTop = true;
//            dummyDice.canCollideBorderBottom = true;
//            dummyDice.canCollideBorderLeft = true;
//            dummyDice.canCollideBorderRight = true;
//            return;
//        }
//            if (diceContainer.borderTop.overlaps(dummyDice.bounds) && dummyDice.canCollideBorderTop){
//                dummyDice.collideWithWall(false, 't');
//            }
//            if (diceContainer.borderBottom.overlaps(dummyDice.bounds) && dummyDice.canCollideBorderBottom){
//                dummyDice.collideWithWall(false, 'b');
//            }
//            if (diceContainer.borderLeft.overlaps(dummyDice.bounds) && dummyDice.canCollideBorderLeft){
//                dummyDice.collideWithWall(true, 'l');
//            }
//            if (diceContainer.borderRight.overlaps(dummyDice.bounds) && dummyDice.canCollideBorderRight){
//                dummyDice.collideWithWall(true, 'r');
//            }
        // </editor-fold>
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
}