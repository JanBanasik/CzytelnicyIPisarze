package pl.edu.agh.kis.pz1;
import lombok.Getter;

import java.security.SecureRandom;

@Getter
public class Writer extends Thread {

    private final int writerId;
    private final Library library;
    private final SecureRandom r = new SecureRandom();
    private volatile boolean running = true;
    private boolean wroteAtLeastOnce = true;

    public Writer(int writerId, Library library) {
        this.writerId = writerId;
        this.library = library;
    }

    @Override
    public void run() {
        while (running) {
            try {
                library.requestWrite(writerId);
                Thread.sleep(1000 + getRandomTime() * 2000);

                library.finishWrite(writerId);
                wroteAtLeastOnce = true;
                Thread.sleep(1000 + getRandomTime() * 2000);
            } catch (InterruptedException e) {
                SafePrinter.safePrint("Błąd w działaniu pisarza " + writerId + ": " + e.getMessage());
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