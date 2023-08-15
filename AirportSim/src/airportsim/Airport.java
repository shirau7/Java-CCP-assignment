package airportsim;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Objects;

public class Airport {
    
    private String airportId;
    private static final String AirportName = "Asia Pacific Airport";
    private static final int noRunway = 1;
    private boolean isRunwayOccupied = false;
    private boolean isFuelTruckOccupied = false;
    private int noOfGates;
    private List<Plane> gatesList;
    private Map<String, Plane> planeStats = new LinkedHashMap<>();

    public Airport(int noOfGates) {
        this.noOfGates = noOfGates;
        this.gatesList = new ArrayList<>();
        this.airportId = "AP-" + AirportUtil.getRandomNumber();
    }

    Airport() {
       throw new UnsupportedOperationException("Not yet supported!");
    }
    
    //getter methods
    
    // Returns the list of planes currently occupying the gates.
    public List<Plane> getGates() {
        return gatesList;
    }

    //Checks if there is an available gate at the airport.
    public boolean isAirportGateAvailable() {
        return gatesList.size() < noOfGates;
    }


    //Checks if the runway is currently occupied.
    public boolean isRunwayFree() {
        return isRunwayOccupied;
    }

     //Checks if the fueling truck is currently occupied.
    public boolean isFuelTruckFree() {
        return isFuelTruckOccupied;
    }

    //Returns the name of the airport.
    public static String getAirportName() {
        return AirportName;
    }

    //Returns the map containing the statistics of planes at the airport.
    public Map<String, Plane> getPlaneStats() {
        return planeStats;
    }
    
    //setter method which gets the plane statistics
       public void setPlaneStats(Map<String, Plane> planeStats) {
        this.planeStats = planeStats;
    }
        
    public void occupyRunway() {
        isRunwayOccupied = true;
    }

   
    public void freeRunway() {
        isRunwayOccupied = false;
    }

    public void occupyFuelTruck() {
        isFuelTruckOccupied = true;
    }

    public void freeFuelTruck() {
        isFuelTruckOccupied = false;
    }

    @Override
    public String toString() {
       return  "AIRPORT INFORMATION \n\n" + "Airport ID = " + airportId + 
               ", \nAirport Name = " + AirportName + 
               ", \nNumber of Gates = " + noOfGates + 
               ", \nNumber of Runways = " + noRunway;
    }

}
