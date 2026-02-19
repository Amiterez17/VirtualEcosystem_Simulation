package com.example.eccogui;
import java.awt.*;
import java.util.List;
import java.awt.Point;
public interface I_AnimalAction
{
    void eat (Point current_point);
    void move(String previous_direction);
}