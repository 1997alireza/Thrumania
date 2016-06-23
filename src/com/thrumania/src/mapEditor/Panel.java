package com.thrumania.src.mapEditor;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.gameSpace.Area;
import com.thrumania.src.objects.DrawToolButton;
import com.thrumania.src.objects.GameButton;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.swing.*;
import javax.xml.transform.sax.SAXSource;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Panel implements GraphicHandler {
    private com.thrumania.src.draw.GamePanel drawPanel;
    private com.thrumania.src.menu.Panel menuPanel;

    private LinkedList<LinkedList< Object[/* Constant.GROUND , Constant.OBJECT*/] >> ground ;

    private LinkedList <GameObject> gameObjects;
    // another linkedlist for staticObjects



    private int selectedDrawTool = -1;
    private float scale = 1;
    private static int x0, y0;
    private final Dimension minMapSize = new Dimension(1920 * 2, 1200 * 2);    // height / width = 8 / 5
    private static Dimension mapSize;  // height / width = 8 / 5
    private static int maxXMap, maxYMap;



    private static boolean isRunningMap;


    public Panel(com.thrumania.src.draw.GamePanel drawPanel, com.thrumania.src.menu.Panel menuPanel,LinkedList<LinkedList< Object[/* Constant.GROUND , Constant.OBJECT*/] >> ground ) {


        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.drawPanel.map_panel = this;

        isRunningMap = true;


        mapSize = new Dimension(1920 * 2, 1200 * 2);
        maxXMap = mapSize.width - 1920;
        maxYMap = mapSize.height - 1080 + Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/;

        scale = 1;
        x0 = 0;
        y0 = 0;

        this.ground = ground;


        new Thread(this::moveMap).start();


    }


    @Override
    public void render(Graphics g) {

        for (int i = 0; i < Constant.NUM_OF_SEA_IN_EACH_ROW / scale; i++)
            for (int j = 0; j < Constant.NUM_OF_SEA_IN_EACH_COLUMN / scale; j++) {
                g.drawImage(new ImageIcon("src/res/images/ground/sea.jpg").getImage(), -(x0 % Constant.MIN_WIDTH_OF_EACH_SEA) + i * Constant.MIN_WIDTH_OF_EACH_SEA, -(y0 % Constant.MIN_HEIGHT_OF_EACH_SEA) + j * Constant.MIN_HEIGHT_OF_EACH_SEA, Constant.MIN_WIDTH_OF_EACH_SEA, Constant.MIN_HEIGHT_OF_EACH_SEA, null);
            }

        for(int i = x0 / Constant.MIN_WIDTH_OF_EACH_GROUND ; i < (1920 + x0)/Constant.MIN_WIDTH_OF_EACH_GROUND - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND +1; i++)
            for(int j = y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND ; j < (1080-250 + y0)/Constant.MIN_HEIGHT_OF_EACH_GROUND +1; j++)
            {
                Constant.GROUND thisGround = (Constant.GROUND)ground.get(i).get(j)[0];
                switch (thisGround){
                    case LOWLAND:
                        g.drawImage(new ImageIcon("src/res/images/ground/lowLand/0-0.png").getImage() , -x0%Constant.MIN_WIDTH_OF_EACH_GROUND + (i-x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND , -y0%Constant.MIN_HEIGHT_OF_EACH_GROUND + (j-y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND ,Constant.MIN_WIDTH_OF_EACH_GROUND,Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        break;
                    case HIGHLAND:
                        break;
                }


            }

        ImageIcon bottomFrame = new ImageIcon("src/res/images/map/frame.png");
        g.drawImage(bottomFrame.getImage(),0,0,1920,1080,null);

        for(GameObject GO : gameObjects){
            g.drawImage(GO.getImage(),GO.getX(),GO.getY(),GO.getWidth(),GO.getHeight(),null);
        }

    }

    @Override
    public void updateComponents() {
        gameObjects = new LinkedList<GameObject>();

        ImageIcon load_1 = new ImageIcon("src/res/images/map/button/load.1.png");
        ImageIcon load_2 = new ImageIcon("src/res/images/map/button/load.2.png");
        gameObjects.add(new GameButton(this,"load",1,1498,844,67,67, load_1.getImage(), load_2.getImage()));

        ImageIcon zoom_in_1 = new ImageIcon("src/res/images/map/button/zoom in 1.png");
        ImageIcon zoom_in_2 = new ImageIcon("src/res/images/map/button/zoom in 2.png");
        gameObjects.add(new GameButton(this,"zoom in",2,1604,844,67,67, zoom_in_1.getImage(), zoom_in_2.getImage()));

        ImageIcon zoom_out_1 = new ImageIcon("src/res/images/map/button/zoom out 1.png");
        ImageIcon zoom_out_2 = new ImageIcon("src/res/images/map/button/zoom out 2.png");
        gameObjects.add(new GameButton(this,"zoom_out",3,1707,844,67,67, zoom_out_1.getImage(), zoom_out_2.getImage()));

        ImageIcon save_1 = new ImageIcon("src/res/images/map/button/save.1.png");
        ImageIcon save_2 = new ImageIcon("src/res/images/map/button/save.2.png");
        gameObjects.add(new GameButton(this,"save",4,1812,844,67,67, save_1.getImage(), save_2.getImage()));

        ImageIcon undo_1 = new ImageIcon("src/res/images/map/button/undo.1.png");
        ImageIcon undo_2 = new ImageIcon("src/res/images/map/button/undo.2.png");
        gameObjects.add(new GameButton(this,"undo",5,1498,917,67,67 ,undo_1.getImage(), undo_2.getImage()));

        ImageIcon redo_1 = new ImageIcon("src/res/images/map/button/redo.1.png");
        ImageIcon redo_2 = new ImageIcon("src/res/images/map/button/redo.2.png");
        gameObjects.add(new GameButton(this,"redo",6,1812,917,67,67, redo_1.getImage(), redo_2.getImage()));

        ImageIcon preview_1 = new ImageIcon("src/res/images/map/button/season preview 1.png");
        ImageIcon preview_2 = new ImageIcon("src/res/images/map/button/season preview 2.png");
        gameObjects.add(new GameButton(this,"preview",7,1498,991,67,67 ,preview_1.getImage(), preview_2.getImage()));

        ImageIcon erase_1 = new ImageIcon("src/res/images/map/button/erase 1.png");
        ImageIcon erase_2 = new ImageIcon("src/res/images/map/button/erase 2.png");
        gameObjects.add(new DrawToolButton(this,"erase",8,1604,991,67,67 ,erase_1.getImage(), erase_2.getImage()));

        ImageIcon erase_all_1 = new ImageIcon("src/res/images/map/button/erase all 1.png");
        ImageIcon erase_all_2 = new ImageIcon("src/res/images/map/button/erase all 2.png");
        gameObjects.add(new GameButton(this,"erase all",9,1707,991,67,67 ,erase_all_1.getImage(), erase_all_2.getImage()));

        ImageIcon exit_1 = new ImageIcon("src/res/images/map/button/exit 1.png");
        ImageIcon exit_2 = new ImageIcon("src/res/images/map/button/exit 2.png");
        gameObjects.add(new GameButton(this,"exit",10,1812,991,67,67, exit_1.getImage(), exit_2.getImage()));

        ImageIcon up_1 = new ImageIcon("src/res/images/map/button/speed_up.1.png");
        ImageIcon up_2 = new ImageIcon("src/res/images/map/button/speed_up.2.png");
        gameObjects.add(new GameButton(this,"up",11,1301,874,51,51, up_1.getImage(), up_2.getImage()));

        ImageIcon down_1 = new ImageIcon("src/res/images/map/button/speed_down.1.png");
        ImageIcon down_2 = new ImageIcon("src/res/images/map/button/speed_down.2.png");
        gameObjects.add(new GameButton(this,"down",12,1301,1016,51,51, down_1.getImage(), down_2.getImage()));

        ImageIcon left_1 = new ImageIcon("src/res/images/map/button/speed_left.1.png");
        ImageIcon left_2 = new ImageIcon("src/res/images/map/button/speed_left.2.png");
        gameObjects.add(new GameButton(this,"left",13,1237,944,51,51, left_1.getImage(), left_2.getImage()));

        ImageIcon right_1 = new ImageIcon("src/res/images/map/button/speed_right.1.png");
        ImageIcon right_2 = new ImageIcon("src/res/images/map/button/speed_right.2.png");
        gameObjects.add(new GameButton(this,"right",14,1363,944,51,51, right_1.getImage(), right_2.getImage()));

        ImageIcon decrease_1 = new ImageIcon("src/res/images/map/button/decrease.1.png");
        ImageIcon decrease_2 = new ImageIcon("src/res/images/map/button/decrease.2.png");
        gameObjects.add(new GameButton(this,"decrease",15,1363,1016,51,51, decrease_1.getImage(), decrease_2.getImage()));

        ImageIcon increase_1 = new ImageIcon("src/res/images/map/button/increase.1.png");
        ImageIcon increase_2 = new ImageIcon("src/res/images/map/button/increase.2.png");
        gameObjects.add(new GameButton(this,"increase",16,1237,874,51,51, increase_1.getImage(), increase_2.getImage()));




        ImageIcon stone_1 = new ImageIcon("src/res/images/map/button/stone-pile.1.png");
        ImageIcon stone_2 = new ImageIcon("src/res/images/map/button/stone-pile.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",102,665,890,77,77, stone_1.getImage(), stone_2.getImage()));

        ImageIcon peaks_1 = new ImageIcon("src/res/images/map/button/peaks.1.png");
        ImageIcon peaks_2 = new ImageIcon("src/res/images/map/button/peaks.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",103,794,890,77,77, peaks_1.getImage(), peaks_2.getImage()));


        ImageIcon farm_1 = new ImageIcon("src/res/images/map/button/farm.1.png");
        ImageIcon farm_2 = new ImageIcon("src/res/images/map/button/farm.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",111,536,890,77,77, farm_1.getImage(), farm_2.getImage()));

        ImageIcon mine_iron_1 = new ImageIcon("src/res/images/map/button/mine-wagon.1.png");
        ImageIcon mine_iron_2 = new ImageIcon("src/res/images/map/button/mine-wagon.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",112,923,890,77,77, mine_iron_1.getImage(), mine_iron_2.getImage()));

        ImageIcon mine_gold_1 = new ImageIcon("src/res/images/map/button/minerals.1.png");
        ImageIcon mine_gold_2 = new ImageIcon("src/res/images/map/button/minerals.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",113,1052,890,77,77, mine_gold_1.getImage(), mine_gold_2.getImage()));

        ImageIcon at_sea_1 = new ImageIcon("src/res/images/map/button/at-sea.1.png");
        ImageIcon at_sea_2 = new ImageIcon("src/res/images/map/button/at-sea.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",101,536,990,77,77, at_sea_1.getImage(), at_sea_2.getImage()));

        ImageIcon beech_1  = new ImageIcon("src/res/images/map/button/beech.1.png");
        ImageIcon beech_2 = new ImageIcon("src/res/images/map/button/beech.2.png");
        gameObjects.add(new DrawToolButton(this,"beech",114,665,990,77,77, beech_1.getImage(), beech_2.getImage()));

        ImageIcon pine_tree_1 = new ImageIcon("src/res/images/map/button/pine-tree.1.png");
        ImageIcon pine_tree_2 = new ImageIcon("src/res/images/map/button/pine-tree.2.png");
        gameObjects.add(new DrawToolButton(this,"pine tree",115,794,990,77,77, pine_tree_1.getImage(), pine_tree_2.getImage()));

        ImageIcon salmon_1 = new ImageIcon("src/res/images/map/button/salmon.1.png");
        ImageIcon salmon_2 = new ImageIcon("src/res/images/map/button/salmon.2.png");
        gameObjects.add(new DrawToolButton(this,"salmon",116,932,990,77,77, salmon_1.getImage(), salmon_2.getImage()));

        ImageIcon piranha_1 = new ImageIcon("src/res/images/map/button/piranha.1.png");
        ImageIcon piranha_2 = new ImageIcon("src/res/images/map/button/piranha.2.png");
        gameObjects.add(new DrawToolButton(this,"piranha",117,1052,990,77,77, piranha_1.getImage(), piranha_2.getImage()));

    }


    @Override
    public void repaint() {
        drawPanel.repaint();
    }

    @Override
    public void mouseClick(int x, int y) {
        if(y>=1080 - Constant.BOTTOM_FRAME_HEIGHT) {
            for (GameObject GO : gameObjects) {
                if (GO.isInArea(x, y)) {
                    GO.mouseClicked();
                    break;
                }
            }
        }

        else{
            int i = (x+x0) / Constant.MIN_WIDTH_OF_EACH_GROUND;
            int j = (y+y0) / Constant.MIN_HEIGHT_OF_EACH_GROUND;
            Object [] room = new Object[2];
            switch (selectedDrawTool){
                case (101) :
                    room[0] = Constant.GROUND.SEA;
                    room[1] = null;
                    ground.get(i).set(j , room);
                    break;
                case(102):
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = null;
                    ground.get(i).set(j , room);
                    break;
                case(103):
                    room[0] = Constant.GROUND.HIGHLAND;
                    room[1] = null;
                    ground.get(i).set(j , room);
                    break;

            }

        }

    }

    @Override
    public void mouseEnter(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseEntered();
                break;
            }
        }

    }

    @Override
    public void mouseExit(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseExited();
                break;
            }
        }

    }

    @Override
    public void mouseDrag(int x, int y) {

    }

    @Override
    public void mousePress(int x, int y) {

    }

    @Override
    public void mouseRelease(int x, int y) {

    }


    @Override
    public void pressButton(int code) {
        switch(code){
           // case :
        }
    }


    private void moveMap() {
        while (isRunningMap) {

            int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
            if (x <= 5 && x0 > 0) {
                x0--;
            } else if (x >= Constant.Screen_Width - 5 && x0 < maxXMap) {
                x0++;
            }

            if (y <= 5 && y0 > 0) {
                y0--;
            } else if (y >= Constant.Screen_Height - 5 && y0 < maxYMap) {
                y0++;
            }


            try {
                Thread.sleep(2);
            } catch (InterruptedException e1) {
            }
            repaint();
        }


    }

    public static boolean isRunningMap() {
        return isRunningMap;
    }

    public static void setRunningMap(boolean isRunningMap) {
        Panel.isRunningMap = isRunningMap;
    }

    public void selectDrawTool(int code){
        System.out.println("aa");
        if(selectedDrawTool!=-1){
            for(GameObject GO : gameObjects)
                if(GO instanceof  DrawToolButton && ((DrawToolButton)GO).isSelected()) {
                    ((DrawToolButton) GO).select(false);
                    System.out.println(((GameButton) GO).getCode());
                }
        }

        selectedDrawTool = code;
    }

    public void unSelectDrawTool(){
        selectedDrawTool = -1;
    }

}
    // on click on "finish" change state to menu and give the "ground" and "object" to menu.Panel.editMap(...)

