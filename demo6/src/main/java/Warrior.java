package com.example.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Warrior extends com.example.game.Fighter {
    public Warrior(double x, double y, Pane root, com.example.game.Weapon startingWeapon) {
        super(x, y, root, Color.RED, startingWeapon);
    }

    @Override
    public com.example.game.Projectile shoot(double dirX, double dirY, Pane root) {
        double dx = dirX;
        double dy = dirY;
        if (dx == 0 && dy == 0) { dx = getFacingX(); dy = getFacingY(); }
        double len = Math.sqrt(dx*dx + dy*dy);
        if (len == 0) { dx = 1; dy = 0; len = 1; }
        dx /= len; dy /= len;

        double px = getX() + width / 2.0 + dx * (width / 2.0 + 2);
        double py = getY() + height / 2.0 + dy * (height / 2.0 + 2);
        double speed = weapon.getProjectileSpeed();
        double vx = dx * speed;
        double vy = dy * speed;
        return new com.example.game.Projectile(px, py, vx, vy, weapon, root, Color.DARKRED, 6);
    }
}
