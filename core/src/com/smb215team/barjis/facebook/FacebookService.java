package com.smb215team.barjis.facebook;

public interface FacebookService {

    public void login(final LoginHandler loginHandler);

    public String getPictureURL();

    public String getName();
}