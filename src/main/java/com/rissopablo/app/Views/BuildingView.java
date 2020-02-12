/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rissopablo.app.Views;

import com.rissopablo.app.Controllers.BuildingPresenter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author kamikaze
 */
public class BuildingView extends JFrame {
    
    public JButton addPerson;
    public JButton addPacket;
    public JButton start;
    public JButton stop;
    
    private JLabel currentPublicFloor;
    private JLabel currentFreightFloor;
    
    public JTextField personW;
    public JTextField packetW;
    public JTextField personCF;
    public JTextField packetCF;
    public JTextField personTF;
    public JTextField packetTF;
    public JTextField cardNumber;
    
    private JPanel pane;
    private BuildingPresenter presenter;
    
    public BuildingView(BuildingPresenter presenter) {
        super("Kindalab Test");
        pane = new JPanel();
        this.presenter = presenter;
        
        createLabels();
        createTextFields();
        createButtons();
        
        layoutComponents();
        pack();
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void createButtons() {
        addPerson = new JButton("Add person");
        addPerson.addActionListener(presenter);
        addPacket = new JButton("Add packet");
        addPacket.addActionListener(presenter);
        start = new JButton("Start elevators");
        start.addActionListener(presenter);
        stop = new JButton("Stop elevators");
        stop.addActionListener(presenter);
        stop.setEnabled(false);
    }
    
    private void createLabels() {
        currentPublicFloor = new JLabel("Floor: 0");
        currentFreightFloor = new JLabel("Floor: 0");
    }
    
    private void createTextFields() {
        personW = new JTextField(3);
        packetW = new JTextField(3);
        personCF = new JTextField(3);
        packetCF = new JTextField(3);
        personTF = new JTextField(3);
        packetTF = new JTextField(3);
        cardNumber = new JTextField(3);
    }
    
    private void layoutComponents() {
        pane.setLayout(new BorderLayout());
        
        JLabel publicE = new JLabel("Public Elevator");
        JLabel freight = new JLabel("Freight Elevator");
        JPanel titlesPane = new JPanel();
        titlesPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        titlesPane.add(publicE);
        titlesPane.add(freight);
        pane.add(titlesPane, BorderLayout.NORTH);
        
        JPanel elevatorsPanel = new JPanel();
        elevatorsPanel.setLayout(new GridLayout(3, 2, 30, 10));
        JPanel wfPerPanel = new JPanel();
        wfPerPanel.setLayout(new GridLayout(4, 2, 10, 0));
        JPanel wfPacPanel = new JPanel();
        wfPacPanel.setLayout(new GridLayout(4, 2, 10, 0));
        JLabel weightPerson = new JLabel("Weight");
        JLabel weightPack = new JLabel("Weight");
        JLabel cFloorPerson = new JLabel("Curr Floor");
        JLabel cFloorPack = new JLabel("Curr Floor");
        JLabel tFloorPerson = new JLabel("Tgt Floor");
        JLabel tFloorPack = new JLabel("Tgt Floor");
        JLabel cardNumberTxt = new JLabel("Card num");
        wfPerPanel.add(weightPerson);
        wfPerPanel.add(personW);
        wfPacPanel.add(weightPack);
        wfPacPanel.add(packetW);
        wfPerPanel.add(cFloorPerson);
        wfPerPanel.add(personCF);
        wfPacPanel.add(cFloorPack);
        wfPacPanel.add(packetCF);
        wfPerPanel.add(tFloorPerson);
        wfPerPanel.add(personTF);
        wfPacPanel.add(tFloorPack);
        wfPacPanel.add(packetTF);
        wfPerPanel.add(cardNumberTxt);
        wfPerPanel.add(cardNumber);
        elevatorsPanel.add(currentPublicFloor);
        elevatorsPanel.add(currentFreightFloor);
        elevatorsPanel.add(wfPerPanel);
        elevatorsPanel.add(wfPacPanel);
        elevatorsPanel.add(addPerson);
        elevatorsPanel.add(addPacket);
        pane.add(elevatorsPanel, BorderLayout.CENTER);
        
        JPanel btnPane = new JPanel();
        btnPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnPane.add(start);
        btnPane.add(stop);
        pane.add(btnPane, BorderLayout.SOUTH);
        
        add(pane);
    }
    
    public void setFloorForPublicElev(int floor) {
        currentPublicFloor.setText("Floor: "+ floor);
    }
    
    public void setFloorForFreightElev(int floor) {
        currentFreightFloor.setText("Floor: "+ floor);
    }
}
