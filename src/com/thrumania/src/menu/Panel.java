package com.thrumania.src.menu;
/**
 * Created by AliReza on 23/05/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import com.sun.prism.*;
import com.thrumania.src.GraphicHandler;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.objects.GameButton;
import com.thrumania.src.objects.GameObject;
import res.values.*;
public class Panel implements GraphicHandler{

    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.mapEditor.Panel mapPanel;private com.thrumania.src.game.Panel gamePanel;

    private int state ;



    private LinkedList <GameObject> gameObjects;
    private final int [][] PT= {
            {110,1,2,6,-1,-1,-1,-1,-1,-1,-1},
            {0,-1,-1,-1,-1,-1,-1,-1,8,-1,101},
            {0,-1,-1,-1,-1,3,-1,5,-1,-1,-1},
            {2,-1,-1,-1,-1,-1,4,-1,8,-1,-1},
            {3,-1,-1,-1,-1,-1,-1,-1,-1,-1,102},
            {2,-1,-1,-1,-1,-1,-1,-1,-1,-1,103},
            {0,-1,-1,-1,7,-1,-1,-1,-1,-1,-1},
            {6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,3,-1}            //state = PT[state][code]
    };
    private final int eventNumber_ForFinishingMapEditing_InAutomata = 9;

    private LinkedList<LinkedList<Constant.GROUND>> ground ;
    private LinkedList<ArrayList<Object>> object;

    public Panel(com.thrumania.src.draw.GamePanel drawPanel,com.thrumania.src.mapEditor.Panel mapPanel,com.thrumania.src.game.Panel gamePanel){
        this.drawPanel = drawPanel;
        this.mapPanel = mapPanel;
        this.gamePanel = gamePanel;
        state = 0;
        gameObjects = new LinkedList<GameObject>();
    }
    @Override
    public void render(Graphics g) {

        //default background here
        g.setColor(Color.black);
        g.drawRect(50,50,Constant.Screen_Width-100,Constant.Screen_Height-100);
        switch (state){
            case 0:
                gameObjects.add(new GameButton(this,"Single Player",100,100,100,300,100,(new ImageIcon("src/res/images/1.png")).getImage() , (new ImageIcon("src/res/images/2.png")).getImage()));
                break;
            case 1:
                break;


        }

        for(GameObject GO : gameObjects){
            g.drawImage(GO.getImage(),GO.getX(),GO.getY(),GO.getWidth(),GO.getHeight(),null);
        }
    }

    @Override
    public void pressButton(int code){
        state = PT[state][code];
        switch (state){
            case 8:
                mapPanel = new com.thrumania.src.mapEditor.Panel(drawPanel,this,ground,object);
                drawPanel.changeState(GamePanel.STATE.MAP);
                break;
            case 9:
                mapPanel = new com.thrumania.src.mapEditor.Panel(drawPanel,this,ground,object);
                drawPanel.changeState(GamePanel.STATE.MAP);
                break;
            default:
                drawPanel.changeState(GamePanel.STATE.MENU);
        }

    }

    public void editMap(LinkedList<LinkedList<Constant.GROUND>> ground , LinkedList<ArrayList<Object> /* 3 Objects per array : Integer x , Integer y , Constant.OBJECT*/> object){
        this.ground = ground;
        this.object = object;
        pressButton(eventNumber_ForFinishingMapEditing_InAutomata);
    }

    @Override
    public void mouseClick(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseClicked();
                break;
            }
        }

    }

    @Override
    public void mouseEnter(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseClicked();
                break;
            }
        }

    }

    @Override
    public void mouseExit(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseClicked();
                break;
            }
        }

    }


}
