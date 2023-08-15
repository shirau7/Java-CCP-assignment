package airportsim;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Objects;

public class Passenger {

    private String passengerId;
    
    //  It generates a random passenger ID using the getRandomNumberP() method from the utility class
    public Passenger() {

        this.passengerId = "Passgenger-" +"P"+ AirportUtil.getRandomNumberP();
    }

    // Returns the passenger ID of the current Passenger object.
    public String getPassengerId() {
        return passengerId;
    }
    
     @Override
    public String toString() {
        return "Passenger[" + "passengerID = " + passengerId + ']';
    }
}
