package AssignmentsSummer1.COMP352.Assignment1;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.Random;

/*
- Implement a fixed base grid for testing the amount of paths feature and performance testing
- fuck it 

*/


public class Assignment1 {
    static Scanner sc = new Scanner(System.in);
    static int[][] grid;
    static int cols, rows, percentage, numPaths;

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

    }

    public static void generateGrid() {
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
    } 

    public static void displayGrid() {
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

    public static int validPathsRecursive() {










        return 0;
    }

    public static int sumbs() {
        
        }

        








        return 0;
    }


    /*
    if j = 1, check i, if i = 1, stop.
    if j = 0, set new position, check j again, and so on
    how tf do I make it not do the same path again
    */
}