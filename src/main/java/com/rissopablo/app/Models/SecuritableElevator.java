/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Models;

import com.rissopablo.app.Controllers.ElevatorControlSystem;
import com.rissopablo.app.Controllers.ElevatorSecuritySystem;
import com.rissopablo.app.Interfaces.Securitable;
import java.util.ArrayList;

/**
 *
 * @author kamikaze
 */
public class SecuritableElevator extends Elevator {
    
    private final ElevatorSecuritySystem securitySystem;
    
    public SecuritableElevator(ElevatorType type,
            ElevatorControlSystem control, ElevatorSecuritySystem security) {
        super(type, control);
        this.securitySystem = security;
    }

    @Override
    protected ArrayList<Object> addObjects(ArrayList<Object> objs) {
        ArrayList<Object> couldntEnter = new ArrayList<>();
        int index = 0;
        while (index < objs.size() && !isShutOff) {
            Object o = objs.get(index);
            if (securitySystem.isSecuredFloorNumber(o.targetFloor)) {
                Securitable secureObj;
                if (o instanceof Securitable) {
                    secureObj = (Securitable)o;
                    if (!securitySystem.checkCardNumber(secureObj.getCardNumber())) {
                        System.out.println("Couldn't enter person due to security system");
                        couldntEnter.add(o);
                        index++;
                        continue;
                    }
                }
            }
            currentWeight += o.weight;
            if (currentWeight > maxWeight) {
                isShutOff = true;
                break;
            } else {
                objects.add(o);
                System.out.println("Current weight: " + currentWeight + ", Max: " + maxWeight);
                System.out.println("Leaving person at floor: " + o.targetFloor);
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
}
