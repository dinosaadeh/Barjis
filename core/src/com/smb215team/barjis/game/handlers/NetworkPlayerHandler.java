/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.handlers;

import com.badlogic.gdx.Gdx;
import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.DataEvent;
import com.netease.pomelo.DataListener;
import com.netease.pomelo.PomeloClient;
import com.smb215team.barjis.game.ConfigurationController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 *
 * @author dinosaadeh
 */
public class NetworkPlayerHandler implements PlayerHandler {
    private static final String TAG = NetworkPlayerHandler.class.getName();
    public int initialThreeDicesThrowValue;
    private PomeloClient client;
    private String playerId;
    /**
     * This index tells the local NetworkPlayerHandler if it is his turn, or the opponent's
     */
    private int playerIndex;

    /**
     * Create a user ID to send to the game server
     * Game server returns the player index of this network player handler (is this player handler, player 1 or 2)
     * Game server returns the initial 3 dices value to see who goes first.
     */
    @Override
    public void initiateGame() {
        playerId = UUID.randomUUID().toString();
        connectToGameServer();
    }
    
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // <editor-fold desc="Helper Methods">
    public void connectToGameServer() {
        try {
            String[] PomeloGateAddress = ConfigurationController.getPomeloGameServerGateAddress().split(":");
            client = new PomeloClient(PomeloGateAddress[0], Integer.parseInt(PomeloGateAddress[1]));
            client.init();
            queryEntry();

        } catch (Exception e) {
            Gdx.app.log(TAG, e.toString());
        }
    }

    private void initListeners() {
        client.on("startGame", new DataListener() {// the player 0 should start the game and send the result
            @Override
            public void receiveData(DataEvent event) {
                JSONObject msg = event.getMessage();
                try {
                    JSONArray result = msg.getJSONObject("body").getJSONArray("playerInfo");

                    for (int i = 0; i < result.length(); i++) {

                        if (playerId.equals(result.getJSONObject(i).getString("username"))) {

                            playerIndex = Integer.parseInt(result.getJSONObject(i).getString("playerIndex"));

                            initialThreeDicesThrowValue = Integer.parseInt(result.getJSONObject(i).getString("initialThreeDicesThrowValue"));

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        client.on("wait", new DataListener() {// only one player , we should create loading text and do nothing
            @Override
            public void receiveData(DataEvent event) {
                Gdx.app.log(TAG, "user should wait");
            }

        });


    }

    /**
     * This method connects to the gate of the Pomelo server. The gate load balances requests
     * and sends back the IP and port of the connector that will deal with the player device.
     */
    private void queryEntry() {
        JSONObject msg = new JSONObject();
        try {
            msg.put("uid", playerId);
            client.request("gate.gateHandler.queryEntry", msg,
                    new DataCallBack() {
                        @Override
                        public void responseData(JSONObject msg) {
                            client.disconnect();
                            try {
                                String ip = msg.getString("host");
                                enter(ip, msg.getInt("port"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method connects to the connector designated after initiating the connection with the Pomelo gate
     * @param host the connector IP (returned by the pomelo gate)
     * @param port the connector port (returned by the pomelo gate)
     */
    private void enter(final String host, final int port) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("username", playerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client = new PomeloClient(host, port);
        client.init();
        client.request("connector.entryHandler.enter", msg, new DataCallBack() {
            @Override
            public void responseData(JSONObject msg) {
//                if (msg.has("error")) {
//                    //TODO: after creating all your messaging logic, reconfirm you don't need to do anything here.
//                    client.disconnect();
//                    client = new PomeloClient(host, port);
//                    client.init();
//                    return;
//                }
//                try {
//                    JSONArray jr = msg.getJSONArray("users");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
        initListeners();
    }
    // </editor-fold>
}