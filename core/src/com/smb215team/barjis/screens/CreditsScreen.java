/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
 * @author dinosaadeh
 */
public class CreditsScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    private Stage stage;
    private Image mainScreenName;
    private Image mainScreenLogo;
    private Table creditsTable;
    private Image textContainer;
    private ImageButton btnClose;
    private Label mainTitle;
    private Label paragraph01;
    private Label subTitle01;
    private Label paragraph02;
    private Label subTitle02;
    private Label paragraph03;

    private FitViewport fitViewport;

    public CreditsScreen(Game game) {
        super(game);

        fitViewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stage = new Stage(fitViewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float deltaTime) {
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x94 / 255.0f, 0xfe / 255.0f, 0xe3 / 255.0f, 0xff / 255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        //mainTable.setDebug(true, true);
        mainTable.align(Align.topLeft);

        Table leftSide = new Table();
        mainScreenName = new Image(Assets.instance.menuScreenImages.assetMainScreenName);
        leftSide.add(mainScreenName).padLeft(39.0625f).padTop(24f).left();
        leftSide.row();
        mainScreenLogo = new Image(Assets.instance.menuScreenImages.assetMainScreenLogo);
        leftSide.add(mainScreenLogo).minWidth(300).padLeft(115f);//.size(327.34375f, 277.6f);

        mainTable.add(leftSide).left().top();

        Stack stack = new Stack();

        textContainer = new Image(Assets.instance.menuScreenImages.textContainer);
        stack.add(textContainer);

        btnClose = new ImageButton(new TextureRegionDrawable(Assets.instance.mainScreenButtons.btnClose));
        btnClose.addListener(new ChangeListener() {
            public void changed(ChangeEvent e, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        btnClose.align(Align.topRight).pad(20, 0, 0, 30);
        stack.add(btnClose);

        creditsTable = new Table();
        creditsTable.align(Align.top);

        Label.LabelStyle mainTitleStyle = new Label.LabelStyle();
        mainTitleStyle.font = Assets.instance.fonts.defaultBig;
        mainTitle = new Label("Credits", mainTitleStyle);
        mainTitle.setFontScale(0.6f);
        creditsTable.add(mainTitle).height(70f).padTop(10f);
        creditsTable.row();
        Label.LabelStyle paragraphStyle = new Label.LabelStyle();
        paragraphStyle.font = Assets.instance.fonts.LucidiaBig;

        Label.LabelStyle pinkStyle = new Label.LabelStyle();
        pinkStyle.font = Assets.instance.fonts.LucidiaBig;

        paragraph01 = new Label("This project was realised as part of a university project."
                + " The choice was open and I, @dinosaadeh, wanted to dive into the world of game development. "
                + "I’ve been wanting to develop this traditional game for a while now. So I suggested the game, "
                + "created a team and this is the outcome. I hope you all enjoy it.\n\n"
                + "Thanks to all who collaborated on realising this game:", paragraphStyle);
        paragraph01.setFontScale(0.25f);
        paragraph01.setWrap(true);
        paragraph01.setAlignment(Align.center);

        creditsTable.add(paragraph01).width(290);
        creditsTable.row();

        subTitle01 = new Label("Design", pinkStyle);
        subTitle01.setColor(Color.PINK);
        subTitle01.setFontScale(0.28f);
        creditsTable.add(subTitle01);
        creditsTable.row();

        paragraph02 = new Label("Vanessa BITAR", pinkStyle);
        paragraph02.setFontScale(0.28f);
        paragraph02.setWrap(true);
        paragraph02.setAlignment(Align.center);
        creditsTable.add(paragraph02).width(290);
        creditsTable.row();

        subTitle02 = new Label("Coding", pinkStyle);
        subTitle02.setColor(Color.PINK);
        subTitle02.setFontScale(0.28f);
        subTitle02.setAlignment(Align.center);
        creditsTable.add(subTitle02).width(290);
        creditsTable.row();

        paragraph03 = new Label("Mohieddine SAADEH (Dino)\n"
                + "Ali AMMAR", paragraphStyle);
        paragraph03.setFontScale(0.25f);
        paragraph03.setWrap(true);
        paragraph03.setAlignment(Align.center);
        creditsTable.add(paragraph03).width(290);
        creditsTable.row();

        stack.add(creditsTable);

        mainTable.add(stack).minWidth(280).height(Constants.VIEWPORT_GUI_HEIGHT - 30).top().padTop(11);

        stage.addActor(mainTable);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public InputProcessor getInputProcessor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}