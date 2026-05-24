import java.awt.*;
abstract class Animal extends Organism implements I_AnimalAction
{
    public String direction = "Down";
    public boolean ifMoveToday = false;
    public boolean ifEatToday = false;

    public Animal(Point location, char name, int energy, Board board)
    {
        super(location, name, energy, board);
    }

    public void setDirection(String newDirection)
    {
        this.direction = newDirection;
    }

    public void setIfMoveToday(boolean answer)
    {
        this.ifMoveToday = answer;
    }

    public void setIfEatToday(boolean answer)
    {
        this.ifEatToday = answer;
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
        Organism[][] organism_array = board.getOrganismArray();
        if (next_point.x < 0 || next_point.x >= board.getRows() || next_point.y < 0 || next_point.y >= board.getCols())
            return false;
        // movement allowed only to empty cells
        return organism_array[next_point.x][next_point.y] == null;
    }

    //helper function - updates the attributes that need to change after move to another cell
    public void updates(Point next,Point prev)
    {
        Board board = this.board;
        Organism[][] organism_array = board.getOrganismArray();

        organism_array[next.x][next.y] = organism_array[prev.x][prev.y];
        organism_array[prev.x][prev.y] = null;

        // decrease energy after movement depending on species
        if (organism_array[next.x][next.y] != null)
        {
            if (organism_array[next.x][next.y].getName() == 'H')
                organism_array[next.x][next.y].addEnergy(-10);
            else
                organism_array[next.x][next.y].addEnergy(-15);
        }
    }

    public void eat(Point current_point)
    {
        if (this.ifEatToday) {
            return;
        }
        this.setIfEatToday(true);
        Board board = this.board;
        Organism[][] organism_array = board.getOrganismArray();
        char current_organism = this.getName();
        Point[] arr_points = directions(current_point);

        for (Point point : arr_points)     //Go over the points in the required order.
        {
            //check if the point is out of bounds, so skip to check the next point
            if (point.x < 0 || point.x >= board.getRows() || point.y < 0 || point.y >= board.getCols())
                continue;

            Organism prey = organism_array[point.x][point.y];
            //If the cell is good, we will check whether we have found a match between the organism and what it eats.
            if (prey != null)
            {
                if ((current_organism == 'H' && prey.getName() == 'P')
                        || (current_organism == 'C' && prey.getName() == 'H'))
                {

                    //if so, we will set the attributes accordingly.
                    this.addEnergy(prey.getEnergy());
                    Organism.decrementPopulation();
                    organism_array[point.x][point.y] = null;
                    break;
                }
            }
        }
    }

    public void move(String previous_direction)
    {
        if (this.ifMoveToday)
        {
            return;
        }
        this.setIfMoveToday(true);
        Point p = this.location;
        String[] direction = {"Down", "Left", "Up", "Right"};
        Point[] points = directions(this.getLocation());

        // find the index of the previous direction in the direction array
        int startIndex = 0;
        for (int i = 0; i < direction.length; i++) {
            if (direction[i].equals(previous_direction)) {
                startIndex = i;
                break;
            }
        }

        // check the current direction and the next three directions in order, using modulo to wrap around the array
        for (int attempts = 0; attempts < 4; attempts++)
        {
            int currentIndex = (startIndex + attempts) % 4;
            Point targetPoint = points[currentIndex];

            if (if_possible(targetPoint))
            {
                updates(targetPoint, p);
                this.setLocation(targetPoint);
                this.setDirection(direction[currentIndex]);
                return;
            }
        }
    }
}