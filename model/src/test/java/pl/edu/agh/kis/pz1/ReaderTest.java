package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    void testInterruptedReader()
    {
        Library lib = new Library();
        Reader r = new Reader(5, lib);
        r.start();
        r.interrupt();
        assertTrue(r.isInterrupted());
    }
}