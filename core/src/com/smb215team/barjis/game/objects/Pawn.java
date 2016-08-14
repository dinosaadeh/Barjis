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
import java.util.ArrayList;

/**
 * @author dinosaadeh
 */
public class Pawn extends AbstractGameObject {
    private static final String TAG = Dices.class.getName();
    
    private TextureRegion pawnImage;
    private TextureRegion phPawnOverlapCounter;
    private TextureRegion pawnHighlightCanMove;
    PawnState state;
    Vector2 deadPosition;
    private Vector2[] path;
    public ArrayList<Integer> currentPossibleMoves;

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
    public Pawn(float x, float y){
        position.set(x, y);
        init();
    }
    
    public void init() {
        currentPossibleMoves = new ArrayList();
        deadPosition = new Vector2(0, 0);
        phPawnOverlapCounter = Assets.instance.phPawnOverlapCounter.phPawnOverlapCounter[0];
        pawnHighlightCanMove = Assets.instance.pawnHighlightCanMove.pawnHighlightCanMove;
        init(0, deadPosition, new Vector2[83]);
    }

    public void init(int pawnImageIndex, Vector2 deadPosition, Vector2[] playerPath) {
        Assets.instance.pawn.init(pawnImageIndex);
        pawnImage = Assets.instance.pawn.pawn;
        
        path = playerPath;
        
        dimension.set(0.435f, 0.435f);
        this.setCenter(this.dimension.x / 2, this.dimension.y / 2);
        pawnImage = Assets.instance.pawn.pawn;
        // Set physics values
        velocity.set(3.0f, 4.0f);
        terminalVelocity.set(3.0f, 4.0f);
        //friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, 25.0f);
        
        this.deadPosition = deadPosition;
        this.position = deadPosition;
        positionOnPath = -1;//The pawn starts as dead (out of the board)
        
        // Set bounding box for collision detection
        bounds.set(position.x, position.y, dimension.x, dimension.y);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(pawnImage.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
-         rotation, pawnImage.getRegionX(), pawnImage.getRegionY(), pawnImage.getRegionWidth(), pawnImage.getRegionHeight(), false, false);
        if(canMove()) {
            batch.draw(pawnHighlightCanMove.getTexture(), position.x - 0.065f, position.y - 0.065f, origin.x, origin.y, 0.58f, 0.58f, scale.x, scale.y,
    -         rotation, pawnHighlightCanMove.getRegionX(), pawnHighlightCanMove.getRegionY(), pawnHighlightCanMove.getRegionWidth(), pawnHighlightCanMove.getRegionHeight(), false, false);
        }
    }
    
    public void render(SpriteBatch batch, Integer pileCount) {
        this.render(batch);
        if(pileCount > 1) {
            phPawnOverlapCounter = Assets.instance.phPawnOverlapCounter.phPawnOverlapCounter[pileCount - 2];
            batch.draw(phPawnOverlapCounter.getTexture(), position.x + 0.27f, position.y + 0.25f, origin.x, origin.y, 0.24f, 0.24f, scale.x, scale.y,
    -         rotation, phPawnOverlapCounter.getRegionX(), phPawnOverlapCounter.getRegionY(), phPawnOverlapCounter.getRegionWidth(), phPawnOverlapCounter.getRegionHeight(), false, false);
        }
    }
    
    public void updateAvailableMoves() {
        currentPossibleMoves.clear();
        //Don't bother if the pawn is dead and no Dest or Banj showed up in the combination
        if(this.position == this.deadPosition && Dices.instance.currentHandMoves[1] == 0 &&  Dices.instance.currentHandMoves[5] == 0)
            return;
        //For every available move:
        for(int i = 0; i < Dices.instance.currentHandMoves.length; i++) {
            // This combination didn't show up to begin with
            if(Dices.instance.currentHandMoves[i] == 0)
                continue;
            // Pawn shouldn't get out of the path
            if(positionOnPath + Dices.instance.movesValues[i] > 82)
                continue;
            // TODO: Pawn cannot stand on a Shire if occupied by opponent
            
            currentPossibleMoves.add(i);
        }
    }

    public boolean canMove(){
        return currentPossibleMoves.size() > 0;
    }
    
    public void move(int numberOfSteps) {
        try {
            if(positionOnPath + numberOfSteps > 82) {
                throw new Exception("Number of steps to add greater than the pawn can move.");
            }
            
            positionOnPath += numberOfSteps;
            this.position = path[positionOnPath];
            this.bounds.set(position.x, position.y, dimension.x, dimension.y);
            
            
        }
        catch(Exception e) {
            Gdx.app.debug(TAG, e.getMessage());
        }
    }
    public int getPositionOnPath(){
        return this.positionOnPath;
    }
}