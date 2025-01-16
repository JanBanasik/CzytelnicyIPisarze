package pl.edu.agh.kis.pz1;


import java.util.Arrays;
import java.util.Scanner;
public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter maximum number of people inside: ");
        int maxPeopleInside = scanner.nextInt();

        System.out.println("Enter number of writers: ");
        int writersCount = scanner.nextInt();

        System.out.println("Enter number of readers: ");
        int readersCount = scanner.nextInt();
        System.out.println();

        Library library = new Library(maxPeopleInside);
        Writer[] writers = new Writer[writersCount];
        Reader[] readers = new Reader[readersCount];

        for(int i=0; i<writersCount; i++) {
            writers[i] = new Writer(i, library);
        }

        for(int i=0; i<readersCount; i++) {
            readers[i] = new Reader(i, library);
        }
        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);
    }
}
