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
import com.sun.media.jfxmedia.logging.Logger;
// END Dino: TEST SPRITES

/**
 *
 * @author dinosaadeh
 */
public class GameController {
    private static final String TAG = GameController.class.getName();
    
    public Sprite[] testSprites;
    public Sprite[] branch1Cells;
    public int selectedSprite;
    
    public Sprite middleBlockSprite;

    public Sprite[] dices;
    public float dummyTimerForThrowingDices = 0.0f;
    
    public GameController () {
        init();
    }
    private void init () {
        initTestObjects();
    }
    public void update (float deltaTime) {
        handleDebugInput(deltaTime);

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
        // Creating the middle block of Barjis
        Pixmap middleBlockPixmap = createProceduralPixmap(32, 32, false);
        Texture middleBlockTexture = new Texture(middleBlockPixmap);
        middleBlockSprite = new Sprite(middleBlockTexture);
        middleBlockSprite.setSize(1.5f, 1.5f);
        middleBlockSprite.setOrigin(middleBlockSprite.getWidth() / 2.0f, middleBlockSprite.getHeight() / 2.0f);
        //Gdx.app.log("MyTag", "width: " + middleBlockSprite.getWidth() + " informative message");
        middleBlockSprite.setPosition(-0.5f, -0.5f);
        
        // branch1Cells
        branch1Cells = new Sprite[24];
        Pixmap cellPixmap = createProceduralPixmap(32, 32, false);
        // Create a new texture from pixmap data
        Texture cellTexture = new Texture(cellPixmap);
        for (int i = 0; i < branch1Cells.length; i++) {
            Sprite spr = new Sprite(cellTexture);
            spr.setSize(0.5f, 0.5f);
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
            spr.setPosition(1f, 0.5f);
            if(i == 2)//chou ma kein
                spr.setPosition(1f, 0f);
            if(i == 3)//chou ma kein
                spr.setPosition(1f, -0.5f);
            branch1Cells[i] = spr;
        }
        // branch1Cells end

// Create new array for 5 sprites
        testSprites = new Sprite[5];
// Create empty POT-sized Pixmap with 8 bit RGBA pixel data
        int width = 32;
        int height = 32;
        Pixmap pixmap = createProceduralPixmap(width, height, false);
// Create a new texture from pixmap data
        Texture texture = new Texture(pixmap);
// Create new sprites using the just created texture
        for (int i = 0; i < testSprites.length; i++) {
            Sprite spr = new Sprite(texture);
            spr.setSize(0.5f, 0.5f);
// Set origin to sprite's center
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
// Calculate random position for sprite
            //float randomX = MathUtils.random(-2.0f, 2.0f);
            //float randomY = MathUtils.random(-2.0f, 2.0f);
            spr.setPosition(-0.5f, 0);
// Put new sprite into array
            testSprites[i] = spr;
        }
// Set first sprite as selected one
        selectedSprite = 0;
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
        testSprites[selectedSprite].translate(x, y);
    }
    
    private Pixmap createProceduralPixmap(int width, int height, boolean isChire) {
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
// Fill square with red color at 50% opacity
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();
// Draw a yellow-colored X shape on square
        if(isChire)
        {
            pixmap.setColor(1, 1, 0, 1);
            pixmap.drawLine(0, 0, width, height);
            pixmap.drawLine(width, 0, 0, height);
        }
// Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }
}
