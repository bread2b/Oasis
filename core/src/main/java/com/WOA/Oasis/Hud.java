package com.WOA.Oasis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hud {

    private OrthographicCamera camera;
    private BitmapFont font;

    public Hud(int screenWidth, int screenHeight) {
        // HUD 相机（屏幕坐标系）
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        // 字体（调试足够）
        font = new BitmapFont();
        font.getData().setScale(1.2f);
    }

    /**
     * 渲染 HUD（左下角坐标显示）
     */
    public void render(SpriteBatch batch, Player player) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float x = player.getCenterX();
        float y = player.getCenterY();

        font.draw(
                batch,
                String.format("X: %.1f  Y: %.1f", x, y),
                10,
                20
        );

        batch.end();
    }

    /**
     * 窗口尺寸变化时调用
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