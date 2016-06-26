package com.smb215team.barjis.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.smb215team.barjis.Barjis;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 1024;
                config.height = 600;
                config.resizable = false;
		new LwjglApplication(new Barjis(), config);
	}
}
