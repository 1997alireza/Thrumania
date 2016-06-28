package com.thrumania.src.objects;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.menu.Panel;

import java.awt.*;
import java.util.Map;

/**
 * Created by AliReza on 23/06/2016.
 */
public class DrawToolButton extends GameButton {

    private Image firstImage , secondImage;

    private boolean isDraggingOnIt;

    private boolean isSelected;
    public DrawToolButton(GraphicHandler panel, String text, int code, int x, int y, int width, int height, Image image, Image image_MouseHover) {
        super(panel, text, code, x, y, width, height, image, image_MouseHover);
        firstImage = image;
        secondImage = image_MouseHover;

        isSelected = false;
        isDraggingOnIt = false;

    }

    @Override
    public void mouseClicked(){     //For action
        panel.pressButton(code);
        isSelected = !isSelected;
        super.image = (isSelected) ? secondImage : firstImage ;
        if(!isSelected)
            ((com.thrumania.src.mapEditor.Panel)panel).unSelectDrawTool();
        else
            ((com.thrumania.src.mapEditor.Panel)panel).selectDrawTool(code);
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void select(boolean selected) {
        isSelected = selected;

        super.image = (isSelected) ? secondImage : firstImage ;
        super.currentImage =  (isSelected) ? secondImage : firstImage ;
    }


    @Override
    public void setDraggingOnIt(boolean isDraggingOnIt) {
        this.isDraggingOnIt = isDraggingOnIt;

    }
    @Override
    public boolean isDraggingOnIt() {
        return isDraggingOnIt;
    }

}
