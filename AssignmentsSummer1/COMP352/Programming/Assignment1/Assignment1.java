package AssignmentsSummer1.COMP352.Programming.Assignment1;

import java.util.Scanner;
import java.util.Random;

/**
 * Assignment1: Path Finding Algorithm
 * This program finds all possible paths from the top-left corner to the bottom-right corner of a grid.
 * The grid contains obstacles (represented by 1) that block movement.
 * Movement is restricted to only RIGHT and DOWN directions.
 * The program compares recursive and iterative approaches using a custom stack.
 * It tracks the number of turns (direction changes) for each valid path.
 */
public class Assignment1 {
    // Scanner for user input
    static Scanner sc = new Scanner(System.in);
    
    // 2D grid where 0 = passable cell, 1 = obstacle
    static int[][] grid;
    
    // Grid dimensions and statistics
    static int cols, rows;                   // Number of columns and rows in the grid
    static int percentage;                   // Percentage of cells to fill with obstacles
    static int pathCount;                    // Total number of valid paths found
    static int shortestTurns;                // Minimum number of turns in any valid path
    static int longestTurns;                 // Maximum number of turns in any valid path
    
    // Direction constants for movement and tracking
    static final int NONE = 0, RIGHT = 1, DOWN = 2;

    /**
     * State class: Represents a position in the grid during path exploration
     * Used by the iterative algorithm to store the current exploration state
     */
    static class State {
        int row;              // Current row position
        int col;              // Current column position
        int previousDir;      // Direction of the previous move (NONE, RIGHT, or DOWN)
        int turnsSoFar;       // Number of turns made so far to reach this cell

        State(int row, int col, int previousDir, int turnsSoFar) {
            this.row = row;
            this.col = col;
            this.previousDir = previousDir;
            this.turnsSoFar = turnsSoFar;
        }
    }

    /**
     * StateStack class: Custom stack implementation for storing State objects
     * Used to implement the iterative path exploration algorithm (replaces recursion)
     */
    static class StateStack {
        private State[] data;    // Array to store State objects
        private int top;         // Index of the top element in the stack

        /**
         * Constructor: Initializes stack with given capacity
         * @param capacity Maximum number of State objects the stack can hold
         */
        StateStack(int capacity) {
            data = new State[capacity];
            top = -1;  // -1 indicates empty stack
        }

        /**
         * Checks if the stack is empty
         * @return true if stack is empty, false otherwise
         */
        boolean isEmpty() {
            return top == -1;
        }

        /**
         * Adds a State object to the top of the stack
         * @param s The State object to push onto the stack
         */
        void push(State s) {
            if (top == data.length - 1) {
                System.out.println("Stack overflow");
                return;
            }

            top++;
            data[top] = s;
        }

        /**
         * Removes and returns the State object from the top of the stack
         * @return The State object at the top, or null if stack is empty
         */
        State pop() {
            if (isEmpty())
                return null;

            State temp = data[top];
            top--;
            return temp;
        }
    }

    /**
     * Main method: Entry point for the program
     * Handles user input, grid generation, and algorithm selection
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome.");

        // Get grid dimensions from user
        System.out.print("Please enter number of rows: ");
        rows = sc.nextInt();
        System.out.println();

        System.out.print("Please enter number of columns: ");
        cols = sc.nextInt();
        System.out.println();

        // Get obstacle percentage and validate input
        System.out.print("Please enter percentage of obstacles" +
                        " (the more obstacles the higher chance of no possible answers): ");
        percentage = sc.nextInt();
        if (percentage < 0 || percentage > 100) throw new Exception("Invalid percentage value.");

        // Initialize grid and populate it with random obstacles
        grid = new int[rows][cols];
        generateGrid();
        displayGrid();

        // Let user choose between recursive and iterative algorithms
        System.out.println();
        System.out.print("Would you like to proceed with the recursive or iterative algorithm (1 = rec / 2 = it): ");
        int choice = sc.nextInt();

        // Initialize turn counters
        shortestTurns = Integer.MAX_VALUE;
        longestTurns = Integer.MIN_VALUE;
        
        // Execute the selected algorithm starting from top-left corner (0, 0)
        if (choice == 1)
            exploreRecursive(grid, 0, 0, NONE, 0);
        else
            exploreIterative(grid, 0, 0, NONE, 0);

        // Display results and complexity analysis
        System.out.println();
        System.out.print("""
            STATISTICS:
            - Amount of possible paths found: %d
            - Path with shortest amount of turns: %s
            - Path with biggest amount of turns: %s

            - Space complexity of the program: O(R x C)         //Where R = rows, C = columns
                *because the program stores a 2D grid of size rows x cols.

            - Time complexity of the program: O(2^(R + C))      //Where R = rows, C = columns
                *because from each valid cell, the algorithm tries up to 2 possible moves: right and down.

        """.formatted(pathCount, (shortestTurns == Integer.MAX_VALUE ? "Undefined" : Integer.toString(shortestTurns)), 
                                (longestTurns == Integer.MIN_VALUE ? "Undefined" : Integer.toString(longestTurns))
        ));
    }

    /**
     * Generates a random grid with obstacles
     * Start (0,0) and end (rows-1, cols-1) are always passable
     * Other cells are randomly filled with obstacles based on the given percentage
     */
    private static void generateGrid() {
        Random random = new Random();
        
        // Ensure start and end positions are passable (0 = passable)
        grid[0][0] = 0;
        grid[rows-1][cols-1] = 0; 

        // Fill grid with random obstacles
        for (int i = 0; i < grid.length; i++ ) {
            for (int j = 0; j < grid[0].length; j++) {
                // Skip start and end positions
                if ((i == 0 && j == 0) || (i == rows - 1 && j == cols - 1)) 
                    continue;

                // Generate random number between 0-100
                int randInt = random.nextInt(101);
                // If random number is greater than percentage, cell is passable (0)
                // Otherwise, cell contains an obstacle (1)
                if (randInt > percentage) {
                    grid[i][j] = 0;
                    continue;
                }
                grid[i][j] = 1;
            }
        }
        System.out.println();
    } 

