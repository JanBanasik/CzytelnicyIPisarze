package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void testReaderCanEnterWhenLibraryNotFull() throws InterruptedException {
        Library library = new Library();
        library.requestRead(1);
        assertEquals(new AtomicInteger(1).get(), library.peopleCurrentlyInside.get());
        assertTrue(library.peopleInside.contains("Reader 1"));
    }

    @Test
    void testWriterCanEnterWhenLibraryEmpty() throws InterruptedException {
        Library library = new Library();
        library.requestWrite(1);

        assertEquals(new AtomicInteger(5).get(), library.peopleCurrentlyInside.get());
        assertTrue(library.peopleInside.contains("Writer 1"));
    }


    @Test
    void testWriterCanEnterWhenReaderIsDone() throws InterruptedException {
        Library library = new Library();

        Thread reader = new Thread(() -> {
            try {
                library.requestRead(1);
                library.finishRead(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread writer = new Thread(() -> {
            try {
                library.requestWrite(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        reader.start();

        writer.start();

        writer.join(1000);
        reader.join(1000);


        assertTrue(library.peopleInside.contains("Writer 1"));
    }

    @Test
    void testReaderAndWriterCannotEnterSimultaneously() throws InterruptedException {
        Library library = new Library();

        Thread reader = new Thread(() -> {
            try {
                library.requestRead(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread writer = new Thread(() -> {
            try {
                library.requestWrite(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        reader.start();
        writer.start();

        reader.join(1000);
        writer.join(1000);

        int readersInside = (int) library.peopleInside.stream().filter(p -> p.startsWith("Reader")).count();
        int writersInside = (int) library.peopleInside.stream().filter(p -> p.startsWith("Writer")).count();

        assertTrue(readersInside == 0 || writersInside == 0);
    }

    @Test
    void testRunThreads() throws Exception {
        Library library = new Library(5);
        Writer[] writers = new Writer[3];
        Reader[] readers = new Reader[10];

        for(int i=0; i<3; i++) {
            writers[i] = new Writer(i, library);
        }

        for(int i=0; i<10; i++) {
            readers[i] = new Reader(i, library);
        }
        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);


        Arrays.stream(writers).forEach(Writer::stopRunning);
        Arrays.stream(readers).forEach(Reader::stopRunning);

        for (Writer writer : writers) {
            writer.join(100);
        }
        for (Reader reader : readers) {
            reader.join(100);
        }
        assertFalse(library.peopleInside.contains(library.waitingPeople.peek()));
    }

    @Test
    void testIfEveryOneReadOrWroteAtLeastOnce() throws Exception {
        Library library = new Library(5);
        Writer[] writers = new Writer[2];
        Reader[] readers = new Reader[2];

        for(int i=0; i<writers.length; i++) {
            writers[i] = new Writer(i, library);
        }

        for(int i=0; i< readers.length; i++) {
            readers[i] = new Reader(i, library);
        }
        Arrays.stream(writers).forEach(Writer::start);
        Arrays.stream(readers).forEach(Reader::start);


        Arrays.stream(writers).forEach(Writer::stopRunning);
        Arrays.stream(readers).forEach(Reader::stopRunning);

        for (Writer writer : writers) {
            writer.join(100);
        }
        for (Reader reader : readers) {
            reader.join(100);
        }

        for (Writer writer : writers) {
            assertTrue(writer.isWroteAtLeastOnce());
        }

        for (Reader reader : readers) {
            assertTrue(reader.isReadAtLeastOnce());
        }


    }

    @Test
    void testMultipleReaderAndWriterCannotEnterSimultaneously() throws InterruptedException {

        Library library = new Library(5);
        Writer[] writers = new Writer[3];
        Reader[] readers = new Reader[10];

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

        for(Reader reader : readers) {
            reader.join(1000);
        }
        for(Writer writer : writers) {
            writer.join(1000);
        }

        int readersInside = (int) library.peopleInside.stream().filter(p -> p.startsWith("Reader")).count();
        int writersInside = (int) library.peopleInside.stream().filter(p -> p.startsWith("Writer")).count();

        assertTrue(readersInside == 0 || writersInside == 0);
    }
}
