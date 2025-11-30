package com.WOA.Oasis.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.WOA.Oasis.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Oasis");
        config.setWindowedMode(1280, 720);
        config.useVsync(true);

        // macOS 必须加
        config.setForegroundFPS(60);
        new Lwjgl3Application(new MainGame(), config);
    }
}