package com.example.eccogui;
import javax.swing.*;
import java.awt.*;

public class Simulator
{
    private Board simBoard;

    public Simulator(int boardX, int boardY, int numOfCarnivores, int numOfHerbivores, int numOfPlants)
    {
        //int playedRounds = 0;
        simBoard = new Board(boardX, boardY);
        simBoard.placeOrganisms(numOfCarnivores, numOfHerbivores, numOfPlants);
        System.out.println("Starting board state:");
        simBoard.printBoard();
    }

    public void run(int numOfDays)
    {
        while (numOfDays > 0)
        {
            simBoard.reset_moves();  //reset the board
            simBoard.reset_eats();
            for (int r = 0; r < simBoard.get_rows(); r++)  // going over the board cells
            {
                for (int c = 0; c < simBoard.get_cols(); c++)
                {
                    Organism organism = simBoard.get_organism_array()[r][c];
                    // if the cell contain on organism, so we check.
                    if (organism != null)
                    {
                        if (organism.get_name() == 'H' || organism.get_name() == 'C')
                        {
                            if (!((Animal)organism).if_move_today && organism.get_energy() <= 0)  //if the organism is dead
                            {
                                Organism.set_count();    // update the number of organism in the board
                                simBoard.get_organism_array()[r][c] = null;
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
            // check if there are left organism on the board
            if (Organism.get_count() == 0)
            {
                System.out.println("there is no any organism in the board");
                break;
            }
            //numOfDays -= 1;
            System.out.println();
            simBoard.printBoard();
        }
    }
}