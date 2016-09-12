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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private Skin skin;
    private Image mainScreenName;
    private Image mainScreenLogo;
    private Table creditsTable;
    private Image textContainer;
    private ImageButton btnClose;
    private Label mainTitle;
    private Label paragraph01;

    public HowToScreen (Game game) {
        super(game);
        skin = new Skin(Gdx.files.internal("menuSkin.json"));

        OrthographicCamera cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(false); // flip y-axis
        cameraGUI.update();

        stage = new Stage(new ScreenViewport(cameraGUI));
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
        leftSide.add(mainScreenLogo).minWidth(332.03125f).padLeft(115f);

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
        btnClose.align(Align.topRight).pad(50, 0, 0, 65);
        stack.add(btnClose);
        
        creditsTable = new Table();
        creditsTable.align(Align.top);

        mainTitle = new Label("How to Play", skin);
        mainTitle.setFontScale(1.2f);
        creditsTable.add(mainTitle).height(100f).padTop(10f);
        creditsTable.row();
       
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
                        "- A pawn is safe from being killed by opponent if it is on the home path (the middle path of player’s initial branch) or on cells marked with an X.", skin, "philosopher");
        paragraph01.setFontScale(0.55f);
        paragraph01.setWrap(true);
        
        ScrollPane scrollPane = new ScrollPane(paragraph01);
        scrollPane.debug();
        creditsTable.add(scrollPane).width(400).height(390);
        
        stack.add(creditsTable);
        
        mainTable.add(stack).height(550).top().padTop(11);
        
        stage.addActor(mainTable);
    }
    
    @Override public void hide () { }
    
    @Override public void pause () { }

    @Override
    public InputProcessor getInputProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}