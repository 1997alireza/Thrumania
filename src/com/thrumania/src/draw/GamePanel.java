package com.thrumania.src.draw;

import javax.swing.*;

import com.sun.prism.*;
import com.thrumania.src.*;
import com.thrumania.src.objects.GameObject;
import com.thrumania.src.Tools.PlaySound;
import res.values.Constant;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by AliReza on 23/05/2016.
 */
public class GamePanel extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener {
    public com.thrumania.src.menu.Panel menu_panel;
    public com.thrumania.src.mapEditor.Panel map_panel;
    public com.thrumania.src.game.Panel game_panel;
    private GraphicHandler currentPanel;


    private int lastMouseX , lastMouseY;
    public enum STATE{
        MENU,MAP,GAME
    }
    private STATE lastState;
    private boolean isJustChangedState = false;
    public GamePanel(){
        setLayout(null);

        addMouseListener(this);
        addMouseMotionListener(this);

        menu_panel = new com.thrumania.src.menu.Panel(this,map_panel,game_panel);

        lastState = STATE.MAP;

        changeState(STATE.MENU);

        lastMouseX = MouseInfo.getPointerInfo().getLocation().x;
        lastMouseY = MouseInfo.getPointerInfo().getLocation().y;

        addMouseWheelListener(this);

    }
    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage BI = new BufferedImage(Constant.Screen_Width,Constant.Screen_Height,BufferedImage.TYPE_3BYTE_BGR);
        Graphics BG = BI.getGraphics();
        if(isJustChangedState){
            BG.setColor(Color.BLACK);
            BG.fillRect(0,0,Constant.Screen_Width,Constant.Screen_Height);
            isJustChangedState = false;
        }
        currentPanel.render(BG);
        g.drawImage(BI ,0 ,0 ,null);
    }


    private void updateComponents(){
        currentPanel.updateComponents();
    }
    public void changeState(STATE state){

        if(lastState != STATE.MENU && state == STATE.MENU){

            if(currentPanel instanceof com.thrumania.src.mapEditor.Panel)
                ((com.thrumania.src.mapEditor.Panel)currentPanel).setRunningMap(false);
            else if(currentPanel instanceof  com.thrumania.src.game.Panel)
                ((com.thrumania.src.game.Panel)currentPanel).setRunningGame(false);


            PlaySound.stopAll();
            PlaySound menu_background_sound = new PlaySound("src/res/sounds/menu background.wav");
            menu_background_sound.play(true);

            isJustChangedState = true;

        }
        else if(lastState != STATE.MAP && state == STATE.MAP){
            PlaySound.stopAll();
            //PlaySound menu_background_sound = new PlaySound("src/res/sounds/...?.wav");
            //menu_background_sound.play(true);

            isJustChangedState = true;
        }
        else if(lastState != STATE.GAME && state == STATE.GAME){
            PlaySound.stopAll();
            //PlaySound menu_background_sound = new PlaySound("src/res/sounds/...?.wav");
           // menu_background_sound.play(true);

            isJustChangedState = true;
        }

        switch(state){
            case MENU:
                currentPanel = menu_panel;
                map_panel.setRunningMap(false);
                map_panel = null;
                lastState = STATE.MENU;
                break;
            case MAP:
                currentPanel = map_panel;
                lastState = STATE.MAP;
                break;
            default:    // It's for GAME state
                currentPanel = game_panel;
                menu_panel = null;
                map_panel.setRunningMap(false);
                map_panel = null;
                lastState = STATE.GAME;
        }

        currentPanel.updateComponents();
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        currentPanel.mouseClick(e);
        repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentPanel.mousePress(e.getX(),e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentPanel.mouseRelease(e.getX(),e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPanel.mouseDrag(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentPanel.mouseExit(lastMouseX,lastMouseY);
        currentPanel.mouseEnter(e.getX(),e.getY());
        lastMouseX = e.getX();
        lastMouseY = e.getY();

        repaint();

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        currentPanel.mouseWheelMove(e);
    }

}
