package com.example.pos;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage stage) {

        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, blue, pink);");

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(800);
        vbox.setMaxWidth(900);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle(
                "-fx-background-color: rgba(0,0,0,0.6);" +
                        "-fx-padding:30;" +
                        "-fx-background-radius:15;"
        );

        Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.jpg"));
        ImageView logoView = new ImageView(logoImage);
        logoView.fitWidthProperty().bind(vbox.widthProperty().multiply(0.4));
        logoView.setPreserveRatio(true);
        logoView.setPreserveRatio(true);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.BLACK);
        logoView.setEffect(shadow);

        Label title = new Label("THE GRILL");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);
        Reflection reflection = new Reflection();
        reflection.setFraction(0.4);
        title.setEffect(reflection);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle(
                "-fx-background-color:red;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:16;" +
                        "-fx-padding:8 25 8 25;" +
                        "-fx-background-radius:20;"
        );

        loginBtn.setOnMouseEntered(e ->
                loginBtn.setStyle(
                        "-fx-background-color:#c0392b;" +
                                "-fx-text-fill:white;" +
                                "-fx-font-size:16;" +
                                "-fx-padding:8 25 8 25;" +
                                "-fx-background-radius:20;"
                )
        );

        loginBtn.setOnMouseExited(e ->
                loginBtn.setStyle(
                        "-fx-background-color:#e74c3c;" +
                                "-fx-text-fill:white;" +
                                "-fx-font-size:16;" +
                                "-fx-padding:8 25 8 25;" +
                                "-fx-background-radius:20;"
                )
        );

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if(username.equals("grill") && password.equals("1234")) {
                showMainScreen(stage);
            } else {
                errorLabel.setText("Invalid Username or Password!");
            }
        });

        vbox.getChildren().addAll(
                logoView,
                title,
                usernameField,
                passwordField,
                loginBtn,
                errorLabel
        );

        AnchorPane.setTopAnchor(vbox, 30.0);
        AnchorPane.setBottomAnchor(vbox, 30.0);
        AnchorPane.setLeftAnchor(vbox, 50.0);
        AnchorPane.setRightAnchor(vbox, 50.0);

        root.getChildren().add(vbox);

        Scene scene = new Scene(root, 500, 500);

        stage.setTitle("THE GRILL Login");
        stage.setScene(scene);
        stage.show();
    }

    private void showMainScreen(Stage stage) {
        MainController controller = new MainController();
        controller.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}