package pl.edu.agh.kis.pz1;

import java.util.concurrent.Semaphore;


public class Library {
    private final Semaphore accessSemaphore = new Semaphore(5, true);
    private final Semaphore queueSemaphore = new Semaphore(1, true);

    public void requestRead(Integer readerId) throws InterruptedException {
        queueSemaphore.acquire();
        System.out.println("Czytelnik " + readerId + " chce czytac");
        accessSemaphore.acquire();
        System.out.println("Czytelnik " + readerId + " czyta");
        queueSemaphore.release();
    }

    public synchronized void finishRead(Integer readerId) {
        System.out.println("Czytelnik " + readerId + " zakończył czytanie. ");
        accessSemaphore.release();
    }

    public void requestWrite(Integer writerId) throws InterruptedException {
        queueSemaphore.acquire();
        System.out.println("Pisarz " + writerId + " chce pisać");
        accessSemaphore.acquire(5);
        System.out.println("Pisarz " + writerId + " pisze");
        queueSemaphore.release();
    }

    public synchronized void finishWrite(Integer writerId) {
        System.out.println("Pisarz " + writerId + " zakończył pisanie");
        accessSemaphore.release(5);
    }
}
