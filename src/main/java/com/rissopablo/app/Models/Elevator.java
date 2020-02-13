package com.rissopablo.app.Models;

import com.rissopablo.app.Controllers.ElevatorControlSystem;
import java.util.ArrayList;

public class Elevator {

    public int maxWeight;
    public int currentWeight = 0;
    public int currentFloor = 0;
    public boolean isShutOff = false;
    public ElevatorType type;
    
    protected ArrayList<Object> objects;
    protected ElevatorControlSystem control;
    
    public Elevator(ElevatorType type, ElevatorControlSystem control) {

        this.maxWeight = type.value();
        this.objects = new ArrayList<>();
        this.control = control;
    }

    public ArrayList<Object> openDoor(ArrayList<Object> newObjects) {
        if (!objects.isEmpty())
            removeObjects();
        if (!newObjects.isEmpty()) {
            return addObjects(newObjects);
        }
        return null;
    }
    
    protected ArrayList<Object> addObjects(ArrayList<Object> objs) {
        ArrayList<Object> couldntEnter = new ArrayList<>();
        
        int index = 0;
        while (index < objs.size() && !isShutOff) {
            Object o = objs.get(index);
            if ((currentWeight + o.weight) > maxWeight) {
                System.out.println("Elevator should shut off");
                isShutOff = true;
                break;
            } else {
                objects.add(o);
                currentWeight += o.weight;
                System.out.println("Current weight: " + currentWeight + ", Max: " + maxWeight);
                control.callElevatorFromFloor(o.targetFloor);
                index++;
            }
        }
        
        if (isShutOff) {
            for (int i=index; i < objs.size(); i++) {
                couldntEnter.add(objs.get(i));
            }
        }
        
        return couldntEnter;
    }

    protected void removeObjects() {
        int index = 0;
        System.out.println("Leaving objects at floor: " + currentFloor);
        do {
            Object o = objects.get(index);
            if (o.targetFloor != currentFloor) {
                index++;
            } else {
                currentWeight -= o.weight;
                objects.remove(o);
            }
        } while (index < objects.size());
    }
}
