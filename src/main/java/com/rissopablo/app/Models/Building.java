package com.rissopablo.app.Models;

import com.rissopablo.app.Controllers.BuildingPresenter;
import java.util.ArrayList;
import com.rissopablo.app.Controllers.ElevatorControlSystem;
import com.rissopablo.app.Controllers.ElevatorSecuritySystem;
import com.rissopablo.app.Interfaces.Observer;
import com.rissopablo.app.Interfaces.Securitable;

public class Building implements Observer {

    public static final int NUMBER_OF_FLOORS = 50;
    public static final int NUMBER_OF_BASEMENTS = 1;

    public ArrayList<Packet> packetsOnFloors = new ArrayList<>();
    public ArrayList<Person> personsOnFloors = new ArrayList<>();

    private final ElevatorControlSystem publicControl;
    private final ElevatorControlSystem freightControl;
    
    private final BuildingPresenter presenter;
    
    public Building(BuildingPresenter presenter) {
        this.presenter = presenter;
        
        freightControl = new ElevatorControlSystem(NUMBER_OF_FLOORS,
                NUMBER_OF_BASEMENTS);
        Elevator freight = new Elevator(ElevatorType.FREIGHT, freightControl);
        freightControl.setElevator(freight);

        ArrayList<Integer> securedFloors = new ArrayList<>();
        securedFloors.add(-1);
        securedFloors.add(49);
        ElevatorSecuritySystem security = new ElevatorSecuritySystem(securedFloors);
        publicControl = new ElevatorControlSystem(NUMBER_OF_FLOORS, 
                NUMBER_OF_BASEMENTS);
        SecuritableElevator publicE = new SecuritableElevator(ElevatorType.PUBLIC,
                publicControl, security);
        publicControl.setElevator((Elevator)publicE);
        
        freightControl.registerObserver(this);
        publicControl.registerObserver(this);
    }
    
    public void start() {
        freightControl.registerObserver(this);
        publicControl.registerObserver(this);
        freightControl.start();
        publicControl.start();
    }
    
    public void stop() {
        freightControl.removeObserver(this);
        publicControl.removeObserver(this);
        freightControl.stop();
        publicControl.stop();
    }
    
    public boolean addNewObject(Object o) {
        boolean check = checkFloorNumbers(o.currentFloor, o.targetFloor);
        if (check) {
            if (o instanceof Securitable) {
                personsOnFloors.add((Person)o);
                publicControl.callElevatorFromFloor(o.currentFloor);
            } else {
                System.out.println("Adding a new packet ...");
                packetsOnFloors.add((Packet)o);
                freightControl.callElevatorFromFloor(o.currentFloor);
            }
        }
        return check;
    }
    
    @Override
    public void update(Elevator elevator, int floor) {
        if (elevator.type == ElevatorType.PUBLIC) {
            presenter.setPublicFloor(floor);
        } else {
            presenter.setFreightFloor(floor);
        }
        if (elevator.willNotOpenDoor())
            return;

        ArrayList<Object> objsToIn = new ArrayList<>();
        int index = 0;
        if (elevator.type == ElevatorType.PUBLIC) {
            while(!personsOnFloors.isEmpty() && index < personsOnFloors.size()) {
                Person p = personsOnFloors.get(index);
                if (p.currentFloor == floor) {
                    objsToIn.add((Object)p);
                    personsOnFloors.remove(index);
                } else {
                    index++;
                }
            }
        } else {
            while(!packetsOnFloors.isEmpty() && index < packetsOnFloors.size()) {
                Packet p = packetsOnFloors.get(index);
                if (p.currentFloor == floor) {
                    objsToIn.add((Object)p);
                    packetsOnFloors.remove(index);
                } else {
                    index++;
                }
            }
        }
        ArrayList<Object> couldntEnter = elevator.openDoor(objsToIn);
        
        if (couldntEnter != null && !couldntEnter.isEmpty()) {
            for (Object obj : couldntEnter) {
                addNewObject(obj);
            }
        }
    }
    
    private boolean checkFloorNumbers(int c, int t) {
        return (c != t) && (c >= -1*NUMBER_OF_BASEMENTS || c < NUMBER_OF_FLOORS)
                && (t >= -1*NUMBER_OF_BASEMENTS || t < NUMBER_OF_FLOORS );
    }
}
