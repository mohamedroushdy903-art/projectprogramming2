package com.example.game;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class PlayerController {
    private final com.example.game.Fighter fighter;
    private final Pane root;
    private final Set<KeyCode> keys = new HashSet<>();
    private long lastShotTime = 0L;
    private final double minX, maxX;
    private final KeyCode shootKey;
    private final KeyCode prevWeaponKey;
    private final KeyCode nextWeaponKey;
    private final List<com.example.game.Weapon> availableWeapons;
    private int currentWeaponIndex = 0;
    private final int startHealth;

    // track last movement vector for facing
    private double lastMoveX = 1.0;
    private double lastMoveY = 0.0;

    public PlayerController(com.example.game.Fighter fighter, Pane root, double minX, double maxX,
                            KeyCode shootKey, KeyCode prevWeaponKey, KeyCode nextWeaponKey,
                            List<com.example.game.Weapon> availableWeapons) {
        this.fighter = fighter;
        this.root = root;
        this.minX = minX;
        this.maxX = maxX;
        this.shootKey = shootKey;
        this.prevWeaponKey = prevWeaponKey;
        this.nextWeaponKey = nextWeaponKey;
        this.availableWeapons = availableWeapons;
        this.startHealth = fighter.getHealth();
        if (fighter.getWeapon() == null && !availableWeapons.isEmpty()) {
            fighter.setWeapon(availableWeapons.get(0));
        }
    }

    public com.example.game.Fighter getFighter() { return fighter; }
    public int getHealth() { return fighter.getHealth(); }
    public String getWeaponName() { return fighter.getWeapon() != null ? fighter.getWeapon().getName() : "None"; }
    public int getStartHealth() { return startHealth; }

    public void addKey(KeyCode k) { keys.add(k); }
    public void removeKey(KeyCode k) { keys.remove(k); }
    public boolean isKeyPressed(KeyCode k) { return keys.contains(k); }

    /**
     * Move fighter by dx,dy within allowed bounds.
     * Also updates the fighter's facing vector (rotation) when there is movement.
     */
    public void moveBy(double dx, double dy) {
        double newX = fighter.getX() + dx;
        double newY = fighter.getY() + dy;

        if (newX < minX) newX = minX;
        if (newX > maxX - fighter.width) newX = maxX - fighter.width;

        double maxH = (root.getPrefHeight() > 0) ? root.getPrefHeight() : 400;
        if (newY < 0) newY = 0;
        if (newY > maxH - fighter.height) newY = maxH - fighter.height;

        fighter.setX(newX);
        fighter.setY(newY);


        if (dx != 0 || dy != 0) {

            lastMoveX = dx;
            lastMoveY = dy;
            fighter.setFacingVector(lastMoveX, lastMoveY);
        }
    }

    /**
     * Try to fire a projectile. Uses fighter's facing vector if no explicit direction provided.
     * Returns created projectile or null.
     */
    public com.example.game.Projectile tryFire(long nowNano, List<com.example.game.Projectile> projectileList) {
        if (!keys.contains(shootKey)) return null;
        if (fighter.getWeapon() == null) return null;
        long cooldownNano = fighter.getWeapon().getCooldownMs() * 1_000_000L;
        if (nowNano - lastShotTime < cooldownNano) return null;

        // use facing vector as  direction
        double dirX = fighter.getFacingX();
        double dirY = fighter.getFacingY();

        com.example.game.Projectile p = fighter.shoot(dirX, dirY, root);
        projectileList.add(p);
        lastShotTime = nowNano;
        return p;
    }

    public void handleWeaponSwitchKeys() {
        if (prevWeaponKey != null && keys.contains(prevWeaponKey)) {
            currentWeaponIndex = (currentWeaponIndex - 1 + availableWeapons.size()) % availableWeapons.size();
            fighter.setWeapon(availableWeapons.get(currentWeaponIndex));
        } else if (nextWeaponKey != null && keys.contains(nextWeaponKey)) {
            currentWeaponIndex = (currentWeaponIndex + 1) % availableWeapons.size();
            fighter.setWeapon(availableWeapons.get(currentWeaponIndex));
        }
    }
}
