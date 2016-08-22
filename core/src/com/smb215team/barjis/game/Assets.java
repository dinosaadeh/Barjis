/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//////////////// testing the commit /////////////////// Elie Kassis
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();
    private AssetManager assetManager;
    
    public AssetDice dice;
    public AssetBoard board;
    public AssetDeadPawnPlaceholder deadPawnPlaceholder;
    public AssetPawn pawn;
    public PHPawnOverlapCounter phPawnOverlapCounter;
    public PawnHighlightCanMove pawnHighlightCanMove;
    public AssetFonts fonts;
    public AssetPlayerLabels playerLabels;

    // singleton: prevent instantiation from other classes
    private Assets () {}
    
    public void init (AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();
        //Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        // <editor-fold desc="create game resource objects">
        fonts = new AssetFonts();
        dice = new AssetDice(atlas);
        board = new AssetBoard(atlas);
        deadPawnPlaceholder = new AssetDeadPawnPlaceholder(atlas);
        pawn = new AssetPawn(atlas);
        phPawnOverlapCounter = new PHPawnOverlapCounter(atlas);
        pawnHighlightCanMove = new PawnHighlightCanMove(atlas);
        playerLabels = new AssetPlayerLabels(atlas);
        // </editor-fold>
    }

    @Override
    public void dispose () {
        assetManager.dispose();
        fonts.defaultNormal.dispose();
    }
    
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
    }
    
    public class AssetDice {
        public final AtlasRegion diceDown;
        public final AtlasRegion diceUp;
        
        public AssetDice (TextureAtlas atlas) {
            diceDown = atlas.findRegion("dice-down");
            diceUp = atlas.findRegion("dice-up");
        }
    }
    
    public class AssetBoard {
        public final AtlasRegion board;
        
        public AssetBoard (TextureAtlas atlas){
            board = atlas.findRegion("board");
        }
    }
    
    public class AssetPlayerLabels {
        public final AtlasRegion lblPlaceholderPlayerOff0;
        public final AtlasRegion lblPlaceholderPlayerOn0;
        public final AtlasRegion lblPlayerOff0;
        public final AtlasRegion lblPlayerOn0;
        
        public final AtlasRegion lblPlaceholderPlayerOff1;
        public final AtlasRegion lblPlaceholderPlayerOn1;
        public final AtlasRegion lblPlayerOff1;
        public final AtlasRegion lblPlayerOn1;
        
        public AssetPlayerLabels (TextureAtlas atlas){
            lblPlaceholderPlayerOff0 = atlas.findRegion("lblph-player-off-0");
            lblPlaceholderPlayerOn0 = atlas.findRegion("lblph-player-on-0");
            lblPlayerOff0 = atlas.findRegion("lbl-player-off-0");
            lblPlayerOn0 = atlas.findRegion("lbl-player-on-0");

            lblPlaceholderPlayerOff1 = atlas.findRegion("lblph-player-off-1");
            lblPlaceholderPlayerOn1 = atlas.findRegion("lblph-player-on-1");
            lblPlayerOff1 = atlas.findRegion("lbl-player-off-1");
            lblPlayerOn1 = atlas.findRegion("lbl-player-on-1");
        }
    }
    
    public class AssetDeadPawnPlaceholder {
        public final AtlasRegion deadPawnPlaceholder;
        
        public AssetDeadPawnPlaceholder (TextureAtlas atlas){
            deadPawnPlaceholder = atlas.findRegion("ph-dead-pawn");
        }
    }

    public class AssetPawn {
        public AtlasRegion pawn;
        Array<AtlasRegion> listOfPawnIcons = new Array<AtlasRegion>();
     
        public AssetPawn (TextureAtlas atlas, int pawnImageIndex){
            int pawnIconCounter = 0;
            while (null != atlas.findRegion("pawn-" + pawnIconCounter)) {
                listOfPawnIcons.add(new AtlasRegion(atlas.findRegion("pawn-" + pawnIconCounter)));
                pawnIconCounter++;
            }  
            pawn = listOfPawnIcons.get(pawnImageIndex);
        }

        public AssetPawn (TextureAtlas atlas){
            this (atlas, 0);
        }
        
        public void init() {
            init(0);
        }
        
        public void init(int pawnImageIndex) {
            pawn = listOfPawnIcons.get(pawnImageIndex);
        }
    }

    public class PHPawnOverlapCounter {
        public final AtlasRegion[] phPawnOverlapCounter = new AtlasRegion[3];
        
        public PHPawnOverlapCounter (TextureAtlas atlas){
            phPawnOverlapCounter[0] = atlas.findRegion("pawn-overlap-counter-2");
            phPawnOverlapCounter[1] = atlas.findRegion("pawn-overlap-counter-3");
            phPawnOverlapCounter[2] = atlas.findRegion("pawn-overlap-counter-4");
        }
    }
    
    public class PawnHighlightCanMove {
        public final AtlasRegion pawnHighlightCanMove;
        
        public PawnHighlightCanMove (TextureAtlas atlas){
            pawnHighlightCanMove = atlas.findRegion("pawn-highlight-can-move");
        }
    }
                
    public class AssetFonts {
        public final BitmapFont defaultNormal;
        public final BitmapFont flippedDefaultNormal;
        public AssetFonts () {
        // create three fonts using Libgdx's 15px bitmap font
        defaultNormal = new BitmapFont(Gdx.files.internal("Risque-25.fnt"));
         defaultNormal.getData().setScale(0.7f, 0.7f);

            // enable linear texture filtering for smooth fonts
        defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

            flippedDefaultNormal = new BitmapFont(Gdx.files.internal("Risque-25.fnt"),true);
            flippedDefaultNormal.getData().setScale(0.5f, 0.5f);
            // enable linear texture filtering for smooth fonts
            flippedDefaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        }
    }
}