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
import javax.swing.SwingUtilities;

/**
 *
 * @author Kenric
 */
public class Button extends JButton implements MouseListener{
    ImageIcon F,Q;
    byte value = 0;
    /*
    0:nothing
    1:Flag
    2:Question Mark
    */
    
    public Button(int window_width, int window_height, int width, int height){
        F = new ImageIcon(new ImageIcon(this.getClass().getResource("Flag.png")).getImage().getScaledInstance(window_width/width, window_height/height, java.awt.Image.SCALE_SMOOTH));
        Q = new ImageIcon(new ImageIcon(this.getClass().getResource("QMark.png")).getImage().getScaledInstance(window_width/width, window_height/height, java.awt.Image.SCALE_SMOOTH));
        addMouseListener(this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        //Code for detecting right clicks and adding flags or question marks
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
        if(SwingUtilities.isLeftMouseButton(e)){
            
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
