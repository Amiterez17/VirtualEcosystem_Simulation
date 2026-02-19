package com.example.eccogui;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Plant extends Organism
{
    public Plant(Point location, Board board)
    {
        super(location, 'P',20, board);
    }
}