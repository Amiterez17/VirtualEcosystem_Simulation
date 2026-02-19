package com.example.eccogui;
import java.awt.*;

public class Herbivore extends Animal
{
    public Herbivore(Point location, Board board)
    {
        super(location, 'H', 60, board);
    }
}