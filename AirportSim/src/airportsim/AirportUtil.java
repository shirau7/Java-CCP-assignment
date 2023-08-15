package airportsim;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Objects;

// utility methods for the airport simulation project
public class AirportUtil {

    //prints a message indicating the current thread's name and the provided string
    public static void printActivity(String str) {
        System.out.println(Thread.currentThread().getName() + " = " + str);
    }
    
    //method prints the provided object followed by a new line
    public static void print(Object str) {
        System.out.println(str + "\n");
    }
    
    //prints information that need to be displayed on the same line
    public static void printStat(Object str) {
        System.out.println(str);
    }

    //causes the current thread to sleep for the specified number of milliseconds
    public static void sleepThread(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ex) {
            System.out.println("Exception in sleepThread" + ex.getMessage());
        }
    }
    
    //Generates number between 0 and 99
    public static int getRandomNumber() {

        int random = new Random().nextInt(101)+100;
        return random;
        
    }
    
    //Generates number between 0 and 51
    public static int getRandomNumberP() {

        int random = new Random().nextInt(51);
        return random;
        
    }
}
