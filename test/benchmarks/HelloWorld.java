package benchmarks;

import jwig.logging.SingleSnoop;

public class HelloWorld {
    static {
        SingleSnoop.startSnooping();
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}