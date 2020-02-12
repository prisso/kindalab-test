/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Controllers;

import com.rissopablo.app.Models.Building;
import com.rissopablo.app.Models.Packet;
import com.rissopablo.app.Models.Person;
import com.rissopablo.app.Views.BuildingView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author kamikaze
 */
public class BuildingPresenter implements ActionListener {
    
    private final BuildingView view;
    private final Building building;
    
    public BuildingPresenter() {
        view = new BuildingView(this);
        building = new Building(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton obj = (JButton)e.getSource(); 
        if (obj == view.start) {
            building.start();
            view.start.setEnabled(false);
            view.stop.setEnabled(true);
            
        } else if (obj == view.stop) {
            building.stop();
            view.stop.setEnabled(false);
            view.start.setEnabled(true);
            
        } else if (obj == view.addPerson) {
            if (view.personW.getText().isEmpty() || view.personCF.getText().isEmpty() 
                    || view.personTF.getText().isEmpty())
                return;
            int weight = Integer.parseInt(view.personW.getText());
            int cFloor = Integer.parseInt(view.personCF.getText());
            int tFloor = Integer.parseInt(view.personTF.getText());
            String cardNum = view.cardNumber.getText();
            String toparse = (!cardNum.isEmpty()) ? cardNum : "0";
            int card = Integer.parseInt(toparse);
            
            Person person = new Person(weight, cFloor, tFloor);
            person.setCardNumber(card);
            building.addNewObject((com.rissopablo.app.Models.Object)person);
            view.stop.setEnabled(true);
            
        } else if (obj == view.addPacket) {
            if (view.packetW.getText().isEmpty() || view.packetCF.getText().isEmpty()
                    || view.packetTF.getText().isEmpty())
                return;
            int weight = Integer.parseInt(view.packetW.getText());
            int cFloor = Integer.parseInt(view.packetCF.getText());
            int tFloor = Integer.parseInt(view.packetTF.getText());
            
            Packet pack = new Packet(weight, cFloor, tFloor);
            building.addNewObject((com.rissopablo.app.Models.Object)pack);
            view.stop.setEnabled(true);
        }    
    }
    
    public void setFreightFloor(int floor) {
        view.setFloorForFreightElev(floor);
    }
    
    public void setPublicFloor(int floor) {
        view.setFloorForPublicElev(floor);
    }
}
