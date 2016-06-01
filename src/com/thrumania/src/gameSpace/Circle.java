package com.thrumania.src.gameSpace;

/**
 * Created by AliReza on 01/06/2016.
 */
public class Circle extends Area {
    int x,y,r;
    public Circle(int x,int y,int r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public boolean isInArea(int x, int y) {
        int dx = this.x - x;
        int dy = this.y - y;
        return ( Math.pow(r,2) >= Math.pow(dx,2) + Math.pow(dy,2) );
    }
}
