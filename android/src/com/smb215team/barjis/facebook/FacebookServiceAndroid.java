package com.smb215team.barjis.facebook;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.smb215team.barjis.AndroidLauncher;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FacebookServiceAndroid implements FacebookService {

    private AndroidLauncher androidLauncher;
    public CallbackManager callbackManager;
    private String userID;
    private final String TAG = this.getClass().getSimpleName();
    private String pictureUrl;

    public FacebookServiceAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    public void login(final LoginHandler loginHandler) {
        callbackManager = CallbackManager.Factory.create();

        List<String> permission = new ArrayList<>();
        CallbackManager cm;

        permission.add("user_photos");
        permission.add("public_profile");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken tok;
                        tok = AccessToken.getCurrentAccessToken();
                        userID = tok.getUserId();
                        Gdx.app.log(TAG, tok.getUserId());
                        Bundle params = new Bundle();
                        params.putBoolean("redirect", false);


                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/" + userID + "/picture",
                                params,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        try {

                                            pictureUrl = (String) response.getJSONObject().getJSONObject("data").get("url");
                                            loginHandler.success(true);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        ).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        loginHandler.success(false);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        loginHandler.success(false);
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(androidLauncher, permission);

    }

    @Override
    public String getPictureURL() {

        return this.pictureUrl;
    }

    @Override
    public String getName() {
        return Profile.getCurrentProfile().getName();
    }


}