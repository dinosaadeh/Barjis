/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.smb215team.barjis.game.Assets;

/**
 *
 * @author dinosaadeh
 */
public class Dice extends AbstractGameObject {

    private static final String TAG = Dice.class.getName();
    private TextureRegion diceTexureRegion;

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
        float randomVelocityX = MathUtils.random(-1f,1f);
        float randomVelocityY = MathUtils.random(-1f,1f);
        velocity.set(randomVelocityX, randomVelocityY);
        //terminalVelocity.set(3.0f, 4.0f);
        terminalVelocity.set(randomVelocityX + 3.0f, randomVelocityY + 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, 25.0f);
        
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(diceTexureRegion.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
         rotation, diceTexureRegion.getRegionX(), diceTexureRegion.getRegionY(), diceTexureRegion.getRegionWidth(), diceTexureRegion.getRegionHeight(), false, false);
    }    
}