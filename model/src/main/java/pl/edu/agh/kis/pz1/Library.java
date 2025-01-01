package pl.edu.agh.kis.pz1;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Library {
    private final Semaphore readerSemaphore = new Semaphore(5);
    private final Semaphore writerSemaphore = new Semaphore(1);
    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private int activeReaders = 0;
    private boolean writerActive = false;

    public synchronized void requestRead(String readerId) throws InterruptedException {
        queue.add("Reader-" + readerId);
        while (!queue.peek().equals("Reader-" + readerId) || writerActive) {
            wait();
        }
        queue.poll();
        readerSemaphore.acquire();
        activeReaders++;
        System.out.println("Czytelnik " + readerId + " czyta. Aktywni czytelnicy = " + activeReaders);
    }

    public synchronized void finishRead(String readerId) {
        activeReaders--;
        readerSemaphore.release();
        System.out.println("Czytelnik " + readerId + " zakończył czytanie. Aktywni czytelnicy = " + activeReaders);
        if (activeReaders == 0) {
            notifyAll();
        }
    }

    public synchronized void requestWrite(String writerId) throws InterruptedException {
        queue.add("Writer-" + writerId); // Dodaj pisarza do kolejki
        while (!queue.peek().equals("Writer-" + writerId) || activeReaders > 0 || writerActive) {
            wait();
        }
        queue.poll(); // Usuń z kolejki
        writerSemaphore.acquire();
        writerActive = true;
        System.out.println("Pisarz " + writerId + " pisze");
    }

    public synchronized void finishWrite(String writerId) {
        writerSemaphore.release();
        writerActive = false;
        System.out.println("Pisarz " + writerId + " zakończył pisanie");
        notifyAll();
    }
}
