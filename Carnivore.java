package com.example.eccogui;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Carnivore extends Animal
{
    public Carnivore(Point location, Board board)
    {
        super(location, 'C',40, board);
    }
}