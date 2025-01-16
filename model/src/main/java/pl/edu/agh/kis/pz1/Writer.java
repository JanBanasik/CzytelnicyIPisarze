package pl.edu.agh.kis.pz1;
import java.util.Random;
public class Writer extends Thread {

    private final int writerId;
    private final Library library;
    private final Random r = new Random();
    private volatile boolean running = true;

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
                Thread.sleep(1000 + getRandomTime() * 2000);
            } catch (InterruptedException e) {
                System.out.println("Błąd w działaniu pisarza " + writerId + ": " + e.getMessage());
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