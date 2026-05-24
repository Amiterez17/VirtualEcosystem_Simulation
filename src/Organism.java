import java.awt.Point;

/**
 * Base class for all organisms in the simulation.
 *
 * Responsibilities:
 * - track immutable id assigned on creation
 * - track current population separately from id generation
 */
abstract class Organism
{
    protected Point location;
    private static int nextId = 0;             // for generating unique ids
    private static int populationCount = 0;    // current number of organisms alive
    protected int id;
    protected int energy;
    protected char name;
    protected Board board;

    public Organism(Point location, char name, int energy, Board board)
    {
        populationCount++;
        this.location = location;
        this.id = nextId++;
        this.name = name;
        this.energy = energy;
        this.board = board;
    }

    public char getName()
    {
        return name;
    }

    public static int getPopulationCount()
    {
        return populationCount;
    }

    public static void decrementPopulation()
    {
        if (populationCount > 0)
            populationCount--;
    }

    public int getEnergy()
    {
        return energy;
    }

    public Point getLocation()
    {
        return location;
    }

    public void setLocation(Point location)
    {
        this.location = location;
    }

    /**
     * Add (or subtract) energy to this organism. Positive to add, negative to subtract.
     */
    public void addEnergy(int delta)
    {
        energy = energy + delta;
    }
}