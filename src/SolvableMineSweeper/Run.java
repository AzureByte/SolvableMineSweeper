/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SolvableMineSweeper;

import java.awt.Window;
import java.util.Random;
import javax.swing.JFrame;
import java.util.Arrays;

/**
 *
 * @author Kenric
 */
public class Run extends JFrame {

    /**
     * @param args the command line arguments
     */
    //declare Static Variables and methods
    int no_of_mines = 200;
    int width = 100;
    int height = 100;
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
        for (int y = 0; y < Ranked.length; y++) {
            mined[Ranked[y][1]][Ranked[y][2]] = mine_symbol;
        }
        return mined;
    }

    public boolean isTop(int num, int[][] Ranked) {
        for (int i = 0; i < Ranked.length; i++) {
            if (Ranked[i][0] < num || Ranked[i][0] == 0) {
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

    public void printArray(char[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j]);
            }
            System.out.println();
        }
    }

    public void printArray(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j]);
            }
            System.out.println();
        }
    }

    public void printArrayCSV(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + ",");
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
