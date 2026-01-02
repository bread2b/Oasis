package com.WOA.Oasis.Ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.WOA.Oasis.Player;
import com.WOA.Oasis.Economy.CurrencyType;
import com.WOA.Oasis.Inventory.Bag;
import com.WOA.Oasis.Inventory.ItemStack;

public class Hud {

    private OrthographicCamera camera;

    private Texture toolbarTex;
    private Texture selectTex;
    
    private Texture bagTex;
    private boolean bagVisible = false;

    private BitmapFont countFont;
    private final GlyphLayout layout = new GlyphLayout();

    private int screenWidth;
    private int screenHeight;

    private Texture goldIcon;

    // ===== Health UI =====
    private Texture heartFull;
    private Texture heartHalf;
    private Texture heartEmpty;



    // =========================
    // UI 全局参数
    // =========================
    private static final float UI_SCALE = 2.0f;
    private static final float COUNT_PAD_PX = 2.0f;

    // === Drag & Drop ===
    private ItemStack draggingStack = null;
    private int dragFromIndex = -1;
    private boolean isDragging = false;
    private float mouseX, mouseY;


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

    private final ToolbarLayout bagLayout = new ToolbarLayout(
        402, 102,     // 背包整体尺寸
        2, 5,        // innerX / innerY（和 toolbar 一样）
        391, 80,     // 内部区域
        10,          // 20 格
        40,          // slot 间距
        32           // 物品尺寸
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
        bagTex = new Texture("ui/bagbar.png");
        goldIcon = new Texture("items/goldcoin.png");
        heartFull = new Texture("ui/heart_full.png");
        heartHalf = new Texture("ui/heart_half.png");
        heartEmpty = new Texture("ui/heart_empty.png");


        // ⭐ 像素风必须：禁用线性采样
        toolbarTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        selectTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        bagTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        goldIcon.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        heartFull.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        heartHalf.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        heartEmpty.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
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
        if (bagVisible) {
            drawBag(batch, player);
        }
        drawCurrency(batch, player);
        drawHealthBar(batch, player);
        drawDebugInfo(batch, player);



        if (isDragging && draggingStack != null && draggingStack.Item != null) {
    float size = toolbarLayout.slotSize * UI_SCALE;
    batch.draw(
        draggingStack.Item.Icon,
        mouseX - size / 2f,
        mouseY - size / 2f,
        size,
        size
    );
}

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
            ItemStack stack = bag.get(i);
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

    private void drawBag(SpriteBatch batch, Player player) {

        if (player == null) return;
        Bag bag = player.Getbag();
        if (bag == null) return;
            float scale = UI_SCALE;
        float bagW = bagTex.getWidth() * scale;
        float bagH = bagTex.getHeight() * scale;

        // 居中
        float bagX = snap((screenWidth - bagW) / 2f);
        float bagY = snap((screenHeight - bagH) / 2f);

        batch.draw(bagTex, bagX, bagY, bagW, bagH);
        // ===== 内部区域 =====
        int cols = 10;
        int rows = 2;

        float innerX = snap(bagX + bagLayout.innerX * scale);

        float step = bagLayout.slotStep * scale;

        float slotSize = bagLayout.slotSize * scale;
        float slotHeight = slotSize;

        float innerY = snap(bagY + bagLayout.innerY * scale);

        float itemOffset =
                (bagLayout.slotStep - bagLayout.slotSize) / 2f * scale;
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int index = row * cols + col;

                float slotX = snap(innerX + col * step);
                float slotY = snap(innerY + (rows - 1 - row) * step);

                float itemX = snap(slotX + itemOffset);
                float itemY = snap(slotY + (slotHeight - slotSize) / 2f);

                ItemStack stack = bag.get(index);
                if (stack == null || stack.Isempty()) continue;
                if (stack.Item == null || stack.Item.Icon == null) continue;

                batch.draw(stack.Item.Icon, itemX, itemY, slotSize, slotSize);

                if (stack.Amount > 1) {
                    String txt = String.valueOf(stack.Amount);
                    layout.setText(countFont, txt);

                    float textX = snap(itemX + slotSize - COUNT_PAD_PX * scale - layout.width);
                    float textY = snap(itemY + COUNT_PAD_PX * scale + layout.height);

                    countFont.draw(batch, layout, textX, textY);
                }
            }
        }
    }


    private void drawCurrency(SpriteBatch batch, Player player) {
        if (player == null) return;

        int gold = player.Getwallet().get(CurrencyType.GOLD);

        float scale = UI_SCALE;
        
        float iconSize = 16f * scale;
        // 左上角位置
        float x = snap(screenWidth - iconSize - 10f * scale - 40f * scale);
        float y = snap(screenHeight - 10f * scale);

        // 画金币图标
        batch.draw(goldIcon, x, y - iconSize, iconSize, iconSize);

        // 画数量
        String text = String.valueOf(gold);
        layout.setText(countFont, text);

        float textX = x + iconSize + 4 * scale;
        float textY = y - iconSize / 2f + layout.height / 2f;

        countFont.draw(batch, layout, textX, textY);
    }

    private void drawDebugInfo(SpriteBatch batch, Player player) {
    if (player == null) return;

    float x = 10f;
    float y = 60f;

    String posText = "X: " + player.getX() + "  Y: " + player.getY();

    layout.setText(countFont, posText);
    countFont.draw(batch, layout, x, y);
}


    private void drawHealthBar(SpriteBatch batch, Player player) {
        if (player == null || player.getHealth() == null) return;

        int max = player.getHealth().getMax();
        int cur = player.getHealth().getCurrent();

        float scale = UI_SCALE;

        int heartSize = 16;
        int spacing = 2;

        int heartCount = (int) Math.ceil(max / 2f);

        // 放在左上角，金币下面
        float x = snap(10f * scale);
        float y = snap(screenHeight - 10f * scale);

        for (int i = 0; i < heartCount; i++) {
            int value = (i + 1) * 2;

            Texture tex;
            if (cur >= value)
                tex = heartFull;
            else if (cur == value - 1)
                tex = heartHalf;
            else
                tex = heartEmpty;

            batch.draw(
            tex,
            x + i * (heartSize + spacing) * scale,
            y - heartSize * scale,
            heartSize * scale,
            heartSize * scale
        );
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
        bagTex.dispose();
        heartFull.dispose();
        heartHalf.dispose();
        heartEmpty.dispose();
        goldIcon.dispose();
    }


    public void onMouseDown(int screenX, int screenY, int button, Player player) {
        mouseX = screenX;
        mouseY = screenHeight - screenY;

        // 1️⃣ 先判断背包
        int bagSlot = getBagSlotAt(mouseX, mouseY);
        if (bagSlot != -1) {
            ItemStack stack = player.Getbag().get(bagSlot);
            if (stack != null && !stack.Isempty()) {
                draggingStack = stack;
                dragFromIndex = bagSlot;
                isDragging = true;
            }
            return;
        }

        // 2️⃣ 再判断工具栏
        int slot = getToolbarSlotAt(mouseX, mouseY);
        if (slot != -1) {
            player.Getbag().Selecthotbar(slot);
        }
    }



    public void onMouseDrag(int screenX, int screenY) {
        if (!bagVisible || !isDragging) return;

        mouseX = screenX;
        mouseY = screenHeight - screenY;
    }


    public void onMouseUp(int screenX, int screenY, int button, Player player) {
        if (!isDragging) return;

        mouseX = screenX;
        mouseY = screenHeight - screenY;

        int targetSlot = getBagSlotAt(mouseX, mouseY);
        if (targetSlot != -1 && targetSlot != dragFromIndex) {
            player.Getbag().swap(dragFromIndex, targetSlot);
        }

        draggingStack = null;
        dragFromIndex = -1;
        isDragging = false;
    }


    public void setBagVisible(boolean visible) {
        this.bagVisible = visible;
    }
    // Hud.java
    public boolean isBagVisible() {
        return bagVisible;
    }


    private int getToolbarSlotAt(float x, float y) {

        float scale = UI_SCALE;

        float toolbarW = toolbarTex.getWidth() * scale;
        float toolbarH = toolbarTex.getHeight() * scale;

        float toolbarX = (screenWidth - toolbarW) / 2f;
        float toolbarY = 8f;

        float innerX = toolbarX + toolbarLayout.innerX * scale;
        float innerY = toolbarY + (toolbarTex.getHeight() - toolbarLayout.innerHeight) / 2f * scale;

        float step = toolbarLayout.slotStep * scale;
        float size = toolbarLayout.slotSize * scale;

        for (int i = 0; i < toolbarLayout.slotCount; i++) {
            float sx = innerX + i * step;
            float sy = innerY;

            if (x >= sx && x <= sx + size &&
                y >= sy && y <= sy + size) {
                return i;
            }
        }
        return -1;
    }

    private int getBagSlotAt(float x, float y) {
        if (!bagVisible) return -1;

        float scale = UI_SCALE;

        float bagW = bagTex.getWidth() * scale;
        float bagH = bagTex.getHeight() * scale;

        float bagX = (screenWidth - bagW) / 2f;
        float bagY = (screenHeight - bagH) / 2f;

        int cols = 10;
        int rows = 2;

        float innerX = bagX + bagLayout.innerX * scale;
        float innerY = bagY + bagLayout.innerY * scale;

        float step = bagLayout.slotStep * scale;
        float slotSize = bagLayout.slotSize * scale;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;

                float sx = innerX + col * step;
                float sy = innerY + (rows - 1 - row) * step;

                if (x >= sx && x <= sx + slotSize &&
                    y >= sy && y <= sy + slotSize) {
                    return index;
                }
            }
        }
        return -1;
    }

    public boolean isMouseOnUI(float screenX, float screenY) {

        float y = screenHeight - screenY;

        // 工具栏区域
        if (isOnToolbar(screenX, y)) return true;

        // 背包区域
        if (bagVisible && isOnBag(screenX, y)) return true;

        return false;
    }

    private boolean isOnToolbar(float x, float y) {
        float scale = UI_SCALE;

        float toolbarW = toolbarTex.getWidth() * scale;
        float toolbarH = toolbarTex.getHeight() * scale;

        float toolbarX = (screenWidth - toolbarW) / 2f;
        float toolbarY = 8f;

        return x >= toolbarX && x <= toolbarX + toolbarW
            && y >= toolbarY && y <= toolbarY + toolbarH;
    }
    private boolean isOnBag(float x, float y) {
        float scale = UI_SCALE;

        float bagW = bagTex.getWidth() * scale;
        float bagH = bagTex.getHeight() * scale;

        float bagX = (screenWidth - bagW) / 2f;
        float bagY = (screenHeight - bagH) / 2f;

        return x >= bagX && x <= bagX + bagW
            && y >= bagY && y <= bagY + bagH;
    }

    public boolean handleClick(float screenX, float screenY, Player player) {
        mouseX = screenX;
        mouseY = screenHeight - screenY;

        // 背包
        int bagSlot = getBagSlotAt(mouseX, mouseY);
        if (bagSlot != -1) {
            ItemStack stack = player.Getbag().get(bagSlot);
            if (stack != null && !stack.Isempty()) {
                draggingStack = stack;
                dragFromIndex = bagSlot;
                isDragging = true;
            }
            return true; // 👈 吃掉事件
        }

        

        // 工具栏
        int slot = getToolbarSlotAt(mouseX, mouseY);
        if (slot != -1) {
            player.Getbag().Selecthotbar(slot);
            return true;
        }

        return false;
    }

}