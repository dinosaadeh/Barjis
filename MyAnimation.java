package com.mygdx.animation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class MyAnimation extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
        TextureRegion[] animationFrame;
        Animation animation;
        float elapsedtime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("des011.png");
                TextureRegion[][] tempFrame =TextureRegion.split(img,220,220);
                
                animationFrame = new TextureRegion[4];
                
                int index =0;
                 for ( int i =0 ;i<2;i++) {
                     for (int j=0;j<2;j++){
                     animationFrame[index++] =tempFrame[j][i];
                 }
                 }
                 animation = new Animation(1f/8f,animationFrame);
                     
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
                elapsedtime += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
                batch.draw(animation.getKeyFrame(elapsedtime,true), 0, 0);
		batch.end();
	}
}
