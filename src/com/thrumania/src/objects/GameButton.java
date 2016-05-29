package com.thrumania.src.objects;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by AliReza on 29/05/2016.
 * For menu buttons and map editor and game side
 */
public class GameButton extends Component implements MouseListener{

    private Image image , image_MouseHover;
    private Image currentImage;
    private int x , y;
    private int width , height;

    public GameButton(int x, int y, int width, int height, Image image){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        image_MouseHover = image;
        currentImage = image;
    }
    public GameButton(int x,int y,int width,int height,Image image,Image image_MouseHover){     //Have a image for when mouse enter the component
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.image_MouseHover = image_MouseHover;
        currentImage = image;
    }




    @Override
    public void mouseClicked(MouseEvent e) {    //For action

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {    //For change image : entering
        currentImage = image_MouseHover;
    }

    @Override
    public void mouseExited(MouseEvent e) {     //For change image : exiting
        currentImage = image;
    }





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
