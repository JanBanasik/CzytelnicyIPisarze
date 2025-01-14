package pl.edu.agh.kis.pz1;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;


class Library {
    int maxPeopleInside = 5;
    int peopleCurrentlyInside = 0;
    Queue<String> waitingPeople = new LinkedList<>();
    List<String> peopleInside = new LinkedList<>();


    public synchronized void requestRead(int readerId) throws InterruptedException {
        String currentReaderSymbol = "Reader " + readerId;
        System.out.println(currentReaderSymbol +  " chce czytać");
        System.out.println("---------------------------");

        waitingPeople.add(currentReaderSymbol);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        System.out.println("---------------------------");
        while(!Objects.equals(waitingPeople.peek(), currentReaderSymbol) || peopleCurrentlyInside == maxPeopleInside) {
            wait();
        }
        waitingPeople.poll();
        peopleCurrentlyInside++;
        peopleInside.add(currentReaderSymbol);
        System.out.println(currentReaderSymbol + " wszedl i czyta");
        System.out.println("---------------------------");
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        System.out.println("---------------------------");

    }

    public synchronized void finishRead(int readerId) {
        String currentReaderSymbol = "Reader " + readerId;
        peopleCurrentlyInside--;
        peopleInside.remove(currentReaderSymbol);
        System.out.println(currentReaderSymbol + " zakonczyl czytanie");
        System.out.println("---------------------------");
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        System.out.println("---------------------------");
        notifyAll();
    }


    public synchronized void requestWrite(int writerId) throws InterruptedException {
        String currentWriterSymbol = "Writer " + writerId;
        System.out.println(currentWriterSymbol +  " chce pisać");
        System.out.println("---------------------------");

        waitingPeople.add(currentWriterSymbol);
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        System.out.println("---------------------------");
        while(!Objects.equals(waitingPeople.peek(), currentWriterSymbol) || peopleCurrentlyInside > 0) {
            wait();
        }
        waitingPeople.poll();
        peopleCurrentlyInside = maxPeopleInside;
        peopleInside.add(currentWriterSymbol);
        System.out.println(currentWriterSymbol + " wszedl i pisze");
        System.out.println("---------------------------");
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        System.out.println("---------------------------");
    }

    public synchronized void finishWrite(int writerId) {
        String currentWriterSymbol = "Writer " + writerId;
        peopleCurrentlyInside = 0;
        peopleInside.remove(currentWriterSymbol);
        System.out.println(currentWriterSymbol + " zakonczyl pisanie");
        System.out.println("---------------------------");
        dumpQueue(waitingPeople);
        dumpList(peopleInside);
        notifyAll();
    }

    private void dumpQueue(Queue<String> waitingPeople) {
        if(waitingPeople.isEmpty()) return;
        System.out.println("Currently in queue: ");
        for(String person : waitingPeople) {
            System.out.print(person + ", ");
        }
        System.out.println();
    }

    private void dumpList(List<String> peopleInside) {
        if(peopleInside.isEmpty()) return;
        System.out.println("People currently inside: ");
        for(String s : peopleInside) {
            System.out.print(s + ", ");
        }
        System.out.println();
    }
}