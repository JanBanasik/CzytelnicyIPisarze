package pl.edu.agh.kis.pz1;

import java.io.ByteArrayInputStream;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    void provideInput(String data){
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testGetInfo(){
        provideInput("1\n2\n3");
        List<Integer> res = Main.getUserInfo();
        assertEquals(Arrays.asList(1,2,3), res);
    }

    @Test
    void testRunThreads() {
        provideInput("2\n3\n4");
        Main.createAndRunThreads();
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
        int counter = 0;
        for(Thread thread : threadSet){
            System.out.println(thread.getName());
            if(thread.getName().contains("Reader") || thread.getName().contains("Writer")){
                counter++;
            }
        }
        assertEquals(7, counter);
    }
}
