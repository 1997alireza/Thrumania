package com.thrumania.src;

import com.thrumania.src.Tools.PlaySound;
import com.thrumania.src.objects.GameObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

/**
 * Created by AliReza on 25/05/2016.
 */
public interface GraphicHandler {
    void render(Graphics g) ;
    void updateComponents();
    void mouseClick(MouseEvent e);
    void mouseEnter(int x,int y);
    void mouseExit(int x,int y);
    void mouseDrag(MouseEvent e);
    void mousePress(int x,int y);
    void mouseRelease(int x,int y);
    void mouseWheelMove(MouseWheelEvent e);
    void pressButton(int code);
    void repaint();
}

