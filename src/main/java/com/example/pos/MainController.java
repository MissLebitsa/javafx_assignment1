package com.example.pos;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainController extends Application {

    private TextArea orderArea;
    private TextField totalField;
    private TextField cashField;
    private TextField changeField;
    private Label timeLabel;

    private double total = 0;
    private int transactionNumber = 1;

    private CheckBox[] dinnerItems;
    private RadioButton[] dessertItems;
    private MenuButton drinksMenu;
    private double lastDessertPrice = 0;
    private String lastDessertName = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-image: url('/images/wood.jpg');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;"
        );

        root.setTop(createTop());
        root.setLeft(createMenu());
        root.setCenter(createOrder());
        root.setRight(createPayment());

        Scene scene = new Scene(root,1200,700);
        stage.setTitle("THE GRILL");
        stage.setScene(scene);
        stage.show();

        startClock();
    }

    private HBox createTop(){

        HBox top = new HBox();
        top.setPadding(new Insets(15));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color:#2c3e50");

        Label title = new Label("🍽 THE GRILL");
        title.setStyle("-fx-text-fill:white; -fx-font-size:22; -fx-font-weight:bold");

        timeLabel = new Label();
        timeLabel.setStyle("-fx-text-fill:white; -fx-font-size:14");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        top.getChildren().addAll(title,spacer,timeLabel);

        return top;
    }

    private ScrollPane createMenu(){

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color:white; -fx-background-radius:10");

        Label menuTitle = new Label("MENU");
        menuTitle.setStyle("-fx-font-size:20; -fx-font-weight:bold");

        menu.getChildren().add(menuTitle);

        menu.getChildren().add(new Label("DINNER"));

        dinnerItems = new CheckBox[]{
                new CheckBox("Grilled Chicken - M150"),
                new CheckBox("Beef Steak - M185"),
                new CheckBox("Vegetable Pasta - M80"),
                new CheckBox("Lamb Chops - M195"),
                new CheckBox("Seafood Platter - M245")
        };

        double[] dinnerPrices = {150,185,80,195,245};

        String[] dinnerImages = {
                "Grilled-Chicken.jpg",
                "Beef Steak.jpg",
                "Vegetable Pasta.jpg",
                "lamb-chops.jpg",
                "Seafood-Platter.jpg"
        };

        for(int i=0;i<dinnerItems.length;i++){
            CheckBox cb = dinnerItems[i];
            double price = dinnerPrices[i];
            String img = dinnerImages[i];

            cb.setOnAction(e->{
                if(cb.isSelected()) addItem(cb.getText(),price);
                else removeItem(cb.getText(),price);
            });

            menu.getChildren().add(createMenuItemWithImage(cb,img));
        }

        menu.getChildren().add(new Label("DESSERT"));

        ToggleGroup dessertGroup = new ToggleGroup();

        dessertItems = new RadioButton[]{
                new RadioButton("Chocolate Cake - M50"),
                new RadioButton("Ice Cream - M40"),
                new RadioButton("Cheesecake - M58"),
                new RadioButton("Brownie - M35"),
                new RadioButton("Fruit Salad - M45")
        };

        double[] dessertPrices = {50,40,58,35,45};

        String[] dessertImages = {
                "Chocolate Cake.jpg",
                "Ice Cream.jpg",
                "Cheesecake.jpg",
                "Brownie.jpg",
                "Fruitsalad.jpg"
        };

        for(int i=0;i<dessertItems.length;i++){

            RadioButton rb = dessertItems[i];
            double price = dessertPrices[i];
            String img = dessertImages[i];

            rb.setToggleGroup(dessertGroup);

            rb.setOnAction(e -> {

                if(!lastDessertName.isEmpty()){
                    removeItem(lastDessertName, lastDessertPrice);
                }

                addItem(rb.getText(), price);

                lastDessertName = rb.getText();
                lastDessertPrice = price;
            });

            menu.getChildren().add(createMenuItemWithImage(rb,img));
        }

        menu.getChildren().add(new Label("DRINKS"));

        drinksMenu = new MenuButton("Select Drink");

        // Create drink items with images
        drinksMenu.getItems().add(createDrinkMenuItem("Soft Drink", 23, "Soft Drink.jpg"));
        drinksMenu.getItems().add(createDrinkMenuItem("Coffee", 35, "Coffee.jpg"));
        drinksMenu.getItems().add(createDrinkMenuItem("Fresh Juice", 29, "Fresh Juice.jpg"));
        drinksMenu.getItems().add(createDrinkMenuItem("Tea", 18, "Tea.jpg"));
        drinksMenu.getItems().add(createDrinkMenuItem("Milkshake", 42, "Milkshake.jpg"));

        menu.getChildren().add(drinksMenu);

        ScrollPane scroll = new ScrollPane(menu);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(350);

        return scroll;
    }

    private HBox createMenuItemWithImage(Control control, String imageFile){

        try {
            Image img = new Image(getClass().getResourceAsStream("/images/" + imageFile));
            ImageView view = new ImageView(img);

            view.setFitWidth(40);
            view.setFitHeight(40);

            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER_LEFT);

            box.getChildren().addAll(view, control);

            return box;
        } catch (Exception e) {
            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().add(control);
            return box;
        }
    }

    private CustomMenuItem createDrinkMenuItem(String name, double price, String imageFile) {

        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(5));

        try {
            Image img = new Image(getClass().getResourceAsStream("/images/" + imageFile));
            ImageView view = new ImageView(img);
            view.setFitWidth(30);
            view.setFitHeight(30);
            content.getChildren().add(view);
        } catch (Exception e) {
            // If image not found, add a placeholder
            Label placeholder = new Label("🫗");
            placeholder.setStyle("-fx-font-size:20");
            content.getChildren().add(placeholder);
        }

        Label textLabel = new Label(name + " - M" + String.format("%.0f", price));
        content.getChildren().add(textLabel);

        CustomMenuItem menuItem = new CustomMenuItem(content);
        menuItem.setHideOnClick(true);

        menuItem.setOnAction(e -> addItem(name + " - M" + String.format("%.0f", price), price));

        return menuItem;
    }

    private VBox createOrder() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/order-background.jpg"));

            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );

            box.setBackground(new Background(backgroundImage));

            box.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius:10;");

        } catch (Exception e) {
            box.setStyle("-fx-background-color:white; -fx-background-radius:10");
            System.out.println("Background image not found: " + e.getMessage());
        }

        Label title = new Label("🧾 ORDER DETAILS");
        title.setStyle("-fx-font-size:18; -fx-font-weight:bold");

        orderArea = new TextArea();
        orderArea.setEditable(false);
        orderArea.setPrefHeight(600);

        orderArea.setStyle("-fx-control-inner-background: transparent; -fx-background-color: transparent; " +
                "-fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        box.getChildren().addAll(title, orderArea);

        return box;
    }

    private VBox createPayment(){

        VBox pay = new VBox(10);
        pay.setPadding(new Insets(15));
        pay.setPrefWidth(250);
        pay.setStyle("-fx-background-color:white; -fx-background-radius:10");

        Label title = new Label("💳 PAYMENT");
        title.setStyle("-fx-font-size:18; -fx-font-weight:bold");

        totalField = new TextField();
        totalField.setEditable(false);

        cashField = new TextField();
        cashField.setPromptText("Enter cash");

        changeField = new TextField();
        changeField.setEditable(false);

        Button calcBtn = new Button("💰 Calculate Change");
        Button purchaseBtn = new Button("🛒 Purchase");
        Button resetBtn = new Button("🔄 Reset");
        Button exitBtn = new Button("🚪 Exit");

        calcBtn.setStyle("-fx-background-color:orange; -fx-text-fill:white; -fx-font-weight:bold;");
        purchaseBtn.setStyle("-fx-background-color:blue; -fx-text-fill:white; -fx-font-weight:bold;");
        resetBtn.setStyle("-fx-background-color:maroon; -fx-text-fill:white; -fx-font-weight:bold;");
        exitBtn.setStyle("-fx-background-color:red; -fx-text-fill:white; -fx-font-weight:bold;");

        calcBtn.setMaxWidth(Double.MAX_VALUE);
        purchaseBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setMaxWidth(Double.MAX_VALUE);

        calcBtn.setOnAction(e->calculateChange());
        purchaseBtn.setOnAction(e->saveTransaction());
        resetBtn.setOnAction(e->reset());
        exitBtn.setOnAction(e->System.exit(0));

        pay.getChildren().addAll(
                title,
                new Label("TOTAL"), totalField,
                new Label("CASH"), cashField,
                calcBtn,
                new Label("CHANGE"), changeField,
                purchaseBtn,
                resetBtn,
                exitBtn
        );

        return pay;
    }

    private void addItem(String name,double price){

        orderArea.appendText(name + "\n");

        total += price;

        totalField.setText("M" + String.format("%.2f",total));
    }

    private void removeItem(String name,double price){

        String[] lines = orderArea.getText().split("\n");

        orderArea.clear();
        total = 0;

        for(String line : lines){

            if(!line.contains(name)){

                orderArea.appendText(line + "\n");

                if(line.contains("M")){
                    try{
                        String value = line.substring(line.indexOf("M")+1);
                        total += Double.parseDouble(value);
                    }catch(Exception ignored){}
                }
            }
        }

        totalField.setText("M" + String.format("%.2f",total));
    }

    private void resetDesserts(){

        for(RadioButton rb : dessertItems){
            if(rb.isSelected()){
                String text = rb.getText();

                double price = Double.parseDouble(text.substring(text.indexOf("M")+1));

                String[] lines = orderArea.getText().split("\n");
                orderArea.clear();

                for(String line : lines){
                    if(!line.contains(text)){
                        orderArea.appendText(line + "\n");
                    }
                }

                total -= price;
            }
        }

        totalField.setText("M" + String.format("%.2f", total));
    }

    private void calculateChange(){

        try{

            double cash = Double.parseDouble(cashField.getText());

            if(cash < total){
                showAlert("Error","Not enough money!",Alert.AlertType.ERROR);
                return;
            }

            double change = cash - total;

            changeField.setText("M" + String.format("%.2f",change));

        }catch(Exception e){
            showAlert("Error","Enter valid number",Alert.AlertType.ERROR);
        }
    }

    private void reset(){

        orderArea.clear();
        total = 0;
        lastDessertName = "";
        lastDessertPrice = 0;

        totalField.clear();
        cashField.clear();
        changeField.clear();

        for(CheckBox cb : dinnerItems)
            cb.setSelected(false);

        for(RadioButton rb : dessertItems)
            rb.setSelected(false);
    }

    private void saveTransaction(){

        if(orderArea.getText().isEmpty()){
            showAlert("Error","No items selected",Alert.AlertType.ERROR);
            return;
        }

        try{

            double cash = Double.parseDouble(cashField.getText());

            if(cash < total){
                showAlert("Error","Not enough cash",Alert.AlertType.ERROR);
                return;
            }

            PrintWriter out = new PrintWriter(new FileWriter("transactions.txt",true));

            out.println("================================");
            out.println("TRANSACTION #" + transactionNumber++);
            out.println("TIME: " + timeLabel.getText());
            out.println("--------------------------------");
            out.println(orderArea.getText());
            out.println("--------------------------------");
            out.println("TOTAL: " + totalField.getText());
            out.println("CASH: M" + cash);
            out.println("CHANGE: " + changeField.getText());
            out.println("================================\n");

            out.close();

            showAlert("Success","Transaction Saved",Alert.AlertType.INFORMATION);

            reset();

        }catch(Exception e){
            showAlert("Error","Invalid Cash Amount",Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title,String msg,Alert.AlertType type){

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void startClock(){

        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timeLabel.setText(format.format(new Date()));
                }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}