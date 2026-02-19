package com.example.eccogui;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class Cell extends Pane
{
    public int id;
    public int energy;
    public char name;
    public ImageView image;

    public Cell()
    {
        this.id = Integer.MAX_VALUE;
        setPrefSize(70,50);
        //this.setPrefSize(70, 50);
        this.setStyle("-fx-background-color: white;" + "-fx-border-color: black;");
        this.image = new ImageView();

        this.image.setFitWidth(70);
        this.image.setFitHeight(50);
        this.image.setPreserveRatio(false);
        this.image.fitWidthProperty().bind(this.widthProperty());
        this.image.fitHeightProperty().bind(this.heightProperty());

        getChildren().add(this.image);
        setOnMouseClicked(event -> message());
    }

    // an alert message popup when we clicked on cell
    public void message()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Message");
        if (this.id != Integer.MAX_VALUE)
            alert.setContentText("Name: "+this.name+"\n"+"ID: "+this.id+"\n"+"Energy: "+this.energy);
        else
            alert.setContentText("this cell is empty");
        alert.showAndWait();
    }
}
