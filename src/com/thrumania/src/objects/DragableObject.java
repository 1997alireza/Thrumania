package com.thrumania.src.objects;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.Tools.Division;
import com.thrumania.src.Tools.PlaySound;
import com.thrumania.src.gameSpace.Area;
import com.thrumania.src.gameSpace.Circle;

import javax.sound.sampled.FloatControl;
import java.awt.*;
/**
 * Created by AliReza on 21/06/2016.
 */
public class DragableObject implements GameObject {
    public static final int SOUND_OPTION = 1;
    private boolean isSelected = false;
    private int x,y,width,height;
    private Image image;
    private String text;
    private GraphicHandler panel;
    private int minX,maxX;
    private int code;
    public boolean isSelected() {
        return isSelected;
    }

    public DragableObject(GraphicHandler panel,int code ,String text, int x, int y,int minX,int maxX , int width, int height, Image image)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.panel = panel;
        this.minX = minX;
        this.maxX = maxX;
        this.code=code;
    }

    public void select(boolean selected) {
        isSelected = selected;
    }



    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
    public void changeX(int x){
        if(x>=minX && x<=maxX) {
            this.x = x;
            panel.repaint();
            switch (this.code){
                case SOUND_OPTION:
                    // (731,1151) -> (-40,0)
                    int volume = Division.division((x-731)*40 ,420) -40;
                    PlaySound.changeVolume(volume);
                    break;
            }
        }
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
        boolean t1 = x>=minX && x<=maxX;
        boolean t2 = y<=this.y+50 && y>=this.y-50 ;
        return t1 && t2;
    }

    @Override
    public void mouseClicked() {}

    @Override
    public void mouseEntered() {}

    @Override
    public void mouseExited() {}

    @Override
    public void setDraggingOnIt(boolean isDraggingOnIt) {

    }
    @Override
    public boolean isDraggingOnIt() {
        return false;
    }

}
