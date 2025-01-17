package pl.edu.agh.kis.pz1;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        createAndRunThreads();
    }

    public static List<Integer> getUserInfo(){
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

    public static void createAndRunThreads() {
        List<Integer> values = getUserInfo();

        Library library = new Library(values.get(0));
        Writer[] writers = new Writer[values.get(1)];
        Reader[] readers = new Reader[values.get(2)];

        for(int i=0; i<writers.length; i++) {
            writers[i] = new Writer(i, library);
            writers[i].setName("Writer" + i);
        }

        for(int i=0; i<readers.length; i++) {
            readers[i] = new Reader(i, library);
            readers[i].setName("Reader" + i);
        }
        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);
    }
}


