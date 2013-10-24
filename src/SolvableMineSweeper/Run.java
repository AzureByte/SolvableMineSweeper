/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SolvableMineSweeper;

import java.util.Random;
import javax.swing.JFrame;

/**
 *
 * @author Kenric Anto D'Souza
 */
public class Run extends JFrame {

    /**
     * @param args the command line arguments
     */
    //declare Static Variables and methods
    int no_of_mines = 20;
    int width = 20;
    int height = 30;
    int rnd_num_req = width * height * 2;
    int revealed[][] = new int[height][width];
    char mine_symbol = '*';

    private char[][] mineRandomRanked(int height, int width, int num_mines) {
        ///Mined via ranking top n locations.
        int[][] rand_arr = new int[height][width]; //Declare empty number array
        Random gen = new Random(); // Declare random number generator
        int[][] Ranked = new int[num_mines][3]; //Declare ranking array
        char[][] mined = new char[height][width];
        //Iterating through the random number empty array
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rand_arr[y][x] = gen.nextInt(rnd_num_req) + 1; //Adding random numbers to each element
                if (isTop(rand_arr[y][x], Ranked)) {
                    //Check if the current value is greater than at least the lowest value in the ranked array
                    Ranked = replaceLowest(Ranked, rand_arr[y][x], y, x); //Replaces the lowest value in the existing array with the new value.
                    //Note: This process uses two functions isTop and replaceLowest.
                }
            }
        }
        for (int[] mine : Ranked) {
            //store the location of the mines from Ranked in the mined array. 
            mined[mine[1]][mine[2]] = mine_symbol;
        }
        return mined;
    }

    private boolean isTop(int num, int[][] Ranked) {
        for (int[] mine : Ranked) {  //each 'mine' is a 1x3 array with [mine_probability, y_ord(row), x_ord(col)]
            if (mine[0] < num || mine[0] == 0) {
                return true;
            }
        }
        return false;
    }

    private int[][] replaceLowest(int[][] Ranked, int num, int y, int x) {
        int j = 0;
        int lowest = Ranked[j][0];
        for (int i = 1; i < Ranked.length; i++) {
            if (Ranked[i][0] < lowest) {
                lowest = Ranked[i][0];
                j = i;
            }
        }
        Ranked[j][0] = num;
        Ranked[j][1] = y;
        Ranked[j][2] = x;
        return Ranked;
    }

    private int minesAround(char[][] minefield, int y, int x) {
        int mines = 0;
        int y_wall = minefield.length;
        int x_wall = minefield[y].length;
        int ymax = y + 2;
        int xmax = x + 2;
        y -= (y == 0) ? 0 : 1;
        x -= (x == 0) ? 0 : 1;
        for (int j = y; j < y_wall && j < ymax; j++) {
            for (int i = x; i < x_wall && i < xmax; i++) {
                mines += (minefield[j][i] == mine_symbol) ? 1 : 0; // Can we use ternary without assigning
            }
        }
        return mines;
    }

    private char[][] addNumbersToGrid(char[][] minefield) {
        for (int y = 0; y < minefield.length; y++) {
            for (int x = 0; x < minefield[y].length; x++) {
                if (minefield[y][x] != mine_symbol) {
                    minefield[y][x] = (char) ('0' + minesAround(minefield, y, x));
                }
            }
        }
        return minefield;
    }

    private void printArray(char[][] array) {
        for (char[] row : array) {
            for (char row_ele : row) {
                System.out.print(row_ele);
            }
            System.out.println();
        }
    }

    private void printArray(int[][] array) {
        for (int[] row : array) {
            for (int row_ele : row) {
                System.out.print(row_ele);
            }
            System.out.println();
        }
    }

    private void printArrayCSV(int[][] array) {
        for (int[] row : array) {
            for (int row_ele : row) {
                System.out.print(row_ele + ",");
            }
            System.out.println();
        }
    }

    public Run() {
        setTitle("Solvable MineSweeper");
        setSize(400, 400);
        setLocation(50,50);
        setResizable(false);
        //setVisible(true); //Redundant since its used later.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        /*Creates a newly seeded minefield*/
        char[][] minefield1;
        minefield1 = addNumbersToGrid(mineRandomRanked(height, width, no_of_mines));
        printArray(minefield1);
        //dispose(); //Closes the window. In here to check run times.
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame window = new Run();
        window.show();
    }//main
}//class Run
