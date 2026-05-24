

import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Objects;
import java.util.Random;
import java.awt.Point;

public class Board
{
    private Image image_pp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("//plant.jpg")));
    private Image image_hh = new Image(Objects.requireNonNull(getClass().getResourceAsStream("//herbivore.jpg")));
    private Image image_cc = new Image(Objects.requireNonNull(getClass().getResourceAsStream("//carnivore.jpg")));
    private GridPane grid;
    private Cell[][] cell_grid;
    private Organism[][] board;
    private int rows;
    private int cols;
    Random rand;
    long seed;

    // also fill the [][] cell array and add to the grid
    public Board(int rows, int cols)
    {
        this.grid = new GridPane();
        this.cell_grid = new Cell[rows][cols];
        this.board = new Organism[rows][cols];
        this.rows = rows;
        this.cols = cols;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                cell_grid[i][j] = new Cell();
                grid.add(cell_grid[i][j], j, i);
            }
        }
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            rowConstraints.setFillHeight(true);
            grid.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            colConstraints.setFillWidth(true);
            grid.getColumnConstraints().add(colConstraints);
        }

        // reset all board cells to null
        this.resetBoard();
        seed = System.nanoTime();
        rand = new Random(seed);
    }

    // the function updates all data from the organism board to the grid and add the right image
    public void fromBoardToGrid()
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if (board[i][j] != null)
                {
                    this.cell_grid[i][j].id = this.board[i][j].id;
                    this.cell_grid[i][j].energy = this.board[i][j].energy;
                    this.cell_grid[i][j].name = this.board[i][j].name;

                    if (this.board[i][j].name == 'P')
                        this.cell_grid[i][j].image.setImage(image_pp);
                    if (this.board[i][j].name == 'C')
                        this.cell_grid[i][j].image.setImage(image_cc);
                    if (this.board[i][j].name == 'H')
                        this.cell_grid[i][j].image.setImage(image_hh);
                }
                else
                {
                    this.cell_grid[i][j].id = -1;  // -1 means that there is no organism in this cell
                    this.cell_grid[i][j].energy = 0;  // if there is no organism in the cell, so the energy is 0
                    this.cell_grid[i][j].name = '\0';  // if there is no organism in the cell, so the name is null character
                    this.cell_grid[i][j].image.setImage(null);
                }
            }
        }
    }

    // Set all cells on the board to null.
    // This is the initial state of the board
    // before organisms are placed.
    public void resetBoard()
    {
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
                board[i][j] = null;
        }
    }
    // the function reset the data in every cell in [][]cell array
    public void resetCellGrid()
    {
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
            {
                this.cell_grid[i][j].image.setImage(null);
                this.cell_grid[i][j].id = -1;
                this.cell_grid[i][j].energy = 0;
                this.cell_grid[i][j].name = '\0';
            }
        }
    }

    public Boolean isEmpty(Point p)
    {
        return board[p.x][p.y] == null;
    }

    // Find a random empty cell on the board
    // returns a Point object if found or Null if not found
    private Point getNextRandomEmptyLocation()
    {
        Point p = new Point();
        long attempts = (long)this.rows * this.cols * 2;
        do
        {
            attempts--;
            p.x = rand.nextInt(this.rows);
            p.y = rand.nextInt(this.cols);
        }
        while(!isEmpty(p) && (attempts > 0));

        return((attempts > 0)? p : null);
    }

    // Place organisms on the board
    // inputs: the number of figures of each type to be placed
    public void placeOrganisms(int numOfCarnivores, int numOfHerbivores, int numOfPlants)
    {
//        check if the numbers of organism is smaller than 80% of the cells
//        Scanner scanner = new Scanner(System.in);
//        while ((numOfCarnivores + numOfHerbivores + numOfPlants > 0.8 * (this.rows * this.cols))
//        || numOfCarnivores < 0 || numOfHerbivores < 0 || numOfPlants < 0)
//        {
//            System.out.println("The amount of organisms should be smaller than 80% of the cells or one of the inputs is a negative number ");
//            System.out.print("Enter an integer number of Carnivores: ");
//            numOfCarnivores = scanner.nextInt();
//            System.out.print("Enter an integer number of Herbivores: ");
//            numOfHerbivores = scanner.nextInt();
//            System.out.print("Enter an integer number of Plants: ");
//            numOfPlants = scanner.nextInt();
//        }

        while (numOfCarnivores + numOfHerbivores + numOfPlants > 0)
        {
            Point p = getNextRandomEmptyLocation();
            if (p != null)
            {
                if (numOfPlants > 0)                          // placing the plants on board
                {
                    board[p.x][p.y] = new Plant(p,this);
                    numOfPlants--;
                }
                else if (numOfHerbivores > 0)                // after placing the plants on board, moving to Herbivores
                {
                    board[p.x][p.y] = new Herbivore(p,this);
                    numOfHerbivores--;
                }
                else
                {
                    board[p.x][p.y] = new Carnivore(p,this);      //at the end placing the Carnivores
                    numOfCarnivores--;
                }
            }
            else
                break;                                      //if there is no empty cell in the board
        }
    }

    // print the current board state
    // Organisms are printed by name
    // empty cells are printed as 0
    public void printBoard()
    {
        char printed;
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
            {
                printed = (board[i][j] == null) ? '0' : board[i][j].getName();
                System.out.print(printed + "" + ' ');
            }
            System.out.println();
        }
    }
    /**
     * Simulate a single day on this board: reset move/eat flags, process each organism's actions,
     * remove dead organisms and let plants gain energy.
     */
    public void simulateDay()
    {
        // reset per-day flags
        resetDailyAnimalFlags();
        for (int r = 0; r < this.rows; r++)
        {
            for (int c = 0; c < this.cols; c++)
            {
                Organism organism = this.board[r][c];
                if (organism != null)
                {
                    if (organism.getName() == 'H' || organism.getName() == 'C')
                    {
                        Animal animal = (Animal) organism;
                        if (animal.ifMoveToday)
                            continue;

                        if (animal.getEnergy() <= 0)
                        {
                            // organism is dead
                            Organism.decrementPopulation();
                            this.board[r][c] = null;
                            continue;
                        }
                        else
                        {
                            animal.eat(animal.getLocation());
                            animal.move(animal.direction);
                        }
                    }
                    else if (organism.getName() == 'P')
                    {
                        organism.addEnergy(10);
                    }
                }
            }
        }
        System.out.println("new day");
        printBoard();
    }

    // helper function - reset all animal cells to false - if_move_today cause animal eats once a day
    public void resetDailyAnimalFlags()
    {
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                if (board[r][c] != null)
                {
                    Organism organism = board[r][c];
                    if (organism.getName() == 'H' || organism.getName() == 'C')
                    {
                        Animal animal = (Animal) organism;
                        animal.setIfMoveToday(false);
                        animal.setIfEatToday(false);
                    }
                }
            }
        }
    }

    public int getRows()
    {
        return this.rows;
    }
    public int getCols()
    {
        return this.cols;
    }
    public GridPane getGrid()
    {
        return this.grid;
    }
    public Organism[][] getOrganismArray()
    {
        return this.board;
    }

    // Backwards-compatible wrappers (old method names)
    public int get_rows() { return getRows(); }
    public int get_cols() { return getCols(); }
    public GridPane get_grid() { return getGrid(); }
    public Organism[][] get_organism_array() { return getOrganismArray(); }
}