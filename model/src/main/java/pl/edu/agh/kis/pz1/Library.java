package pl.edu.agh.kis.pz1;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;


public class Library {
    private final Semaphore readerSemaphore = new Semaphore(5);
    private final Semaphore writerSemaphore = new Semaphore(1);
    private final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    boolean writerTurn = false;


    public synchronized void requestRead(Integer readerId) throws InterruptedException {
        queue.add(readerId);
        while (readerSemaphore.availablePermits() == 0 || writerTurn) {
            wait();
        }
        queue.poll();
        readerSemaphore.acquire();
        if(readerSemaphore.availablePermits() == 0) {
            writerTurn = true;
        }
        System.out.println("Czytelnik " + readerId + " czyta.");
    }

    public synchronized void finishRead(Integer readerId) {
        readerSemaphore.release();
        System.out.println("Czytelnik " + readerId + " zakończył czytanie. Aktywni czytelnicy = ");
        if (readerSemaphore.availablePermits() == 5) {
            notifyAll();
        }
    }

    public synchronized void requestWrite(Integer writerId) throws InterruptedException {
        while (writerSemaphore.availablePermits() == 0 || !writerTurn) {
            wait();
        }
        queue.poll(); // Usuń z kolejki
        writerSemaphore.acquire();
        writerTurn = false;
        System.out.println("Pisarz " + writerId + " pisze");
    }

    public synchronized void finishWrite(Integer writerId) {
        writerSemaphore.release();
        System.out.println("Pisarz " + writerId + " zakończył pisanie");
        notifyAll();
    }
}
