/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;  
import com.badlogic.gdx.math.Vector2;
import com.smb215team.barjis.game.objects.Pawn;
import com.smb215team.barjis.game.GameController;
import io.socket.client.IO; 
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import com.smb215team.barjis.game.objects.Player;

/**
 *
 * @author Naji Dagher
 */
public class UpdateServer {
    private static final String TAG = UpdateServer.class.getName();
    public static final UpdateServer instance = new UpdateServer();  
    private final float UPDATE_TIME = 1/30f;   
    boolean multiPlayerGame;
    float timer; 
    public Socket socket;  
    Player [] players; 
    
    public UpdateServer() {   
            init();
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
                    Gdx.app.log(TAG, " My id is " + id);     
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
                    Gdx.app.log(TAG, " New Player connected " + id ) ;    
                } catch (JSONException e) {
                    Gdx.app.log(TAG, " configSocket Error getting newPlayer ID");
                }
            } 
        }).once("pawnMoved" , new Emitter.Listener() {
            @Override
            public void call(Object... args) {                

                JSONObject data = (JSONObject) args[0];
                try {
                    String pawnId = data.getString("id");
                    Double positionX  = data.getDouble("x"); 
                    Double positionY  = data.getDouble("y");
                    int playerIndex = data.getInt("playerIndex");
                    int pawnIndex   = data.getInt("pawnIndex");                    
                
                            for(Player player : players) {
            for(Pawn pawn : player.pawns) {  
//here we should call the function that updates the pawn position
            }
        }
   /*                 
if (players[0].pawns[0] != null) {
              
                  players[0].pawns[0].pawnUpdatePosition(x.floatValue(), y.floatValue());
 
}*/     
                   
                } catch (JSONException e) {
                    Gdx.app.log(TAG, " configSocket Error moving Pawn");
                }
            }

        }); 
    }

    public void updatePawns(float dt,int playerIndex, int pawnIndex, Vector2 pawnNewPosition) {
        timer += dt;
   
        //if (timer > UPDATE_TIME) {
            JSONObject data = new JSONObject();
   Gdx.app.log(TAG, "pawnmove" + dt + playerIndex + pawnIndex +"");
            try {
                data.put("x",pawnNewPosition.x );
                data.put("y",pawnNewPosition.y );
                data.put("playerIndex",playerIndex);
                data.put("pawnIndex", pawnIndex);
                socket.emit("pawnMoved", data);
            } catch (JSONException e) {
                Gdx.app.log(TAG, "" + e);
            }
      //  }
    }    
}
