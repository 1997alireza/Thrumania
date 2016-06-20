package com.thrumania.src.objects;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by AliReza on 29/05/2016.
 */
public class StaticObject implements GameObject{

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void mouseClicked() {

    }

    @Override
    public void mouseEntered() {

    }

    @Override
    public void mouseExited() {

    }

    @Override
    public boolean isInArea(int x, int y) {
        return false; // must return area.isInArea
    }
}
