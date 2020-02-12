/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Models;

/**
 *
 * @author kamikaze
 */
public class Object {
    
    public int weight;
    
    public int currentFloor;
    
    public int targetFloor;
    
    public Object(int weight, int currentFloor, int targetFloor) {
        
        this.weight = weight;
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
    }
}
