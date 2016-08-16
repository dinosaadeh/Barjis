/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.smb215team.barjis.game.enums.GameState;
import com.smb215team.barjis.game.objects.Dice;
import com.smb215team.barjis.game.objects.DiceContainer;
import com.smb215team.barjis.game.objects.Dices;
import com.smb215team.barjis.game.objects.Pawn;
import com.smb215team.barjis.game.objects.Player;
import com.smb215team.barjis.screens.MenuScreen;
import com.smb215team.barjis.util.Constants;

/**
 *
 * @author dinosaadeh
 */
public class GameController extends InputAdapter {
    private static final String TAG = GameController.class.getName();

    private Game game;
    public OrthographicCamera camera;
    GameState state;
    Player[] players;
    public int currentPlayerIndex;
    DiceContainer diceContainer;
    public float timerForThrowingDices = 0.0f;
    public float timerForPlayerTurn = 0.0f;
    // <editor-fold desc="click protector variables. When a click takes place, click position and deltatime are saved to compare and make sure nothing else interacts to one click">
    private Vector2 clickProtectorPosition;
    private float clickProtectorTime;
    // </editor-fold>
    private Pawn currentSelectedPawnForPlay;
    private int selectedIndexInTable;

    public Stage stage;
    private Table table;

    // <editor-fold desc="Dino: TO DELETE Dummy stuff">
    Array<Pawn> dummyPawnToFillMap = new Array<Pawn>();
    Pawn dummyPawn = new Pawn();
    Vector2 touchPosition = new Vector2(0,0);
    // </editor-fold>

    public GameController (Game game) {
        this.game = game;
        init();
    }

    private void init () {
        state = GameState.gameStart;

        Dices.instance.init();
        timerForThrowingDices = 0.0f;
        ConfigurationController.initCells();

        // <editor-fold desc="Initialising players' pawns">
        players = new Player[2]; //TODO: account for variable number of players (1 (AI), 2, 4)
        currentPlayerIndex = 0;
        if(2 == players.length) {
            players[0] = new Player(3, 0, ConfigurationController.GetPawnInitialPlaceholder(0));
            players[1] = new Player(1, 1, ConfigurationController.GetPawnInitialPlaceholder(1));
        }
        else {
            for(int i = 0; i < players.length; i++) {
                players[i] = new Player(i, i, ConfigurationController.GetPawnInitialPlaceholder(i));
            }
        }
        // </editor-fold>

        // initialize stage
        stage = new Stage(new FillViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));

