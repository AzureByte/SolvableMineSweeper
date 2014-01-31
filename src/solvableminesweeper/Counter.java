/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package solvableminesweepernetbeans;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Counter extends JPanel {
    JLabel units,tens,hundreds;
    int width=33;
    int height=20;
    int value;
    
    public Counter(int num){
        this.value = num;
        if(num>=0 && num<1000){
            units = toJLabel(num%10+".gif");
            num /= 10;
            tens = toJLabel(num%10+".gif");
            num /= 10;
            hundreds = toJLabel(num%10+".gif");
        }
        add(hundreds);
        add(tens);
        add(units);
    }
    
    private void updateDigits(){
        int num = value;
        if(num>=0 && num<1000){
            units.setIcon(toImageIcon(num%10+".gif"));
            num /= 10;
            tens.setIcon(toImageIcon(num%10+".gif"));
            num /= 10;
            hundreds.setIcon(toImageIcon(num%10+".gif"));
        }
    }
    
    public void addOne(){
        this.value++;
        updateDigits();
    }
    
    public void removeOne(){
        this.value--;
        updateDigits();
    }
    
    private ImageIcon toImageIcon(String path){
        return new ImageIcon(new ImageIcon(this.getClass().getResource("images/digits/"+path)).getImage().getScaledInstance(width/3, height, java.awt.Image.SCALE_SMOOTH));
    }
    
    private JLabel toJLabel(String path){
        return new JLabel(toImageIcon(path));
    }
}