/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solvableminesweepernetbeans;

import java.util.Random;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Kenric Anto D'Souza
 */
public class Run extends JFrame implements MouseListener {

    /**
     * 
     */
	private static final long serialVersionUID = 1L;
    /**
     * @param args the command line arguments
     */
    
    //declare Static Variables and methods
    int no_of_mines = 15;
    int width = 10;
    int height = 10;
    
    JPanel p = new JPanel();
    Button buttons[] = new Button[width*height];
    
    static char[][] new_minefield;
    int rnd_num_req = width * height * 2;
    int revealed[][] = new int[height][width];
    char mine_symbol = '*';
    final int window_width = 400;
    final int window_height = 400;

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

    private void openWhites(int y, int x) {
        int y_wall = new_minefield.length;
        int x_wall = new_minefield[y].length;
        int ymax = y + 2;
        int xmax = x + 2;
        y -= (y == 0) ? 0 : 1;
        x -= (x == 0) ? 0 : 1;
        for (int j = y; j < y_wall && j < ymax; j++) {
            for (int i = x; i < x_wall && i < xmax; i++) {
                Button b = buttons[j*x_wall+i];
                if(!b.dug){
                    digUp(b);
                }
            }
        }
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
        super("Solvable MineSweeper");
        setSize(window_width, window_height);
        setLocation(50,50);
        setResizable(false);
        //setVisible(true); //Redundant since its used later.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        /*Menus*/
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;
        
        menuBar = new JMenuBar();
        
        /*Creates a newly seeded minefield*/
        new_minefield = addNumbersToGrid(mineRandomRanked(height, width, no_of_mines));
        printArray(new_minefield);
        
        /*Displays the covered Minefield*/
        p.setLayout(new GridLayout(width,height));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int dimension_tf = y * width + x; //transforms the co-ordinates from a 2d-array to a 1d-array
                buttons[dimension_tf] = new Button(x, y, (int) ((window_width/this.width)*0.8), (int) ((window_height/this.height)*0.8), new_minefield[y][x]);
                buttons[dimension_tf].addMouseListener(this);
                p.add(buttons[dimension_tf]);
            }
        }
        add(p); //Adds the Jpanel to the Jframe window
        setVisible(true);
        //dispose(); //Closes the window. Uncomment to check run times.
    }
    
    private static void endGame(){
        //Add endGame logic
    }
    
    public static void gameLoss(){
        showMessageDialog(null, "Sorry, you lose");
        endGame();
    }
    
    public static void gameWin(){
        showMessageDialog(null, "Congrats! you win!");
        endGame();
    }
    
    public void checkForWin(){
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int dimension_tf = y * width + x; //transforms the co-ordinates from a 2d-array to a 1d-array
                if(!buttons[dimension_tf].dug && buttons[dimension_tf].beneath != '*'){
                    return;
                }
            }
        }
        gameWin();
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        new Run();
    }//main

    @Override
    public void mouseClicked(MouseEvent e){
        Button b = (Button) e.getSource();
        if(!b.dug)
        {
            if(SwingUtilities.isRightMouseButton(e) || e.isControlDown()){
                b.value++;
                b.value%=3;
                switch(b.value){
                    case 0:
                        b.setIcon(null);
                        break;
                    case 1:
                        b.setIcon(b.F);
                        break;
                    case 2:
                        b.setIcon(b.Q);
                        break;
                }
            }
            //Code for checking for the mine, empty space or number.
            //Also prevents clicking if button is flagged or question marked.
            else if(SwingUtilities.isLeftMouseButton(e) && b.value==0){
                digUp(b);
                if(b.beneath=='*'){
                    gameLoss();
                }
                else{
                    checkForWin();
                }
            }
        }
    }
    
    public void digUp(Button b){
        if(b.beneath=='0' && !b.dug){
            b.dug=true;
            b.setIcon(b.I);
            openWhites(b.y, b.x);
        }
        else{
            b.dug=true;
            b.setIcon(b.I);
        }
    }
                
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}//class Run
