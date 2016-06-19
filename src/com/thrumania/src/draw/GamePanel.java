package com.thrumania.src.draw;

import javax.swing.*;
import com.thrumania.src.*;
import com.thrumania.src.objects.GameObject;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

/**
 * Created by AliReza on 23/05/2016.
 */
public class GamePanel extends JPanel implements MouseListener,MouseMotionListener {
    public com.thrumania.src.menu.Panel menu_panel;
    public com.thrumania.src.mapEditor.Panel map_panel;
    public com.thrumania.src.game.Panel game_panel;
    private GraphicHandler currentPanel;

    private LinkedList<GameObject> panelObjects;

    private int lastMouseX , lastMouseY;
    public enum STATE{
        MENU,MAP,GAME
    }
    private STATE state;
    public GamePanel(){
        setLayout(null);

        addMouseListener(this);
        addMouseMotionListener(this);

        menu_panel = new com.thrumania.src.menu.Panel(this,map_panel,game_panel);
        changeState(STATE.MENU);

        lastMouseX = MouseInfo.getPointerInfo().getLocation().x;
        lastMouseY = MouseInfo.getPointerInfo().getLocation().y;

    }
    @Override
    protected void paintComponent(Graphics g) {
        currentPanel.render(g);
    }//avaz she be in :: objecta moqeye changeState remoall va sepas add shan tuye panele marbut. render faqat objectaye mojud ro mikshe va ezafe kam nmikone


    private void updateComponents(){
        currentPanel.updateComponents();
    }
    public void changeState(STATE state){
        switch(state){
            case MENU:
                currentPanel = menu_panel;
                map_panel = null;
                break;
            case MAP:
                currentPanel = map_panel;
                break;
            default:    // It's for GAME state
                currentPanel = game_panel;
                menu_panel = null;
                map_panel = null;
        }

        currentPanel.updateComponents();
        repaint();
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        currentPanel.mouseClick(e.getX() , e.getY());

    }

    @Override
    public void mousePressed(MouseEvent e) {

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
        currentPanel.mouseExit(lastMouseX,lastMouseY);
        currentPanel.mouseEnter(e.getX(),e.getY());
        lastMouseX = e.getX();
        lastMouseY = e.getY();


    }

}