    /**
     * Displays the generated grid in a formatted table
     * 0 = passable cell, 1 = obstacle cell
     */
    private static void displayGrid() {
        System.out.println();
        // Create top border
        String s = "-";
        for (int i = 0; i < cols; i++)
            s += "----";
        System.out.println(s);

        // Print each row of the grid with borders
        for (int i = 0; i < grid.length; i++) {
            System.out.print("| ");

            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " | ");
            }
            System.out.println();
            System.out.println(s);
        }
    }

    /**
     * Recursively explores all possible paths from current position to bottom-right corner
     * Only moves RIGHT or DOWN, tracking direction changes as "turns"
     * @param grid The grid containing obstacles and passable cells
     * @param row Current row position
     * @param col Current column position
     * @param previousDir Direction of the previous move (to count turns)
     * @param turnsSoFar Number of turns made so far
     */
    private static void exploreRecursive(int[][] grid, int row, int col, int previousDir, int turnsSoFar) {
        // Check bounds: if out of bounds, this path is invalid
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) return;

        // Check obstacles: if current cell has an obstacle, this path is blocked
        if (grid[row][col] == 1) return;

        // Check if reached the destination (bottom-right corner)
        if (row == grid.length - 1 && col == grid[0].length - 1) {
            pathCount++;  // Found a valid path
            
            // Update shortest and longest turn counts
            if (turnsSoFar < shortestTurns) shortestTurns = turnsSoFar;
            if (turnsSoFar > longestTurns) longestTurns = turnsSoFar;

            return;
        }

        // Try moving RIGHT (column + 1)
        int turnsRight = updatedTurns(previousDir, RIGHT, turnsSoFar);
        exploreRecursive(grid, row, col + 1, RIGHT, turnsRight);

        // Try moving DOWN (row + 1)
        int turnsDown = updatedTurns(previousDir, DOWN, turnsSoFar);
        exploreRecursive(grid, row + 1, col, DOWN, turnsDown);
    }

    /**
     * Calculates the updated turn count when changing direction
     * A "turn" occurs when the direction of movement changes from the previous move
     * @param previousDirection The direction of the previous move
     * @param newDirection The direction of the current move
     * @param turnsSoFar The number of turns made before this move
     * @return The updated turn count (incremented if direction changed, otherwise unchanged)
     */
    private static int updatedTurns(int previousDirection, int newDirection, int turnsSoFar) {
        // First move (no previous direction): no turn
        if (previousDirection == NONE) return turnsSoFar;
        // Same direction as previous: no turn
        else if (previousDirection == newDirection) return turnsSoFar;
        // Different direction: increment turn count
        else return ++turnsSoFar;
    }

    /**
     * Iteratively explores all possible paths using a custom stack (non-recursive approach)
     * This is an alternative to the recursive method that avoids potential stack overflow
     * @param grid The grid containing obstacles and passable cells
     * @param row Starting row position
     * @param col Starting column position
     * @param previousDir Direction of the initial move
     * @param turnsSoFar Starting turn count
     */
    private static void exploreIterative(int[][] grid, int row, int col, int previousDir, int turnsSoFar) {
        // Create stack with large capacity to store exploration states
        StateStack stack = new StateStack(100000);

        // Push the initial state onto the stack
        stack.push(new State(row, col, previousDir, turnsSoFar));
        
        // Process states until stack is empty
        while (!stack.isEmpty()) {
            // Get the next state from the stack
            State current = stack.pop();

            // Extract state information
            row = current.row;
            col = current.col;
            previousDir = current.previousDir;
            turnsSoFar = current.turnsSoFar;

            // Skip if out of bounds
            if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) continue;

            // Skip if current cell contains an obstacle
            if (grid[row][col] == 1) continue;

            // Check if reached the destination
            if (row == grid.length - 1 && col == grid[0].length - 1) {
                pathCount++;  // Found a valid path
                
                // Update shortest and longest turn counts
                if (turnsSoFar < shortestTurns) shortestTurns = turnsSoFar;
                if (turnsSoFar > longestTurns) longestTurns = turnsSoFar;

                continue;
            }

            // Try moving RIGHT: calculate turns and push new state onto stack
            int turnsRight = updatedTurns(previousDir, RIGHT, turnsSoFar);
            stack.push(new State(row, col + 1, RIGHT, turnsRight));

            // Try moving DOWN: calculate turns and push new state onto stack
            int turnsDown = updatedTurns(previousDir, DOWN, turnsSoFar);
            stack.push(new State(row + 1, col, DOWN, turnsDown));
        }
    }
}