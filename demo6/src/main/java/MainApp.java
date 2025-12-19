package com.example.game;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainApp extends Application {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 540;

    // Media player
    private MediaPlayer bgMusic;

    @Override
    public void start(Stage stage) {
        // show CharacterSelection scene
        com.example.game.CharacterSelection cs = new com.example.game.CharacterSelection();
        List<com.example.game.Weapon> weapons = com.example.game.WeaponFactory.getAllWeapons();

        cs.show(stage, weapons, (p1Choice, p2Choice) -> {

            stopMusic();
            Scene game = createGameScene(stage, p1Choice, p2Choice);
            stage.setScene(game);
            stage.centerOnScreen();
            // start music for the gameplay
            playBackgroundMusic();
        });
    }


    private void playBackgroundMusic() {
        try {

            URL res = getClass().getResource("error3");
            if (res == null) {
                System.out.println("bg music resource not found at /sfx/music.mp3 — skip music.");
                return;
            }
            String path = res.toExternalForm();
            Media media = new Media(path);
            bgMusic = new MediaPlayer(media);
            bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
            bgMusic.setVolume(0.20); // default volume (0.0 - 1.0)
            bgMusic.play();
        } catch (Exception ex) {
            System.out.println("Error starting background music: " + ex.getMessage());
            // don't crash the game for audio problems
        }
    }

    private void stopMusic() {
        try {
            if (bgMusic != null) {
                bgMusic.stop();
                bgMusic.dispose();
                bgMusic = null;
            }
        } catch (Exception ignored) {}
    }

    private void toggleMusicMute() {
        if (bgMusic == null) return;
        bgMusic.setMute(!bgMusic.isMute());
    }

    private Scene createGameScene(Stage stage, String p1Choice, String p2Choice) {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);
        // neon-ish background
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #071028 0%, #0b1f2f 40%, #16314a 100%);");

        // (visual only)
        Region mid = new Region();
        mid.setLayoutX(WIDTH/2.0 - 2);
        mid.setLayoutY(0);
        mid.setPrefWidth(4);
        mid.setPrefHeight(HEIGHT);
        mid.setStyle("-fx-background-color: linear-gradient(#4dd0e1,#00e5ff); -fx-opacity: 0.22;");
        root.getChildren().add(mid);

        List<com.example.game.Weapon> weapons = com.example.game.WeaponFactory.getAllWeapons();
        double p1StartX = WIDTH * 0.12;
        double p2StartX = WIDTH * 0.88 - 40;
        double startY = HEIGHT / 2.0 - 20;

        com.example.game.Fighter f1 = com.example.game.FighterFactory.createFighter(p1Choice, p1StartX, startY, root, weapons.get(0));
        com.example.game.Fighter f2 = com.example.game.FighterFactory.createFighter(p2Choice, p2StartX, startY, root, weapons.get(1));

        com.example.game.PlayerController pc1 = new com.example.game.PlayerController(f1, root, 0, WIDTH, KeyCode.F, KeyCode.DIGIT1, KeyCode.DIGIT2, weapons);
        com.example.game.PlayerController pc2 = new com.example.game.PlayerController(f2, root, 0, WIDTH, KeyCode.L, KeyCode.DIGIT8, KeyCode.DIGIT9, weapons);

        com.example.game.HealthBar hb1 = new com.example.game.HealthBar(14, 14, 260, 16, root);
        Text lbl1 = new Text("P1: " + p1Choice + " | W: " + pc1.getWeaponName());
        lbl1.setFill(Color.web("#aee7ff"));
        lbl1.setFont(Font.font(13));
        lbl1.setX(14); lbl1.setY(36);

        com.example.game.HealthBar hb2 = new com.example.game.HealthBar(WIDTH - 274, 14, 260, 16, root);
        Text lbl2 = new Text("P2: " + p2Choice + " | W: " + pc2.getWeaponName());
        lbl2.setFill(Color.web("#aee7ff"));
        lbl2.setFont(Font.font(13));
        lbl2.setX(WIDTH - 274); lbl2.setY(36);

        root.getChildren().addAll(lbl1, lbl2);

        List<com.example.game.Projectile> projectiles = new ArrayList<>();

        Scene scene = new Scene(root);

        // key handle
        scene.setOnKeyPressed(e -> {
            KeyCode c = e.getCode();
            if (c == KeyCode.W || c == KeyCode.A || c == KeyCode.S || c == KeyCode.D ||
                    c == KeyCode.F || c == KeyCode.DIGIT1 || c == KeyCode.DIGIT2) {
                pc1.addKey(c);
            }
            if (c == KeyCode.UP || c == KeyCode.DOWN || c == KeyCode.LEFT || c == KeyCode.RIGHT ||
                    c == KeyCode.L || c == KeyCode.DIGIT8 || c == KeyCode.DIGIT9) {
                pc2.addKey(c);
            }

            // M to mute/unmute bg music
            if (c == KeyCode.M) {
                toggleMusicMute();
            }
        });
        scene.setOnKeyReleased(e -> {
            KeyCode c = e.getCode();
            pc1.removeKey(c);
            pc2.removeKey(c);
        });

        // game loop
        new AnimationTimer() {
            private long lastTime = System.nanoTime();
            private boolean gameOver = false;

            @Override
            public void handle(long now) {// frames
                if (gameOver) return;
                double dt = (now - lastTime) / 1_000_000_000.0;
                if (dt > 0.05) dt = 0.05;
                lastTime = now;

                // movement
                double moveStep = 260 * dt;
                double dx1 = 0, dy1 = 0;
                if (pc1.isKeyPressed(KeyCode.A)) dx1 -= moveStep;
                if (pc1.isKeyPressed(KeyCode.D)) dx1 += moveStep;
                if (pc1.isKeyPressed(KeyCode.W)) dy1 -= moveStep;
                if (pc1.isKeyPressed(KeyCode.S)) dy1 += moveStep;
                pc1.moveBy(dx1, dy1);

                double dx2 = 0, dy2 = 0;
                if (pc2.isKeyPressed(KeyCode.LEFT)) dx2 -= moveStep;
                if (pc2.isKeyPressed(KeyCode.RIGHT)) dx2 += moveStep;
                if (pc2.isKeyPressed(KeyCode.UP)) dy2 -= moveStep;
                if (pc2.isKeyPressed(KeyCode.DOWN)) dy2 += moveStep;
                pc2.moveBy(dx2, dy2);

                // weapon switch & shooting
                pc1.handleWeaponSwitchKeys();
                pc2.handleWeaponSwitchKeys();
                pc1.tryFire(now, projectiles);
                pc2.tryFire(now, projectiles);

                // update projectiles and collisions
                Iterator<com.example.game.Projectile> pit = projectiles.iterator();
                while (pit.hasNext()) {
                    com.example.game.Projectile p = pit.next();
                    p.update(dt);

                    if (p.isOffScreen(WIDTH, HEIGHT)) {
                        p.destroy();
                        pit.remove();
                        continue;
                    }

                    // detect collision
                    double px = p.getView().getCenterX();
                    double py = p.getView().getCenterY();
                    double d1 = Math.hypot(px - f1.centerX(), py - f1.centerY());
                    double d2 = Math.hypot(px - f2.centerX(), py - f2.centerY());
                    com.example.game.Fighter target = d1 < d2 ? f1 : f2;

                    if (p.getView().getBoundsInParent().intersects(target.getView().getBoundsInParent())) {
                        target.damage(p.getWeapon().getDamage());
                        p.destroy();
                        pit.remove();
                    }
                }

                // update frontend
                hb1.update(pc1.getFighter().getHealth(), pc1.getStartHealth());
                hb2.update(pc2.getFighter().getHealth(), pc2.getStartHealth());
                lbl1.setText("P1: " + p1Choice + " | W: " + pc1.getWeaponName() + " | HP: " + pc1.getFighter().getHealth());
                lbl2.setText("P2: " + p2Choice + " | W: " + pc2.getWeaponName() + " | HP: " + pc2.getFighter().getHealth());

                // check end
                if (pc1.getFighter().isDead() || pc2.getFighter().isDead()) {
                    gameOver = true;
                    String winner;
                    if (pc1.getFighter().isDead() && pc2.getFighter().isDead()) winner = "Draw!";
                    else if (pc1.getFighter().isDead()) winner = "Player 2 Wins!";
                    else winner = "Player 1 Wins!";

                    // stop music
                    stopMusic();

                    // show fancy winner scene instead of Alert
                    showWinnerScene(stage, winner, () -> {
                        // restart
                        Platform.runLater(() -> MainApp.this.start(stage));

                    });
                    stop();
                }
            }
        }.start();

        return scene;
    }


    private void showWinnerScene(Stage stage, String winnerText, Runnable onReplay) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #071028, #00121a);");
        root.setPrefSize(WIDTH, HEIGHT);

        VBox panel = new VBox(18);
        panel.setPadding(new Insets(24));
        panel.setMaxWidth(520);
        panel.setAlignment(javafx.geometry.Pos.CENTER);
        panel.setStyle("-fx-background-color: rgba(255,255,255,0.03); -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: linear-gradient(#ffd86b,#ff6b6b); -fx-border-width: 3;");

        Text title = new Text(winnerText);
        title.setFill(Color.web("#ffd86b"));
        title.setFont(Font.font("Verdana", 36));
        title.setEffect(new javafx.scene.effect.DropShadow(20, Color.web("#ffb86b")));

        Text subtitle = new Text("Congratulations!");
        subtitle.setFill(Color.web("#aee7ff"));
        subtitle.setFont(Font.font(18));

        HBox buttons = new HBox(14);
        buttons.setAlignment(javafx.geometry.Pos.CENTER);

        Button replay = new Button("Play Again");
        replay.setStyle("-fx-background-radius: 8; -fx-background-color: linear-gradient(#4dd0e1,#00e5ff); -fx-text-fill: #001;");
        replay.setOnAction(e -> {
            // ensure music stopped, then replay (start shows selection again)
            stopMusic();
            onReplay.run();
        });

        Button exit = new Button("Exit");
        exit.setStyle("-fx-background-radius: 8; -fx-background-color: linear-gradient(#ff6b6b,#ff3b3b); -fx-text-fill: white;");
        exit.setOnAction(e -> {
            stopMusic();
            Platform.exit();
        });

        buttons.getChildren().addAll(replay, exit);

        panel.getChildren().addAll(title, subtitle, buttons);


        Pane confetti = new Pane();
        confetti.setPickOnBounds(false);
        for (int i = 0; i < 18; i++) {
            javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(6, Color.hsb(Math.random()*360, 0.75, 0.95));
            c.setTranslateX(Math.random() * WIDTH - WIDTH/2.0);
            c.setTranslateY(-Math.random()*80 - 20);
            confetti.getChildren().add(c);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(1.8 + Math.random()), c);
            tt.setFromY(c.getTranslateY());
            tt.setToY(HEIGHT/2.0 + Math.random()*200);
            tt.setCycleCount(1);
            tt.setDelay(Duration.seconds(Math.random() * 0.6));
            tt.play();

            RotateTransition rt = new RotateTransition(Duration.seconds(2 + Math.random()), c);
            rt.setByAngle(360);
            rt.setCycleCount(Animation.INDEFINITE);
            rt.play();
        }

        root.getChildren().addAll(confetti, panel);
        StackPane.setAlignment(panel, javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public static void main(String[] args) { launch(args); }
}
