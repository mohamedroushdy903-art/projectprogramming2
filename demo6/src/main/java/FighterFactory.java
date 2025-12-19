package com.example.game;

import javafx.scene.layout.Pane;

import static com.example.game.FighterType.MAGE;


public class FighterFactory {
    private FighterFactory() {

    }

    public static com.example.game.Fighter createFighter(String type, double x, double y, Pane root, com.example.game.Weapon weapon) {
        return switch (type.toLowerCase()) {
            case "warrior" -> new com.example.game.Warrior(x, y, root, weapon);
            case "mage" -> new com.example.game.Mage(x, y, root, weapon);
            case "archer" -> new com.example.game.Archer(x, y, root, weapon);
            default -> new com.example.game.Warrior(x, y, root, weapon);
        };
    }

    public static com.example.game.Fighter createFighter(com.example.game.FighterType type, double x, double y, Pane root, Weapon weapon) {
        return switch (type) {
            case WARRIOR -> new com.example.game.Warrior(x, y, root, weapon);
            case MAGE -> new com.example.game.Mage(x, y, root, weapon);
            case ARCHER -> new com.example.game.Archer(x, y, root, weapon);
        };
    }
}