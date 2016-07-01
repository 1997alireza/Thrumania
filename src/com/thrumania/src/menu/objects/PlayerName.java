package com.thrumania.src.menu.objects;

import com.thrumania.src.gameSpace.Rectangle;
import com.thrumania.src.objects.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by AliReza on 30/06/2016.
 */
public class PlayerName implements GameObject {
    private int x,y,width,height;



    private BufferedImage BI;
    public PlayerName(String name , int x , int y , int width , int height , boolean isLight){
        this.x = x;
        this.y = y;
        this.width = width;
        this. height = height;
        BI = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics g = BI.getGraphics();
        g.setColor(new Color(74, 74, 74,50));
        g.fillRect(0,0,width,height);
        Color c = (isLight) ? Color.WHITE : new Color(136, 136, 136) ;
        g.setColor(c);
        g.setFont(new Font("Bodoni MT",g.getFont().getStyle(),30));
        g.drawString(name,( width - ((Graphics2D)g).getFontMetrics().stringWidth(name) ) / 2,-12 + height);


    }


    @Override
    public Image getImage() {
        return BI;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isInArea(int x, int y) {
        return false;
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
    public void setDraggingOnIt(boolean isDraggingOnIt) {

    }

    @Override
    public boolean isDraggingOnIt() {
        return false;
    }
}
