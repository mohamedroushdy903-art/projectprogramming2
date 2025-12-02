package com.example.game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile {
    private final Circle view;
    private final double vx; // horizontal speed (px/s) - 0 for vertical
    private final double vy; // vertical speed (px/s) - negative = up, positive = down
    private final com.example.game.Weapon weapon;
    private boolean alive = true;
    private final Pane root;

    public Projectile(double x, double y, double vx, double vy, com.example.game.Weapon weapon, Pane root) {
        this.vx = vx;
        this.vy = vy;
        this.weapon = weapon;
        this.root = root;

        view = new Circle(5, Color.DODGERBLUE);
        view.setCenterX(x);
        view.setCenterY(y);
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
        return x < -10 || x > width + 10 || y < -10 || y > height + 10;
    }

    public void destroy() {
        if (!alive) return;
        alive = false;
        root.getChildren().remove(view);
    }

    public boolean isAlive() { return alive; }
    public Circle getView() { return view; }
    public com.example.game.Weapon getWeapon() { return weapon; }
}
