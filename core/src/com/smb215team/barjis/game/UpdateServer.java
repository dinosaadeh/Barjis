/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;   
import com.smb215team.barjis.game.GameController;
import com.smb215team.barjis.game.objects.Player;
import io.socket.client.IO; 
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject; 

/**
 *
 * @author Naji Dagher
 */
public class UpdateServer {
    private static final String TAG = UpdateServer.class.getName();
  //  public static final UpdateServer instance = new UpdateServer(GameController gameController);  
    private final float UPDATE_TIME = 1/30f;   
    boolean multiPlayerGame;
    float timer; 
    public Socket socket;       
    public static int playerOrder ;
    GameController gameController;
    public UpdateServer(GameController gameController) {   
            init();
            this.gameController =gameController;
    }
 
    public void init() {      
     
        connectSocket();///function responsible of socket configuration
        configSocketEvents(); 
        
               // players =new Player[2]; 
    }       
    
    public void connectSocket() {
        try {
            socket = IO.socket("http://0.0.0.0:8082");
            socket.connect();
        } catch (Exception e) { 
            Gdx.app.log(TAG, e.toString());
        }
    }
 
    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected " ); 
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    playerOrder = data.getInt("playerOrder");
                    Gdx.app.log(TAG, "I am player" + playerOrder);  
                 } catch (JSONException e) {
                    Gdx.app.log(TAG, " configSocket Error in getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {         
                    String id = data.getString("id"); 
                    Gdx.app.log(TAG, " New Player connected  " + id ) ;    
      
                } catch (JSONException e) {
                    Gdx.app.log(TAG, " configSocket Error getting newPlayer ID");
                }
            } 
        }).on("pawnMoved" , new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String pawnId = data.getString("id");
                    int selectedIndexInTable = data.getInt("selectedIndexInTable");
                    int playerIndex = data.getInt("playerIndex");
                    int pawnIndex = data.getInt("pawnIndex");
                    Gdx.app.log(TAG, "player " + playerIndex + " pawn " + pawnIndex + " IndexInTable " + selectedIndexInTable);
                    gameController.movePawnByServer(playerIndex, pawnIndex, selectedIndexInTable,false);
                } catch (JSONException e) {
                    Gdx.app.log(TAG, " configSocket Error moving Pawn");
                }
            }

        }); 
    } 

    public void updatePawns(int playerIndex, int pawnIndex, int selectedIndexInTable) {
        timer += Gdx.graphics.getDeltaTime();

        //if (timer > UPDATE_TIME) {
        JSONObject data = new JSONObject(); 
        try {
            data.put("selectedIndexInTable", selectedIndexInTable);
            data.put("playerIndex", playerIndex);
            data.put("pawnIndex", pawnIndex);
            socket.emit("pawnMoved", data);
        } catch (JSONException e) {
            Gdx.app.log(TAG, "" + e);
        }
        //  }
    }   
} 
