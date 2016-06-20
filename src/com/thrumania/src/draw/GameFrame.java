package com.thrumania.src.draw;
/**
 * Created by AliReza on 23/05/2016.
 */
import res.values.Constant;

import java.awt.*;
import javax.swing.JFrame;
public class GameFrame extends JFrame {

    GamePanel panel;

    public GameFrame(){

        setLayout(null);
        setLocation(0,0);
        setSize(Constant.Screen_Dimension);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new GamePanel();
        panel.setSize(Constant.Screen_Dimension);
        this.getContentPane().add(panel);


        setVisible(true);
    }
}