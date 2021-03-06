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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.smb215team.barjis.game.enums.GameState;
import com.smb215team.barjis.game.handlers.AIPlayerHandler;
import com.smb215team.barjis.game.handlers.LocalPlayerHandler;
import com.smb215team.barjis.game.handlers.NetworkPlayerHandler;
import com.smb215team.barjis.game.handlers.PlayerHandler;
import com.smb215team.barjis.game.interfaces.NetworkListener;
import com.smb215team.barjis.game.objects.Dice;
import com.smb215team.barjis.game.objects.DiceContainer;
import com.smb215team.barjis.game.objects.Dices;
import com.smb215team.barjis.game.objects.Pawn;
import com.smb215team.barjis.game.objects.Player;
import com.smb215team.barjis.screens.MenuScreen;
import com.smb215team.barjis.util.Constants;
import com.smb215team.barjis.util.GamePreferences;

/**
 *
 * @author dinosaadeh
 */
public class GameController extends InputAdapter implements NetworkListener {
    private static final String TAG = GameController.class.getName();

    protected Game game;
    public OrthographicCamera camera;
    GameState state;
    public PlayerHandler playerHandler;
    Player[] players;
    public int currentPlayerIndex;
    DiceContainer diceContainer;
    public int playerPawnUpdateIndex = 0, pawnUpdateIndex = 0;
    private int didNetworkHandlerThrowThreeDices = 0;
    // <editor-fold desc="Timers">
    public float timerForThrowingDicesAtGameStart = 0.0f;
    public float timerForThrowingDices = 0.0f;
    public float timerForPlayerWithNoMoves = 0.0f;//when a player has no moves, disable buttons and wait for an enough amount of time for the player to realise what happened
    // </editor-fold>
    // <editor-fold desc="click protector variables. When a click takes place, click position and deltatime are saved to compare and make sure nothing else interacts to one click">
    protected Vector2 clickProtectorPosition;
    protected float clickProtectorTime;
    // </editor-fold>
    protected Pawn currentSelectedPawnForPlay;
    protected int selectedIndexInTable;

    public Stage stage;
    protected Label winnerLabel;
    protected Label.LabelStyle labelStyle = new Label.LabelStyle();

    public HorizontalGroup hGroup;// put the button in it
    
    public GameController (Game game) {
        this.game = game;
        init();

    }

    protected void init () {
        // First of all, initialise the player handler
        // <editor-fold desc="WORK IN PROGRESS TAKING OUT LOGIC FROM GAME CONTROLLER TO HANDLERS">
        if(null == playerHandler) {
            switch (GamePreferences.instance.gameMode) {
                case solo: {
                    playerHandler = new LocalPlayerHandler();//AIPlayerHandler();
                    break;
                }
                case pvpLocal: {
                    playerHandler = new LocalPlayerHandler();
                    break;
                }
                case pvpNetwork: {
                    playerHandler = new NetworkPlayerHandler();
                    ((NetworkPlayerHandler) playerHandler).addListener(this);
                    break;
                }
            }
            playerHandler.initiateGame();
        }
        // </editor-fold>
        state = GameState.gameInit;
    }

    public void update (float deltaTime) {
        switch (state) {
            case gameInit:
                gameInit(deltaTime);
                break;
            case gameStart:
                gameStart(deltaTime);
                break;
            case playerTurnThrowDice:
                playOneHand(deltaTime);
                break;
            case playerTurnPlayPawns:
                interpretPlayerMoves(deltaTime);
                break;
            case gameOver:
                announceTheWinner(deltaTime);
                break;
            default:
                throw new AssertionError(state.name());
        }
    }

    public void dispose() {
        playerHandler.dispose();
        Dices.instance.dispose();
    }

    private void backToMenu () {
        // switch to menu screen
        game.setScreen(new MenuScreen(game));
    }

    // <editor-fold desc="STATE: game init">

