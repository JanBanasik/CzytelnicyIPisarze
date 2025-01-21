package pl.edu.agh.kis.pz1;

import lombok.Getter;

import java.security.SecureRandom;

/**
 * Represents a writer that accesses a library in a separate thread.
 * Each writer attempts to gain exclusive access to the library to write,
 * performs the writing task, and then takes a break before trying again.
 * The writer stops when its {@code stopRunning} method is called.
 */
@Getter
public class Writer extends Thread {

    /** Unique identifier for the writer. */
    private final int writerId;

    /** The library that the writer interacts with. */
    private final Library library;

    /** A secure random number generator for simulating delays. */
    private final SecureRandom r = new SecureRandom();

    /** Flag controlling the running state of the writer. */
    private volatile boolean running = true;

    /** Flag indicating whether the writer has written at least once. */
    private boolean wroteAtLeastOnce = true;

    /**
     * Constructs a Writer with a unique identifier and a reference to the library.
     *
     * @param writerId the unique ID of the writer.
     * @param library the library that the writer interacts with.
     */
    public Writer(int writerId, Library library) {
        this.writerId = writerId;
        this.library = library;
    }

    /**
     * Main logic for the writer's thread. The writer repeatedly:
     * <ul>
     *   <li>Requests exclusive access to the library to write.</li>
     *   <li>Simulates writing for a random time.</li>
     *   <li>Finishes writing and releases exclusive access to the library.</li>
     *   <li>Waits for a random time before attempting to write again.</li>
     * </ul>
     * The loop runs until {@code stopRunning} is called.
     */
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

    /**
     * Generates a random time value to simulate varying delays.
     *
     * @return a random time value.
     */
    private long getRandomTime() {
        return (long) ((r.nextLong() - Long.MIN_VALUE) / (Long.MAX_VALUE - (double) Long.MIN_VALUE));
    }

    /**
     * Stops the writer's execution by setting the {@code running} flag to {@code false}.
     * The thread will finish its current iteration before terminating.
     */
    public void stopRunning() {
        running = false;
    }
}
