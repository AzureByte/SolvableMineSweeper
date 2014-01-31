/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package solvableminesweepernetbeans;

/**
 *
 * @author Kenric
 */
public class Stopwatch implements Runnable {
    private final Run r;
    private boolean started;
    public Stopwatch(Run r){
        this.r = r;
        started = false;
    }

    @Override
    public void run() {
        started = true;
        while(!Thread.interrupted()&& started){
            try{
                Thread.sleep(1000);
                r.counter_time.addOne();
            }
            catch(InterruptedException e){
                started = false;
            }
        }
    }
}
