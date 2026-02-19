package com.example.eccogui;
public class Main
{
    public static void run(String[] args)
    {
        Simulator simulator = new Simulator(5, 5, 2, 2, 1);
        simulator.run(4);
    }
    public static void main(String[] args)
    {
        Main.run(args);
    }
}