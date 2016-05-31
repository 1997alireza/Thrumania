package com.thrumania.src.objects;

import java.awt.*;

/**
 * Created by AliReza on 31/05/2016.
 */
public interface GameObject {
    Image getImage();
    int getX() ;
    int getY() ;
    int getWidth();
    int getHeight();

    void mouseClicked();

}
