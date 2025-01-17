package pl.edu.agh.kis.pz1;

public class SafePrinter {

    private SafePrinter() {
        throw new IllegalStateException("Utility class");
    }

    public static void safePrint(){
        System.out.println();
    }

    public static void safePrint(String message){
        System.out.println(message);
    }

    public static void safePrintWithoutNewLine(String message){
        System.out.print(message);
    }
}
