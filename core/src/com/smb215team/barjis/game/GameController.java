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
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    protected Game game;
    public OrthographicCamera camera;
    GameState state;
    Player[] players;
    public int currentPlayerIndex;
    DiceContainer diceContainer;
    public int playerPawnUpdateIndex =0,pawnUpdateIndex=0;
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
    public HorizontalGroup hGroup;// put the button in it
    public GameController (Game game) {
        this.game = game;
        init();
    }

    protected void init () {
        state = GameState.gameStart;
        Dices.instance.init();
        timerForThrowingDices = 0.0f;
        ConfigurationController.initCells();

        // <editor-fold desc="Initialising players' pawns">
        players = new Player[2]; //TODO: account for variable number of players (1 (AI), 2, 4)
        currentPlayerIndex = -1;
  
        if(2 == players.length) {
            players[0] = new Player(Player.PLAYER_LEFT_BRANCH, 0, ConfigurationController.GetPawnInitialPlaceholder(0));
            players[1] = new Player(Player.PLAYER_RIGHT_BRANCH, 1, ConfigurationController.GetPawnInitialPlaceholder(1));
        }
        else {
            for(int i = 0; i < players.length; i++) {
                players[i] = new Player(i, i, ConfigurationController.GetPawnInitialPlaceholder(i));
            }
            
        }    
        // </editor-fold>
    }

    public void update (float deltaTime) {
        switch (state) {
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
        while (-1 == currentPlayerIndex) {
            currentPlayerIndex = Dices.instance.throwDicesOnEachSideToDecideFirstPlayer(diceContainerLeft, diceContainerRight);
        }
        //wait for a moment for player to realise what happened
        while(timerForThrowingDicesAtGameStart < Constants.TIMER_LIMIT_FOR_THROWING_DICES_AT_GAME_START)
            return;
        //After knowing who goes first, diceContainer needs to be re-initialised
        diceContainer = new DiceContainer("SIDE0" + (currentPlayerIndex + 1));//TODO: this value is dummy till gameStart logic is full written
        //Once done knowing and setting who goes first, set the game state to playerTurnThrowDice
        this.state = GameState.playerTurnThrowDice;
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
    
    // <editor-fold desc="STATE: dices rolling">
    public void playOneHand(float deltaTime) {
        testDicesCollisions();
        // Play the dice
        playDices(deltaTime);

        // Once dices are fully rolled, let the player do his moves
        if(!Dices.instance.canPlayerThrowDices && Dices.instance.dicesReachedAFullStop()) {
            prepareForStatePlayerTurnPlayPawns();
            this.state = state.playerTurnPlayPawns; 
        }
    }
 
    private void playDices(float deltaTime) {
        Dices.instance.update(deltaTime); //commentToDelete: later on this will be called only when needed 
        timerForThrowingDices += deltaTime;
        if(timerForThrowingDices >= Constants.TIMER_LIMIT_FOR_THROWING_DICES && Dices.instance.canPlayerThrowDices) {
            Dices.instance.throwDices(diceContainer.diceMarginFromX, diceContainer.diceMarginToX, diceContainer.diceMarginFromY, diceContainer.diceMarginToY);
            timerForThrowingDices -= Constants.TIMER_LIMIT_FOR_THROWING_DICES; // If you reset it to 0 you will loose a few milliseconds every 2 seconds.

            fillDiceButtonText();
        
        } 
    }

    public void fillDiceButtonText(){
        stage.clear();
        hGroup=new HorizontalGroup();
        // create button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font= Assets.instance.fonts.defaultNormal;

        // create button
        for (int i = 0; i < Dices.instance.currentHandMoves.length; i++) {
            if(Dices.instance.currentHandMoves[i] == 0) {
                continue;
            }
            TextButton button;

            // for example :if the label is just 1 x dest we should see dest without "1x"
            if(Dices.instance.currentHandMoves[i] == 1) {
                button = new TextButton((hGroup.getChildren().size==0?" ":" + ") +Dices.movesLabels[i], buttonStyle);
            } else {//greater than 1
                button = new TextButton((hGroup.getChildren().size ==0?" ":" + ") + Dices.instance.currentHandMoves[i] + "x" + Dices.movesLabels[i], buttonStyle);

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
            hGroup.wrap();// wrap the data to the next line
            hGroup.left().top();// start writing from the left and the top of the rectangle
        }
        if(currentPlayerIndex == 0) {
            hGroup.setBounds(5,230,180,100);// set limits for  player 1

        } else {
            if(currentPlayerIndex == 1) {//Dino: Needs to be changed if in the case of 4 players the placement is changed.
                hGroup.setBounds(660,230,180,100); // set limits for  player 1

            }
        }

        stage.addActor(hGroup);
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

    public void textClicked(ChangeListener.ChangeEvent e, Actor actor){
        if(currentSelectedPawnForPlay!= null) {
            selectedIndexInTable = ((Integer) actor.getUserObject());
  
       movePawnByServer(currentPlayerIndex,pawnUpdateIndex,selectedIndexInTable,true);    
        }
        
    }
 
 
    public void movePawnByServer (int playerIndex,int pawnIndex,int selectedIndexInTable,boolean playerHimSelf)
    {
        if(Dices.movesValues[selectedIndexInTable] < 1){ 
            return;// if the
        }
        if(players[playerIndex].pawns[pawnIndex].isDead()){// the selected is Bonus

            if(currentPlayerIndex==1){
                players[playerIndex].pawns[pawnIndex].playMilitarySound();

            }else{
                players[playerIndex].pawns[pawnIndex].playHorseSound();
            }

        }
        if(selectedIndexInTable==5){// Banj is selected
            players[playerIndex].pawns[pawnIndex].playJumpSound();
        }
        Vector2 newPawnPosition = players[playerIndex].pawns[pawnIndex].move(Dices.movesValues[selectedIndexInTable]);

        killPawnsOnPosition(newPawnPosition);

        if(players[playerIndex].pawns[pawnIndex].positionOnPath==83){
            players[playerIndex].pawns[pawnIndex].playBadaDump();
        }

        if(players[playerIndex].hasWonTheGame()) {
            this.state = state.gameOver;
            players[playerIndex].pawns[0].playLargeCheering();
            return;
        }
       if (playerHimSelf)        
       {
      Dices.instance.currentHandMoves[selectedIndexInTable]--;
        }
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
//         if ( currentPlayerIndexByServer ==0)  ////currentPlayerIndexByServer should replace currentPlayerIndex later on
//            {
//            updateServer.switchPlayerServer(1);
//            currentPlayerIndexByServer ++;
//
//      Gdx.app.log(TAG,"switchPlayerIndex " + currentPlayerIndexByServer + " playerOder " + updateServer.playerOrder );
//            }
//            else   {
//            updateServer.switchPlayerServer(0);
//            currentPlayerIndexByServer --;
//      Gdx.app.log(TAG,"switchPlayerIndex " + currentPlayerIndexByServer + " playerOder " + updateServer.playerOrder );
//                    }
        Dices.instance.reset();
        currentSelectedPawnForPlay = null;
        this.state = GameState.playerTurnThrowDice;
    }
//    public void switchPlayerByServer (int switchPlayerToIndex) {
//        currentPlayerIndexByServer =switchPlayerToIndex;
//      Gdx.app.log(TAG,"switchPlayerIndex " + currentPlayerIndexByServer + " playerOder " + updateServer.playerOrder );
//
//    }
    
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
    // </editor-fold>



}