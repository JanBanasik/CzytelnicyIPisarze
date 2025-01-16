package pl.edu.agh.kis.pz1;

import java.util.Random;

public class Reader extends Thread {

    private final int readerId;
    private final Library library;
    private final Random r = new Random();
    private volatile boolean running = true;

    public Reader(int readerId, Library library) {
        this.readerId = readerId;
        this.library = library;
    }

    @Override
    public void run() {
        while(running) {
            try {
                library.requestRead(readerId);
                Thread.sleep(1000 + getRandomTime() * 2000);
                library.finishRead(readerId);
                Thread.sleep(1000 + getRandomTime() * 2000);
            } catch (InterruptedException e) {
                System.out.println("Błąd w działaniu czytelnika " + readerId + ": " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private long getRandomTime() {
        return (long) ((r.nextLong() - Long.MIN_VALUE) / (Long.MAX_VALUE - (double) Long.MIN_VALUE));
    }

    public void stopRunning() {
        running = false;
    }
}
