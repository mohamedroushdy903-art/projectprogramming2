package com.example.game;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    private final Rectangle rect;
    private final Pane root;
    private final Set<KeyCode> keys = new HashSet<>();
    private com.example.game.Weapon currentWeapon;
    private long lastShotTime = 0L; // nanoTime
    private int health = 100;

    public Player(double x, double y, Pane root) {
        this.root = root;
        rect = new Rectangle(40, 40, Color.RED);
        rect.setX(x);
        rect.setY(y);
        root.getChildren().add(rect);
    }

    public void setWeapon(com.example.game.Weapon w) { this.currentWeapon = w; }
    public com.example.game.Weapon getWeapon() { return currentWeapon; }

    public void addKey(KeyCode code) { keys.add(code); }
    public void removeKey(KeyCode code) { keys.remove(code); }

    public void update(double dt) {
        double speed = 220;
        if (keys.contains(KeyCode.LEFT)) rect.setX(rect.getX() - speed * dt);
        if (keys.contains(KeyCode.RIGHT)) rect.setX(rect.getX() + speed * dt);
        if (keys.contains(KeyCode.UP)) rect.setY(rect.getY() - speed * dt);
        if (keys.contains(KeyCode.DOWN)) rect.setY(rect.getY() + speed * dt);

        // clamp to window bounds (assume 600x400, or check root width/height)
        if (rect.getX() < 0) rect.setX(0);
        if (rect.getY() < 0) rect.setY(0);
        if (rect.getX() > root.getPrefWidth() - rect.getWidth()) rect.setX(root.getPrefWidth() - rect.getWidth());
        if (rect.getY() > root.getPrefHeight() - rect.getHeight()) rect.setY(root.getPrefHeight() - rect.getHeight());
    }

    // Try to fire; if successful, return a Projectile, else null.
    public com.example.game.Projectile tryFire(long nowNano, List<com.example.game.Projectile> projectileList) {
        if (currentWeapon == null) return null;
        long cooldownNano = currentWeapon.getCooldownMs() * 1_000_000L;
        if (nowNano - lastShotTime < cooldownNano) return null;

        // spawn projectile at top-center of player, going up
        double px = rect.getX() + rect.getWidth() / 2.0;
        double py = rect.getY(); // from player's top
        double speed = currentWeapon.getProjectileSpeed(); // px/s
        com.example.game.Projectile p = new com.example.game.Projectile(px, py, 0, -speed, currentWeapon, root);
        projectileList.add(p);
        lastShotTime = nowNano;
        return p;
    }

    public Rectangle getRect() { return rect; }

    public void damage(int d) {
        health -= d;
        if (health < 0) health = 0;
    }
    public int getHealth() { return health; }
}
