package com.WOA.Oasis.World.Drop;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.WOA.Oasis.Player;
import com.WOA.Oasis.Economy.CurrencyType;

public class DropCurrency extends DropItem {

    private static Texture goldTexture;
    private CurrencyType type;

    public DropCurrency(float x, float y, CurrencyType type, int amount) {
        super(x, y, null, amount);  // ⚠️ Item 为 null
        this.type = type;

        if (goldTexture == null) {
            goldTexture = new Texture("items/goldcoin.png");
            goldTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    @Override
    public void onPickup(Player player) {
        player.Getwallet().add(type, Amount);
    }

    @Override
    public void Render(SpriteBatch batch) {
        batch.draw(goldTexture, Position.x, Position.y, SIZE, SIZE);
    }
}
