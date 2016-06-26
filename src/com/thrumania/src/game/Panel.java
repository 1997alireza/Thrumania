package com.thrumania.src.game;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.objects.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

/**
 * Created by AliReza on 23/05/2016.
 */
public class Panel  implements GraphicHandler{
    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.menu.Panel menuPanel; private com.thrumania.src.mapEditor.Panel mapPanel;


    private boolean isRunningGame;

    public Panel(com.thrumania.src.draw.GamePanel drawPanel,com.thrumania.src.menu.Panel menuPanel,com.thrumania.src.mapEditor.Panel mapPanel){
        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.mapPanel = mapPanel;

        isRunningGame = true;

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void updateComponents() {

    }


    @Override
    public void repaint() {
        drawPanel.repaint();
    }


    @Override
    public void mouseClick(MouseEvent e) {

    }

    @Override
    public void mouseEnter(int x, int y) {

    }

    @Override
    public void mouseExit(int x, int y) {

    }
    @Override
    public void mouseDrag(MouseEvent e) {

    }

    @Override
    public void mousePress( int x, int y) {

    }
    @Override
    public void mouseRelease(int x, int y) {

    }

    @Override
    public void mouseWheelMove(MouseWheelEvent e) {

    }


    @Override
    public void pressButton(int code) { // inja bar asase codi ke be buttone tuye panele payino rast dadim migim chikar kone

    }


    public boolean isRunningGame() {
        return isRunningGame;
    }

    public void setRunningGame(boolean runningGame) {
        isRunningGame = runningGame;
    }


}
