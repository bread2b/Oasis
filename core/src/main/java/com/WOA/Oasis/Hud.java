package com.WOA.Oasis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.WOA.Oasis.Inventory.Bag;
import com.WOA.Oasis.Inventory.Itemstack;
import com.WOA.Oasis.Ui.ToolbarLayout;

public class Hud {

    private OrthographicCamera camera;

    private Texture toolbarTex;
    private Texture selectTex;

    private BitmapFont countFont;
    private final GlyphLayout layout = new GlyphLayout();

    private int screenWidth;
    private int screenHeight;

    // =========================
    // UI 全局参数
    // =========================
    private static final float UI_SCALE = 2.0f;
    private static final float COUNT_PAD_PX = 2.0f;

    // =========================
    // Toolbar 布局数据（已校准）
    // =========================
    private final ToolbarLayout toolbarLayout = new ToolbarLayout(
            322, 42,   // toolbar.png 尺寸
            1, 0,      // innerX, innerY（Y 不再用，仅占位）
            311, 38,   // innerWidth, innerHeight（39px slot）
            8,         // slot 数量
            40,        // slot 步进
            32         // item 尺寸
    );

    public Hud(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        countFont = new BitmapFont();
        countFont.getData().setScale(1.1f);

        toolbarTex = new Texture("ui/toolbar.png");
        selectTex  = new Texture("ui/slot_select.png");

        // ⭐ 像素风必须：禁用线性采样
        toolbarTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        selectTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }

    // =====================================================
    // Render
    // =====================================================
    public void render(SpriteBatch batch, Player player) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float toolbarW = toolbarTex.getWidth()  * UI_SCALE;
        float toolbarH = toolbarTex.getHeight() * UI_SCALE;

        // ⭐ 像素对齐
        float toolbarX = snap((screenWidth - toolbarW) / 2f);
        float toolbarY = snap(8f);

        batch.draw(toolbarTex, toolbarX, toolbarY, toolbarW, toolbarH);

        drawHotbar(batch, player, toolbarX, toolbarY);

        batch.end();
    }

    // =====================================================
    // Hotbar（最终稳定版）
    // =====================================================
    private void drawHotbar(SpriteBatch batch, Player player,
                            float toolbarX, float toolbarY) {

        if (player == null) return;
        Bag bag = player.Getbag();
        if (bag == null) return;

        float scale = UI_SCALE;

        // ===== 内部 slot 区域 =====
        float innerX = snap(toolbarX + toolbarLayout.innerX * scale);

        // ⭐ 关键：Y 方向用“垂直居中”，不信 innerY
        float innerY = snap(
                toolbarY
                + (toolbarTex.getHeight() - toolbarLayout.innerHeight)
                / 2f * scale
        );

        float step       = toolbarLayout.slotStep * scale;
        float slotHeight = toolbarLayout.innerHeight * scale;
        float slotSize   = toolbarLayout.slotSize * scale;

        float itemOffset =
                (toolbarLayout.slotStep - toolbarLayout.slotSize) / 2f * scale;

        float cursorSize = selectTex.getWidth() * scale;
        float countPad   = COUNT_PAD_PX * scale;

        for (int i = 0; i < toolbarLayout.slotCount; i++) {

            float slotX = snap(innerX + i * step);
            float slotY = innerY;

            float itemX = snap(slotX + itemOffset);
            float itemY = snap(slotY + (slotHeight - slotSize) / 2f);

            // ===== 光标（像素对齐）=====
            if (i == bag.getSelectedHotbarIndex()) {
                float cx = snap(slotX + (step - cursorSize) / 2f);
                float cy = snap(slotY + (slotHeight - cursorSize) / 2f);
                batch.draw(selectTex, cx, cy, cursorSize, cursorSize);
            }

            // ===== item =====
            Itemstack stack = bag.get(i);
            if (stack == null || stack.Isempty()) continue;
            if (stack.Item == null || stack.Item.Icon == null) continue;

            batch.draw(stack.Item.Icon, itemX, itemY, slotSize, slotSize);

            // ===== 数量 =====
            if (stack.Amount > 1) {
                String txt = String.valueOf(stack.Amount);
                layout.setText(countFont, txt);

                float textX = snap(itemX + slotSize - countPad - layout.width);
                float textY = snap(itemY + countPad + layout.height);

                countFont.draw(batch, layout, textX, textY);
            }
        }
    }

    // =====================================================
    // 像素对齐（核心工具）
    // =====================================================
    private static float snap(float v) {
        return Math.round(v);
    }

    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        camera.setToOrtho(false, width, height);
    }

    public void dispose() {
        countFont.dispose();
        toolbarTex.dispose();
        selectTex.dispose();
    }
}