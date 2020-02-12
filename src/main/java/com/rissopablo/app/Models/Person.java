/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Models;

import com.rissopablo.app.Interfaces.Securitable;

/**
 *
 * @author kamikaze
 */
public class Person extends Object implements Securitable {

    private int cardNumber;
    
    public Person(int weight, int currentFloor, int targetFloor) {
        super(weight, currentFloor, targetFloor);
        this.cardNumber = 0;
    }
    
    public void setCardNumber(int cardNumber) {
        if (cardNumber != 0) {
            this.cardNumber = cardNumber;
        }
    }
    
    @Override
    public boolean haveCard() {
        return this.cardNumber != 0;
    }

    @Override
    public int getCardNumber() {
        return this.cardNumber;
    }
}
