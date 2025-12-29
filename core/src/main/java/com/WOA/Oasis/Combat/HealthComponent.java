package com.WOA.Oasis.Combat;

public class HealthComponent {

    private final Health health;

    public HealthComponent(int maxHealth) {
        this.health = new Health(maxHealth);
    }

    public Health getHealth() {
        return health;
    }

    public void takeDamage(int dmg) {
        health.damage(dmg);
    }

    public void heal(int value) {
        health.heal(value);
    }

    public boolean isDead() {
        return health.isDead();
    }
}
