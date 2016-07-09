/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.bullet.collision.CollisionJNI;
import com.smb215team.barjis.game.Assets;

/**
 *
 * @author dinosaadeh
 */
public class Dice extends AbstractGameObject {

    private static final String TAG = Dice.class.getName();
    private TextureRegion diceTexureRegion;
    public boolean canCollideBorderTop = true;
    public boolean canCollideBorderBottom = true;
    public boolean canCollideBorderLeft = true;
    public boolean canCollideBorderRight = true;

    public Dice() {
        init(true);
    }
    
    public Dice(boolean isDiceUp) {
        init(isDiceUp);
    }
    
    private void init(boolean isDiceUp) {
        diceTexureRegion = isDiceUp ? Assets.instance.dice.diceUp : Assets.instance.dice.diceDown;
        dimension.set(0.5f, 0.5f);
        // Set physics values
        //velocity.set(0f, 1f);
        float randomVelocityX = MathUtils.random(-5f, 5f);
        float randomVelocityY = MathUtils.random(-5f, 5f);
        velocity.set(randomVelocityX, randomVelocityY);
        terminalVelocity.set(5.0f, 5.0f);
        //terminalVelocity.set(randomVelocityX + 2.0f, randomVelocityY + 2.0f);
        friction.set(0.2f, 0.2f);
        //acceleration.set(2.0f, 2.0f);
        
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(diceTexureRegion.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
         rotation, diceTexureRegion.getRegionX(), diceTexureRegion.getRegionY(), diceTexureRegion.getRegionWidth(), diceTexureRegion.getRegionHeight(), false, false);
        //Gdx.app.log(TAG, "Dice velocity: " + this.velocity.x + ", " + this.velocity.y);
    }
    
    public void collideWithWall(boolean isVerticalWall, char side) {
        switch(side){
            case 't': canCollideBorderTop = false;
                break;
            case 'b': canCollideBorderBottom = false;
                break;
            case 'l': canCollideBorderLeft = false;
                break;
            case 'r': canCollideBorderRight = false;
                break;
        }
        if(isVerticalWall)
            this.velocity.x *= -1;
        else
            this.velocity.y *= -1;
        
        //Increasing friction so that the dice comes to a stop shortly after collision with a wall
        this.friction.x = 0.85f * this.velocity.x;
        this.friction.y = 0.85f * this.velocity.y;
    }
}