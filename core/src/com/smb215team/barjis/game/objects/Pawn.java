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
public class Pawn extends AbstractGameObject {

    private static final String TAG = Dices.class.getName();
    private TextureRegion pawnImage;
    
    public Pawn () {
        init();
    }
    
    public void init () {
        dimension.set(0.45f, 0.45f);
        pawnImage = Assets.instance.pawn.pawn;
        // Set physics values
        velocity.set(3.0f, 4.0f);
        terminalVelocity.set(3.0f, 4.0f);
        //friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, 25.0f);
        
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(pawnImage.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
         rotation, pawnImage.getRegionX(), pawnImage.getRegionY(), pawnImage.getRegionWidth(), pawnImage.getRegionHeight(), false, false);
    }
}