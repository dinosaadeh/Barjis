package com.smb215team.barjis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.smb215team.barjis.game.Assets;
import com.smb215team.barjis.screens.MenuScreen;
import com.smb215team.barjis.game.objects.*;
import io.socket.client.IO;
import io.socket.client.Socket;

public class Barjis extends Game {
    private static final String TAG = Barjis.class.getName();
    private Socket socket;///socket communicating between Server and Client

    @Override
    public void create () {
        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Start game at menu screen
        setScreen(new MenuScreen(this)); 
        connectSocket();///function responsible of socket configuration
    }
    public void connectSocket(){
        try{
            socket =IO.socket("http://0.0.0.0:8082");
            socket.connect(); 
        }catch (Exception e){
       //     System.out.println(e);
       Gdx.app.log(TAG, e.toString());
        }
            
    }
}