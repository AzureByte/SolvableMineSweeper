/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package solvableminesweepernetbeans;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.SwingUtilities;

/**
 *
 * @author Kenric
 */
public class Button extends JButton implements MouseListener{
    ImageIcon F,Q,I; //Flag, Question mark, Identity
    byte value = 0;
    /*
    0:nothing
    1:Flag
    2:Question Mark
    */
    int width;
    int height;
    char beneath;
    boolean dug = false; // to check if the square has been dug up and prevent further interraction if it has.
    
    public Button(int window_width, int window_height, int width, int height, char identity){
        beneath = identity;
        switch(beneath){
            case '*':
                I = new ImageIcon(new ImageIcon(this.getClass().getResource("Mine.png")).getImage().getScaledInstance(window_width/width, window_height/height, java.awt.Image.SCALE_SMOOTH));
                break;
            default:
                I = new ImageIcon(new ImageIcon(this.getClass().getResource(beneath+".png")).getImage().getScaledInstance(window_width/width, window_height/height, java.awt.Image.SCALE_SMOOTH));
                break;                
        }
        F = new ImageIcon(new ImageIcon(this.getClass().getResource("Flag.png")).getImage().getScaledInstance(window_width/width, window_height/height, java.awt.Image.SCALE_SMOOTH));
        Q = new ImageIcon(new ImageIcon(this.getClass().getResource("QMark.png")).getImage().getScaledInstance(window_width/width, window_height/height, java.awt.Image.SCALE_SMOOTH));
        addMouseListener(this);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        if(!this.dug)
        {
            //Code for detecting right clicks (includes support for mac computers with control clicking) and adding flags or question marks
            if(SwingUtilities.isRightMouseButton(e) || e.isControlDown()){
                value++;
                value%=3;
                switch(value){
                    case 0:
                        this.setIcon(null);
                        break;
                    case 1:
                        this.setIcon(F);
                        break;
                    case 2:
                        this.setIcon(Q);
                        break;
                }
            }
            //Code for checking for the mine, empty space or number
            else if(SwingUtilities.isLeftMouseButton(e)){
                this.setIcon(I);
                this.dug = true;
                if(this.beneath=='*'){
                    solvableminesweepernetbeans.Run.gameLoss();
                }
                else{
//                    solvableminesweepernetbeans.Run.checkForWin();
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
}
