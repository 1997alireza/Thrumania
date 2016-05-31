package com.thrumania.src.objects;

import java.awt.*;
import java.awt.event.*;

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
    private com.thrumania.src.menu.Panel menu_panel;
    public GameButton(com.thrumania.src.menu.Panel menu_panel, String text,int code,int x, int y, int width, int height, Image image){
        this(menu_panel,text,code,x,y,width,height,image,image);
    }
    public GameButton(com.thrumania.src.menu.Panel menu_panel, String text,int code,int x,int y,int width,int height,Image image,Image image_MouseHover){     //Have a image for when mouse enter the component
        this.menu_panel = menu_panel;
        this.text = text;
        this.code = code;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.image_MouseHover = image_MouseHover;
        currentImage = image;
    }

    @Override
    public void mouseClicked(){     //For action
        menu_panel.pressButton(code);
    }

    public void mouseEntered() {    //For change image : entering
        currentImage = image_MouseHover;
    }

    public void mouseExited() {     //For change image : exiting
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

}
