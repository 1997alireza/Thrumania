package com.thrumania.src.mapEditor;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Panel implements GraphicHandler , MouseListener,MouseMotionListener{
    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.menu.Panel menuPanel;
    LinkedList<LinkedList<Constant.GROUND>> ground ;    // ground.get(i).get(j) -> i and j matching with graphical axises   //  per tile
    LinkedList<ArrayList<Object>> object; // 3 Objects per array : Integer x , Integer y , Constant.OBJECT  //  x and y matching with graphical axises  //  per 4pixel

    private int x0 = 0 , y0 = 0;
    private int maxXMap=1000 , maxYMap=1000;//   MUUUSSTT CHNAGEEE    >>>>>     depend on zoom scale and map size && minus screen size
    public Panel(com.thrumania.src.draw.GamePanel drawPanel, com.thrumania.src.menu.Panel menuPanel, LinkedList<LinkedList<Constant.GROUND>> ground , LinkedList<ArrayList<Object>> object ){
        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.drawPanel.map_panel = this;
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
    public void mouseClick(int x, int y) {

    }


    @Override
    public void mouseEnter(int x, int y) {

    }

    @Override
    public void mouseExit(int x, int y) {

    }
    @Override
    public void mouseDrag( int x, int y) {

    }

    @Override
    public void mousePress( int x, int y) {

    }
    @Override
    public void mouseRelease(int x, int y) {

    }


    @Override
    public void pressButton(int code) {
            // inja code un dokme kenariaro migire va bar asase un migi harki chikar kone .
    }


    /// < for mouse listener ...
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent g ) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        while(MouseInfo.getPointerInfo().getLocation().getX()<=5 && x0>0)
        {
            x0--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {}
        }

        while(MouseInfo.getPointerInfo().getLocation().getX()>=Constant.Screen_Width-5 && x0<maxXMap)
        {
            x0++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {}
        }

        while(MouseInfo.getPointerInfo().getLocation().getY()<=5 && y0>0)
        {
            y0--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {}
        }

        while(MouseInfo.getPointerInfo().getLocation().getY()>=Constant.Screen_Height-5 && y0<maxYMap)
        {
            y0++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {}
        }

        System.out.println(x0+","+y0);

    }
    /// ... />



    // on click on "finish" change state to menu and give the "ground" and "object" to menu.Panel.editMap(...)
}
