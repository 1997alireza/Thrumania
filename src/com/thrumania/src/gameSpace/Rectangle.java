package com.thrumania.src.gameSpace;

/**
 * Created by AliReza on 01/06/2016.
 */
public class Rectangle extends Area {
    private int x;
    private int y;

    private int width;
    private int height;
    public Rectangle(int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isInArea(int x, int y) {
        boolean tX = (x>=this.x) && (x<=this.x+this.width);
        boolean tY = (y>=this.y) && (y<=this.y+this.height);
        return (tX && tY);
    }

    //for test :

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

}
