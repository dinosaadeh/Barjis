/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.smb215team.barjis.game.Assets;

public abstract class AbstractGameScreen implements Screen {

	protected Game game;

	public AbstractGameScreen (Game game) {
		this.game = game;
	}

	public abstract void render (float deltaTime);

	public abstract void resize (int width, int height);

	public abstract void show ();

	public abstract void hide ();

	public abstract void pause ();

	public abstract InputProcessor getInputProcessor ();

	public void resume () {
		Assets.instance.init(new AssetManager());
	}

	public void dispose () {
		Assets.instance.dispose();
	}
}