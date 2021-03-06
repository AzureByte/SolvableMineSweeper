/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solvableminesweepernetbeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.border.BevelBorder;

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
    int no_of_mines = 20;
    int minefield_width = 15;
    int minefield_height = 10;
    int closed_squares = minefield_height * minefield_width;
    int flagged_squares = 0;
    
    JPanel p = new JPanel();
        JPanel p_counters = new JPanel();
            Counter counter_mines = new Counter(no_of_mines);
            Counter counter_time = new Counter(0);
            Stopwatch stop_watch;
            Thread stop_watch_thread;
            JButton luck_button = new JButton();
            Counter counter_luck = new Counter(50); //Probabability of opening a white square
        JPanel p_minefield = new JPanel();
    
    Button buttons[] = new Button[minefield_width*minefield_height];
    char[][] active_minefield;
    char mine_symbol = '*';
    int window_width = 600;
    int window_height = 480;

    private char[][] mineRandomRanked(int height, int width, int num_mines) {
        ///Mined via ranking top n locations (Top Probabilities).
        int[][] rand_arr = new int[height][width]; //Declare empty number array
        Random gen = new Random(); // Declare random number generator
        int[][] ranked = new int[num_mines][3]; //Declare ranking array
        char[][] mined = new char[height][width];
        int rnd_num_req = width * height * 2;
        //Iterating through the random number empty array
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rand_arr[y][x] = gen.nextInt(rnd_num_req) + 1; //Adding random numbers to each element
                if (isTop(rand_arr[y][x], ranked)) {
                    //Check if the current value is greater than at least the lowest value in the ranked array
                    ranked = replaceLowest(ranked, rand_arr[y][x], y, x); //Replaces the lowest value in the existing array with the new value.
                    //Note: This process uses two functions isTop and replaceLowest.
                }
            }
        }
        for (int[] mine : ranked) {
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
        int y_wall = active_minefield.length;
        int x_wall = active_minefield[y].length;
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
        b.setEnabled(false);
        closed_squares--;
        checkForWin();
        updateLuckCounter();
    }
    
    private void updateLuckCounter(){
        float luck = 100-(((no_of_mines-flagged_squares)*100.0F)/closed_squares);
        counter_luck.updateValue(Math.round(luck));
        
        //Sets threshold for using the button
        if(luck >= 50){
            luck_button.setEnabled(true);
        }
        else{
            luck_button.setEnabled(false);
        }
    }
    
    private void openRandomWhite(){
        Random gen = new Random();
        int len = minefield_height*minefield_width;
        int loc = 0; // To store the position to open up
        int max = 0; // To store the maximum randomly generated value
        int[] ranked = new int[len];
        for (int i = 0; i < len; i++) {
            if(!buttons[i].dug && buttons[i].beneath != mine_symbol)
                ranked[i] = gen.nextInt(len*2);
            else
                ranked[i]=0;
        }
        for (int i = 0; i < len; i++) {
            if(ranked[i]>max){
                max = ranked[i];
                loc = i;
            }
        }
        if(max!=0){
            digUp(buttons[loc]);
        }
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
        
        //Formats the main Jpanel
        p.setLayout(new BorderLayout());
        
        //Show the window
        setVisible(true);

        //dispose(); //Closes the window. Uncomment to check start up times.
    }
    
    private void newGame(){
        
        /*Remove previous containers*/
        p_minefield.removeAll();
        p_counters.removeAll();
        
        /*Add and reset the counters*/
        closed_squares = minefield_height * minefield_width;
        flagged_squares = 0;
        counter_mines = new Counter(no_of_mines);
        counter_time = new Counter(0);
        counter_luck = new Counter(50);
        updateLuckCounter();
        p_counters.setLayout(new FlowLayout());

        JLabel counter_mines_label = new JLabel();
        counter_mines_label.setText("Mines");
        p_counters.add(counter_mines_label);
        p_counters.add(counter_mines);
        
        JLabel counter_time_label = new JLabel();
        counter_time_label.setText("Time");
        p_counters.add(counter_time_label);
        p_counters.add(counter_time);
        
        JLabel counter_luck_label = new JLabel();
        counter_luck_label.setText("Luck Required"); //TODO Could be replaced with an icon of a shamrock
        p_counters.add(counter_luck_label);
        p_counters.add(counter_luck);
        luck_button.setEnabled(true);
        luck_button.addMouseListener(this);
        luck_button.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("images/Clover.gif")).getImage().getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH)));

        p_counters.add(luck_button);
        
        p.add(p_counters, BorderLayout.PAGE_START);
        
        /*Creates a newly seeded minefield*/
        active_minefield = addNumbersToGrid(mineRandomRanked(minefield_height, minefield_width, no_of_mines));
        printArray(active_minefield); //TODO Remove when done debugging.
        
        /*Displays the covered Minefield*/
        p_minefield.setLayout(new GridLayout(minefield_height,minefield_width));
        int width_tf = (int) ((window_width - minefield_width)/this.minefield_width); // The extra '-minefield_width' is to remove pixels taken up by the border
        int height_tf = (int) ((window_height - minefield_height)/this.minefield_height); 
        for (int y = 0; y < minefield_height; y++) {
            for (int x = 0; x < minefield_width; x++) {
                int dimension_tf = y * minefield_width + x; //transforms the co-ordinates from a 2d-array to a 1d-array
                buttons[dimension_tf] = new Button(x, y, width_tf , height_tf , active_minefield[y][x]);
                buttons[dimension_tf].addMouseListener(this);
                buttons[dimension_tf].setIcon(buttons[dimension_tf].D);
                buttons[dimension_tf].setDisabledIcon(buttons[dimension_tf].I);
                buttons[dimension_tf].setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY,Color.BLACK));
                this.p_minefield.add(buttons[dimension_tf]);
            }
        }
        p.add(new JLabel(), BorderLayout.LINE_START);
        p.add(this.p_minefield, BorderLayout.CENTER); //Adds the minefield Jpanel to the Jframe window
        p.add(new JLabel(), BorderLayout.LINE_END);
        p.add(new JLabel(), BorderLayout.PAGE_END);
        add(p);
        setVisible(true);
        stop_watch = new Stopwatch(this);
        stop_watch_thread = new Thread(stop_watch);
        stop_watch_thread.start();
    }
    
    private void endGame(){
        stop_watch_thread.interrupt();
        luck_button.setEnabled(false);
        for (Button b : buttons) {
            if(!b.dug)
                if(b.beneath != mine_symbol && b.value == 1)
                    b.setIcon(b.WF);
                else if(b.beneath == mine_symbol){
                    b.setIcon(b.MM);
            }
        }
        
        p_minefield.removeAll();
        if (JOptionPane.showConfirmDialog(null, "Would you like to play again?", "",  JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            newGame();
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
    
    public void checkForWin(){
        for (int y = 0; y < minefield_height; y++) {
            for (int x = 0; x < minefield_width; x++) {
                int dimension_tf = y * minefield_width + x; //transforms the co-ordinates from a 2d-array to a 1d-array
                if(!buttons[dimension_tf].dug && !(buttons[dimension_tf].beneath == '*'))
                    return;
            }
        }
        gameWin();
    }
    
    public static void main(String[] args) {
        //To mimic the native look on different OS uncomment the following
//        try { 
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Run window = new Run();
    }//main

    @Override
    public void mouseClicked(MouseEvent e){
        if(e.getSource().equals(luck_button)){
            if(luck_button.isEnabled())
                openRandomWhite();
        }
        else{
            Button b = (Button) e.getSource();
            if(!b.dug){
                if(SwingUtilities.isRightMouseButton(e) || e.isControlDown()){
                    b.value++;
                    b.value%=3;
                    switch(b.value){
                        case 0:
                            b.setIcon(b.D);
                            break;
                        case 1:
                            b.setIcon(b.F);
                            counter_mines.removeOne();
                            flagged_squares++;
                            updateLuckCounter();
                            checkForWin();
                            //Removes 1 from the 'mines' counter.
                            break;
                        case 2:
                            b.setIcon(b.Q);
                            counter_mines.addOne();
                            flagged_squares--;
                            updateLuckCounter();
                            //Adds 1 to the 'mines' counter.
                            break;
                    }
                }
                //Code for checking for the mine, empty space or number.
                //Also prevents clicking if button is flagged or question marked.
                else if(SwingUtilities.isLeftMouseButton(e) && b.value==0){
                    digUp(b);
                    if(b.beneath=='*')
                        gameLoss();
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
        }
    }
}//class Run


