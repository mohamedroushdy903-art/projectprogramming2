package com.example.game;

import java.util.Arrays;
import java.util.List;

public class WeaponFactory {
    public static List<com.example.game.Weapon> getAllWeapons() {
        com.example.game.Weapon pistol = new com.example.game.Weapon("Pistol", 10, 700, 400);
        com.example.game.Weapon cannon = new com.example.game.Weapon("Cannon", 30, 350, 1000);
        com.example.game.Weapon rifle  = new com.example.game.Weapon("Rifle", 8, 900, 250);
        com.example.game.Weapon mageBolt = new com.example.game.Weapon("Magic Bolt", 12, 600, 450);
        com.example.game.Weapon shotgun = new com.example.game.Weapon("Shotgun", 18, 500, 800);
        return Arrays.asList(pistol, cannon, rifle, mageBolt, shotgun);
    }
}

