package pl.edu.agh.kis.pz1;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a Library that manages readers and writers trying to access its resources.
 * This class ensures synchronized access to its resources, following the readers-writers problem.
 */
public class Library {
    /** Delimiter used for printing debug messages. */
    String delim = "---------------------------";
    /** Delimiter used for printing info about current people inside*/
    String literalPeopleInside = "Number of people currently inside: ";
    /** Maximum number of readers allowed inside the library at one time. */
    int maxReadersInside;
    /** Current number of people inside the library. */
    AtomicInteger peopleCurrentlyInside = new AtomicInteger(0);
    /** Queue of people (readers/writers) waiting to access the library. */
    Queue<String> waitingPeople = new ConcurrentLinkedQueue<>();
    /** List of people currently inside the library. */
    List<String> peopleInside = new LinkedList<>();

    /**
     * Default constructor. Initializes the library with a default maximum of 5 readers allowed inside.
     */
    public Library() {
        this.maxReadersInside = 5;
    }

    /**
     * Constructs a Library with a specified maximum number of readers allowed inside.
     *
     * @param maxPeopleInside the maximum number of readers allowed inside the library at one time.
     */
    public Library(int maxPeopleInside) {
        this.maxReadersInside = maxPeopleInside;
    }

    /**
     * Handles a reader's request to enter the library and read.
     *
     * @param readerId the ID of the reader requesting access.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized void requestRead(int readerId) throws InterruptedException {
        String currentReaderSymbol = "Reader " + readerId;
        SafePrinter.safePrint(currentReaderSymbol + " chce czytać");
        SafePrinter.safePrint(delim);

        waitingPeople.add(currentReaderSymbol);

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(literalPeopleInside + peopleCurrentlyInside.get());
        SafePrinter.safePrint(delim);

        // Wait until the reader is at the front of the queue and there is space inside
        while (!Objects.equals(waitingPeople.peek(), currentReaderSymbol) || peopleCurrentlyInside.get() == maxReadersInside) {
            wait();
        }
        waitingPeople.poll();
        peopleCurrentlyInside.incrementAndGet();
        peopleInside.add(currentReaderSymbol);
        SafePrinter.safePrint(currentReaderSymbol + " wszedl i czyta");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(literalPeopleInside + peopleCurrentlyInside.get());
        SafePrinter.safePrint(delim);
    }

    /**
     * Handles a reader's request to finish reading and leave the library.
     *
     * @param readerId the ID of the reader finishing their reading session.
     */
    public synchronized void finishRead(int readerId) {
        String currentReaderSymbol = "Reader " + readerId;
        peopleCurrentlyInside.decrementAndGet();
        peopleInside.remove(currentReaderSymbol);
        SafePrinter.safePrint(currentReaderSymbol + " zakonczyl czytanie");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(literalPeopleInside + peopleCurrentlyInside.get());
        SafePrinter.safePrint(delim);
        notifyAll(); // Notify other waiting threads
    }

    /**
     * Handles a writer's request to enter the library and write.
     *
     * @param writerId the ID of the writer requesting access.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized void requestWrite(int writerId) throws InterruptedException {
        String currentWriterSymbol = "Writer " + writerId;
        SafePrinter.safePrint(currentWriterSymbol + " chce pisać");
        SafePrinter.safePrint(delim);

        waitingPeople.add(currentWriterSymbol);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(literalPeopleInside + peopleCurrentlyInside.get());
        SafePrinter.safePrint(delim);

        // Wait until the writer is at the front of the queue and no one else is inside
        while (!Objects.equals(waitingPeople.peek(), currentWriterSymbol) || peopleCurrentlyInside.get() > 0) {
            wait();
        }
        waitingPeople.poll();
        peopleCurrentlyInside.set(maxReadersInside); // Reserve all slots for the writer
        peopleInside.add(currentWriterSymbol);
        SafePrinter.safePrint(currentWriterSymbol + " wszedl i pisze");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(literalPeopleInside + peopleCurrentlyInside.get());
        SafePrinter.safePrint(delim);
    }

    /**
     * Handles a writer's request to finish writing and leave the library.
     *
     * @param writerId the ID of the writer finishing their writing session.
     */
    public synchronized void finishWrite(int writerId) {
        String currentWriterSymbol = "Writer " + writerId;
        peopleCurrentlyInside.set(0); // Free all slots
        peopleInside.remove(currentWriterSymbol);
        SafePrinter.safePrint(currentWriterSymbol + " zakonczyl pisanie");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(literalPeopleInside + peopleCurrentlyInside.get());
        SafePrinter.safePrint(delim);

        notifyAll(); // Notify other waiting threads
    }

    /**
     * Dumps the queue of people waiting to access the library for debugging purposes.
     *
     * @param waitingPeople the queue of waiting people.
     */
    private void dumpQueue(Queue<String> waitingPeople) {
        if (waitingPeople.isEmpty()) return;
        SafePrinter.safePrint("Currently in queue: ");
        for (String person : waitingPeople) {
            SafePrinter.safePrintWithoutNewLine(person + ", ");
        }
        SafePrinter.safePrint();
    }

    /**
     * Dumps the list of people currently inside the library for debugging purposes.
     *
     * @param peopleInside the list of people currently inside.
     */
    private void dumpList(List<String> peopleInside) {
        if (peopleInside.isEmpty()) return;
        SafePrinter.safePrint("People currently inside: ");
        for (String s : peopleInside) {
            SafePrinter.safePrintWithoutNewLine(s + ", ");
        }
        SafePrinter.safePrint();
    }
}
