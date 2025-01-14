package pl.edu.agh.kis.pz1;

public class Writer extends Thread {
    private final int writerId;
    private final Library library;

    public Writer(int writerId, Library library) {
        this.writerId = writerId;
        this.library = library;
    }

    @Override
    public void run() {
        for (;;) {
            try {
                library.requestWrite(writerId);

                // Symulacja pisania
                Thread.sleep((long) (1000 + Math.random() * 2000)); // Losowy czas od 1 do 3 sekund

                library.finishWrite(writerId);

                // Krótkie opóźnienie przed kolejną próbą
                Thread.sleep((long) (500 + Math.random() * 500));
            } catch (InterruptedException e) {
                System.out.println("Błąd w działaniu pisarza " + writerId + ": " + e.getMessage());
            }
        }
    }

}