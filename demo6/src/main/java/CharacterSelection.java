package com.example.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/**
 * Scene 1 — Character selection with neon look.
 * Keeps the simple callback interface so MainApp can continue to use it.
 */
public class CharacterSelection {
    public void show(Stage stage, List<com.example.game.Weapon> weapons, SelectionCallback callback) {

        VBox root = new VBox(18);
        root.setPadding(new Insets(26));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0b1020 0%, #13203a 60%);");

        // title (neon)
        Text title = new Text("NEON ARENA — PICK YOUR FIGHTER");
        title.setFill(Color.web("#ffd86b"));
        title.setFont(Font.font("Verdana", 28));
        DropShadow titleGlow = new DropShadow(18, Color.web("#ffb86b"));
        title.setEffect(titleGlow);

        // horizontal selection area
        HBox selRow = new HBox(22);
        selRow.setAlignment(Pos.CENTER);

        // Player 1 column
        VBox p1Col = new VBox(10);
        p1Col.setAlignment(Pos.CENTER);
        Text p1Label = new Text("Player 1");
        p1Label.setFill(Color.web("#9fe6ff"));
        p1Label.setFont(Font.font(16));

        ComboBox<String> player1Choice = new ComboBox<>();
        player1Choice.getItems().addAll("Warrior", "Mage", "Archer");
        player1Choice.getSelectionModel().selectFirst();

        // preview box
        Rectangle p1Preview = new Rectangle(120, 80, Color.web("#222"));
        p1Preview.setArcWidth(10); p1Preview.setArcHeight(10);
        p1Preview.setStroke(Color.web("#3fe6ff"));
        p1Preview.setStrokeWidth(3);
        p1Preview.setEffect(new DropShadow(10, Color.web("#3fe6ff")));

        p1Col.getChildren().addAll(p1Label, player1Choice, p1Preview);

        // Player 2 column
        VBox p2Col = new VBox(10);
        p2Col.setAlignment(Pos.CENTER);
        Text p2Label = new Text("Player 2");
        p2Label.setFill(Color.web("#ff9fe6"));
        p2Label.setFont(Font.font(16));

        ComboBox<String> player2Choice = new ComboBox<>();
        player2Choice.getItems().addAll("Warrior", "Mage", "Archer");
        player2Choice.getSelectionModel().select(1);

        Rectangle p2Preview = new Rectangle(120, 80, Color.web("#222"));
        p2Preview.setArcWidth(10); p2Preview.setArcHeight(10);
        p2Preview.setStroke(Color.web("#ff6bd8"));
        p2Preview.setStrokeWidth(3);
        p2Preview.setEffect(new DropShadow(10, Color.web("#ff6bd8")));

        p2Col.getChildren().addAll(p2Label, player2Choice, p2Preview);

        selRow.getChildren().addAll(p1Col, p2Col);

        // hint text
        Text hint = new Text("Controls: P1 (WASD / F shoot). P2 (Arrows / L shoot). Switch weapons with 1/2 and 8/9.");
        hint.setFill(Color.web("#cfe8ff"));
        hint.setFont(Font.font(12));

        // Start button
        Button startBtn = new Button("START SHOWDOWN");
        startBtn.setFont(Font.font("Arial", 16));
        startBtn.setTextFill(Color.WHITE);
        startBtn.setStyle("-fx-background-radius: 12; -fx-padding: 10 22 10 22; -fx-background-color: linear-gradient(#ff6b6b, #ff3b3b);");
        startBtn.setEffect(new DropShadow(14, Color.web("#ff6b6b")));

        // disable start until both chosen and different (optional)
        startBtn.setDisable(false); // allow same picks if wanted

        // update previews when selection changes (simple color mapping)
        player1Choice.setOnAction(e -> {
            String s = player1Choice.getValue();
            p1Preview.setFill(mapPreviewColor(s));
        });
        player2Choice.setOnAction(e -> {
            String s = player2Choice.getValue();
            p2Preview.setFill(mapPreviewColor(s));
        });


        p1Preview.setFill(mapPreviewColor(player1Choice.getValue()));
        p2Preview.setFill(mapPreviewColor(player2Choice.getValue()));


        startBtn.setOnAction(e -> {
            String p1 = player1Choice.getValue();
            String p2 = player2Choice.getValue();
            callback.onSelected(p1, p2);
        });


        root.getChildren().addAll(title, selRow, hint, startBtn);
        Scene scene = new Scene(root, 520, 340);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private Color mapPreviewColor(String name) {
        if (name == null) return Color.web("#222");
        switch (name.toLowerCase()) {
            case "warrior": return Color.web("#ff6b6b");
            case "mage": return Color.web("#9b59b6");
            case "archer": return Color.web("#2ecc71");
            default: return Color.web("#444");
        }
    }

    public interface SelectionCallback {
        void onSelected(String player1Class, String player2Class);
    }
}