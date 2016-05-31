package com.thrumania.src.draw;

import javax.swing.*;
import com.thrumania.src.*;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by AliReza on 23/05/2016.
 */
public class GamePanel extends JPanel implements MouseListener,MouseMotionListener {
    public com.thrumania.src.menu.Panel menu_panel;
    public com.thrumania.src.mapEditor.Panel map_panel;
    public com.thrumania.src.game.Panel game_panel;
    private GraphicHandler currentPanel;

    public enum STATE{
        MENU,MAP,GAME
    }
    private STATE state;
    public GamePanel(){
        setLayout(null);

        state = STATE.MENU;
        menu_panel = new com.thrumania.src.menu.Panel(this,map_panel,game_panel);
        currentPanel = menu_panel;
    }
    @Override
    protected void paintComponent(Graphics g) {
        currentPanel.render(g);
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
    }



    @Override
    public void mouseClicked(MouseEvent e) {

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

    }

}
