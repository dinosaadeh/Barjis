package com.smb215team.barjis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Barjis extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ShapeRenderer shapeRendererTest;
        
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
                shapeRendererTest = new ShapeRenderer();
                //shapeRendererTest.rect(0, 0, 20, 50);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
                shapeRendererTest.setColor(1, 1, 1, (float)0.5);
                shapeRendererTest.begin(ShapeRenderer.ShapeType.Filled);
                shapeRendererTest.rect(10, 50, 50, 100);
                shapeRendererTest.end();
	}
}