    /**
     * Game initialisation when player handler is ready
     * @param deltaTime
     */
    public void gameInit(float deltaTime) {
        if(playerHandler.getReadiness()) {
            state = GameState.gameStart;
            Dices.instance.init();
            timerForThrowingDices = 0.0f;
            ConfigurationController.initCells();
            initWinnerLabel();

            // <editor-fold desc="Initialising players' pawns">
            players = new Player[2]; //TODO: account for variable number of players (1 (AI), 2, 4)
            currentPlayerIndex = playerHandler.getCurrentPlayerIndexPreference();

            //TODO: change the labels player 1, player 2 to You/Opponent in case of network
            if (2 == players.length) {
                players[0] = new Player(Player.PLAYER_LEFT_BRANCH, 0, ConfigurationController.GetPawnInitialPlaceholder(0));
                players[1] = new Player(Player.PLAYER_RIGHT_BRANCH, 1, ConfigurationController.GetPawnInitialPlaceholder(1));
            } else {
                for (int i = 0; i < players.length; i++) {
                    players[i] = new Player(i, i, ConfigurationController.GetPawnInitialPlaceholder(i));
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="STATE: game starting">
    /**
     * play three dices on each side to decide who goes first. 
     * Dices keep rolling until there is no tie (but this is not apparent to player)
     * Once a player is set, we wait for a moment to let the player realize who goes first and then we start the game.
     */
    public void gameStart(float deltaTime) {
        Dices.instance.update(deltaTime);
        timerForThrowingDicesAtGameStart += deltaTime;
        DiceContainer diceContainerLeft = new DiceContainer("SIDE01");
        DiceContainer diceContainerRight = new DiceContainer("SIDE02");
        testDicesCollisionsInTwoContainers (diceContainerLeft, diceContainerRight);

        // <editor-fold desc="If it is a pvpLocal, throw the dices to see who goes first">
        if(playerHandler instanceof LocalPlayerHandler) {
            while (-1 == currentPlayerIndex) {
                currentPlayerIndex = Dices.instance.throwDicesOnEachSideToDecideFirstPlayer(diceContainerLeft, diceContainerRight);
            }
        }
        // </editor-fold>

        // <editor-fold desc="If it is a pvpNetwork, throws are already known.. All that needs to be done is show players the values">
        if(playerHandler instanceof NetworkPlayerHandler && didNetworkHandlerThrowThreeDices == 0) {
            NetworkPlayerHandler temporaryHandler = (NetworkPlayerHandler) playerHandler;
            int leftInitialThreeDicesThrowValue = (0 == temporaryHandler.localPlayerIndex) ? temporaryHandler.localInitialThreeDicesThrowValue : temporaryHandler.networkInitialThreeDicesThrowValue;
            int rightInitialThreeDicesThrowValue = (1 == temporaryHandler.localPlayerIndex) ? temporaryHandler.localInitialThreeDicesThrowValue : temporaryHandler.networkInitialThreeDicesThrowValue;
            Dices.instance.throwDicesOnEachSide(diceContainerLeft, leftInitialThreeDicesThrowValue, diceContainerRight, rightInitialThreeDicesThrowValue);
            didNetworkHandlerThrowThreeDices = 1;
        }
        // </editor-fold>

        // wait for a moment for player to realise what happened
        while(timerForThrowingDicesAtGameStart < Constants.TIMER_LIMIT_FOR_THROWING_DICES_AT_GAME_START)
            return;
        //After knowing who goes first, diceContainer needs to be re-initialised
        diceContainer = new DiceContainer("SIDE0" + (currentPlayerIndex + 1));
        //Once done knowing and setting who goes first, set the game state to playerTurnThrowDice
        this.state = GameState.playerTurnThrowDice;
    }
    // </editor-fold>
    
    // <editor-fold desc="STATE: dices rolling">
    public void playOneHand(float deltaTime) {
        testDicesCollisions();

        // <editor-fold desc="If it is a pvpLocal, throw the dices with no account to anything">
        if(playerHandler instanceof LocalPlayerHandler) {
            // Play the dice
            playDices(deltaTime);

            // Once dices are fully rolled, let the player do his moves
            if(!Dices.instance.canPlayerThrowDices && Dices.instance.dicesReachedAFullStop()) {
                prepareForStatePlayerTurnPlayPawns();
                this.state = state.playerTurnPlayPawns;
            }
        }
        // </editor-fold>
        // <editor-fold desc="If it is a pvpNetwork, DINO DESCRIBE">
        if(playerHandler instanceof NetworkPlayerHandler) {
            NetworkPlayerHandler temporaryHandler = (NetworkPlayerHandler) playerHandler;
            // If it is my turn, play the dices and send the value to opponent
            if (temporaryHandler.localPlayerIndex == currentPlayerIndex) {
                int valueToSend = playDices(deltaTime);
                if (valueToSend != -1) {
                    temporaryHandler.sendDicesValue(valueToSend);

                    if (!Dices.instance.canPlayerThrowDices) {
                        prepareForStatePlayerTurnPlayPawns();
                        this.state = GameState.playerTurnPlayPawns;
                    }
                }
            }
            // If it is the opponent turn, wait for dice values
        }
        // </editor-fold>
    }

    private int playDices(float deltaTime) {
        Dices.instance.update(deltaTime);
        timerForThrowingDices += deltaTime;
        int result = -1;
        if(timerForThrowingDices >= Constants.TIMER_LIMIT_FOR_THROWING_DICES && Dices.instance.canPlayerThrowDices) {
            //TODO: take the value from throw dices and pass it to handlers
            result = Dices.instance.throwDices(diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
            timerForThrowingDices -= Constants.TIMER_LIMIT_FOR_THROWING_DICES; // If you reset it to 0 you will lose a few milliseconds every 2 seconds.

            fillDiceButtonText();
        }
        return result;
    }

    //TODO: this was a patch for the problem we didn't solve when some dices leave the container. Once solved, this method can be removed.
    InputListener screenClickListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            Dices.instance.bringDicesToFullStop();

            return super.touchDown(event, x, y, pointer, button);
        }
    };

    public void fillDiceButtonText(){
        stage.clear();
        stage.addListener(screenClickListener);
        hGroup = new HorizontalGroup();
        // create button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = Assets.instance.fonts.defaultNormal;

        // create button
        for (int i = 0; i < Dices.instance.currentHandMoves.length; i++) {
            if(Dices.instance.currentHandMoves[i] == 0) {
                continue;
            }
            TextButton button;

            // for example :if the label is just 1 x dest we should see dest without "1x"
            if(Dices.instance.currentHandMoves[i] == 1) {
                button = new TextButton((hGroup.getChildren().size == 0 ? " " : " + ") + Dices.movesLabels[i], buttonStyle);
            } else {//greater than 1
                button = new TextButton((hGroup.getChildren().size == 0 ? " " : " + ") + Dices.instance.currentHandMoves[i] + "x" + Dices.movesLabels[i], buttonStyle);

            }
            button.setUserObject(i);
            button.getLabel().setFontScale(0.6f);
            //listener on every button
            button.addListener(new ChangeListener() {
                public void changed (ChangeEvent e, Actor actor) {
                    textClicked(e, actor);
                }
            });
            hGroup.addActor(button);// add button to horizontalGroup
            hGroup.wrap(true);// wrap the data to the next line
            hGroup.left().top();// start writing from the left and the top of the rectangle
        }
        if(currentPlayerIndex == 0) {
            hGroup.setBounds(5, 230, 158, 100);// set limits for  player

        } else {
            if(currentPlayerIndex == 1) {//Dino: Needs to be changed if in the case of 4 players the placement is changed.
                hGroup.setBounds(660, 230, 158, 100); // set limits for  player 1
            }
        }
        stage.addActor(hGroup);

    }
    // </editor-fold>

    // <editor-fold desc="STATE: player moving his pawns">
    private void prepareForStatePlayerTurnPlayPawns() {
        //Notify the player of the cells he cannot move to (Shires occupied by opponent)
        players[currentPlayerIndex].opponentPawnsAddressesOnShire.clear();
        for(int i = 0; i < players.length; i++) {
            if(currentPlayerIndex == i)
                continue;
            players[currentPlayerIndex].opponentPawnsAddressesOnShire.addAll(players[i].pawnsOnShire());
        }
        players[currentPlayerIndex].updateAvailableMoves();

        timerForPlayerWithNoMoves = 0f;
        currentSelectedPawnForPlay = null;
        disableAllButtonStyle();
    }
    
    private void interpretPlayerMoves(float deltaTime) {
        //If the current player has nothing to play, switch to next player.
        if(!players[currentPlayerIndex].hasMovesToPlay()) {
            handlePlayerWithNoPossibleMoves(deltaTime);
        }
        else {        
            handlePlayerInput(deltaTime);
            if(null != currentSelectedPawnForPlay)
                enableButtonPawnCanPlay();
        }
    }

    /**
     * When a player has no moves to play, this method dims his moves buttons, waits for a short while
     * and switches to the next player.
     * @param deltaTime 
     */
    private void handlePlayerWithNoPossibleMoves(float deltaTime) {
        timerForPlayerWithNoMoves += deltaTime;
        if(timerForPlayerWithNoMoves >= Constants.TIMER_LIMIT_FOR_PLAYER_WITH_NO_MOVES) {
                switchToNextPlayer();
                timerForPlayerWithNoMoves -= Constants.TIMER_LIMIT_FOR_PLAYER_WITH_NO_MOVES; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.
        }
        else {//dim all text buttons so that the user knows he cannot play anything
            disableAllButtonStyle();
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
            for(int i = 0; i < players[currentPlayerIndex].pawns.length; i++) {
                if(players[currentPlayerIndex].pawns[i].bounds.contains(translatedTouchedRegion.x, translatedTouchedRegion.y)){
                    manageCurrentSelectedPawn(i);
                    enableButtonPawnCanPlay(); 
                    break;
                }
            }
        }
    }

    public void textClicked(ChangeListener.ChangeEvent e, Actor actor) {
        if (currentSelectedPawnForPlay != null) {
            selectedIndexInTable = ((Integer) actor.getUserObject());

            movePawn(currentPlayerIndex, pawnUpdateIndex, selectedIndexInTable);
            if (playerHandler instanceof NetworkPlayerHandler) {
                ((NetworkPlayerHandler) playerHandler).sendMoveValue(currentPlayerIndex, pawnUpdateIndex, selectedIndexInTable);
            }
        }
    }


    /**
     * this method can be access from local and remotely from server using NetworkPlayerHandler listener
     */
    public void movePawn(int playerIndex, int pawnIndex, int selectedIndexFromTable) {
        if (Dices.movesValues[selectedIndexFromTable] < 1) {
            return;
        }

        
        if (selectedIndexFromTable == 5) {// Banj is selected
            players[playerIndex].pawns[pawnIndex].playJumpSound();
        }
        
        Vector2 newPawnPosition = players[playerIndex].pawns[pawnIndex].move(Dices.movesValues[selectedIndexFromTable]);

        killPawnsOnPosition(newPawnPosition);

        if (players[playerIndex].pawns[pawnIndex].positionOnPath == 83) {
            players[playerIndex].pawns[pawnIndex].playBadaDump();
        }

        if (players[playerIndex].hasWonTheGame()) {
            this.state = state.gameOver;
            players[playerIndex].pawns[0].playCheering();
            return;
        }
        Dices.instance.currentHandMoves[selectedIndexFromTable]--;
        //re-create the button Table
        players[playerIndex].updateAvailableMoves();
        fillDiceButtonText();
        enableButtonPawnCanPlay();

    }
 
    private void enableButtonPawnCanPlay() {
        if(currentSelectedPawnForPlay != null) {

                for(Actor button:hGroup.getChildren()) {
                Integer indexForButton = (Integer) button.getUserObject();
                boolean indexIsFindInTable = false;
                for (Integer EnabledIndex : currentSelectedPawnForPlay.currentPossibleMoves) {//start inner for
                    if (EnabledIndex == indexForButton) {
                        button.setColor(1, 1, 1, 1f);
                        ((TextButton) (button)).setDisabled(false);
                        indexIsFindInTable = true;
                        break;// stop inner for
                    }
                }
                if (!indexIsFindInTable) {
                    button.setColor(1, 1, 1, 0.5f);
                    ((TextButton) (button)).setDisabled(true);
                }
            }
        }
    }

    private void disableAllButtonStyle() {
        for (Actor button : hGroup.getChildren()) {
            button.setColor(1, 1, 1, 0.5f);
            ((TextButton) (button)).setDisabled(true);
        }
    }
    
    private void killPawnsOnPosition(Vector2 position) {
        for(int i = 0; i < players.length; i++) {
            if(currentPlayerIndex == i)
                continue;
            for(Pawn pawn : players[i].pawns) {
                if(position.equals(pawn.position))
                    pawn.die();
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="STATE: game over">
    public void announceTheWinner(float deltaTime){
        if(deltaTime < 1) {
            stage.clear();

            if (players[0].hasWonTheGame()) {
                winnerLabel.setPosition(20, 200);
                stage.addActor(winnerLabel);
            } else {
                if (players[1].hasWonTheGame()) {
                    winnerLabel.setPosition(675, 200);
                    stage.addActor(winnerLabel);
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Helper Methods">
    private void switchPlayer() {
        if (currentPlayerIndex == players.length - 1) {
            currentPlayerIndex = 0;
            diceContainer.init("SIDE01");
        } else {
            currentPlayerIndex++;

            diceContainer.init("SIDE02");
        }

        Dices.instance.reset();
        currentSelectedPawnForPlay = null;
        this.state = GameState.playerTurnThrowDice;
    }

    private void switchToNextPlayer() {//TODO: Dino check if this method can be unified with the other switch player.
        switchPlayer();
        if (playerHandler instanceof NetworkPlayerHandler) {
            ((NetworkPlayerHandler) playerHandler).sendYourTurnMessage();
        }
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
    
    public void manageCurrentSelectedPawn(Integer currentPlayerPawnIndex) {
        //Resetting all pawns as not selected
        for(Player player : players) {
            for(Pawn pawn : player.pawns) {
                pawn.isSelected = false;
               
            }
        }
        players[currentPlayerIndex].pawns[currentPlayerPawnIndex].isSelected = true;
        currentSelectedPawnForPlay = players[currentPlayerIndex].pawns[currentPlayerPawnIndex];
        playerPawnUpdateIndex = currentPlayerIndex ;
        pawnUpdateIndex = currentPlayerPawnIndex;
    }

    public void initWinnerLabel(){
       labelStyle.font = Assets.instance.fonts.defaultNormal;
        winnerLabel = new Label("The Winner", labelStyle);
        winnerLabel.setFontScale(0.8f);
    }

    /**
     * Tests the dices collision with the current dice container.
     */
    private void testDicesCollisions() {
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
            //////limitations to reduce borders bypassing
            if (dice.position.x < diceContainer.borderLeft.x)
            {dice.position.x = diceContainer.borderLeft.x;
                dice.velocity.x = dice.velocity.x /3 ;}

            if (dice.position.x > diceContainer.borderRight.x)
            {dice.position.x = diceContainer.borderRight.x-0.25f;
                dice.velocity.x = dice.velocity.x /3 ;}

            if (dice.position.y > diceContainer.borderTop.y)
            {dice.position.y = diceContainer.borderTop.y;
                dice.velocity.y = dice.velocity.y /3 ;}

            if (dice.position.y < diceContainer.borderBottom.y)
            {dice.position.y = diceContainer.borderBottom.y+0.25f;
                dice.velocity.y = dice.velocity.y /3 ;}
            //////limitations to reduce borders bypassing
        }
        // </editor-fold>
    }

    private void testDicesCollisionsInTwoContainers (DiceContainer diceContainerLeft, DiceContainer diceContainerRight) {
        // <editor-fold desc="Test collision: Dice <-> Dice borders Left Side">
        for (int i = 0; i <= 2; i++) {
            Dice dice = Dices.instance.dices[i];
            if(null == dice)
                return;
            if (!diceContainerLeft.borderTop.overlaps(dice.bounds) && !diceContainerLeft.borderBottom.overlaps(dice.bounds) && !diceContainerLeft.borderLeft.overlaps(dice.bounds) && !diceContainerLeft.borderRight.overlaps(dice.bounds)) {
                dice.canCollideBorderTop = true;
                dice.canCollideBorderBottom = true;
                dice.canCollideBorderLeft = true;
                dice.canCollideBorderRight = true;
                continue;
            }
            if (diceContainerLeft.borderTop.overlaps(dice.bounds) && dice.canCollideBorderTop){
                dice.collideWithWall(false, 't');
            }
            if (diceContainerLeft.borderBottom.overlaps(dice.bounds) && dice.canCollideBorderBottom){
                dice.collideWithWall(false, 'b');
            }
            if (diceContainerLeft.borderLeft.overlaps(dice.bounds) && dice.canCollideBorderLeft){
                dice.collideWithWall(true, 'l');
            }
            if (diceContainerLeft.borderRight.overlaps(dice.bounds) && dice.canCollideBorderRight){
                dice.collideWithWall(true, 'r');
            }
            ////limitations to reduce borders bypassing
            if (dice.position.x < diceContainerLeft.borderLeft.x)
            {dice.position.x = diceContainerLeft.borderLeft.x+0.25f;
                dice.velocity.x = dice.velocity.x /3 ;}

            if (dice.position.x > diceContainerLeft.borderRight.x)
            {dice.position.x = diceContainerLeft.borderRight.x;
                dice.velocity.x = dice.velocity.x /3 ;}

            if (dice.position.y > diceContainerLeft.borderTop.y)
            {dice.position.y = diceContainerLeft.borderTop.y;
                dice.velocity.y = dice.velocity.y /3 ;}

            if (dice.position.y < diceContainerLeft.borderBottom.y)
            {dice.position.y = diceContainerLeft.borderBottom.y+0.25f;
                dice.velocity.y = dice.velocity.y /3 ;}

            //////limitations to reduce borders bypassing
        }
        // </editor-fold>
        // <editor-fold desc="Test collision: Dice <-> Dice borders Right Side">
        for (int i = 3; i < Dices.instance.dices.length; i++) {
            Dice dice = Dices.instance.dices[i];
            if(null == dice)
                return;
            if (!diceContainerRight.borderTop.overlaps(dice.bounds) && !diceContainerRight.borderBottom.overlaps(dice.bounds) && !diceContainerRight.borderLeft.overlaps(dice.bounds) && !diceContainerRight.borderRight.overlaps(dice.bounds)) {
                dice.canCollideBorderTop = true;
                dice.canCollideBorderBottom = true;
                dice.canCollideBorderLeft = true;
                dice.canCollideBorderRight = true;
                continue;
            }
            if (diceContainerRight.borderTop.overlaps(dice.bounds) && dice.canCollideBorderTop){
                dice.collideWithWall(false, 't');
            }
            if (diceContainerRight.borderBottom.overlaps(dice.bounds) && dice.canCollideBorderBottom){
                dice.collideWithWall(false, 'b');
            }
            if (diceContainerRight.borderLeft.overlaps(dice.bounds) && dice.canCollideBorderLeft){
                dice.collideWithWall(true, 'l');
            }
            if (diceContainerRight.borderRight.overlaps(dice.bounds) && dice.canCollideBorderRight){
                dice.collideWithWall(true, 'r');
            }
            //////limitations to reduce borders bypassing
            if (dice.position.x < diceContainerRight.borderLeft.x)
            {dice.position.x = diceContainerRight.borderLeft.x;
                dice.velocity.x = dice.velocity.x /3 ;}

            if (dice.position.x > diceContainerRight.borderRight.x)
            {dice.position.x = diceContainerRight.borderRight.x-0.25f;
                dice.velocity.x = dice.velocity.x /3 ;}

            if (dice.position.y > diceContainerRight.borderTop.y)
            {dice.position.y = diceContainerRight.borderTop.y;
                dice.velocity.y = dice.velocity.y /3 ;}

            if (dice.position.y < diceContainerRight.borderBottom.y)
            {dice.position.y = diceContainerRight.borderBottom.y+0.25f;
                dice.velocity.y = dice.velocity.y /3 ;}
            //////limitations to reduce borders bypassing

        }
        // </editor-fold>
    }
    // </editor-fold>

    @Override
    public void moveRemotePlayerPawn(int playerIndex, int pawnIndex, int selectedIndexFromTable) {
        movePawn(playerIndex, pawnIndex, selectedIndexFromTable);
    }

    @Override
    public void throwRemotePlayerDices(int value) {
        Dices.instance.throwDicesWithKnownValue(value, diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
        fillDiceButtonText();
    }

    @Override
    public void play() {
        switchPlayer();

    }
}