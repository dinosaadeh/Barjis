/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.StreamUtils;
import com.smb215team.barjis.Barjis;
import com.smb215team.barjis.facebook.FacebookService;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Ammar
 */
public class MultiplayerGameRenderer extends GameRenderer implements Disposable {

    TextureRegion image;
    BitmapFont font;
    SpriteBatch batch;
    FacebookService facebookService;


    public MultiplayerGameRenderer(MultiplayerGameController gameController) {
        super(gameController);
        batch = new SpriteBatch();
        font = Assets.instance.fonts.defaultNormal;
        facebookService = ((Barjis) gameController.game).facebookService;
//        facebookService.login(new LoginHandler() {
//            @Override
//            public void success(boolean result) {
//                downloadImage();
//            }
//        });
//        downloadImage();
    }


    @Override
    public void render() {
        super.render();
//        if (image != null) {
//            batch.begin();
//            batch.draw(image, 500, 50);
//            batch.end();
//        } else {
//            batch.begin();
//            font.draw(batch, "Downloading...", 500, 50);
//            batch.end();
//        }
    }


    // if the player connect to FACEBOOK
//    @Override
//    protected void initLabels() {
//    super.initLabels();
//        //// TODO: 10/5/16
//    }


    // to read facebook image we should download it first
    // this method will be called if the loginToFacebook is succeeded
    public void downloadImage() {

        if (image == null)
            new Thread(new Runnable() {
                /**
                 * Downloads the content of the specified url to the array. The
                 * array has to be big enough.
                 */
                private int download(byte[] out, String url) {
                    InputStream in = null;
                    try {
                        HttpURLConnection conn = null;
                        conn = (HttpURLConnection) new URL(url)
                                .openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(false);
                        conn.setUseCaches(true);
                        conn.connect();
                        in = conn.getInputStream();
                        int readBytes = 0;
                        while (true) {
                            int length = in.read(out, readBytes, out.length
                                    - readBytes);
                            if (length == -1)
                                break;
                            readBytes += length;
                        }
                        return readBytes;
                    } catch (Exception ex) {
                        return 0;
                    } finally {
                        StreamUtils.closeQuietly(in);
                    }
                }

                @Override
                public void run() {
                    byte[] bytes = new byte[200 * 1024]; // assuming the content
                    // is
                    // not bigger than
                    // 200kb.

                    int numBytes = download(bytes, facebookService.getPictureURL());
                    if (numBytes != 0) {
                        // load the pixmap, make it a power of two if necessary
                        // (not
                        // needed for GL ES 2.0!)
                        Pixmap pixmap = new Pixmap(bytes, 0, numBytes);
                        final int originalWidth = pixmap.getWidth();
                        final int originalHeight = pixmap.getHeight();
                        int width = MathUtils.nextPowerOfTwo(pixmap.getWidth());
                        int height = MathUtils.nextPowerOfTwo(pixmap
                                .getHeight());
                        final Pixmap potPixmap = new Pixmap(width, height,
                                pixmap.getFormat());
                        potPixmap.drawPixmap(pixmap, 0, 0, 0, 0,
                                pixmap.getWidth(), pixmap.getHeight());
                        pixmap.dispose();
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                image = new TextureRegion(
                                        new Texture(potPixmap), 0, 0,
                                        originalWidth, originalHeight);
                            }
                        });
                    }
                }
            }).start();

    }


}