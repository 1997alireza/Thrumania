package com.thrumania.src.mapEditor;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Panel implements GraphicHandler{
    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.menu.Panel menuPanel;
    LinkedList<LinkedList<Constant.GROUND>> ground ;    // ground.get(i).get(j) -> i and j matching with graphical axises   //  per tile
    LinkedList<ArrayList<Object>> object; // 3 Objects per array : Integer x , Integer y , Constant.OBJECT  //  x and y matching with graphical axises  //  per 4pixel
    public Panel(com.thrumania.src.draw.GamePanel drawPanel, com.thrumania.src.menu.Panel menuPanel, LinkedList<LinkedList<Constant.GROUND>> ground , LinkedList<ArrayList<Object>> object ){
        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;

        drawPanel.changeState(GamePanel.STATE.MAP);
    }

    @Override
    public void render(Graphics g) {


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
    public void pressButton(int code) {
            // inja code un dokme kenariaro migire va bar asase un migi harki chikar kone .
    }

    // on click on "finish" change state to menu and give the "ground" and "object" to menu.Panel.editMap(...)
}
