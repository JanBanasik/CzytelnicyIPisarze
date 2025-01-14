package pl.edu.agh.kis.pz1;

import java.util.LinkedList;
import java.util.Queue;

public class Semaphore {

    protected int capacity;
    protected int currentValue = 0;
    public Queue<String> myQueue = new LinkedList<String>();

    public Semaphore(int capacity) {
        this.capacity = capacity;
    }

    synchronized public void acquire(String who) {
        if (currentValue < capacity) {
            currentValue++;
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    synchronized public void release() {
        if (currentValue > 0) {
            currentValue--;
            notify();
        }
    }


}
