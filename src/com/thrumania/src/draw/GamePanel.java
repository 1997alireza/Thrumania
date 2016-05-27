package com.thrumania.src.draw;

import javax.swing.*;
import com.thrumania.src.*;

import java.awt.*;

/**
 * Created by AliReza on 23/05/2016.
 */
public class GamePanel extends JPanel {
    public com.thrumania.src.menu.Panel menu_panel;
    public com.thrumania.src.mapEditor.Panel map_panel;
    public com.thrumania.src.game.Panel game_panel;
    public enum STATE{
        MENU,MAP,GAME
    }

    public STATE state;

    public GamePanel(){
        state = STATE.MENU;
        menu_panel = new com.thrumania.src.menu.Panel(this,map_panel,game_panel);
    }
    @Override
    protected void paintComponent(Graphics g) {
        if(state == STATE.MENU) {
            menu_panel.render(g);

        }
        else if( state == STATE.MAP){

        }
    }
}
