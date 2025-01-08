package pl.edu.agh.kis.pz1;

import java.util.Arrays;

public class Main {
    public static final int readersCount = 10;
    public static final int writersCount = 3;

    public static void main( String[] args ) {
        Library library = new Library();

        Writer[] writers = new Writer[writersCount];
        Reader[] readers = new Reader[readersCount];

        for(int i=0; i<writersCount; i++) {
            writers[i] = new Writer(i, library);
        }

        for(int i=0; i<readers.length; i++) {
            readers[i] = new Reader(i, library);
        }
        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);
    }
}
