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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.smb215team.barjis.game.objects.Dice;
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
    
    Rectangle dicesContainerBorderTop;
    Rectangle dicesContainerBorderBottom;
    Rectangle dicesContainerBorderLeft;
    Rectangle dicesContainerBorderRight;
    Rectangle r2 = new Rectangle();

    
    public GameController () {
        init();
    }
    
    private void init () {
        initTestObjects();
    }
    
    private void initTestObjects() {
        dummyPawn = new Pawn();
        Gdx.app.log("MyTag", "width: informative message");
        dicesContainerBorderTop = new Rectangle();
        dicesContainerBorderBottom = new Rectangle();
        dicesContainerBorderLeft = new Rectangle();
        dicesContainerBorderRight = new Rectangle();
    }  
    
    public void update (float deltaTime) {
        handleDebugInput(deltaTime);
        dummyPawn.update(deltaTime);
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed
        
        testCollisions ();

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
        dicesContainerBorderTop.set(-7.65f, 0f, 3.5f, 5);//top border
        dicesContainerBorderBottom.set(-7.65f, -4.5f, 3.5f, 0.1f);//bottom border
        dicesContainerBorderLeft.set(-7.65f, -4.5f, 0.1f, 4.5f);//left border
        dicesContainerBorderRight.set(-4.2f, -4.5f, 0.1f, 4.5f);//right border
        
        // Test collision: Dice <-> Dice border(s)
        int counter = 0;
        for (Dice dice : Dices.instance.dices) {
            counter++;
            if(null == dice)
                return;
            r2.set(dice.position.x, dice.position.y, dice.bounds.width, dice.bounds.height);

            if (!dicesContainerBorderTop.overlaps(r2) && !dicesContainerBorderBottom.overlaps(r2) && !dicesContainerBorderLeft.overlaps(r2) && !dicesContainerBorderRight.overlaps(r2)) continue;
            if (dicesContainerBorderTop.overlaps(r2)){
                dice.velocity.set(0, -1f);
                dice.acceleration.set(0, 0);
            }
            if (dicesContainerBorderBottom.overlaps(r2)){
                dice.velocity.set(0, 1f);
                dice.acceleration.set(0, 0);
            }
            if (dicesContainerBorderLeft.overlaps(r2)){
                dice.velocity.set(0, -1f);
                dice.acceleration.set(0, 0);
            }
            if (dicesContainerBorderRight.overlaps(r2)){
                dice.velocity.set(0, -1f);
                dice.acceleration.set(0, 0);
            }
        }
    }
}
