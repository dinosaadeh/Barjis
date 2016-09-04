/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//////////////// testing the commit /////////////////// Elie Kassis!!
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
    public AssetHint hint;
    public AssetMainScreenLogo mainScreenLogo;
    public AssetMainScreenButtons mainScreenButtons;

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
        hint = new AssetHint(atlas);
        board = new AssetBoard(atlas);
        deadPawnPlaceholder = new AssetDeadPawnPlaceholder(atlas);
        pawn = new AssetPawn(atlas);
        phPawnOverlapCounter = new PHPawnOverlapCounter(atlas);
        pawnHighlightCanMove = new PawnHighlightCanMove(atlas);
        playerLabels = new AssetPlayerLabels(atlas);
        hint = new AssetHint(atlas);
        mainScreenLogo = new AssetMainScreenLogo(atlas);
        mainScreenButtons = new AssetMainScreenButtons(atlas);
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
    
    public static class AssetHint {
        public final AtlasRegion hint;

        public AssetHint(TextureAtlas atlas) {
            hint = atlas.findRegion("hint");
        }
    }
    
    public class AssetFonts {
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultSmall;
        public final BitmapFont rochesterScaled;
        public AssetFonts() {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("Rochester-20.fnt"), false);

            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

            defaultNormal = new BitmapFont(Gdx.files.internal("Rochester-30.fnt"), false);
            // enable linear texture filtering for smooth fonts
            defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

            rochesterScaled=new BitmapFont(Gdx.files.internal("Rochester-30.fnt"), false);
            rochesterScaled.getData().setScale(0.6f);
            rochesterScaled.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        }
    }
    
    // <editor-fold desc="Main screen assets">
    public class AssetMainScreenLogo {
        public final AtlasRegion assetMainScreenLogo;
        public final AtlasRegion assetMainScreenName;

        public AssetMainScreenLogo (TextureAtlas atlas){
            assetMainScreenLogo = atlas.findRegion("main-screen-logo");
            assetMainScreenName = atlas.findRegion("main-screen-name");
        }
    }

    public class AssetMainScreenButtons {
        public final AtlasRegion btnPlaySolo;
        public final AtlasRegion btnPvp;
        public final AtlasRegion btnHowToPlay;
        public final AtlasRegion btnCredits;
        public final AtlasRegion btnSoundOff;
        public final AtlasRegion btnSoundOn;
        public final AtlasRegion btnClose;
        public final AtlasRegion btnsSeparator;

        public AssetMainScreenButtons (TextureAtlas atlas){
            btnPlaySolo = atlas.findRegion("btn-play-solo");
            btnPvp = atlas.findRegion("btn-pvp");
            btnHowToPlay = atlas.findRegion("btn-how-to-play");
            btnCredits = atlas.findRegion("btn-credits");
            btnSoundOff = atlas.findRegion("btn-sound-off");
            btnSoundOn = atlas.findRegion("btn-sound-on");
            btnClose = atlas.findRegion("btn-close");
            btnsSeparator = atlas.findRegion("main-screen-buttons-separator");
        }
    }
    // </editor-fold>
}