package com.company;

import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame(){
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setVisible(true);
        //sprawia że lewy górny róg okienka pojawi się centralnie na środku ekranu
        this.setLocationRelativeTo(null);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();



    }






}
