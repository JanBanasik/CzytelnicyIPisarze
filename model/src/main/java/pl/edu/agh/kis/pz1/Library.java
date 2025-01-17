package pl.edu.agh.kis.pz1;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;


class Library {
    String delim = "---------------------------";
    int maxReadersInside;
    int peopleCurrentlyInside = 0;
    Queue<String> waitingPeople = new LinkedList<>();
    List<String> peopleInside = new LinkedList<>();

    public Library(){
        this.maxReadersInside = 5;
    }
    public Library(int maxPeopleInside) {
        this.maxReadersInside = maxPeopleInside;
    }


    public synchronized void requestRead(int readerId) throws InterruptedException {
        String currentReaderSymbol = "Reader " + readerId;
        SafePrinter.safePrint(currentReaderSymbol +  " chce czytać");
        SafePrinter.safePrint(delim);

        waitingPeople.add(currentReaderSymbol);

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(delim);

        while(!Objects.equals(waitingPeople.peek(), currentReaderSymbol) || peopleCurrentlyInside == maxReadersInside) {
            wait();
        }
        waitingPeople.poll();
        peopleCurrentlyInside++;
        peopleInside.add(currentReaderSymbol);
        SafePrinter.safePrint(currentReaderSymbol + " wszedl i czyta");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(delim);

    }

    public synchronized void finishRead(int readerId) {
        String currentReaderSymbol = "Reader " + readerId;
        peopleCurrentlyInside--;
        peopleInside.remove(currentReaderSymbol);
        SafePrinter.safePrint(currentReaderSymbol + " zakonczyl czytanie");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(delim);
        notifyAll();
    }


    public synchronized void requestWrite(int writerId) throws InterruptedException {
        String currentWriterSymbol = "Writer " + writerId;
        SafePrinter.safePrint(currentWriterSymbol +  " chce pisać");
        SafePrinter.safePrint(delim);

        waitingPeople.add(currentWriterSymbol);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(delim);
        while(!Objects.equals(waitingPeople.peek(), currentWriterSymbol) || peopleCurrentlyInside > 0) {
            wait();
        }
        waitingPeople.poll();
        peopleCurrentlyInside = maxReadersInside;
        peopleInside.add(currentWriterSymbol);
        SafePrinter.safePrint(currentWriterSymbol + " wszedl i pisze");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(delim);
    }

    public synchronized void finishWrite(int writerId) {
        String currentWriterSymbol = "Writer " + writerId;
        peopleCurrentlyInside = 0;
        peopleInside.remove(currentWriterSymbol);
        SafePrinter.safePrint(currentWriterSymbol + " zakonczyl pisanie");

        SafePrinter.safePrint(delim);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        SafePrinter.safePrint(delim);

        notifyAll();
    }

    private void dumpQueue(Queue<String> waitingPeople) {
        if(waitingPeople.isEmpty()) return;
        SafePrinter.safePrint("Currently in queue: ");
        for(String person : waitingPeople) {
            SafePrinter.safePrintWithoutNewLine(person + ", ");
        }
        SafePrinter.safePrint();
    }

    private void dumpList(List<String> peopleInside) {
        if(peopleInside.isEmpty()) return;
        SafePrinter.safePrint("People currently inside: ");
        for(String s : peopleInside) {
            SafePrinter.safePrintWithoutNewLine(s + ", ");
        }
        SafePrinter.safePrint();
    }
}