/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.handlers;

import com.badlogic.gdx.Gdx;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 *
 * @author dinosaadeh
 */
public class NetworkPlayerHandler implements PlayerHandler {
    private static final String TAG = NetworkPlayerHandler.class.getName();
    public Socket socket;

    @Override
    public void initiateGame() {
        connectSocket();
    }
    
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // <editor-fold desc="Helper Methods">
    public void connectSocket() {
        try {
            //PomeloClient client = new PomeloClient("localhost",8081); an example

            socket = IO.socket("http://localhost:8082");//("http://192.168.1.106:8082");
            socket.connect();
        } catch (Exception e) {
            Gdx.app.log(TAG, e.toString());
        }
    }
    // </editor-fold>
}