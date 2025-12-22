package com.WOA.Oasis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hud {

    private OrthographicCamera camera;
    private BitmapFont font;

    // ⭐ 工具栏
    private Texture toolbarTex;

    private int screenWidth;
    private int screenHeight;

    // ⭐ UI 缩放比例（只改这个）
    private static final float UI_SCALE = 3.0f;

    public Hud(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // HUD 使用屏幕坐标系
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        font = new BitmapFont();
        font.getData().setScale(2f);

        // 加载工具栏贴图
        toolbarTex = new Texture("ui/toolbar.png");
    }

    /**
     * 渲染 HUD（坐标 + 工具栏）
     */
    public void render(SpriteBatch batch, Player player) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // ===== 左下角：玩家坐标 =====
        int x = player.getX();
        int y = player.getY();

        font.draw(
                batch,
                "X: " + x + "  Y: " + y,
                10,
                24
        );

        // ===== 底部中间：工具栏（缩放后） =====
        int drawW = (int)(toolbarTex.getWidth()  * UI_SCALE);
        int drawH = (int)(toolbarTex.getHeight() * UI_SCALE);

        int toolbarX = (screenWidth - drawW) / 2;
        int toolbarY = 8;

        batch.draw(toolbarTex, toolbarX, toolbarY, drawW, drawH);

        batch.end();
    }

    /**
     * 窗口尺寸变化
     */
    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        camera.setToOrtho(false, width, height);
    }

    /**
     * 释放资源
     */
    public void dispose() {
        font.dispose();
        toolbarTex.dispose();
    }
}
