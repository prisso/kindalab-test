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

    private void moveElevatorFrom(int cFloor, int newFloor) {
        System.out.println("Moving elevator from floor: " + cFloor);
        System.out.println("Moving elevator to floor: " + newFloor);

        elevator.targetFloor = newFloor;

        int nextFloor = cFloor;
        int interFloor = 0;
        do {
            interFloor = getNextFloor(nextFloor);
            if (newFloor != interFloor) {
                moveElevatorFrom(nextFloor, interFloor);
                nextFloor = interFloor;
            }
            if (direction == Direction.UP)
                nextFloor++;
            if (direction == Direction.DOWN)
                nextFloor--;
            try {
                Thread.sleep(1000);
                notifyObservers(nextFloor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (nextFloor != newFloor);

        currentFloor = newFloor;
        elevator.currentFloor = newFloor;
        floorsToGo.put(newFloor, Boolean.FALSE);
        notifyObservers(newFloor);
    }

    private int getNextFloor(int cFloor) {
        int nextFloor = cFloor;
        if (direction == Direction.DOWN) {
            nextFloor = checkFloorsDown(cFloor);
            if (nextFloor >= -1*numberOfBasements)
                return nextFloor;
            nextFloor = checkFloorsUp(cFloor);
            if (nextFloor < numberOfFloors) {
                direction = Direction.UP;
                return nextFloor;
            }
            nextFloor = cFloor;
        }

        if (direction == Direction.UP) {
            nextFloor = checkFloorsUp(cFloor);
            if (nextFloor < numberOfFloors)
                return nextFloor;
            nextFloor = checkFloorsDown(cFloor);
            if (nextFloor >= -1*numberOfBasements) {
                direction = Direction.DOWN;
                return nextFloor;
            }
            nextFloor = cFloor;
        }

        return nextFloor;
    }

    private int checkFloorsDown(int cFloor) {
        int nextFloor = cFloor - 1;
        while (nextFloor >= -1 * numberOfBasements && !floorsToGo.get(nextFloor)) {
            nextFloor -= 1;
        }
        return nextFloor;
    }

    private int checkFloorsUp(int cFloor) {
        int nextFloor = cFloor + 1;
        while (nextFloor < numberOfFloors && !floorsToGo.get(nextFloor)) {
            nextFloor += 1;
        }
        return nextFloor;
    }
    
    @Override
    public void run() {
        int currFloor, toFloor = getNextFloor(currentFloor);
        moveElevatorFrom(currentFloor, toFloor);
        while (!elevator.isShutOff && direction != Direction.STILL) {
            currFloor = toFloor;
            toFloor = getNextFloor(currFloor);
            if (toFloor == currFloor)
                direction = Direction.STILL;
            else
                moveElevatorFrom(currFloor, toFloor);
        }
        System.out.println("Elevator has stopped!");
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
    public void notifyObservers(int floor) {
        for (Observer obs : observers)
            obs.update(this.elevator, floor);
    }
}
