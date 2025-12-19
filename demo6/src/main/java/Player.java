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
    private final com.example.game.FighterType fighterType;


    public Player(double x, double y, Pane root, com.example.game.FighterType fighterType) {
        this.root = root;
        this.fighterType = fighterType;
        rect = new Rectangle(40, 40, fighterType.getColor());
        rect.setX(x);
        rect.setY(y);
        root.getChildren().add(rect);


        this.currentWeapon = fighterType.getDefaultWeapon();
    }

    public void setWeapon(com.example.game.Weapon w) { this.currentWeapon = w; }
    public com.example.game.Weapon getWeapon() { return currentWeapon; }

    public void addKey(KeyCode code) { keys.add(code); }
    public void removeKey(KeyCode code) { keys.remove(code); }

    public boolean isKeyPressed(KeyCode k) {
        return keys.contains(k);
    }

    public void update(double dt) {
        double speed = 220;
        if (keys.contains(KeyCode.LEFT)) rect.setX(rect.getX() - speed * dt);
        if (keys.contains(KeyCode.RIGHT)) rect.setX(rect.getX() + speed * dt);
        if (keys.contains(KeyCode.UP)) rect.setY(rect.getY() - speed * dt);
        if (keys.contains(KeyCode.DOWN)) rect.setY(rect.getY() + speed * dt);

        double maxW = (root.getPrefWidth() > 0) ? root.getPrefWidth() : 600;
        double maxH = (root.getPrefHeight() > 0) ? root.getPrefHeight() : 400;

        if (rect.getX() < 0) rect.setX(0);
        if (rect.getY() < 0) rect.setY(0);
        if (rect.getX() > maxW - rect.getWidth()) rect.setX(maxW - rect.getWidth());
        if (rect.getY() > maxH - rect.getHeight()) rect.setY(maxH - rect.getHeight());
    }

    /**
     * Try to fire a projectile.
     * Uses the updated Projectile constructor which requires color and radius.
     * Returns the created Projectile or null if cooldown or no weapon or SPACE not pressed.
     */
    public com.example.game.Projectile tryFire(long nowNano, List<com.example.game.Projectile> projectileList) {
        if (currentWeapon == null) return null;
        if (!isKeyPressed(KeyCode.SPACE)) return null;

        long cooldownNano = currentWeapon.getCooldownMs() * 1_000_000L;
        if (nowNano - lastShotTime < cooldownNano) return null;

        double px = rect.getX() + rect.getWidth() / 2.0;
        double py = rect.getY();
        double speed = currentWeapon.getProjectileSpeed();
        // use fighter's color for projectile and radius 6
        Color projColor = fighterType.getColor();
        double radius = 6.0;

        //
        com.example.game.Projectile p = new com.example.game.Projectile(px, py, 0.0, -speed, currentWeapon, root, projColor, radius);
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

    public com.example.game.FighterType getFighterType() { return fighterType; }
}