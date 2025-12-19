package com.example.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile {
    private final Circle view;
    private double vx; // px/s
    private double vy; // px/s
    private final com.example.game.Weapon weapon;
    private boolean alive = true;
    private final Pane root;

    public Projectile(double x, double y, double vx, double vy, com.example.game.Weapon weapon, Pane root, Color color, double radius) {
        this.vx = vx;
        this.vy = vy;
        this.weapon = weapon;
        this.root = root;

        view = new Circle(radius, color);
        view.setCenterX(x);
        view.setCenterY(y);

        double angle = Math.toDegrees(Math.atan2(vy, vx));
        view.setRotate(angle);

        root.getChildren().add(view);
    }

    public void update(double dt) {
        if (!alive) return;
        view.setCenterX(view.getCenterX() + vx * dt);
        view.setCenterY(view.getCenterY() + vy * dt);
    }

    public boolean isOffScreen(double width, double height) {
        double x = view.getCenterX();
        double y = view.getCenterY();
        return x < -40 || x > width + 40 || y < -40 || y > height + 40;
    }

    public void destroy() {
        if (!alive) return;
        alive = false;
        if (root.getChildren().contains(view)) root.getChildren().remove(view);
    }

    public boolean isAlive() { return alive; }
    public Circle getView() { return view; }
    public com.example.game.Weapon getWeapon() { return weapon; }


    public double getVx() { return vx; }
    public double getVy() { return vy; }
}
