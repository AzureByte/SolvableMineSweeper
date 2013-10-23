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
    int no_of_mines = 10;
    int width = 5;
    int height = 5;
    int rnd_num_req = width * height * 2;
    int revealed[][] = new int[height][width];
    char mine_symbol = '*';

    public char[][] mineRandomRanked(int height, int width, int num_mines) {
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

    public boolean isTop(int num, int[][] Ranked) {
        for (int[] mine : Ranked) {  //each 'mine' is a 1x3 array with [mine_probability, y_ord(row), x_ord(col)]
            if (mine[0] < num || mine[0] == 0) {
                return true;
            }
        }
        return false;
    }

    public int[][] replaceLowest(int[][] Ranked, int num, int y, int x) {
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

    public int minesAround(char[][] minefield, int y, int x) {
        int mines = 0;
        if (y != 0) {
            y--;
        }
        if (x != 0) {
            x--;
        }
        for (int jmax = minefield.length; y < jmax; y++) {
            for (int imax = minefield[y].length; x < imax; x++) {
                mines += (minefield[y][x] == '*') ? 1 : 0; // Can we use ternary without assigning
            }
        }
        return mines;
    }
    
    public void printArray(char[][] array) {
        for (char[] row : array) {
            for (char row_ele : row) {
                System.out.print(row_ele);
            }
            System.out.println();
        }
    }

    public void printArray(int[][] array) {
        for (int[] row : array) {
            for (int row_ele : row) {
                System.out.print(row_ele);
            }
            System.out.println();
        }
    }

    public void printArrayCSV(int[][] array) {
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
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        char[][] minefield1 = new char[height][width];
        minefield1 = mineRandomRanked(height, width, no_of_mines);
        printArray(minefield1);
        dispose();
    }

    public static void main(String[] args) {
        // TODO code application logic here
        new Run();
    }
}
