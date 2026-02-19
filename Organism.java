package com.example.eccogui;
import java.awt.Point;
abstract class Organism
{
    protected Point location;
    private static int count = 0;
    protected int id;
    protected int energy;
    protected char name;
    protected Board board;

    public Organism(Point location, char name, int energy, Board board)
    {
        this.location = location;
        id = count++;
        this.name = name;
        this.energy = energy;
        this.board = board;
    }
    public char get_name()
    {
        return name;
    }
    public static int get_count()
    {
        return count;
    }
    public static void set_count()
    {
        count--;
    }
    public int get_energy()
    {
        return energy;
    }
    public Point get_location()
    {
        return location;
    }
    public void set_location(Point location)
    {
        this.location = location;
    }
    public void set_energy(int new_energy)
    {
        energy = energy + new_energy;
    }
}