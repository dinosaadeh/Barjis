/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.smb215team.barjis.game.Assets;
import java.util.Map;
import jdk.nashorn.internal.objects.NativeArray;

/**
 *
 * @author dinosaadeh
 */
public class Dices extends AbstractGameObject {
    public static final Dices instance = new Dices();
    public Sprite[] dices;
    int value;


    private Dices () {
        init();
    }
    
    public void init () {
        dimension.set(0.5f, 0.5f);
        dices = new Sprite[6];
        value = 0;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
    }
    
    public void render(SpriteBatch batch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void throwDices(){
        for(int i = 0; i < dices.length; i++){
            if(Math.random() < 0.5){
                dices[i] = new Sprite(Assets.instance.dice.diceUp);
            } else {
                dices[i] = new Sprite(Assets.instance.dice.diceDown);
                value++;
            }
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            dices[i].setPosition(randomX, randomY);
            dices[i].setSize(0.75f, 0.75f);
        }
    }
    
    public void getValue() {
        for (Sprite dice : dices) {
            //if dice
        }
    }
}
