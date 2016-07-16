/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.game.enums.PawnState;

/**
 * @author dinosaadeh
 */
public class Pawn extends AbstractGameObject {

    private static final String TAG = Dices.class.getName();
    private TextureRegion pawnImage;
    PawnState state;
    //TODO remove the path after creating it with enum
    private String[] path;
    private int currentLocation;

    public Pawn() {
        init();
    }

    /** set the x and y position  */
    public Pawn(float x , float y){
        position.set(x,y);
        init();
    }

    public void init() {
        //dimension.set(0.45f, 0.45f);
        dimension.set(0.35f, 0.35f);
        pawnImage = Assets.instance.pawn.pawn;
        // Set physics values
        velocity.set(3.0f, 4.0f);
        terminalVelocity.set(3.0f, 4.0f);
        //friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, 25.0f);

        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }

//Dino: TO VALIDATE/ELABORATE
    public void move(int steps) {
        currentLocation += steps;
    }

//Dino: TO VALIDATE/ELABORATE
    public boolean canMove(int steps){
        //TODO to fix
        if(currentLocation+steps > path.length){
            return false;
        }
        return true;
    }

//Dino: TO VALIDATE/ELABORATE
    public String getCurrentCell(){
        return path[currentLocation];
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(pawnImage.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
-         rotation, pawnImage.getRegionX(), pawnImage.getRegionY(), pawnImage.getRegionWidth(), pawnImage.getRegionHeight(), false, false);
   }
}