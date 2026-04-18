import java.util.Scanner;
import java.util.Random;
// this draws a 10x10 grid every timec
class Board {
    private char[][] grid=new char[10][10];
    void draw(int pacmanRow,int pacmanCol,int ghostRow,int ghostCol,boolean[][] foodGrid){
        prepareBlankGrid();
        placeBorderWalls();
        placeFoodDots(foodGrid);
        placeCharacters(pacmanRow, pacmanCol, ghostRow, ghostCol);
        printGridToConsole();
    }
    private void prepareBlankGrid(){
        // used to prepare the empty space inside borders
        for(int row=0;row<10;row++){
            for (int col=0;col<10;col++){
                grid[row][col]=' ';
            }
        }
    }
    private void placeBorderWalls(){
        //places the border which is "="
        for(int row=0;row<10;row++){
            for(int col=0;col<10;col++){
                if(row==0||col==0||row==9||col==9){
                    grid[row][col]='=';
                }
            }
        }
    }
    // If a cell has food (true), stamp a dot there
    private void placeFoodDots(boolean[][] foodGrid) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                if (foodGrid[row][col]) {
                    grid[row][col] = '.';
                }
            }
        }
    }

    private void placeCharacters(int pacmanRow, int pacmanCol,
                                 int ghostRow,  int ghostCol) {
        grid[pacmanRow][pacmanCol] = 'P';
        grid[ghostRow][ghostCol]   = 'G';
    }

    private void printGridToConsole() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
    }
}

class Pacman {

    private int row;
    private int col;

    // Constructor sets the starting position
    Pacman(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
    }

    void move(char directionKey) {
        int targetRow = this.row;
        int targetCol = this.col;

        if (directionKey == 'w')  {
            targetRow = this.row - 1; // up
        }
        if (directionKey == 's'){
            targetRow = this.row + 1; // down
        }
        if (directionKey == 'a'){
            targetCol = this.col - 1; // left
        }
        if (directionKey == 'd') {
            targetCol = this.col + 1; // right
        }

        boolean targetIsInsideTheWalls = targetRow > 0 && targetRow < 9 && targetCol > 0 && targetCol < 9;

        if (targetIsInsideTheWalls) {
            this.row = targetRow;
            this.col = targetCol;
        }
    }

    void resetToSpawnPosition() {
        this.row = 1;
        this.col = 1;
    }

    // Getters – data hiding: row and col are private
    int getRow() { return this.row; }
    int getCol() { return this.col; }

    @Override
    public String toString() {
        return "Pacman at (" + this.row + ", " + this.col + ")";
    }
}
class Ghost {
    private int row;
    private int col;
    private Random randomNumberPicker;
    Ghost(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
        this.randomNumberPicker = new Random();
    }
    void wanderRandomly() {
        int targetRow = this.row;
        int targetCol = this.col;
        // 0 = up, 1 = down, 2 = left, 3 = right
        int chosenDirection = randomNumberPicker.nextInt(4);

        if (chosenDirection == 0) {
            targetRow = this.row - 1; // up
        }
        if (chosenDirection == 1){
            targetRow = this.row + 1; // down 
        }
        if (chosenDirection == 2) {
            targetCol = this.col - 1; // left
        }
        if (chosenDirection == 3) {
            targetCol = this.col + 1; // right
        }
        boolean targetIsInsideTheWalls =targetRow > 0 && targetRow < 9 && targetCol > 0 && targetCol < 9;

        if (targetIsInsideTheWalls) {
            this.row = targetRow;
            this.col = targetCol;
        }
    }

    int getRow() { return this.row; }
    int getCol() { return this.col; }