        Gdx.input.setInputProcessor(stage);
        table=new Table();

    }

    public void update (float deltaTime) {
        switch (state) {
            case gameStart:
                gameStart();
                break;
            case playerTurnThrowDice:
                playOneHand(deltaTime);
                break;
            case playerTurnPlayPawns:
                interpretPlayerMoves(deltaTime);
                break;
            case gameOver:
                break;
            default:
                throw new AssertionError(state.name());
        }
    }

    public void dispose() {
        Dices.instance.dispose();
    }

    private void backToMenu () {
        // switch to menu screen
        game.setScreen(new MenuScreen(game));
    }
    
    // <editor-fold desc="STATE: game starting">
    /**
     * TODO
     * play three dices on each side to decide who goes first
     */
    public void gameStart() {
        //After knowing who goes first, diceContainer needs to be re-initialised
        diceContainer = new DiceContainer("SIDE01");//TODO: this value is dummy till gameStart logic is full written
        //Once done knowing and setting who goes first, set the game state to playerTurnThrowDice
        this.state = GameState.playerTurnThrowDice;
    }
    // </editor-fold>
    
    // <editor-fold desc="STATE: dices rolling">
    public void playOneHand(float deltaTime) {
        testDicesCollisions ();
        // Play the dice
        playDices(deltaTime);

        // Once dices are fully rolled, let the player do his moves
        if(!Dices.instance.canPlayerThrowDices && Dices.instance.dicesReachedAFullStop()) {
            //Notify the player of the cells he cannot move to (Shires occupied by opponent)
            Array<Vector2> opponentPawnsAddressesOnShire = new Array<Vector2>();
            for(int i = 0; i < players.length; i++) {
                if(currentPlayerIndex == i)
                    continue;
                opponentPawnsAddressesOnShire.addAll(players[i].pawnsOnShire());
            }
            players[currentPlayerIndex].updateAvailableMoves(opponentPawnsAddressesOnShire);
            this.state = state.playerTurnPlayPawns;
        }
    }

    private void playDices(float deltaTime) {
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed

        timerForThrowingDices += deltaTime;
        if(timerForThrowingDices >= 5 && Dices.instance.canPlayerThrowDices) {
            Dices.instance.throwDices(diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
            timerForThrowingDices -= 5.0f; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.

            fillDiceButtonText();
        }
    }

    public void fillDiceButtonText(){
        stage.clear();
        table.clear();
        // create button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Assets.instance.fonts.defaultNormal;

        for (int i = 0; i < Dices.instance.currentHandMoves.length; i++) {
            if(Dices.instance.currentHandMoves[i] == 0) {
                continue;
            }
            // create button
            TextButton button;
            // for example :if the label is just 1 x dest we should see dest without "1x"
            if(Dices.instance.currentHandMoves[i] == 1) {
                button = new TextButton(Dices.movesLabels[i], buttonStyle);
            } else {//greater then 1
                button = new TextButton(Dices.instance.currentHandMoves[i] + "x" + Dices.movesLabels[i], buttonStyle);
            }
            button.setUserObject(i);
            //listener on every button
            button.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    textClicked(e, actor);
                }
            });

            table.add(button).pad(1, 2, 1, 2);

            if(table.getChildren().size % 2 == 0) {
                // new line
                table.row();
            }
        }
        if(currentPlayerIndex == 0) {
            table.setPosition(5, 345);
        } else {
            if(currentPlayerIndex == 1) {//Dino: Needs to be changed if in the case of 4 players the placement is changed.
                table.setPosition(642, 345);
            }
        }
        table.left().top();
        stage.addActor(table);
    }

    private void testDicesCollisions () {
        // <editor-fold desc="Test collision: Dice <-> Dice borders">
        for (Dice dice : Dices.instance.dices) {
            if(null == dice)
                return;
            if (!diceContainer.borderTop.overlaps(dice.bounds) && !diceContainer.borderBottom.overlaps(dice.bounds) && !diceContainer.borderLeft.overlaps(dice.bounds) && !diceContainer.borderRight.overlaps(dice.bounds)) {
                dice.canCollideBorderTop = true;
                dice.canCollideBorderBottom = true;
                dice.canCollideBorderLeft = true;
                dice.canCollideBorderRight = true;
                continue;
            }
            if (diceContainer.borderTop.overlaps(dice.bounds) && dice.canCollideBorderTop){
                dice.collideWithWall(false, 't');
            }
            if (diceContainer.borderBottom.overlaps(dice.bounds) && dice.canCollideBorderBottom){
                dice.collideWithWall(false, 'b');
            }
            if (diceContainer.borderLeft.overlaps(dice.bounds) && dice.canCollideBorderLeft){
                dice.collideWithWall(true, 'l');
            }
            if (diceContainer.borderRight.overlaps(dice.bounds) && dice.canCollideBorderRight){
                dice.collideWithWall(true, 'r');
            }
        }
        // </editor-fold>
    }
    // </editor-fold>

    // <editor-fold desc="STATE: player moving his pawns">
    private void interpretPlayerMoves(float deltaTime) {
        handlePlayerInput(deltaTime);
        
        timerForPlayerTurn += deltaTime;
        // Once player is finished, switch to the next player
        if(timerForPlayerTurn >= 5) {
            switchToNextPlayer();
            timerForPlayerTurn -= 5.0f; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
        }
    }
    
    private void handlePlayerInput(float deltaTime) {
        if (Gdx.input.isTouched()){
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 translatedTouchedRegion = camera.unproject(new Vector3(x1, y1, 0));
            if(null !=  clickProtectorPosition && translatedTouchedRegion.x == clickProtectorPosition.x && translatedTouchedRegion.y == clickProtectorPosition.y) {
                if(deltaTime != clickProtectorTime) {
                    return;
                }
            } else {
                clickProtectorPosition = new Vector2(translatedTouchedRegion.x, translatedTouchedRegion.y);
                clickProtectorTime = deltaTime;
            }
            for(Pawn pawn : players[currentPlayerIndex].pawns) {
                if(pawn.bounds.contains(translatedTouchedRegion.x, translatedTouchedRegion.y)){
                    currentSelectedPawnForPlay = pawn;
                    changeButtonStyleIfTPawnCantPlay();
                    break;
                }
            }
        }
    }

    public void textClicked(ChangeListener.ChangeEvent e, Actor actor){
        if(currentSelectedPawnForPlay!= null) {
            selectedIndexInTable = ((Integer) actor.getUserObject());
            if(Dices.movesValues[selectedIndexInTable] < 1){
                return;// if the
            }
            currentSelectedPawnForPlay.move(Dices.movesValues[selectedIndexInTable]);
            Dices.instance.currentHandMoves[selectedIndexInTable]--;
            //re-create the button Table
            fillDiceButtonText();
            changeButtonStyleIfTPawnCantPlay();
        }
    }

    private void changeButtonStyleIfTPawnCantPlay() {
        if(currentSelectedPawnForPlay != null) {
            if(currentSelectedPawnForPlay.getPositionOnPath() == -1) {
                for (Actor button : table.getChildren()) {
                    if ((button.getUserObject()).equals(7)) {// equal the Bonus khal
                        button.setColor(1, 1, 1, 1f);
                        ((TextButton) (button)).setDisabled(false);
                    } else {
                        button.setColor(1, 1, 1, 0.5f);
                        ((TextButton) (button)).setDisabled(true);
                    }
                }
            }
            else{// Pawn is in The Game
                for(Actor button:table.getChildren()){
                    Integer indexInTable= (Integer) button.getUserObject();

                    if(currentSelectedPawnForPlay.getPositionOnPath()+Dices.movesValues[indexInTable] > Constants.DiceNumber){
                        button.setColor(1, 1, 1, 0.5f);
                        ((TextButton)(button)).setDisabled(true);
                    } else {
                        button.setColor(1, 1, 1, 1f);
                        ((TextButton)(button)).setDisabled(false);
                    }
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold desc="Helper Methods">
    private void switchToNextPlayer() {
        if(currentPlayerIndex == players.length-1) {
            currentPlayerIndex = 0;
            diceContainer.init("SIDE01");
        }
        else {
            currentPlayerIndex++;
            diceContainer.init("SIDE02");
        }
        Dices.instance.reset();
    }
    
    /**
     * This method returns true of the position on a pawn's path is a shire. Shire positions are set 
     * in constants as path index and not as x, y addresses.
     * @param positionOnPath
     * @return 
     */
    public static boolean isCellShire(int positionOnPath) {
        for(int i = 0; i < Constants.SHIRE_INDEXES.length; i++) {
            if(Constants.SHIRE_INDEXES[i] == positionOnPath)
                return true;
        }
        return false;
    }
    
    public boolean isCellOccupiedByOtherPlayers(int positionOnPath) {
        for(int i = 0; i < players.length; i++) {
            //if(curren)
        }
        return false;
    }
    // </editor-fold>
}