/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.game.enums.PawnState;

/**
 * @author dinosaadeh
 */
public class Pawn extends AbstractGameObject {
    private static final String TAG = Dices.class.getName();
    
    private TextureRegion pawnImage;
    PawnState state;
    Vector2 deadPosition;
    /**
     * Reflects the pawn's position index on the player's path
     * - value of -1 means the pawn is dead (out of the board)
     * - value between 0 and 82, on the board within the player's path
     * - value of 83, the pawn finished its circuit.
     */
    private int positionOnPath;

    public Pawn() {
        init();
    }

    /** set the x and y position  */
    public Pawn(float x , float y){
        position.set(x, y);
        init();
    }
    
    public void init() {
        deadPosition = new Vector2(0, 0);
        init(0, deadPosition);
    }

    public void init(int pawnImageIndex, Vector2 deadPosition) {
        Assets.instance.pawn.init(pawnImageIndex);
        pawnImage = Assets.instance.pawn.pawn;
        
        dimension.set(0.435f, 0.435f);
        this.setCenter(this.dimension.x / 2, this.dimension.y / 2);
        pawnImage = Assets.instance.pawn.pawn;
        // Set physics values
        velocity.set(3.0f, 4.0f);
        terminalVelocity.set(3.0f, 4.0f);
        //friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, 25.0f);

        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        
        this.deadPosition = deadPosition;
        this.position = deadPosition;
        positionOnPath = -1;//The pawn starts as dead (out of the board)
    }

//Dino: TO VALIDATE/ELABORATE
    public boolean canMove(int steps){
        //TODO: a pawn can move if
        // One of the dice combinations allows it to move on board
        // One of the dice combinations allows it to enter the board
        
        return true;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(pawnImage.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
-         rotation, pawnImage.getRegionX(), pawnImage.getRegionY(), pawnImage.getRegionWidth(), pawnImage.getRegionHeight(), false, false);
   }
    
    public void move(int numberOfSteps) {
        try {
            if(positionOnPath + numberOfSteps > 82) {
                throw new Exception("Number of steps to add greater than the pawn can move.");
            }
            
            positionOnPath += numberOfSteps;
        }
        catch(Exception e) {
            Gdx.app.debug(TAG, e.getMessage());
        }
    }
}