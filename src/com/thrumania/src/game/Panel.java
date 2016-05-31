package com.thrumania.src.game;

import com.thrumania.src.GraphicHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AliReza on 23/05/2016.
 */
public class Panel  implements GraphicHandler{
    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.menu.Panel menuPanel; private com.thrumania.src.mapEditor.Panel mapPanel;
    public Panel(com.thrumania.src.draw.GamePanel drawPanel,com.thrumania.src.menu.Panel menuPanel,com.thrumania.src.mapEditor.Panel mapPanel){
        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.mapPanel = mapPanel;

    }

    @Override
    public void render(Graphics g) {

    }

}
