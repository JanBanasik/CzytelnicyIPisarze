package pl.edu.agh.kis.pz1;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The main class for managing and running the threads of readers and writers accessing a library.
 * This class orchestrates the creation of reader and writer threads and manages their interaction with the library.
 */
public class Main {

    /**
     * The entry point of the program. It starts the process by creating and running the reader and writer threads.
     *
     * @param args the command line arguments (not used in this case).
     */
    public static void main(String[] args) {
        createAndRunThreads();
    }

    /**
     * Prompts the user for input to specify the configuration for the library system.
     * The method collects the maximum number of people allowed inside the library,
     * the number of writers, and the number of readers.
     *
     * @return a List of integers containing the maximum number of people inside the library,
     *         the number of writers, and the number of readers.
     */
    public static List<Integer> getUserInfo() {
        Scanner scanner = new Scanner(System.in);

        SafePrinter.safePrint("Enter maximum number of people inside: ");
        int maxPeopleInside = scanner.nextInt();

        SafePrinter.safePrint("Enter number of writers: ");
        int writersCount = scanner.nextInt();

        SafePrinter.safePrint("Enter number of readers: ");
        int readersCount = scanner.nextInt();

        SafePrinter.safePrint();
        return List.of(maxPeopleInside, writersCount, readersCount);
    }

    /**
     * Creates and starts the threads for readers and writers based on the user input.
     * It initializes the library, creates the specified number of reader and writer threads,
     * and starts their execution.
     */
    public static void createAndRunThreads() {
        List<Integer> values = getUserInfo();

        Library library = new Library(values.get(0));
        Writer[] writers = new Writer[values.get(1)];
        Reader[] readers = new Reader[values.get(2)];

        for (int i = 0; i < writers.length; i++) {
            writers[i] = new Writer(i, library);
            writers[i].setName("Writer" + i);
        }

        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Reader(i, library);
            readers[i].setName("Reader" + i);
        }

        // Start all writers and readers in separate threads
        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);
    }
}
