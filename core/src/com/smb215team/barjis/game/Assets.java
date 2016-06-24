/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public AssetFonts fonts;

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
        // create game resource objects
        fonts = new AssetFonts();
        dice = new AssetDice(atlas);
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
    
    public class AssetFonts {
        public final BitmapFont defaultNormal;
        public AssetFonts () {
        // create three fonts using Libgdx's 15px bitmap font
        defaultNormal = new BitmapFont(Gdx.files.internal("arial-15.fnt"), true);
        // enable linear texture filtering for smooth fonts
        defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}