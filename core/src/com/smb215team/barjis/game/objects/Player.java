/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author dinosaadeh
 */
public class Player {
    private static final String TAG = Dice.class.getName();

    Pawn[] pawns;
    Vector2[] path;
    
    public Player() {
        init();
    }
    
    private void init() {
        pawns = new Pawn[4];
        path = new Vector2[83];
    }
}