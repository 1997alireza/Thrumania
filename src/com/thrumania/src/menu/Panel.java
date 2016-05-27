package com.thrumania.src.menu;
/**
 * Created by AliReza on 23/05/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sun.prism.*;
import com.thrumania.src.GraphicHandler;
import res.values.*;
public class Panel implements ActionListener , GraphicHandler{

    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.mapEditor.Panel mapPanel;private com.thrumania.src.game.Panel gamePanel;
    private enum STATE{ // add states to this and to codeToState function and the buttons and background to render function
        START(0),
        TEST(1);
        int code;

        STATE(int code){
            this.code = code;
        }
        //states must have number -> for automata states
    }

    private STATE codeToState(int code){
        switch (code){
            case 0 : return STATE.START;
            case 1 : return STATE.TEST;
            default: return STATE.START;
        }
    }
    private STATE state;
    public Panel(com.thrumania.src.draw.GamePanel drawPanel,com.thrumania.src.mapEditor.Panel mapPanel,com.thrumania.src.game.Panel gamePanel){
        this.drawPanel = drawPanel;
        this.mapPanel = mapPanel;
        this.gamePanel = gamePanel;
        state = STATE.START;
    }
    @Override
    public void render(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.Screen_Width, Constant.Screen_Height);
        switch (state){
            case START:
                g.drawImage(new ImageIcon("src/res/images/map editor button.png").getImage(), Constant.Screen_Width / 10, Constant.Screen_Height / 16, null);
                break;
            case TEST:

                break;

        }
    }
    @Override
    public void addGameComponent (Container container){

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int code = ((menuButton)e.getSource()).getCode();
        int stateCode = action(code);
        state = codeToState(stateCode);
    }
    private int action(int code){
        // must have automata and choose what to do
        // automata : actions -> buttons code , states -> in witch section
        return 1;// for clicking on one button
    }
}

class menuButton extends JButton{
    private int code;
    private String value;
    public menuButton(int code , String value){
        super(value);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
