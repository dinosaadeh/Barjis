/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.game.ConfigurationController;
import com.smb215team.barjis.game.GameController;
import com.smb215team.barjis.game.enums.PawnState;
import com.smb215team.barjis.util.Constants;
import java.util.ArrayList;

/**
 * @author dinosaadeh
 */
public class Pawn extends AbstractGameObject {
    private static final String TAG = Pawn.class.getName();

    private TextureRegion pawnImage;
    private TextureRegion phPawnOverlapCounter;
    private TextureRegion pawnHighlightCanMove;
    private TextureRegion hint;
    private Array<Vector3> boardHint = new Array<Vector3>();  
    PawnState state;
    Vector2 deadPosition;
    private Vector2[] path;
    private Integer[] hintArray;
    public ArrayList<Integer> currentPossibleMoves;
    public boolean isSelected;
    Sound horseSound = Gdx.audio.newSound(Gdx.files.internal("horse.wav"));
    Sound soldierSound = Gdx.audio.newSound(Gdx.files.internal("sir-yes-sir.mp3"));
    Sound jumpSound= Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
    Sound stompSound= Gdx.audio.newSound(Gdx.files.internal("stomp.mp3"));
    Sound baDaDumSound= Gdx.audio.newSound(Gdx.files.internal("ba-da-dum.wav"));
    Sound largeCheeringSound=Gdx.audio.newSound(Gdx.files.internal("large-cheering.mp3"));
    /**
     * Reflects the pawn's position index on the player's path
     * - value of -1 means the pawn is dead (out of the board)
     * - value between 0 and 82, on the board within the player's path
     * - value of 83, the pawn finished its circuit.
     */
    public int positionOnPath, positionHint, hintIndex, rotationOnFinish;

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
        hint = Assets.instance.hint.hint;
        boardHint = ConfigurationController.hintBoardMap;
        init(0, deadPosition, new Vector2[83],new Integer[83]);
    }

    public void init(int pawnImageIndex, Vector2 deadPosition, Vector2[] playerPath,Integer[] hintPlayerArray) {
        Assets.instance.pawn.init(pawnImageIndex);
        pawnImage = Assets.instance.pawn.pawn;
        
        path = playerPath;
        hintArray = hintPlayerArray;
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
        //Render pawns when they pile on one location
        if(pileCount > 1) {
            phPawnOverlapCounter = Assets.instance.phPawnOverlapCounter.phPawnOverlapCounter[pileCount - 2];
            batch.draw(phPawnOverlapCounter.getTexture(), position.x + 0.27f, position.y + 0.25f, origin.x, origin.y, 0.24f, 0.24f, scale.x, scale.y,
    -         rotation, phPawnOverlapCounter.getRegionX(), phPawnOverlapCounter.getRegionY(), phPawnOverlapCounter.getRegionWidth(), phPawnOverlapCounter.getRegionHeight(), false, false);
        }
        
        //Render hints 
        if(isSelected) { 
            for (Integer hintPossibleMove : this.currentPossibleMoves) { 
                    this.moveHint(hintPossibleMove);
                    batch.draw(hint, boardHint.get(hintIndex).x
                                    , boardHint.get(hintIndex).y
                                    , 0.12f /2, 0.14f/2
                                    , 0.12f   , 0.14f
                                    ,  1      , 1
                                    , boardHint.get(hintIndex).z + rotationOnFinish);
            }
        }
    }
    public void updateAvailableMoves(Array<Integer> inaccessibleShireIndexes) {
        currentPossibleMoves.clear();
        Gdx.app.log(TAG,currentPossibleMoves.toString());
        //Don't bother if the pawn is dead and no Bonus showed up in the combination
        if(this.position == this.deadPosition && Dices.instance.currentHandMoves[7] == 0 )
            return;
        //For every available move:
        for(int i = 0; i < Dices.instance.currentHandMoves.length; i++) {

            if(position==deadPosition && i!=7){// just enable the Bonus Button
                continue;
            }
            // This combination didn't show up to begin with
            if(Dices.instance.currentHandMoves[i] == 0)
                continue;
            // <editor-fold desc="Pawn shouldn't get out of the path">
            // taking into account we're not evaluation the case of Banj
            if(positionOnPath + Dices.instance.movesValues[i] > 83 && i != 6)
                continue;
            // Pawn shouldn't get out of the path (taking into account we're not evaluation the case of Banj
            if(6 == i && 58 < positionOnPath && positionOnPath + 8 > 83)
                continue;
            // </editor-fold>
            // Pawn cannot stand on a Shire if occupied by opponent
            if(inaccessibleShireIndexes.contains(positionOnPath + Dices.instance.movesValues[i], true))
                continue;
            currentPossibleMoves.add(i);
        }
        
        //TODO: build the addresses of hints
    }

    public boolean canMove(){
        return currentPossibleMoves.size() > 0;
    }
    
    public Vector2 move(int numberOfSteps) {
        try {
            //Accounting for Banj: 
            //If a pawn is at the position > 58, it can move the 8 steps and NOT the 17 extra steps
            if(25 == numberOfSteps && 59 < positionOnPath) {
                    numberOfSteps = 8;
            }
            if(positionOnPath + numberOfSteps > 83) {
                throw new Exception("Number of steps to add greater than the pawn can move.");
            }
            
            positionOnPath += numberOfSteps;
            this.position = path[positionOnPath];
            this.bounds.set(position.x, position.y, dimension.x, dimension.y);
        }
        catch(Exception e) {
            Gdx.app.debug(TAG, e.getMessage());
        }
        return this.position;
    }
    
      public void moveHint(Integer hintPossibleMove) {
        try {
            if (this != null) {
                int numberOfSteps = Dices.movesValues[hintPossibleMove];
                if (25 == numberOfSteps && 58 < positionOnPath) {
                    throw new Exception("Number of steps to add greater than the pawn can move.");
                }
                positionHint = positionOnPath;
                positionHint += numberOfSteps;
                hintIndex = hintArray[positionHint];
                rotationOnFinish = positionHint > 75 ? 180 : 0;
                //Gdx.app.log(TAG, "hintindexconfig " + hintIndex + " pathposition " + positionHint + " Rotation " + rotationOnFinish);
            }
        } catch (Exception e) {
            Gdx.app.debug(TAG, e.getMessage());
        }
    }

    
    public boolean isOnShire() {
        for(int i = 0; i < Constants.SHIRE_INDEXES.length; i++) {
            if(Constants.SHIRE_INDEXES[i] == positionOnPath)
                return true;
        }
        return false;
    }
    
    public int getPositionOnPath(){
        return this.positionOnPath;
    }

    public boolean isDead(){
        return this.position==this.deadPosition;
    }

    public void die() {
        this.position = deadPosition;
        this.bounds.set(position.x, position.y, dimension.x, dimension.y);
        stompSound.play();

    }

    public void playHorseSound(){

        horseSound.play();
    }
    public void playMilitarySound(){
        soldierSound.play();
    }
    public void playJumpSound(){
        jumpSound.play();
    }
    public void playBadaDump(){baDaDumSound.play();}
    public void playLargeCheering(){largeCheeringSound.play();}
}
