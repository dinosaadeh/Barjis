/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.smb215team.barjis.game.ConfigurationController;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dinosaadeh
 */
public class Player {
    private static final String TAG = Player.class.getName();

    public Pawn[] pawns;
    public Vector2[] path;
    private Array<Vector2> boardMap = new Array<Vector2>();
    
    public Player(int branch, int pawnImageIndex, Array<Vector2> deadPositions) {
        init(branch, pawnImageIndex, deadPositions);
    }
    
    private void init(int branch, int pawnImageIndex, Array<Vector2> deadPositions) {
        // <editor-fold desc="Dino: Getting the full path">
        path = new Vector2[83];
        int pathBuilderPointer = 0;
        ConfigurationController.initCells();
        boardMap = ConfigurationController.boardMap;
        Vector2[] initialPath = buildInitialPath(branch);
        Vector2[] lastBranchPath = buildLastBranchPath(branch);
        for(int i = 0; i < 16; i++) {
            path[i] = initialPath[i];
            pathBuilderPointer++;
        }
        
        //Second branch
        int nextBranch = branch + 1 == 4 ? 0 : branch + 1;
        Vector2[] branchPath = buildOtherBranchPath(nextBranch);
        for(int i = 0; i < branchPath.length; i++) {
            path[i + pathBuilderPointer] = branchPath[i];
        }
        pathBuilderPointer += branchPath.length;
        
        //Third branch
        nextBranch = nextBranch + 1 == 4 ? 0 : nextBranch + 1;
        branchPath = buildOtherBranchPath(nextBranch);
        for(int i = 0; i < branchPath.length; i++) {
            path[i + pathBuilderPointer] = branchPath[i];
        }
        pathBuilderPointer += branchPath.length;
        
        //Fourth branch
        nextBranch = nextBranch + 1 == 4 ? 0 : nextBranch + 1;
        branchPath = buildOtherBranchPath(nextBranch);
        for(int i = 0; i < branchPath.length; i++) {
            path[i + pathBuilderPointer] = branchPath[i];
        }
        pathBuilderPointer += branchPath.length;
        
        for(int i = 0; i < lastBranchPath.length; i++) {
            path[i + pathBuilderPointer] = lastBranchPath[i];
        }
        // </editor-fold>
        // <editor-fold desc="Initialising pawns">
        pawns = new Pawn[4];
        for(int i = 0; i < pawns.length; i++) {
            pawns[i] = new Pawn();
            pawns[i].init(pawnImageIndex, deadPositions.get(i), this.path);
        }
        // </editor-fold>
    }
    
    // <editor-fold desc="Methods that build the path for the player">
    private Vector2[] buildInitialPath(int branch) {
        //first branch 0 -> start from 0 * 24 = 0
        //second branch 1 -> start from 1 * 24 = 24
        //second branch 2 -> start from 2 * 24 = 48
        //second branch 3 -> start from 3 * 24 = 72
        Vector2[] resultToreturn = new Vector2[16];
        int indexOfAddressToStartFrom = branch * 24;
        for(int i = 0; i < 16; i++) {
            resultToreturn[i] = new Vector2(boardMap.get(indexOfAddressToStartFrom + i).x, boardMap.get(indexOfAddressToStartFrom + i).y);
        }
        return resultToreturn;
    }
    
    private Vector2[] buildOtherBranchPath(int branch) {
        Vector2[] resultToreturn = new Vector2[17];
        int indexOfAddressToStartFrom = branch * 24;
        
        //Get the left line
        for(int i = 0; i < 8; i++) {
            resultToreturn[i] = new Vector2(boardMap.get(indexOfAddressToStartFrom + 16 + i).x, boardMap.get(indexOfAddressToStartFrom + 16 + i).y);
        }
        //Get last cell in middle and right line
        int counter = 0;
         for(int i = 8; i < 17; i++) {
            resultToreturn[i] = new Vector2(boardMap.get(indexOfAddressToStartFrom + 7 + counter).x, boardMap.get(indexOfAddressToStartFrom + 7 + counter).y);
            counter++;
        }
        return resultToreturn;
    }

    private Vector2[] buildLastBranchPath(int branch) {
        Vector2[] resultToreturn = new Vector2[16];
        int indexOfAddressToStartFrom = branch * 24;
        
        //Get the left line
        for(int i = 0; i < 8; i++) {
            resultToreturn[i] = new Vector2(boardMap.get(indexOfAddressToStartFrom + 16 + i).x, boardMap.get(indexOfAddressToStartFrom + 16 + i).y);
        }
        //Get the middle line going back home
        int counter = 0;
         for(int i = 8; i < 16; i++) {
            resultToreturn[i] = new Vector2(boardMap.get(indexOfAddressToStartFrom + 7 - counter).x, boardMap.get(indexOfAddressToStartFrom + 7 - counter).y);
            counter++;
        }
        return resultToreturn;
    }
    // </editor-fold>
    
    public void render(SpriteBatch batch) {
        Map<Vector2, Integer> pawnAddresses = new HashMap<Vector2, Integer>();
        for(Pawn pawn : pawns) {
            if(pawnAddresses.containsKey(pawn.position))
                pawnAddresses.put(pawn.position, pawnAddresses.get(pawn.position) + 1);
            else
                pawnAddresses.put(pawn.position, 1);
            pawn.render(batch, pawnAddresses.get(pawn.position));
        }
    }
    
    private int getPositionByAddress(Vector2 cellAddress) {
        for(int i = 0; i < path.length; i++)
            if(path[i].equals(cellAddress))
                return i;
        return -1;
    }
    
    public Array<Vector2> pawnsOnShire() {
        Array<Vector2> resultsToReturn = new Array<Vector2>();
        for(Pawn pawn : pawns) {
            if(pawn.isOnShire())
                resultsToReturn.add(pawn.position);
        }
        return resultsToReturn;
    }
    
    public void updateAvailableMoves(Array<Vector2> opponentPawnsAddressesOnShire) {
        Array<Integer> inaccessibleShireIndexes = new Array<Integer>();
        for(Vector2 address : opponentPawnsAddressesOnShire) {
            int positionOfAddress = getPositionByAddress(address);
            if(-1 == positionOfAddress)
                continue;
            inaccessibleShireIndexes.add(positionOfAddress);
        }
        for(Pawn pawn : pawns) {
            pawn.updateAvailableMoves(inaccessibleShireIndexes);
        }
    }
    
    public boolean hasMovesToPlay() {
        for(Pawn pawn : pawns) {
            if(pawn.canMove())
                return true;
        }
        return false;
    }
}