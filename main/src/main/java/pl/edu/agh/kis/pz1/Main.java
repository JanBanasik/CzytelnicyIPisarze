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
        if(args.length == 0) {
            createAndRunThreads();
        }
        else if(args.length == 2){
            Library library = new Library(5);
            Writer[] writers = new Writer[Integer.parseInt(args[1])];
            Reader[] readers = new Reader[Integer.parseInt(args[0])];

            runThreads(library, writers, readers);
        }
    }

    /**
     * Starts the threads for readers and writers.
     * Initializes and starts all writer and reader threads using the provided library instance.
     *
     * @param library the library instance shared among all threads
     * @param writers an array of Writer threads to be created and started
     * @param readers an array of Reader threads to be created and started
     */
    public static void runThreads(Library library, Writer[] writers, Reader[] readers) {
        for (int i = 0; i < writers.length; i++) {
            writers[i] = new Writer(i, library);
            writers[i].setName("Writer" + i);
        }

        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Reader(i, library);
            readers[i].setName("Reader" + i);
        }


        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);
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
     * It initializes the library, creates the specified number of reader and writer threads,
     */
    public static void createAndRunThreads() {
        List<Integer> values = getUserInfo();

        Library library = new Library(values.get(0));
        Writer[] writers = new Writer[values.get(1)];
        Reader[] readers = new Reader[values.get(2)];

        runThreads(library, writers, readers);
    }
}
