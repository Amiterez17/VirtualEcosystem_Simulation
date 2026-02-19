package com.example.eccogui;
import java.awt.*;
abstract class Animal extends Organism implements I_AnimalAction
{
    public String direction = "Down";
    public Boolean if_move_today = false;
    public Boolean if_eat_today = false;

    public Animal(Point location, char name, int energy, Board board)
    {
        super(location, name, energy, board);
    }
    public void set_direction(String new_direction)
    {
        this.direction = new_direction;
    }
    public void set_if_move_today(Boolean answer)
    {
        this.if_move_today = answer;
    }
    public void set_if_eat_today(Boolean answer)
    {
        this.if_eat_today = answer;
    }

    //helper func - return an array of Points according to the current point, in order of : down,left,up,right
    public Point[] directions(Point current_point)
    {
        Point p_down = new Point(current_point.x + 1, current_point.y);
        Point p_left = new Point(current_point.x, current_point.y - 1);
        Point p_up = new Point(current_point.x - 1, current_point.y);
        Point p_right = new Point(current_point.x, current_point.y + 1);
        return new Point[]{p_down, p_left, p_up, p_right};
    }

    //helper func - return true it is possible to move to another cell and false otherwise
    public boolean if_possible(Point next_point)
    {
        Board board = this.board;
        Organism[][] organism_array = board.get_organism_array();
        if (next_point.x < 0 || next_point.x >= board.get_rows() || next_point.y < 0 || next_point.y >= board.get_cols())
            return false;
        if(organism_array[next_point.x][next_point.y] != null)
        {
            char name = organism_array[next_point.x][next_point.y].get_name();
            // if there is an organism there
            return name != 'H' && name != 'P' && name != 'C';
        }
        return true;
    }

    //helper function - updates the attributes that need to change after move to another cell
    public void updates(Point next,Point prev)
    {
        Board board = this.board;
        Organism[][] organism_array = board.get_organism_array();

        organism_array[next.x][next.y] = organism_array[prev.x][prev.y];
        organism_array[prev.x][prev.y] = null;

        //if herbivore so level energy should be 10 less
        if (organism_array[next.x][next.y] != null)
        {
            if (organism_array[next.x][next.y].get_name() == 'H')
                organism_array[next.x][next.y].set_energy(-10);

            //if carnivore so level energy should be 15 less
            else
                organism_array[next.x][next.y].set_energy(-15);
        }
    }

    public void eat(Point current_point)
    {
        Board board = this.board;
        Organism[][] organism_array = board.get_organism_array();
        char current_organism = organism_array[current_point.x][current_point.y].get_name();
        Point[] arr_points = directions(current_point);
        if (this.if_eat_today)
            return;
        this.if_eat_today = true;
        for (Point point : arr_points)     //Go over the points in the required order.
        {

            //check if the point is out of bounds, so skip to check the next point
            if (point.x == -1 || point.x == board.get_rows() || point.y == -1 || point.y == board.get_cols())
                continue;
            Organism organism = organism_array[point.x][point.y];
            //If the cell is good, we will check whether we have found a match between the organism and what it eats.
            if (organism != null)
            {
                if ((current_organism == 'H' && organism.get_name() == 'P')
                        || (current_organism == 'C' && organism.get_name() == 'H'))
                {

                    //if so, we will set the attributes accordingly.
                    this.set_energy(organism.get_energy());
                    Organism.set_count();
                    organism_array[point.x][point.y].set_energy(0);
                    organism_array[point.x][point.y] = null;
                    break;
                }
            }
        }
    }

    public void move(String previous_direction)
    {
        Point p = this.location;
        String[] direction = {"Down", "Left", "Up", "Right"};
        Point[] points = directions(this.get_location());
        for (int i = 0; i < points.length; i++)   // going over the points around the current point in the required order
        {
            if (previous_direction.equals(direction[i]))     //check if it is possible to move at the same direction as the last move
            {
                if (if_possible(points[i]))
                {
                    if(this.if_move_today)
                        break;
                    this.set_if_move_today(true);
                    updates(points[i],p);       //if so, update attributes
                    this.set_location(points[i]);
                    this.set_direction(direction[i]);
                    break;
                }
                else
                {
                    // going over direction array, to check other direction
                    int attempts = 0;
                    int j = i + 1;
                    if (j == points.length)
                        j = 0;

                    // if attempts = 3, so we can stop check, cause there is no possible movement.
                    while (!if_possible(points[j]) && attempts < 3)
                    {
                        j++;
                        attempts++;
                        if (j == points.length)
                            j = 0;
                    }
                    if (attempts == 3)
                    {
                        //System.out.print("No movement is performed");
                        break;
                    }
                    else
                    {
                        // if we go out the while loop and attempts != 3, so we found a possible direction, now doing the updates.
                        if (if_possible(points[j]))
                        {
                            if(this.if_move_today)
                                break;
                            this.set_if_move_today(true);
                            updates(points[j], p);
                            this.set_location(points[j]);
                            this.set_direction(direction[j]);
                            break;
                        }
                    }
                }
            }
        }
    }
}