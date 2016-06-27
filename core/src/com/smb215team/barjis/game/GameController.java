/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

// Dino: TEST SPRITES
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;//for testing
import com.smb215team.barjis.game.objects.Dices;
import com.smb215team.barjis.game.objects.Pawn;
import com.sun.media.jfxmedia.logging.Logger;
// END Dino: TEST SPRITES

/**
 *
 * @author dinosaadeh
 */
public class GameController {
    private static final String TAG = GameController.class.getName();
    
    public float dummyTimerForThrowingDices = 0.0f;
    Pawn dummyPawn;
    
    public GameController () {
        init();
    }
    
    private void init () {
        initTestObjects();
    }
    
    public void update (float deltaTime) {
        handleDebugInput(deltaTime);
        dummyPawn.update(deltaTime);
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed
        // <editor-fold desc="Dino: Dummy timer to throw dices">
        dummyTimerForThrowingDices += deltaTime;
        if(dummyTimerForThrowingDices >= 5) {
            //throwDices();
            Dices.instance.throwDicesForOneTurn();
            dummyTimerForThrowingDices -= 5.0f; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
            Gdx.app.log(TAG, Dices.instance.getValue());
        }
        // </editor-fold>
    }
    
    private void initTestObjects() {
        dummyPawn = new Pawn();
        Gdx.app.log("MyTag", "width: informative message");
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
}