    @Override
    public String toString() {
        return "Ghost at (" + this.row + ", " + this.col + ")";
    }
}
class Food {
    // true  = food dot is still here
    // false = already eaten (or a wall / spawn cell)
    private boolean[][] foodGrid = new boolean[10][10];
    private int totalFoodCount;
    Food(int pacmanStartRow,int pacmanStartCol,int ghostStartRow,int ghostStartCol){
        this.totalFoodCount = 0;
        for(int row=1;row<9;row++){
            for(int col=1;col<9;col++){
                boolean thisCellIsPacmansSpawn =
                        (row ==pacmanStartRow && col==pacmanStartCol);
                boolean thisCellIsGhostsSpawn =(row == ghostStartRow && col == ghostStartCol);
                boolean safeToPlaceFoodHere = !thisCellIsPacmansSpawn && !thisCellIsGhostsSpawn;
                if (safeToPlaceFoodHere){
                    foodGrid[row][col]=true; // places food at empty places
                    this.totalFoodCount++;
                } else{
                    foodGrid[row][col]=false;// prevents from placing food at spawn point
                }
            }
        }
    }
    // called when pacman steps on a cell removes food if present at the same point
    // returns true if food was actually eaten(this is done so we can add it to score )
    boolean tryEatFoodAt(int row, int col) {
        if (foodGrid[row][col]) {
            foodGrid[row][col] = false; // food is now gone from this cell
            this.totalFoodCount--;
            return true;              // returns true referencing pacman ate something
        }
        return false;                 // nothing to eat here is present
    }
    boolean allFoodIsGone() {
        return this.totalFoodCount == 0;
    }
    // Getter so Board can read the grid for drawing
    boolean[][] getFoodGrid() {
        return this.foodGrid;
    }
    @Override
    public String toString() {
        return "Food remaining: " + this.totalFoodCount;
    }
}
public class OOP_PROJECT {
    public static void clearScreen() {
        for(int blankLine=0;blankLine<60;blankLine++){
            System.out.println();//since there is not an option to clear console so i printed empty lines to make it look like console is clearing
        }
    }
    public static void main(String[] args) {
        //this set up the all the things
        Scanner keyboard=new Scanner(System.in);
        Board board=new Board();
        Pacman pacman=new Pacman(1, 1);
        Ghost ghost=new Ghost(8, 8);
        Food food=new Food(pacman.getRow(),pacman.getCol(),ghost.getRow(),ghost.getCol());//takes starting postion to avoide putting food there
        int score=0;
        int lives=3;
        //main loop
        while(true){
            //refreshes the display
            clearScreen();
            board.draw(pacman.getRow(),pacman.getCol(),ghost.getRow(),ghost.getCol(),food.getFoodGrid());
            System.out.printf("Score: %d   Lives: %d%n", score, lives);
            //asks the player which way to move
            System.out.printf("Move(W=up, A = left, S = down, D = right): ");
            char keyPressed = keyboard.next().toLowerCase().charAt(0);
            //ghost moves randomly wherease pacman moves depending on input
            pacman.move(keyPressed);
            ghost.wanderRandomly();
            //checks if pacman ate the food or not if food and pacman are at same position pacman ate food otherwise not
            boolean pacmanJustAteFood=food.tryEatFoodAt(pacman.getRow(),pacman.getCol());
            if (pacmanJustAteFood) {
                score++;// increases the score upon eating food
            }
            //checks if pacman and ghost are at the same postion or not if are decrease lives by 1
            boolean ghostCaughtPacman= (pacman.getRow()==ghost.getRow() && pacman.getCol() == ghost.getCol());
            if (ghostCaughtPacman){
                lives--;
                System.out.println("The ghost caught you! Lives remaining:"+lives);
                pacman.resetToSpawnPosition();
                boolean noLivesLeft=(lives==0);
                if(noLivesLeft){
                    System.out.printf("Game Over! Final Score:%d%n",score);
                    break;
                }
            }
            //checks if all food has been eaten or not
            if (food.allFoodIsGone()) {
                System.out.printf("You Win! Final Score: %d%n", score);
                break;
            }
        } //end of the main lop
        keyboard.close();   //closes the scanner object after taking input it is optional
    }
}
