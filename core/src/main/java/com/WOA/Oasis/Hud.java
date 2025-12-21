package com.WOA.Oasis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hud {

    private OrthographicCamera camera;
    private BitmapFont font;

    public Hud(int screenWidth, int screenHeight) {
        // HUD 使用屏幕坐标系
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        font = new BitmapFont();
        font.getData().setScale(2f);
    }

    /**
     * 渲染 HUD（左下角显示像素坐标）
     */
    public void render(SpriteBatch batch, Player player) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 直接显示整数像素坐标
        int x = player.getX();
        int y = player.getY();

        font.draw(
                batch,
                "X: " + x + "  Y: " + y,
                10,
                24
        );

        batch.end();
    }

    /**
     * 窗口尺寸变化
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    /**
     * 释放资源
     */
    public void dispose() {
        font.dispose();
    }
}