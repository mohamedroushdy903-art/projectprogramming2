package com.example.game;

import javafx.scene.paint.Color;

public enum FighterType {
    WARRIOR("Warrior", Color.DARKRED, new com.example.game.Weapon("Sword", 15, 500, 350)),
    MAGE("Mage", Color.PURPLE, new com.example.game.Weapon("Magic Bolt", 12, 700, 450)),
    ARCHER("Archer", Color.DARKGREEN, new com.example.game.Weapon("Arrow", 10, 900, 200));

    private final String displayName;
    private final Color color;
    private final com.example.game.Weapon defaultWeapon;

    FighterType(String displayName, Color color, com.example.game.Weapon defaultWeapon) {
        this.displayName = displayName;
        this.color = color;
        this.defaultWeapon = defaultWeapon;
    }

    public String getDisplayName() { return displayName; }
    public Color getColor() { return color; }
    public com.example.game.Weapon getDefaultWeapon() { return defaultWeapon; }
}
