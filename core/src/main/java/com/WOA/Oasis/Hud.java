package com.WOA.Oasis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.WOA.Oasis.Inventory.Bag;
import com.WOA.Oasis.Inventory.Itemstack;

public class Hud {

    private OrthographicCamera camera;

    private Texture toolbarTex;
    private Texture selectTex;

    // 两套字体：正常 / 数量更小
    private BitmapFont font;
    private BitmapFont countFont;
    private final GlyphLayout layout = new GlyphLayout();

    private int screenWidth;
    private int screenHeight;

    // ===== UI 缩放 =====
    private static final float UI_SCALE = 2.0f;
    private static final int HOTBAR_SLOTS = 8;

    // ===== toolbar 贴图规格 =====
    private static final float SLOT_STEP_PX = 39f; // 38 可用 + 1 分割线
    private static final float ITEM_PX      = 32f;
    private static final float ITEM_OFFSET  = 3f;  // (38 - 32) / 2
    private static final float OUTER_BORDER = 1f;  // 左 / 上外框
    private static final float USABLE_PX    = 38f;

    // ===== 数量显示配置（你只调这里）=====
    private static final float COUNT_SCALE = 1.1f;      // 数量字体大小（想更小：0.9f / 0.8f）
    private static final float COUNT_PAD_PX = 2.0f;      // 离右下角内边距（逻辑像素）

    public Hud(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        font = new BitmapFont();
        font.getData().setScale(1.6f);

        countFont = new BitmapFont();
        countFont.getData().setScale(COUNT_SCALE);

        toolbarTex = new Texture("ui/toolbar.png");      // 313×40
        selectTex  = new Texture("ui/slot_select.png");  // 40×40
    }

    // =========================================================
    // render
    // =========================================================
    public void render(SpriteBatch batch, Player player) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float toolbarW = toolbarTex.getWidth()  * UI_SCALE;
        float toolbarH = toolbarTex.getHeight() * UI_SCALE;

        float toolbarX = (screenWidth - toolbarW) / 2f;
        float toolbarY = 8f;

        batch.draw(toolbarTex, toolbarX, toolbarY, toolbarW, toolbarH);

        drawHotbar(batch, player, toolbarX, toolbarY);

        batch.end();
    }

    // =========================================================
    // Hotbar
    // =========================================================
    private void drawHotbar(SpriteBatch batch, Player player, float toolbarX, float toolbarY) {

        if (player == null) return;
        Bag bag = player.Getbag();
        if (bag == null) return;

        float step        = SLOT_STEP_PX * UI_SCALE;
        float usableSize  = USABLE_PX * UI_SCALE;

        float itemSize    = ITEM_PX * UI_SCALE;
        float itemOffset  = ITEM_OFFSET * UI_SCALE;

        float cursorSize  = selectTex.getWidth() * UI_SCALE;

        float countPad = COUNT_PAD_PX * UI_SCALE; // 右下角内边距（跟随 UI_SCALE）

        for (int i = 0; i < HOTBAR_SLOTS; i++) {

            float slotX = toolbarX + OUTER_BORDER * UI_SCALE + i * step;
            float slotY = toolbarY + OUTER_BORDER * UI_SCALE;

            float itemX = slotX + itemOffset;
            float itemY = slotY + itemOffset;

            // 光标
            if (i == bag.getSelectedHotbarIndex()) {
                float cursorX = slotX + (usableSize - cursorSize) / 2f;
                float cursorY = slotY + (usableSize - cursorSize) / 2f;
                batch.draw(selectTex, cursorX, cursorY, cursorSize, cursorSize);
            }

            // item
            Itemstack stack = bag.get(i);
            if (stack == null || stack.Isempty()) continue;
            if (stack.Item == null || stack.Item.Icon == null) continue;

            batch.draw(stack.Item.Icon, itemX, itemY, itemSize, itemSize);

            // 数量（右下角）
            if (stack.Amount > 1) {
                String txt = String.valueOf(stack.Amount);

                // 量出文字宽高
                layout.setText(countFont, txt);

                // 右下角对齐：x = 右边 - padding - 文本宽度
                float textX = itemX + itemSize - countPad - layout.width;

                // 注意：BitmapFont 的 y 是“基线”
                // 让文字贴近下边：y = itemY + padding + 文本高度
                float textY = itemY + countPad + layout.height;

                countFont.draw(batch, layout, textX, textY);
            }
        }
    }

    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        camera.setToOrtho(false, width, height);
    }

    public void dispose() {
        font.dispose();
        countFont.dispose();
        toolbarTex.dispose();
        selectTex.dispose();
    }
}