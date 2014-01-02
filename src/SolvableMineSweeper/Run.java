/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solvableminesweepernetbeans;

import java.util.Random;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Kenric Anto D'Souza
 */
public class Run extends JFrame implements MouseListener, ItemListener, ActionListener {

    /**
     * 
     */
	private static final long serialVersionUID = 1L;
    /**
     * @param args the command line arguments
     */
    
    //declare Static Variables and methods
    int no_of_mines = 15;
    int minefield_width = 10;
    int minefield_height = 10;
    
    JPanel p = new JPanel();
    Button buttons[] = new Button[minefield_width*minefield_height];
    
    char[][] new_minefield;
    int revealed[][] = new int[minefield_height][minefield_width];
    char mine_symbol = '*';
    int window_width = 400;
    int window_height = 400;

    private char[][] mineRandomRanked(int height, int width, int num_mines) {
        ///Mined via ranking top n locations (Top Probabilities).
        int[][] rand_arr = new int[height][width]; //Declare empty number array
        Random gen = new Random(); // Declare random number generator
        int[][] Ranked = new int[num_mines][3]; //Declare ranking array
        char[][] mined = new char[height][width];
        int rnd_num_req = width * height * 2;
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

    private char[][] mineWithRandomLocations(int height, int width, int num_mines){
        //Implemented using 'Random Location'
        Random gen = new Random();
        char[][] mined = new char[height][width];
        while(num_mines!=0){
            int h = gen.nextInt(height);
            int w = gen.nextInt(width);
            if (mined[h][w] != mine_symbol){
                mined[h][w] = mine_symbol;
                num_mines--;
            }
        }
        return mined;
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
    
    private int minesAroundAlternate(char[][] minefield, int y, int x){
        int mines = 0;
        int[] loc_x = {x-1, x, x+1};
        int[] loc_y = {y-1, y, y+1};
        for (int h : loc_y) {
            if(h < 0 || h > minefield_height-1)
                continue;
            for(int w : loc_x){
                if(w < 0 || w > minefield_width-1)
                    continue;
                if(minefield[h][w] == mine_symbol)
                    mines++;
            }
        }
        return mines;
    }

    private char[][] addNumbersToGrid(char[][] minefield) {
        //Implemented using Location Check
        for (int y = 0; y < minefield.length; y++) {
            for (int x = 0; x < minefield[y].length; x++) {
                if (minefield[y][x] != mine_symbol) {
                    minefield[y][x] = (char) ('0' + minesAround(minefield, y, x)); // trick to convert from int to corresponding char.
                }
            }
        }
        return minefield;
    }
    
    private char[][] addNumbersToGridAlt(char[][] minefield){
        //Implemented using Mine Quake
        for (int y = 0; y < minefield.length; y++) {
            for (int x = 0; x < minefield[y].length; x++) {
                if (minefield[y][x] == mine_symbol){
                    int[] loc_x = {x-1, x, x+1};
                    int[] loc_y = {y-1, y, y+1};
                    for (int h : loc_y) {
                        if(h < 0 || h > minefield_height-1)
                            continue;
                        for(int w : loc_x){
                            if(w < 0 || w > minefield_width-1)
                                continue;
                            if(minefield[h][w] == ' ') //This character is not spacebar, it was extracted from output.
                                minefield[h][w] = '0';
                            if(minefield[h][w] != mine_symbol)
                                minefield[h][w]++; // Even the character datatype can use the ++ operator
                        }
                    }
                }
                else if(minefield[y][x] == ' ') //This character is not spacebar, it was extracted from output.
                    minefield[y][x] = '0';
            }
        }
        return minefield;
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
    
    private void digUp(Button b){
        if(b.beneath=='0' && !b.dug){
            b.dug=true;
            openWhites(b.y, b.x);
        }
        else{
            b.dug=true;
        }
        b.setIcon(b.I);
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        /*Menus*/
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
        
        menuBar = new JMenuBar();
        
        menu = new JMenu("Main");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription("The main menu");
        menuBar.add(menu);
        
        menuItem = new JMenuItem("New Game", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Exit the game.");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        setJMenuBar(menuBar);

        //Show the window
        setVisible(true);

        //dispose(); //Closes the window. Uncomment to check start up times.
    }
    
    private void newGame(){
        
        p.removeAll();
        /*Creates a newly seeded minefield*/
        new_minefield = addNumbersToGrid(mineRandomRanked(minefield_height, minefield_width, no_of_mines)); //TODO changed for testing, change back when done.
        printArray(new_minefield); //TODO Remove when done debugging.
        
        /*Displays the covered Minefield*/
        p.setLayout(new GridLayout(minefield_width,minefield_height));
        int width_tf = (int) ((window_width/this.minefield_width)*0.8);
        int height_tf = (int) ((window_height/this.minefield_height)*0.8);
        for (int y = 0; y < minefield_height; y++) {
            for (int x = 0; x < minefield_width; x++) {
                int dimension_tf = y * minefield_width + x; //transforms the co-ordinates from a 2d-array to a 1d-array
                buttons[dimension_tf] = new Button(x, y, width_tf , height_tf , new_minefield[y][x]);
                buttons[dimension_tf].addMouseListener(this);
                p.add(buttons[dimension_tf]);
            }
        }
        add(p); //Adds the Jpanel to the Jframe window
        setVisible(true);
    }
    
    private void endGame(){
        //Add endGame logic
        for (Button b : buttons) {
            if(!b.dug)
                if(b.beneath != mine_symbol && b.value == 1)
                    b.setIcon(b.WF);
                else if(b.beneath == mine_symbol){
                    b.setIcon(b.MM);
            }
        }
        
        if (JOptionPane.showConfirmDialog(null, "Would you like to play again?", "",  JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            newGame();
        }
        else
        {
            p.removeAll();
        }
    }
    
    public void gameLoss(){
        showMessageDialog(null, "Sorry, you lose");
        endGame();
    }
    
    public void gameWin(){
        showMessageDialog(null, "Congrats! you win!");
        endGame();
    }
    
    public boolean checkForWin(){
        for (int y = 0; y < minefield_width; y++) {
            for (int x = 0; x < minefield_height; x++) {
                int dimension_tf = y * minefield_width + x; //transforms the co-ordinates from a 2d-array to a 1d-array
                if(!buttons[dimension_tf].dug && buttons[dimension_tf].beneath != '*'){
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
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
                    if(checkForWin())
                        gameWin();
                }
            }
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

    @Override
    public void itemStateChanged(ItemEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();
        if(source.getText()=="Exit"){
            dispose();
        }
        if(source.getText()=="New Game"){
            newGame();
            //TODO add a function for new game
        }
    }
}//class Run


