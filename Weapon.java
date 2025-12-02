package com.example.game;

public class Weapon {
    private final String name;
    private final int damage;
    private final double projectileSpeed; // pixels per second
    private final long cooldownMs;

    public Weapon(String name, int damage, double projectileSpeed, long cooldownMs) {
        this.name = name;
        this.damage = damage;
        this.projectileSpeed = projectileSpeed;
        this.cooldownMs = cooldownMs;
    }

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public double getProjectileSpeed() { return projectileSpeed; }
    public long getCooldownMs() { return cooldownMs; }

    @Override
    public String toString() {
        return name + " (DMG:" + damage + ", SPD:" + projectileSpeed + "px/s, CD:" + cooldownMs + "ms)";
    }
}
