package airportsim;;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Objects;


// class encapsulates the behaviour of the plane during the airport simulation
public class Plane {
    

    //declaration of variables used
    List<Plane> emergencyPlanes = new ArrayList<>(); 
    private static final int planeCapacity = 50;
    private String PlaneId;
    private boolean isThreadVisited = false;
    private boolean isPassgEmbAndDisembCompleted = false;
    private boolean RefuelCompleted = false;
    private boolean ResupplyCompleted = false;
    private boolean emergencyLandingRequested;
    private int gateNoAssigned;    
    private List<Passenger> psgList;
    private float planeWaitingTime;
 
    // initializes the Plane object. It sets the PlaneId, creates an empty passenger list, and fills the passengers to the plane.
    public Plane() {

        this.PlaneId = " Flight-" + "MY" + AirportUtil.getRandomNumber();
        this.psgList = new ArrayList<>(planeCapacity);
        this.fillPassengersPlane(false);
    }


    // Represents the activities to be performed after the plane occupies a gate.
    public void activityAfterOccupingGate(Airport airportObj) {
        // to represent reality, passengers disembark
        this.passengerDisembarking();
        // after disembarking is completed, passengers embark
        this.passengerEmbarking();
        this.isPassgEmbAndDisembCompleted = true;

        // Create a Runnable for resupplying and cleaning the plane
        Runnable resupplyAndCleanActivity = () -> {
            // Simulate the process of resupplying the plane
            AirportUtil.print("CT-Thread = " + this.getPlaneID() + " Resupplying the plane");
            AirportUtil.sleepThread(500);

            // Simulate the process of cleaning the plane
            AirportUtil.print("CT-Thread = " + this.getPlaneID() + " Cleaning the plane");
            AirportUtil.sleepThread(500);
        };

        // Create a new thread for resupplying and cleaning the plane
        Thread resupplyAndCleanThread = new Thread(resupplyAndCleanActivity, "CT-Thread");
        resupplyAndCleanThread.start();

        // Wait for the resupply and cleaning thread to complete
        try {
            resupplyAndCleanThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.confirmAndTakeOff(airportObj);
    }
    
    

    // Simulates the process of passengers boarding the plane
    //calls passenger list to fill it with passengers boarding the plane
    public void passengerEmbarking() {
        this.fillPassengersPlane(true);

    }
    
    // Simulates the process of passengers disembarking from the plane.
    // iterates over the passenger list until all disembarks
    public void passengerDisembarking() {
        AirportUtil.printActivity("Passengers are Disembarking, total Passenger count = " + this.psgList.size());

        List<Passenger> disembarkedPassengers = new ArrayList<>();

        for (Passenger passenger : this.psgList) {
            AirportUtil.printActivity("Disembarking " + passenger.getPassengerId());
            AirportUtil.sleepThread(80);
            disembarkedPassengers.add(passenger);
        }

        this.psgList.removeAll(disembarkedPassengers);
    }
    // Represents the process of refueling the plane. 
    //It waits for the fuel truck to be available, locks the Plane class, performs the refueling process, and releases the lock.
    public void refuel(Airport airportobj) {
        if (airportobj.isFuelTruckFree()) {
            AirportUtil.printActivity("Waiting for the Fuel Truck");
        }

        boolean flag = false;
        //looping continues until thread gets locked
        while (!flag) {
            //this is class level lock. 
            //Only one instance of the plane class thread will run.
            synchronized (Plane.class) {
                if (!airportobj.isFuelTruckFree()) {
                    AirportUtil.printActivity("Refuelling Truck is Connected");
                    airportobj.occupyFuelTruck();
                    AirportUtil.printActivity("Refuelling Plane");
                    AirportUtil.sleepThread(150);
                    AirportUtil.printActivity("Completed Refuelling!");
                    airportobj.freeFuelTruck();
                    this.RefuelCompleted = true;
                    flag = true;
                }

            }
        }

    }
    
    //Confirms the plane for takeoff and performs the takeoff process.
    public void confirmAndTakeOff(Airport airportobj) {
        boolean flag = false;
        while (!flag) {
            //Another class level lock
            //Only one instance of aeroplane class at a time will allow to enter this block using thread.
            synchronized (Plane.class) {
                if (this.isPassgEmbAndDisembCompleted && this.RefuelCompleted && !airportobj.isRunwayFree()) {
                    AirportUtil.printActivity("is ready for takeoff!");
                    AirportUtil.printActivity("approaching the runway");
                    airportobj.occupyRunway();
                    AirportUtil.sleepThread(200);
                    airportobj.getGates().remove(this);
                    airportobj.freeRunway();
                    AirportUtil.printActivity("takeoff successful.");
                    AirportUtil.print("Airport Gates occupancy count After takeoff = " + airportobj.getGates().size());
                    flag = true;
                }

            }
        }

    }
    
    // requests for emergency landing
    public void setEmergencyLandingRequested(boolean emergencyLandingRequested) {
        this.emergencyLandingRequested = emergencyLandingRequested;
    }
    

    //Fills the plane with a random number of passengers.
    private void fillPassengersPlane(boolean printActivity) {
        Random r = new Random();
        int lowerLimitPass = 1;
        int higherLimitPass = 51;
        int noOfPassInPlane = r.nextInt(higherLimitPass - lowerLimitPass) + lowerLimitPass;
        for (int i = 0; i < noOfPassInPlane; i++) {
            Passenger passg = new Passenger();
            psgList.add(new Passenger());
            if (printActivity) {
                AirportUtil.printActivity("Embarking " + passg.getPassengerId() + " Now!");
                AirportUtil.sleepThread(50);
            }
        }

        if (printActivity) {
            AirportUtil.printActivity("New Passengers Boarded successfully, Passenger count = " + this.psgList.size());
        }
    }
    
  


    public boolean emergencyLandingRequested() {
        return emergencyLandingRequested;
    }

    public String getPlaneID() {
        return PlaneId;
    }

    public boolean isIsThreadVisited() {
        return isThreadVisited;
    }

    public void setIsThreadVisited(boolean isThreadVisited) {
        this.isThreadVisited = isThreadVisited;
    }

    public int getGateNoAssigned() {
        return gateNoAssigned;
    }

    public void setGateNoAssigned(int gateNoAssigned) {
        this.gateNoAssigned = gateNoAssigned;
    }

    public float getPlaneWaitingTime() {
        return planeWaitingTime;
    }

    public void setPlaneWaitingTime(float planeWaitingTime) {
        this.planeWaitingTime = planeWaitingTime;
    }

    public List<Passenger> getPsgList() {
        return psgList;
    }
    @Override
    public String toString() {
        return "Plane[" + "PlaneID = " + PlaneId + ", Passenger List = " + psgList.size() + ']';
    }
}