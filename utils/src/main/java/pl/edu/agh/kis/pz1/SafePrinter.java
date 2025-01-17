package pl.edu.agh.kis.pz1;

/**
 * A utility class for printing messages to the console in a thread-safe manner.
 * This class provides methods for printing messages with or without a newline,
 * and ensures that the methods are not instantiable.
 */
public class SafePrinter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an exception if called, as this class is not meant to be instantiated.
     */
    private SafePrinter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Prints an empty line to the console.
     */
    public static void safePrint() {
        System.out.println();
    }

    /**
     * Prints the specified message to the console with a newline at the end.
     *
     * @param message the message to print.
     */
    public static void safePrint(String message) {
        System.out.println(message);
    }

    /**
     * Prints the specified message to the console without a newline at the end.
     *
     * @param message the message to print.
     */
    public static void safePrintWithoutNewLine(String message) {
        System.out.print(message);
    }
}
