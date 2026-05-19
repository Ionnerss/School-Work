package AssignmentsSummer1.COMP352.Assignment1;

import java.util.Scanner;
import java.util.Random;

public class Assignment1 {
    static Scanner sc = new Scanner(System.in);
    static int[][] grid;
    static int cols, rows, percentage, pathCount, shortestTurns, longestTurns;
    static final int NONE = 0, RIGHT = 1, DOWN = 2;

    static class State {
        int row;
        int col;
        int previousDir;
        int turnsSoFar;

        State(int row, int col, int previousDir, int turnsSoFar) {
            this.row = row;
            this.col = col;
            this.previousDir = previousDir;
            this.turnsSoFar = turnsSoFar;
        }
    }

    static class StateStack {
        private State[] data;
        private int top;

        StateStack(int capacity) {
            data = new State[capacity];
            top = -1;
        }

        boolean isEmpty() {
            return top == -1;
        }

        void push(State s) {
            if (top == data.length - 1) {
                System.out.println("Stack overflow");
                return;
            }

            top++;
            data[top] = s;
        }

        State pop() {
            if (isEmpty())
                return null;

            State temp = data[top];
            top--;
            return temp;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome.");

        System.out.print("Please enter number of rows: ");
        rows = sc.nextInt();
        System.out.println();

        System.out.print("Please enter number of columns: ");
        cols = sc.nextInt();
        System.out.println();

        System.out.print("Please enter percentage of obstacles" +
                        " (the more obstacles the higher chance of no possible answers): ");
        percentage = sc.nextInt();
        if (percentage < 0 || percentage > 100) throw new Exception("Invalid percentage value.");

        grid = new int[rows][cols];
        generateGrid();
        displayGrid();

        System.out.println();
        System.out.print("Would you like to proceed with the recursive or iterative algorithm (1 = rec / 2 = it): ");
        int choice = sc.nextInt();

        shortestTurns = Integer.MAX_VALUE;
        longestTurns = Integer.MIN_VALUE;
        
        if (choice == 1)
            exploreRecursive(grid, 0, 0, NONE, 0);
        else
            exploreIterative(grid, 0, 0, NONE, 0);

        System.out.println();
        System.out.print("""
            STATISTICS:
            - Amount of possible paths found: %d
            - Path with shortest amount of turns: %s
            - Path with biggest amount of turns: %s

        """.formatted(pathCount, (shortestTurns == Integer.MAX_VALUE ? "Unditerminable" : String.valueOf(shortestTurns)), 
                                (longestTurns == Integer.MIN_VALUE ? "Unditerminable" : String.valueOf(longestTurns))));
    }

    private static void generateGrid() {
        Random random = new Random();
        
        grid[0][0] = 0;
        grid[rows-1][cols-1] = 0; 

        for (int i = 0; i < grid.length; i++ ) {
            for (int j = 0; j < grid[0].length; j++) {
                if ((i == 0 && j == 0) || (i == rows - 1 && j == cols - 1)) 
                    continue;

                int randInt = random.nextInt(101);
                if (randInt > percentage) {
                    grid[i][j] = 0;
                    continue;
                }
                grid[i][j] = 1;
            }
        }
        System.out.println();
    } 

    private static void displayGrid() {
        System.out.println();
        String s = "-";
        for (int i = 0; i < cols; i++)
            s += "----";
        System.out.println(s);

        for (int i = 0; i < grid.length; i++) {
            System.out.print("| ");

            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " | ");
            }
            System.out.println();
            System.out.println(s);
        }
    }

    private static void exploreRecursive(int[][] grid, int row, int col, int previousDir, int turnsSoFar) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) return;

        if (grid[row][col] == 1) return;

        if (row == grid.length - 1 && col == grid[0].length - 1) {
            pathCount++;
            
            if (turnsSoFar < shortestTurns) shortestTurns = turnsSoFar;
            if (turnsSoFar > longestTurns) longestTurns = turnsSoFar;

            return;
        }

        // 4. Try moving RIGHT
        int turnsRight = updatedTurns(previousDir, RIGHT, turnsSoFar);
        exploreRecursive(grid, row, col + 1, RIGHT, turnsRight);

        // 4. Try moving DOWN
        int turnsDown = updatedTurns(previousDir, DOWN, turnsSoFar);
        exploreRecursive(grid, row + 1, col, DOWN, turnsDown);
    }

    private static int updatedTurns(int previousDirection, int newDirection, int turnsSoFar) {
        if (previousDirection == NONE) return turnsSoFar;
        else if (previousDirection == newDirection) return turnsSoFar;
        else return ++turnsSoFar;
    }

    private static void exploreIterative(int[][] grid, int row, int col, int previousDir, int turnsSoFar) {
        StateStack stack = new StateStack(100000);

        stack.push(new State(row, col, previousDir, turnsSoFar));
        
        while (!stack.isEmpty()) {
            State current = stack.pop();

            row = current.row;
            col = current.col;
            previousDir = current.previousDir;
            turnsSoFar = current.turnsSoFar;

            if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) continue;

            if (grid[row][col] == 1) continue;

            if (row == grid.length - 1 && col == grid[0].length - 1) {
                pathCount++;
                
                if (turnsSoFar < shortestTurns) shortestTurns = turnsSoFar;
                if (turnsSoFar > longestTurns) longestTurns = turnsSoFar;

                continue;
            }

            int turnsRight = updatedTurns(previousDir, RIGHT, turnsSoFar);
            stack.push(new State(row, col + 1, RIGHT, turnsRight));

            int turnsDown = updatedTurns(previousDir, DOWN, turnsSoFar);
            stack.push(new State(row + 1, col, DOWN, turnsDown));
        }
    }
}