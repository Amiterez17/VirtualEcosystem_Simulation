package com.example.eccogui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

public class Grid extends Application
{
    private Board board;
    TextField carnivores_field;
    TextField herbivores_field;
    TextField plants_field;
    BorderPane root;
    private Button next_day;
    private int day_number;
    private Label day_label;

    public Grid()
    {
        this.root = new BorderPane();
        this.board = new Board(10, 10);
        this.carnivores_field = new TextField("0");
        this.herbivores_field = new TextField("0");
        this.plants_field = new TextField("0");
        this.day_number = 0;
        this.day_label = new Label("Day: " + this.day_number);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        // making 3 Hboxs (bottom left - day. bottom center - organisms, bottom right - simulation
        HBox day = new HBox();
        day.setSpacing(10);
        next_day = new Button("Next");
        next_day.setDisable(true);
        day.getChildren().addAll(day_label, next_day);
        day.setAlignment(Pos.BOTTOM_LEFT);

        HBox organisms = new HBox();
        Label carnivores_label = new Label("Carnivores ");
        Label herbivores_label = new Label("  Herbivores ");
        Label plants_label = new Label("  Plants ");
        organisms.getChildren().addAll(carnivores_label, this.carnivores_field, herbivores_label, this.herbivores_field, plants_label, this.plants_field);
        organisms.setAlignment(Pos.BOTTOM_CENTER);

        HBox simulation = new HBox();
        Button simula = new Button("New Simulation");
        simulation.getChildren().add(simula);
        simulation.setAlignment(Pos.BOTTOM_RIGHT);

        root.setCenter(this.board.get_grid());
        root.setBottom(organisms);
        root.setLeft(day);
        root.setRight(simulation);

        Scene scene = new Scene(root);
        stage.setTitle("Virtual Eco Simulator");
        stage.setScene(scene);
        stage.show();

        simula.setOnAction(e -> new_simulation());
        next_day.setOnAction(e -> next_day());

    }
    // check inputs, if negative,empty,not integer-input or total numbers is over than 80
    private boolean check_inputs()
    {
        String carnivores = this.carnivores_field.getText();
        String herbivores = this.herbivores_field.getText();
        String plants = this.plants_field.getText();
        try
        {
            int num_carnivores = Integer.parseInt(carnivores);
            int num_herbivores = Integer.parseInt(herbivores);
            int num_plants = Integer.parseInt(plants);
            if (num_herbivores < 0 || num_carnivores < 0 || num_plants < 0 || carnivores.isEmpty() || herbivores.isEmpty() || plants.isEmpty())
                throw new NumberFormatException();
            if (num_herbivores + num_carnivores + num_plants >= 80)
                throw new Exception();
            else
                return true;
        }
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please try again, fields must contain digits only and must not be empty");
            alert.showAndWait();
            return false;
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please try again, total numbers must be less then 80");
            alert.showAndWait();
            return false;
        }
    }

    // when next day button is clicked, so day number value increase by one and so the label updates
    // calling to run() function from the previous assignment in order to do all actions (check energy levels, eat and move)
    private void next_day()
    {
        this.day_number++;
        this.day_label.setText("Day: " + this.day_number);
        run();
        this.board.from_board_to_grid();
        this.root.setCenter(this.board.get_grid());
    }

    // when new simulation button is clicked, so we will check if next-day button is disable,
    // if so, we have to check the inputs and placed organisms on the board and grid
    // if not, so we need to clear the board and grid, and wait for new inputs from the user
    private void new_simulation()
    {
        if (this.next_day.isDisabled() && check_inputs())
        {
            this.next_day.setDisable(false);
            int num_carnivores = Integer.parseInt(this.carnivores_field.getText());
            int num_herbivores = Integer.parseInt(this.herbivores_field.getText());
            int num_plants = Integer.parseInt(this.plants_field.getText());
            this.board.reset_cell_grid();
            this.board.resetBoard();
            this.board.placeOrganisms(num_carnivores, num_herbivores, num_plants);
            this.board.printBoard();
            this.board.from_board_to_grid();
            this.root.setCenter(this.board.get_grid());
        }
        else
        {
            this.board.reset_cell_grid();
            this.board.resetBoard();
            this.root.setCenter(this.board.get_grid());
            this.carnivores_field.setText("0");
            this.herbivores_field.setText("0");
            this.plants_field.setText("0");
            this.next_day.setDisable(true);
            this.day_number = 0;
            this.day_label.setText("Day: " + this.day_number);
        }
    }

    // same function from the previous assignment, (except the number of days).
    // the function called from next_day() function
    public void run()
    {
        //reset the board
        this.board.reset_moves();
        this.board.reset_eats();
        for (int r = 0; r < this.board.get_rows(); r++)  // going over the board cells
        {
            for (int c = 0; c < this.board.get_cols(); c++)
            {

                Organism organism = this.board.get_organism_array()[r][c];
                // if the cell contain on organism, so we check.
                if (organism != null)
                {
                    if (organism.get_name() == 'H' || organism.get_name() == 'C')
                    {
                        if (!((Animal)organism).if_move_today && organism.get_energy() <= 0)  //if the organism is dead and didnt move today
                        {
                            Organism.set_count();    // update the number of organism in the board
                            this.board.get_organism_array()[r][c] = null;
                            continue;
                        }
                        else
                        {
                            Animal animal = (Animal) organism;
                            Point p = new Point(r, c);   //current point
                            animal.eat(p);      //check if the animal can eat
                            animal.move(animal.direction);
                        }
                    }
                    if (organism.get_name() == 'P')   //  plants gain 10 energy each day
                        organism.set_energy(10);
                }
            }
        }
        System.out.println("new day");
        board.printBoard();

    }

   public static void main(String[] args)
   {
        launch();
   }

}