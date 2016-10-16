/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.handlers;

import com.badlogic.gdx.Gdx;
import com.smb215team.barjis.game.ConfigurationController;
import com.netease.pomelo.PomeloClient;
import com.netease.pomelo.DataCallBack;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.UUID;

/**
 *
 * @author dinosaadeh
 */
public class NetworkPlayerHandler implements PlayerHandler {
    private static final String TAG = NetworkPlayerHandler.class.getName();
    private PomeloClient client;
    private String playerId;
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
            msg.put("rid", "smb215");//TODO: remove this.. The room name will be created and controlled by the server
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client = new PomeloClient(host, port);
        client.init();
        client.request("connector.entryHandler.enter", msg, new DataCallBack() {
            @Override
            public void responseData(JSONObject msg) {
                if (msg.has("error")) {
                    //TODO: after creating all your messaging logic, reconfirm you don't need to do anything here.
                    client.disconnect();
                    client = new PomeloClient(host, port);
                    client.init();
                    return;
                }
                try {
                    JSONArray jr = msg.getJSONArray("users");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // </editor-fold>
}