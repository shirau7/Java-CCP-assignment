package airportsim;

import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Objects;


//main file
public class AirportSim {
    


    public static void main(String[] args) throws InterruptedException {
        
        List<Plane> emergencyPlanes = new ArrayList<>(); 
        
        //integer represnting gates at airport
        int noOfGatesAtAirport = 3;
        //integer representing intial number of planes
        int initialNoOfPlanes = 6;

        //initialize airport object
        Airport airportObj = new Airport(noOfGatesAtAirport);
        AirportUtil.print(airportObj);
        AirportUtil.sleepThread(1000);

        //initialize control tower object
        ControlTower CTobj = new ControlTower(airportObj);
        AirportUtil.print(CTobj);
        AirportUtil.sleepThread(1000);



    // 6 Planes approaching airport simulation
    for (int i = 1; i <= initialNoOfPlanes; i++) {
        // create threads of control tower objects
        Runnable r1 = () -> {
            // creating plane object
            Plane plane = new Plane();

            // randomly decide if emergency landing is requested
            Random random = new Random();
            boolean isEmergencyLandingRequested = random.nextBoolean();
            if (isEmergencyLandingRequested) {
                plane.setEmergencyLandingRequested(true);
                emergencyPlanes.add(plane); // add the emergency plane to the list
            }

            // print the status of the plane
            String approachMessage = "Approaching " + airportObj.getAirportName();
            if (plane.emergencyLandingRequested()) {
                approachMessage += " For Emergency Landing ";
            }
            AirportUtil.print("CT-Thread = " + plane.getPlaneID() + " " + approachMessage);

            // calling method of ControlTower class.
            // synchronization due to the same object used to call the threads.
            CTobj.planeCT(plane);
        };

        new Thread(r1, "CT-Thread").start();
    }

        // print the emergency planes list
    for (Plane plane : emergencyPlanes) {
        String approachMessage = "Approaching Airport " + airportObj.getAirportName() + " Emergency Landing Requested";
        AirportUtil.print("CT-Thread = " + Thread.currentThread().getName() + " " + plane.getPlaneID() + " " + approachMessage);
    }



            //plane is being processed which is near the gate
            boolean flagActivity = false;
            while (true) {

                for (int i = 0; i < airportObj.getGates().size(); i++) {

                    //loops the different objects from plane class (list of gates, and corresponding planes to the gate)
                    Plane planeObj = airportObj.getGates().get(i);

                    //stops from the same plane object which is preoccupied
                    if (!planeObj.isIsThreadVisited()) {

                        planeObj.setIsThreadVisited(true);
                        //create Runnable for plane. 
                        Runnable aeroplaneAtivity = () -> {
                            planeObj.activityAfterOccupingGate(airportObj);
                        };
                        //create Runnable for refuel truck. 
                        Runnable refuelActivity = () -> {
                            planeObj.refuel(airportObj);
                        };
                        //the display of plane pilot and passenger activity occuring concurrently
                        Thread t1 = new Thread(aeroplaneAtivity, "Plane-Passenger-Thread : " + planeObj.getPlaneID());
                        Thread t2 = new Thread(refuelActivity, "Plane-Pilot-Thread :" + planeObj.getPlaneID());
                        t1.start();
                        t2.start();
                    }
                    flagActivity = true;
                    AirportUtil.sleepThread(500);
                }

                //exits loop after completeion of all thread tasks
                if (airportObj.getGates().isEmpty() && flagActivity) {
                    CTobj.printStatistics();
                    break;
                }

                // After each iteration of the loop, it pauses the execution for 250 milliseconds
                AirportUtil.sleepThread(250);
            }

        }

    }