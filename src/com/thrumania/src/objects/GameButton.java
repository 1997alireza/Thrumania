package com.thrumania.src.objects;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.gameSpace.*;
import com.thrumania.src.gameSpace.Rectangle;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

/**
 * Created by AliReza on 29/05/2016.
 * For menu buttons and map editor and game side
 */
public class GameButton implements GameObject{

    private Image image , image_MouseHover;
    private Image currentImage;
    private int x , y;
    private int width , height;
    private String text;
    private int code;
    private GraphicHandler panel;
    private Area area;
    public GameButton(com.thrumania.src.menu.Panel menu_panel, String text,int code,int x, int y, int width, int height, Image image){
        this(menu_panel,text,code,x,y,width,height,image,image);
    }
    public GameButton(GraphicHandler panel, String text,int code,int x,int y,int width,int height,Image image,Image image_MouseHover){     //Have a image for when mouse enter the component
        this.panel = panel;
        this.text = text;
        this.code = code;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.image_MouseHover = image_MouseHover;
        currentImage = image;
        area = new Rectangle(x,y,width,height);
    }

    @Override
    public void mouseClicked(){     //For action
        panel.pressButton(code);
    }

    @Override
    public void mouseEntered() {
        currentImage = image_MouseHover;
    }

    @Override
    public void mouseExited() {
        currentImage = image;
    }


    @Override
    public Image getImage() {
        return currentImage;
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
        return area.isInArea(x,y);
    }

}
