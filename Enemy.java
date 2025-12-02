package com.example.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Enemy {
    private final Rectangle rect;
    private final Pane root;
    private double speed = 100;
    private int health = 50;

    public Enemy(double x, double y, Pane root) {
        this.root = root;
        rect = new Rectangle(40, 40, Color.GREEN);
        rect.setX(x);
        rect.setY(y);
        root.getChildren().add(rect);
    }

    public void update(double dt) {
        rect.setY(rect.getY() + speed * dt);
    }

    public Rectangle getRect() { return rect; }
    public double getY() { return rect.getY(); }

    public void damage(int d) {
        health -= d;
        if (health <= 0) {
            // destroy visually
            root.getChildren().remove(rect);
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() { return health; }
}
