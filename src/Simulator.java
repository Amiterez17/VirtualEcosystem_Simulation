public class Simulator
{
    private Board simBoard;

    public Simulator(int boardX, int boardY, int numOfCarnivores, int numOfHerbivores, int numOfPlants)
    {
        //int playedRounds = 0;
        simBoard = new Board(boardX, boardY);
        simBoard.placeOrganisms(numOfCarnivores, numOfHerbivores, numOfPlants);
        System.out.println("Starting board state:");
        simBoard.printBoard();
    }

    public void run(int numOfDays)
    {
        for (int day = 0; day < numOfDays; day++)
        {
            simBoard.simulateDay();
            if (Organism.getPopulationCount() == 0)
            {
                System.out.println("there is no any organism in the board");
                break;
            }
            System.out.println();
        }
    }
}