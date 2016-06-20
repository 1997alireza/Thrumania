package com.thrumania.src.objects;

import com.thrumania.src.gameSpace.Area;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by AliReza on 31/05/2016.
 */
public interface GameObject {
    Image getImage();
    int getX() ;
    int getY() ;
    int getWidth();
    int getHeight();

    boolean isInArea(int x, int y);

    void mouseClicked();
    void mouseEntered();
    void mouseExited();

}
