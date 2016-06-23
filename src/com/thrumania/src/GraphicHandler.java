package com.thrumania.src;

import com.thrumania.src.Tools.PlaySound;
import com.thrumania.src.objects.GameObject;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by AliReza on 25/05/2016.
 */
public interface GraphicHandler {
    void render(Graphics g) ;
    void updateComponents();
    void mouseClick(int x,int y);
    void mouseEnter(int x,int y);
    void mouseExit(int x,int y);
    void mouseDrag(int x,int y);
    void mousePress(int x,int y);
    void mouseRelease(int x,int y);
    void pressButton(int code);
    void repaint();
}

