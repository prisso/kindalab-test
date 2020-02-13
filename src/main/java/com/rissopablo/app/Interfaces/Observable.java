/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Interfaces;

/**
 *
 * @author kamikaze
 */
public interface Observable {
    
    public void registerObserver(Observer o);
    
    /**
     *
     * @param o is the observer to be removed
     * @return true if observer was successfully removed
     */
    public boolean removeObserver(Observer o);
    
    /**
     *
     */
    public void notifyObservers(int f);
    
}
