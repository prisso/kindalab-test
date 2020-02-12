/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Controllers;

import java.util.ArrayList;

/**
 *
 * @author kamikaze
 */
public class ElevatorSecuritySystem {
    
    private final ArrayList<Integer> securedFloors;
    private static final int NUM_OF_VALID_CARDS = 10;
    private final int[] validCardNumber;
    
    public ElevatorSecuritySystem(ArrayList<Integer> securedFloors) {
        this.validCardNumber = new int[]{9, 13, 15, 23, 27, 33, 39, 43, 45, 50};
        //this.generateCards();
        this.securedFloors = securedFloors;
    }
    
    public boolean checkCardNumber(int cardNumber) {
        for (int i = 0; i < NUM_OF_VALID_CARDS; i++) {
            if ( cardNumber == validCardNumber[i])
                return true;
        }
        return false;
    }
    
    public boolean isSecuredFloorNumber(int index) {
        return this.securedFloors.indexOf(index) >= 0;
    }
    
    private void generateCards() {
        /*this.validCardNumber = new ArrayList<>();
        for (int i=0; i<20; i++) {
            int value = (int)(Math.random() * (60 + 1));
            int index = this.validCardNumber.indexOf(value);
            if (index < 0) {
                this.validCardNumber.add(value);
            }
        }*/
    }
}
