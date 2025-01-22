package pl.edu.agh.kis.pz1;

import lombok.Getter;

import java.security.SecureRandom;

/**
 * Represents a reader that accesses a library in a separate thread.
 * Each reader tries to access the library to read and then takes a break before trying again.
 * The reader stops when its {@code stopRunning} method is called.
 */
@Getter
public class Reader extends Thread {

    /** Unique identifier for the reader. */
    private final int readerId;

    /** The library that the reader interacts with. */
    private final Library library;

    /** A secure random number generator for simulating delays. */
    private final SecureRandom r = new SecureRandom();

    /** Flag indicating whether the reader has read at least once. */
    private boolean readAtLeastOnce = true;

    /** Flag controlling the running state of the reader. */
    private volatile boolean running = true;

    /**
     * Constructs a Reader with a unique identifier and a reference to the library.
     *
     * @param readerId the unique ID of the reader.
     * @param library the library that the reader interacts with.
     */
    public Reader(int readerId, Library library) {
        this.readerId = readerId;
        this.library = library;
    }

    /**
     * Main logic for the reader's thread. The reader repeatedly:
     * <ul>
     *   <li>Requests to read from the library.</li>
     *   <li>Simulates reading for a random time.</li>
     *   <li>Finishes reading and notifies the library.</li>
     *   <li>Waits for a random time before trying to read again.</li>
     * </ul>
     * The loop runs until {@code stopRunning} is called.
     */
    @Override
    public void run() {
        while (running) {
            try {
                library.requestRead(readerId);
                Thread.sleep(1000 + getRandomTime() * 2000);

                library.finishRead(readerId);
                readAtLeastOnce = true;
                Thread.sleep(1000 + getRandomTime() * 2000);

            } catch (InterruptedException e) {
                SafePrinter.safePrint("Error running the reader " + readerId + ": " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Generates a random time value to simulate varying delays.
     *
     * @return a random time value.
     */
    private long getRandomTime() {
        return (long) ((r.nextLong() - Long.MIN_VALUE) / (Long.MAX_VALUE - (double) Long.MIN_VALUE));
    }

    /**
     * Stops the reader's execution by setting the {@code running} flag to {@code false}.
     * The thread will finish its current iteration before terminating.
     */
    public void stopRunning() {
        running = false;
    }
}
