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
    private GraphicHandler currentPanel;
    public enum STATE{
        MENU,MAP,GAME
    }
    private Container container; // Frame Container ( Main Container )
    private STATE state;
    public GamePanel(Container container){
        this.container = container;

        container.add(this);

        state = STATE.MENU;
        menu_panel = new com.thrumania.src.menu.Panel(this,map_panel,game_panel);
    }
    @Override
    protected void paintComponent(Graphics g) {
        currentPanel.render(g);
    }

    private void updateComponents() {

        while (container.getComponentCount() > 1)  // for removing all components from frame except main panel
            container.remove(1);

        currentPanel.addGameComponent(container);
    }

    public void changeState(STATE state){
        this.state = state;
        switch(state){
            case MENU:
                currentPanel = menu_panel;
                break;
            case MAP:
                currentPanel = map_panel;
                break;
            default:    // It's for GAME state
                currentPanel = game_panel;
        }
        updateComponents();
    }
}
