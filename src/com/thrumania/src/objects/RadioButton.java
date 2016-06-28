package com.thrumania.src.objects;

import com.thrumania.src.gameSpace.Area;
import com.thrumania.src.gameSpace.Circle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by AliReza on 28/06/2016.
 */
public class RadioButton implements GameObject {
    private Image defaultImage , selectedImage;

    private int x;
    private int y;
    private int diameter;

    private Area area;

    private int value;

    private Radio parent;

    private boolean isSelect = false;


    public void setParent(Radio parent){
        this.parent = parent;
    }


    public RadioButton(int value ,int x,int y,int diameter){
        this.value = value;
        this.x = x;
        this.y = y;
        this.diameter = diameter;

        area = new Circle( x + diameter/2 , y + diameter/2 , diameter/2) ;



        try
        {
            BufferedImage numImage = ImageIO.read(new File("src/res/images/menu/radio button/"+value+".png"));
            BufferedImage default_BI = ImageIO.read(new File("src/res/images/menu/radio button/default.png"));
            BufferedImage selected_BI = ImageIO.read(new File("src/res/images/menu/radio button/selected.png"));

            Graphics g = default_BI.getGraphics();
            g.drawImage(numImage, 0, 0, null);
            defaultImage = default_BI;

            selectedImage = selected_BI;

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void select(){
        isSelect = true;
    }

    public void unSelect(){
        isSelect = false;
    }


    public Image getImage(){
        return isSelect?selectedImage:defaultImage;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return diameter;
    }

    @Override
    public int getHeight() {
        return diameter;
    }

    @Override
    public boolean isInArea(int x, int y) {
        return area.isInArea(x,y);
    }

    @Override
    public void mouseClicked() {
        parent.select(value);

    }

    @Override
    public void mouseEntered() {

    }

    @Override
    public void mouseExited() {

    }

}
