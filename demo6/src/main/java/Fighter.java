package com.example.game;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * Fighter base class (updated).
 * - يح facing vector و applyRotation()
 * ت
 * - يحvisuals (Rectangle + head + accent) ما
 */
public class Fighter {
    protected double x, y;
    protected final double width = 40;
    protected final double height = 40;
    protected int health = 100;
    protected final Pane root;
    protected com.example.game.Weapon weapon;

    // visuals
    private final Rectangle body;
    private final Circle head;
    private final Polygon accent;
    private final javafx.scene.Group viewGroup;

    // base color
    private final Color baseColor;

    // facing vector
    private double facingX = 1.0;
    private double facingY = 0.0;

    // rotation transform with pivot in center
    private final Rotate rotate = new Rotate(0, width/2.0, height/2.0);

    public Fighter(double x, double y, Pane root, Color color, com.example.game.Weapon weapon) {
        this.x = x;
        this.y = y;
        this.root = root;
        this.weapon = weapon;
        this.baseColor = color;

        // body gradient
        Stop[] stops = new Stop[] {
                new Stop(0, color.brighter()),
                new Stop(0.6, color),
                new Stop(1, color.darker().darker())
        };
        LinearGradient bodyGrad = new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE, stops);
        body = new Rectangle(width, height*0.6, bodyGrad);
        body.setArcWidth(10); body.setArcHeight(10);
        body.setTranslateY(height*0.4);

        // head
        head = new Circle(width/2.0, height*0.25, width*0.22);
        head.setFill(new RadialGradient(0,0,0.3,0.3,0.8,true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE), new Stop(1, color.darker())));

        // accent triangle to indicate forward
        accent = new Polygon();
        accent.getPoints().addAll(
                width*0.5, height*0.08,
                width*0.5 - 6, height*0.08 + 12,
                width*0.5 + 6, height*0.08 + 12
        );
        accent.setFill(color.brighter());
        accent.setOpacity(0.95);

        viewGroup = new javafx.scene.Group(body, head, accent);
        viewGroup.getTransforms().add(rotate);

        Glow glow = new Glow(0.25);
        DropShadow ds = new DropShadow(12, color);
        ds.setSpread(0.25);
        viewGroup.setEffect(glow);
        viewGroup.setBlendMode(javafx.scene.effect.BlendMode.SRC_OVER);

        viewGroup.setLayoutX(x);
        viewGroup.setLayoutY(y);

        root.getChildren().add(viewGroup);
    }

    // position
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) {
        this.x = x;
        viewGroup.setLayoutX(x);
    }
    public void setY(double y) {
        this.y = y;
        viewGroup.setLayoutY(y);
    }

    public int getHealth() { return health; }
    public void damage(int d) { health -= d; if (health < 0) health = 0; }
    public boolean isDead() { return health <= 0; }

    public Node getView() { return viewGroup; }
    public Color getBaseColor() { return baseColor; }

    public com.example.game.Weapon getWeapon() { return weapon; }
    public void setWeapon(com.example.game.Weapon w) { this.weapon = w; }

    public void setFacingVector(double dx, double dy) {
        if (dx == 0 && dy == 0) return;
        this.facingX = dx;
        this.facingY = dy;
        applyRotation();
    }
    public double getFacingX() { return facingX; }
    public double getFacingY() { return facingY; }

    protected void applyRotation() {
        double angleRad = Math.atan2(facingY, facingX);
        double angleDeg = Math.toDegrees(angleRad);
        rotate.setAngle(angleDeg);
    }


    public com.example.game.Projectile shoot(double dirX, double dirY, Pane root) {
        double dx = (dirX == 0 && dirY == 0) ? this.facingX : dirX;
        double dy = (dirX == 0 && dirY == 0) ? this.facingY : dirY;

        double len = Math.sqrt(dx*dx + dy*dy);
        if (len == 0) { dx = 1; dy = 0; len = 1; }
        dx /= len; dy /= len;  //eqation vector

        double px = x + width/2.0 + dx*(width/2.0 + 6);
        double py = y + height/2.0 + dy*(height/2.0 + 6);

        double speed = weapon.getProjectileSpeed();
        double vx = dx*speed;
        double vy = dy*speed;

        return new com.example.game.Projectile(px, py, vx, vy, weapon, root, baseColor.brighter(), 6);
    }


    public double centerX() {
        return this.x + this.width / 2.0;
    }

    public double centerY() {
        return this.y + this.height / 2.0;
    }


    public Bounds getBounds() { return viewGroup.getBoundsInParent(); }
}
