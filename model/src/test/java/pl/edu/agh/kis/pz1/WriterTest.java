package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriterTest {

    @Test
    void testInterruptedWriter()
    {
        Library lib = new Library();
        Writer w = new Writer(5, lib);
        w.start();
        w.interrupt();
        assertTrue(w.isInterrupted());
    }
}