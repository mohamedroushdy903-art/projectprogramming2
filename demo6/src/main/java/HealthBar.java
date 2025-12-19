package com.example.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar {
    private final Rectangle bg;
    private final Rectangle fg;
    private final double x, y, width, height;
    private final Pane root;

    public HealthBar(double x, double y, double width, double height, Pane root) {
        this.x = x; this.y = y; this.width = width; this.height = height; this.root = root;
        bg = new Rectangle(width, height, Color.DARKGRAY);
        bg.setX(x); bg.setY(y);
        fg = new Rectangle(width, height, Color.LIMEGREEN);
        fg.setX(x); fg.setY(y);
        root.getChildren().addAll(bg, fg);
    }

    public void update(double currentHealth, double maxHealth) {
        double ratio = Math.max(0, Math.min(1.0, currentHealth / maxHealth));
        fg.setWidth(width * ratio);
        if (ratio > 0.6) fg.setFill(Color.LIMEGREEN);
        else if (ratio > 0.3) fg.setFill(Color.GOLD);
        else fg.setFill(Color.ORANGERED);
    }

    public void remove() {
        if (root.getChildren().contains(bg)) root.getChildren().remove(bg);
        if (root.getChildren().contains(fg)) root.getChildren().remove(fg);
    }
}