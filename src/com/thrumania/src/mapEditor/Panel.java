package com.thrumania.src.mapEditor;

import com.thrumania.src.GraphicHandler;
import res.values.Constant;

import javax.swing.*;
import java.awt.*;

public class Panel implements GraphicHandler{
    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.menu.Panel menuPanel; private com.thrumania.src.game.Panel gamePanel;
    public Panel(com.thrumania.src.draw.GamePanel drawPanel,com.thrumania.src.menu.Panel menuPanel,com.thrumania.src.game.Panel gamePanel){
        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.gamePanel = gamePanel;
    }

    public static void main(String[] args) {
        System.out.println("asd");
    }
    @Override
    public void render(Graphics g) {


    }
    @Override
    public void addGameComponent (Container container){

    }
}
