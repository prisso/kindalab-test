/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Controllers;

import com.rissopablo.app.Interfaces.Observable;
import com.rissopablo.app.Interfaces.Observer;
import com.rissopablo.app.Models.Direction;
import com.rissopablo.app.Models.Elevator;
import com.rissopablo.app.Models.ElevatorType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kamikaze
 */
public class ElevatorControlSystem implements Runnable, Observable {
    
    private final int numberOfFloors;
    private final int numberOfBasements;
    
    private Thread thread;
    private Elevator elevator;
    private Direction direction = Direction.STILL;
    private Integer currentFloor;
    private final ArrayList<Observer> observers;
    private HashMap<Integer, Boolean> floorsToGo;
    
    public ElevatorControlSystem(int numberFloor, int numberBases) {
        this.currentFloor = 0;
        this.numberOfFloors = numberFloor;
        this.numberOfBasements = numberBases;
        this.observers = new ArrayList<>();
        this.thread = new Thread(this);
        
        generateMap();
    }
    
    protected final void generateMap() {
        floorsToGo = new HashMap<>();
        for (int i = -1*numberOfBasements; i < numberOfFloors; i++) {
            floorsToGo.put(i, Boolean.FALSE);
        }
    }
    
    public void callElevatorFromFloor(int floorNumber) {
        floorsToGo.put(floorNumber, Boolean.TRUE);
        if (direction == Direction.STILL)
            this.start();
    }
    
    public void setElevator(Elevator elev) {
        elevator = elev;
    }
    
    public ElevatorType getElevatorType() {
        return elevator != null? elevator.type : ElevatorType.NOT_DEF;
    }
    
    public void start() {
        elevator.isShutOff = false;
        direction = Direction.UP;
        if (thread == null)
            thread = new Thread(this);
        if (!thread.isAlive())
            thread.start();
    }
    
    public void stop() {
        elevator.isShutOff = true;
        direction = Direction.STILL; 
    }

    private void moveElevatorTo(int newFloor) {
       //Emulate time to get the target floor
        try {
            Thread.sleep(250*(Math.abs(currentFloor-newFloor)+1));
        } catch (InterruptedException ex) {
            Logger.getLogger(ElevatorControlSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentFloor = newFloor;
        elevator.currentFloor = newFloor;
        floorsToGo.put(newFloor, Boolean.FALSE);
        notifyObservers();
    }
    
    private int getNextFloor() {
        int floorToGo = currentFloor;
        boolean isWaiting;
        do {
            if (direction == Direction.UP)
                floorToGo += 1;
            else if (direction == Direction.DOWN)
                floorToGo -= 1;

            if (floorToGo >= numberOfFloors) {
                direction = Direction.DOWN;
                floorToGo -= 1;
            } else if (floorToGo < -1*numberOfBasements){
                direction = Direction.UP;
                floorToGo += 1;
            }
            //System.out.println("Floor to GO: " + floorToGo);
            isWaiting = floorsToGo.get(floorToGo);
        } while (!isWaiting && !elevator.isShutOff);
        if (isWaiting)
            return floorToGo;
        else
            return numberOfBasements+numberOfFloors;
    }
    
    @Override
    public void run() {
        while (!elevator.isShutOff) {
            int nextFloor = getNextFloor();
            System.out.println("Moving elevator from floor: " + currentFloor);
            System.out.println("Moving elevator to floor: " + nextFloor);
            if (nextFloor != numberOfBasements+numberOfFloors) {
               moveElevatorTo(nextFloor);
            }
        }
        direction = Direction.STILL;
        thread = null;
    }

    @Override
    public void registerObserver(Observer o) {
        int index = observers.indexOf(o);
        if (index < 0)
            observers.add(o);
    }

    @Override
    public boolean removeObserver(Observer o) {
        return observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers)
            obs.update(this.elevator, currentFloor);
    }
}
