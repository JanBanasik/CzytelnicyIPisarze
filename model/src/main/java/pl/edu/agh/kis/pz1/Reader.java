package pl.edu.agh.kis.pz1;

public class Reader extends Thread {
    private final int readerId;
    private final Library library;

    public Reader(int readerId, Library library) {
        this.readerId = readerId;
        this.library = library;
    }

    @Override
    public void run() {
        for (;;) {
            try {
                library.requestRead(readerId);

                // Symulacja czytania
                Thread.sleep((long) (1000 + Math.random() * 2000)); // Losowy czas od 1 do 3 sekund

                library.finishRead(readerId);

                // Krótkie opóźnienie przed kolejną próbą
                Thread.sleep((long) (500 + Math.random() * 500));
            } catch (InterruptedException e) {
                System.out.println("Błąd w działaniu czytelnika " + readerId + ": " + e.getMessage());
            }
        }
    }

}
