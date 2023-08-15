package airportsim;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Objects;

//simulates the control towers actions during the airport simulation
public class ControlTower {

    private String CTid;
    private Queue<Plane> planeQueue;
    private Airport airportobj;

    //The constructor initializes the control tower object.
    public ControlTower(Airport airportobj) {
        this.CTid = "CT_" + new Random(System.currentTimeMillis()).nextInt(20000);
        this.planeQueue = new LinkedList<>();
        this.airportobj = airportobj;
    }

    //This method simulates the action of a plane requesting to land and the control tower managing the landing process
    public void planeCT(Plane aeroplaneObj) {
        long start = System.currentTimeMillis();
        boolean flag = false;
        AirportUtil.printActivity(aeroplaneObj.getPlaneID() + " Plane requesting to land");
        //if airport is full, plane is asked to wait
        if (airportobj.isRunwayFree() || !airportobj.isAirportGateAvailable()) {
            AirportUtil.printActivity(aeroplaneObj.getPlaneID() + " airport full, please wait!");
        }
        

    // object-level lock to ensure only one thread enters the critical section.
    synchronized (this) {
        // Check if the Runway is not occupied and a gate is available
        while (airportobj.isRunwayFree() || !airportobj.isAirportGateAvailable()) {
            try {
                // Wait until the conditions are met
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // The Runway is cleared and a gate is available
        AirportUtil.printActivity("Runway cleared, " + aeroplaneObj.getPlaneID() + " please proceed to land.");
        airportobj.occupyRunway();
        AirportUtil.printActivity(aeroplaneObj.getPlaneID() + " is Landing on the Runway.");
        AirportUtil.sleepThread(50);
        airportobj.freeRunway();
        // Add plane to airport
        airportobj.getGates().add(aeroplaneObj);
        AirportUtil.printActivity(aeroplaneObj.getPlaneID() + " Proceed to Assigned Gate");
        AirportUtil.print("Airport Gates occupancy count = " + airportobj.getGates().size());
        AirportUtil.sleepThread(80);
        this.notifyAll();
    }

        //calculating waiting time of plane obejct
        long end = System.currentTimeMillis();
        float diff = (end - start) / 1000F;
        aeroplaneObj.setPlaneWaitingTime(diff);
        airportobj.getPlaneStats().put(aeroplaneObj.getPlaneID(), aeroplaneObj);

    }

        // This method prints the final statistics of the airport simulation.
        // Calculates the minimum, maximum, and average waiting times of the planes.
        public void printStatistics() {
        AirportUtil.printStat("\t\t=Final Stats=\n");
        AirportUtil.printStat("Airport Gates Occupancy count= " + airportobj.getGates().size());
        Map.Entry firstObj = airportobj.getPlaneStats().entrySet().iterator().next();
        Plane firstPlaneOj = (Plane) firstObj.getValue();
        float minIime = Float.valueOf(firstPlaneOj.getPlaneWaitingTime()), maxTime = Float.valueOf(firstPlaneOj.getPlaneWaitingTime()), timeTakenEachPlane = 0;
        
        for (Map.Entry elm : airportobj.getPlaneStats().entrySet()) {
            Plane planeOj = (Plane) elm.getValue();
            float waitTimePlane = Float.valueOf(planeOj.getPlaneWaitingTime());
            if (minIime > waitTimePlane) {
                minIime = waitTimePlane;
            }

            if (maxTime < waitTimePlane) {
                maxTime = waitTimePlane;
            }
            timeTakenEachPlane+=waitTimePlane;
            AirportUtil.printStat("PlaneID: "+elm.getKey()+" Waiting time = "+String.format("%.2f", planeOj.getPlaneWaitingTime())+" seconds");
            AirportUtil.printStat("PlaneID: "+elm.getKey()+" Boarded passenger count = "+planeOj.getPsgList().size());
        }
        AirportUtil.printStat("\nMinimum waiting time of all Planes = "+String.format("%.2f", minIime)+" seconds");
        AirportUtil.printStat("Average waiting time of all Planes = "+String.format("%.2f", (timeTakenEachPlane/airportobj.getPlaneStats().size()))+" seconds");
        AirportUtil.printStat("Maximum waiting time of all Planes = "+String.format("%.2f", maxTime)+" seconds");
        AirportUtil.printStat("\nTotal Number of planes served = " + airportobj.getPlaneStats().size());

    }
        // new code for emergency landing
        public void emergencyLandingRequest(Plane plane) {
        synchronized (this) {
        AirportUtil.printActivity("Emergency landing requested " + plane.getPlaneID());
        AirportUtil.printActivity("Clearing the runway for emergency landing.");
        airportobj.occupyRunway();
        AirportUtil.sleepThread(200);
        airportobj.freeRunway();
        AirportUtil.printActivity("Emergency landing completed " + plane.getPlaneID());
    }
}
    
    @Override
    public String toString() {
        return "ControlTower - [" + "CT-ID = " + CTid + ']';
    }

    //This method returns the plane queue associated with the control tower.
    public Queue<Plane> getPlaneQueue() {
        return planeQueue;
    }
}