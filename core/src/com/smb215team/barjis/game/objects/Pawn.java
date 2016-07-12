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
import com.smb215team.barjis.util.Constants;

/**
 * @author dinosaadeh
 */
public class Pawn extends AbstractGameObject {

    private static final String TAG = Dices.class.getName();
    private TextureRegion pawnImage;
    //TODO remove the path after creating it with enum
    private String[] path;
    private int currentLocation;

    public Pawn() {
        init();
    }

    public void init() {
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
//TODO remove the path after creating it with enum
    public void fillPathForPlayer1() {
        this.path = new String[]{
                "A9", "A10", "A11", "A12", "A13", "A14", "A15", "A16", "A24", "A23", "A22", "A21", "A20", "A19", "A18", "A17",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B16", "B24", "B23", "B22", "B21", "B20", "B19", "B18", "B17",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C16", "C24", "C23", "C22", "C21", "C20", "C19", "C18", "C17",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D16", "D24", "D23", "D22", "D21", "D20", "D19", "D18", "D17",
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A16", "A15", "A14", "A13", "A12", "A11", "A10", "A9"};
    }
//TODO remove the path after creating it with enum

    public void fillPathForPlayer2() {
        this.path = new String[]{
                "C9", "C10", "C11", "C12", "C13", "C14", "C15", "C16", "C24", "C23", "C22", "C21", "C20", "C19", "C18", "C17",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D16", "D24", "D23", "D22", "D21", "D20", "D19", "D18", "D17",
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A16", "A24", "A23", "A22", "A21", "A20", "A19", "A18", "A17",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B16", "B24", "B23", "B22", "B21", "B20", "B19", "B18", "B17",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C16", "C15", "C14", "C13", "C12", "C11", "C10", "C9"};
    }

    public void move(int steps) {


        currentLocation += steps;

    }

    public boolean canMove(int steps){
        //TODO to fix
        if(currentLocation+steps > path.length){
            return false;
        }
        return true;
    }

    public String getCurrentCell(){
        return path[currentLocation];
    }


    
    @Override
    public void render(SpriteBatch batch) {

        for(Cell cell: Constants.CELLS){

            batch.draw(pawnImage.getTexture(), cell.getVector().x, cell.getVector().y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                    rotation, pawnImage.getRegionX(), pawnImage.getRegionY(), pawnImage.getRegionWidth(), pawnImage.getRegionHeight(), false, false);

        }

   }
}