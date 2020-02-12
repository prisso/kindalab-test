/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Interfaces;

import com.rissopablo.app.Models.Elevator;

/**
 *
 * @author kamikaze
 */
public interface Observer {
    
    /**
     * @param o observable which have sent the info
     * @param i represent the current index (current floor in this case) 
     */
    public void update(Elevator e, int i);
    
}
