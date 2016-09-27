/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class HowToScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Image mainScreenName;
    private Image mainScreenLogo;
    private Table creditsTable;
    private Image textContainer;
    private ImageButton btnClose;
    private Label mainTitle;
    private Label paragraph01;
    private FitViewport fitViewport;
    public HowToScreen (Game game) {
        super(game);
        fitViewport=new FitViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);

        stage = new Stage(fitViewport);
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render (float deltaTime) {
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x94/255.0f, 0xfe/255.0f, 0xe3/255.0f, 0xff/255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
    }
    
    @Override public void resize (int width, int height) { }
    
    @Override public void show () {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.topLeft);
       
        Table leftSide = new Table();
        mainScreenName = new Image(Assets.instance.menuScreenImages.assetMainScreenName);
        leftSide.add(mainScreenName).padLeft(39.0625f).padTop(24f).left();
        leftSide.row();
        mainScreenLogo = new Image(Assets.instance.menuScreenImages.assetMainScreenLogo);
        leftSide.add(mainScreenLogo).minWidth(300f).padLeft(115f);

        mainTable.add(leftSide).left().top();
        
        Stack stack = new Stack();
        
        textContainer = new Image(Assets.instance.menuScreenImages.textContainer);
        stack.add(textContainer);
        
        btnClose = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnClose));
        btnClose.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    game.setScreen(new MenuScreen(game));
                }
            });
        btnClose.align(Align.topRight).pad(50, 0, 0, 50);
        stack.add(btnClose);
        
        creditsTable = new Table();
        creditsTable.align(Align.top);

        Label.LabelStyle rochesterFont=new Label.LabelStyle();
        rochesterFont.font= Assets.instance.fonts.defaultBig;

        mainTitle = new Label("How to Play", rochesterFont);
        mainTitle.setFontScale(0.7f);
        creditsTable.add(mainTitle).height(100f).padTop(10f);
        creditsTable.row();

        Label.LabelStyle lucidiaFont=new Label.LabelStyle();
        lucidiaFont.font=Assets.instance.fonts.LucidiaBig;

        paragraph01 = new Label("- The objective is to get pawns on the board, run the circuit & return back home.\n" +
                        "- The first to reach home with all his pawns is the winner\n" +
                        "- Dices of the game are special shells thrown on the board. Different combinations with shells facing down or up imply a certain number of moves.\n" +
                        "- 4 dice combination allow for dices replay (Dest, Banj, Shakki, Bara)\n" +
                        "- Dest = 1 dice closed and the rest is open. This allows for 10 moves\n" +
                        "- Banj = 1 dice open and the rest is closed. 8 moves with a jump booster of 17 steps if feasible\n" +
                        "- Shakki = all dices are open. 6 moves\n" +
                        "- Bara = all dices are closed. 12 moves\n" +
                        "- 2, 3, 4 = these have respectively the same number of closed pawns and have the same number of moves.\n" +
                        "- Two dice combinations include a bonus Dest & Banj. The bonus move is used to allow a pawn into the board.\n" +
                        "- A pawn is safe from being killed by opponent if it is on the home path (the middle path of player’s initial branch) or on cells marked with an X.", lucidiaFont);
        paragraph01.setFontScale(0.25f);
        paragraph01.setWrap(true);

        ScrollPane scrollPane = new ScrollPane(paragraph01);
        scrollPane.debug();
        creditsTable.add(scrollPane).width(280).height(300);
        
        stack.add(creditsTable);
        
        mainTable.add(stack).minWidth(280).height(Constants.VIEWPORT_GUI_HEIGHT-30).top().padTop(11);
        
        stage.addActor(mainTable);
    }
    
    @Override public void hide () { }
    
    @Override public void pause () { }

    @Override
    public InputProcessor getInputProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}