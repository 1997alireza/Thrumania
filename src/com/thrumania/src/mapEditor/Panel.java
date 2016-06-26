package com.thrumania.src.mapEditor;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.Tools.Division;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.objects.DrawToolButton;
import com.thrumania.src.objects.GameButton;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Panel implements GraphicHandler {
    private com.thrumania.src.draw.GamePanel drawPanel;
    private com.thrumania.src.menu.Panel menuPanel;

    private LinkedList<LinkedList< Object[/* Constant.GROUND , Constant.OBJECT*/] >> ground ;

    private LinkedList <GameObject> gameObjects;
    // another linkedlist for staticObjects


    private int season = 0; // 0->spring , 1->summer , 2->autumn , 3->winter
    private int selectedDrawTool = -1;
    private float scale;
    private static int x0, y0;
    private static Dimension mapSize;  // height / width = 8 / 5
    private static int maxXMap, maxYMap;



    private static boolean isRunningMap;


    public Panel(com.thrumania.src.draw.GamePanel drawPanel, com.thrumania.src.menu.Panel menuPanel,LinkedList<LinkedList< Object[/* Constant.GROUND , Constant.OBJECT*/] >> ground ) {


        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.drawPanel.map_panel = this;

        isRunningMap = true;


        mapSize = Constant.minMapSize;

        maxXMap = mapSize.width - Constant.DEFAULT_SCREEN_SIZE.width;
        maxYMap = mapSize.height - Constant.DEFAULT_SCREEN_SIZE.height + Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/;

        scale = 1f;
        x0 = 0;
        y0 = 0;
        x0 = 0;
        y0 = 0;

        this.ground = ground;


    }


    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(Constant.DEFAULT_SCREEN_SIZE.width/2, Constant.DEFAULT_SCREEN_SIZE.height/2);
        g2d.scale(scale, scale);
        g2d.translate(-Constant.DEFAULT_SCREEN_SIZE.width/2, -Constant.DEFAULT_SCREEN_SIZE.height/2);

        for (int i = 0; i < Constant.NUM_OF_SEA_IN_EACH_ROW  ; i++)
            for (int j = 0; j < Constant.NUM_OF_SEA_IN_EACH_COLUMN  ; j++) {
                g.drawImage(new ImageIcon("src/res/images/map/ground/sea.jpg").getImage(), -(x0 % Constant.MIN_WIDTH_OF_EACH_SEA) + i * Constant.MIN_WIDTH_OF_EACH_SEA, -(y0 % Constant.MIN_HEIGHT_OF_EACH_SEA) + j * Constant.MIN_HEIGHT_OF_EACH_SEA, Constant.MIN_WIDTH_OF_EACH_SEA, Constant.MIN_HEIGHT_OF_EACH_SEA, null);
            }

        for (int i = x0 / Constant.MIN_WIDTH_OF_EACH_GROUND; i < (Constant.DEFAULT_SCREEN_SIZE.width  + x0) / Constant.MIN_WIDTH_OF_EACH_GROUND + 1; i++)
            for (int j = y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND; j < (Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/ + y0) / Constant.MIN_HEIGHT_OF_EACH_GROUND + 1; j++) {
                Constant.GROUND thisGround = (Constant.GROUND) ground.get(i).get(j)[0];
                switch (thisGround) {
                    case LOWLAND:
                        int imageNum = chooseLandImageNumber(Constant.GROUND.LOWLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + imageNum + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);

                        if (ground.get(i).get(j)[1] != null) {
                            switch ((Constant.OBJECT) ground.get(i).get(j)[1]) {
                                case TREE1:
                                    /////////////FFFFFFFFFFirst must paint shadows
                                    ///////////// MUSTTT PROVE MOKHTASSATTE va size SHADOW va tree
                                    // va inke sare check krdne highland bega mire
                                    g.drawImage(new ImageIcon("src/res/images/map/tree/1/" + season + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -60 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);
                                    break;
                                case TREE2:
                                    g.drawImage(new ImageIcon("src/res/images/map/tree/2/" + season + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -60 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);
                                    break;
                                case FARMLAND:
                                    break;
                            }
                        }

                        break;
                    case HIGHLAND:
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + 15 + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        //for the lowLand under the highLand

                        int imageNum2 = chooseLandImageNumber(Constant.GROUND.HIGHLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/highLand/" + imageNum2 + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        break;
                }


            }

        g2d.translate(Constant.DEFAULT_SCREEN_SIZE.width/2, Constant.DEFAULT_SCREEN_SIZE.height/2);
        g2d.scale((1f)/scale, (1f)/scale);
        g2d.translate(-Constant.DEFAULT_SCREEN_SIZE.width/2, -Constant.DEFAULT_SCREEN_SIZE.height/2);

        ImageIcon bottomFrame = new ImageIcon("src/res/images/map/editor/frame.png");
        g.drawImage(bottomFrame.getImage(), 0, 0, Constant.DEFAULT_SCREEN_SIZE.width, Constant.DEFAULT_SCREEN_SIZE.height, null);

        for (GameObject GO : gameObjects) {
            g.drawImage(GO.getImage(), GO.getX(), GO.getY(), GO.getWidth(), GO.getHeight(), null);
        }

    }

    @Override
    public void updateComponents() {
        gameObjects = new LinkedList<GameObject>();

        ImageIcon load_1 = new ImageIcon("src/res/images/map/editor/button/load.1.png");
        ImageIcon load_2 = new ImageIcon("src/res/images/map/editor/button/load.2.png");
        gameObjects.add(new GameButton(this,"load",1,1498,844,67,67, load_1.getImage(), load_2.getImage()));

        ImageIcon zoom_in_1 = new ImageIcon("src/res/images/map/editor/button/zoom in 1.png");
        ImageIcon zoom_in_2 = new ImageIcon("src/res/images/map/editor/button/zoom in 2.png");
        gameObjects.add(new GameButton(this,"zoom in",2,1604,844,67,67, zoom_in_1.getImage(), zoom_in_2.getImage()));

        ImageIcon zoom_out_1 = new ImageIcon("src/res/images/map/editor/button/zoom out 1.png");
        ImageIcon zoom_out_2 = new ImageIcon("src/res/images/map/editor/button/zoom out 2.png");
        gameObjects.add(new GameButton(this,"zoom_out",3,1707,844,67,67, zoom_out_1.getImage(), zoom_out_2.getImage()));

        ImageIcon save_1 = new ImageIcon("src/res/images/map/editor/button/save.1.png");
        ImageIcon save_2 = new ImageIcon("src/res/images/map/editor/button/save.2.png");
        gameObjects.add(new GameButton(this,"save",4,1812,844,67,67, save_1.getImage(), save_2.getImage()));

        ImageIcon undo_1 = new ImageIcon("src/res/images/map/editor/button/undo.1.png");
        ImageIcon undo_2 = new ImageIcon("src/res/images/map/editor/button/undo.2.png");
        gameObjects.add(new GameButton(this,"undo",5,1498,917,67,67 ,undo_1.getImage(), undo_2.getImage()));

        ImageIcon redo_1 = new ImageIcon("src/res/images/map/editor/button/redo.1.png");
        ImageIcon redo_2 = new ImageIcon("src/res/images/map/editor/button/redo.2.png");
        gameObjects.add(new GameButton(this,"redo",6,1812,917,67,67, redo_1.getImage(), redo_2.getImage()));

        ImageIcon preview_1 = new ImageIcon("src/res/images/map/editor/button/season preview 1.png");
        ImageIcon preview_2 = new ImageIcon("src/res/images/map/editor/button/season preview 2.png");
        gameObjects.add(new GameButton(this,"preview",7,1498,991,67,67 ,preview_1.getImage(), preview_2.getImage()));

        ImageIcon erase_1 = new ImageIcon("src/res/images/map/editor/button/erase 1.png");
        ImageIcon erase_2 = new ImageIcon("src/res/images/map/editor/button/erase 2.png");
        gameObjects.add(new DrawToolButton(this,"erase",8,1604,991,67,67 ,erase_1.getImage(), erase_2.getImage()));

        ImageIcon erase_all_1 = new ImageIcon("src/res/images/map/editor/button/erase all 1.png");
        ImageIcon erase_all_2 = new ImageIcon("src/res/images/map/editor/button/erase all 2.png");
        gameObjects.add(new GameButton(this,"erase all",9,1707,991,67,67 ,erase_all_1.getImage(), erase_all_2.getImage()));

        ImageIcon exit_1 = new ImageIcon("src/res/images/map/editor/button/exit 1.png");
        ImageIcon exit_2 = new ImageIcon("src/res/images/map/editor/button/exit 2.png");
        gameObjects.add(new GameButton(this,"exit",10,1812,991,67,67, exit_1.getImage(), exit_2.getImage()));

        ImageIcon up_1 = new ImageIcon("src/res/images/map/editor/button/speed_up.1.png");
        ImageIcon up_2 = new ImageIcon("src/res/images/map/editor/button/speed_up.2.png");
        gameObjects.add(new GameButton(this,"up",11,1301,874,51,51, up_1.getImage(), up_2.getImage()));

        ImageIcon down_1 = new ImageIcon("src/res/images/map/editor/button/speed_down.1.png");
        ImageIcon down_2 = new ImageIcon("src/res/images/map/editor/button/speed_down.2.png");
        gameObjects.add(new GameButton(this,"down",12,1301,1016,51,51, down_1.getImage(), down_2.getImage()));

        ImageIcon left_1 = new ImageIcon("src/res/images/map/editor/button/speed_left.1.png");
        ImageIcon left_2 = new ImageIcon("src/res/images/map/editor/button/speed_left.2.png");
        gameObjects.add(new GameButton(this,"left",13,1237,944,51,51, left_1.getImage(), left_2.getImage()));

        ImageIcon right_1 = new ImageIcon("src/res/images/map/editor/button/speed_right.1.png");
        ImageIcon right_2 = new ImageIcon("src/res/images/map/editor/button/speed_right.2.png");
        gameObjects.add(new GameButton(this,"right",14,1363,944,51,51, right_1.getImage(), right_2.getImage()));

        ImageIcon decrease_1 = new ImageIcon("src/res/images/map/editor/button/decrease.1.png");
        ImageIcon decrease_2 = new ImageIcon("src/res/images/map/editor/button/decrease.2.png");
        gameObjects.add(new GameButton(this,"decrease",15,1363,1016,51,51, decrease_1.getImage(), decrease_2.getImage()));

        ImageIcon increase_1 = new ImageIcon("src/res/images/map/editor/button/increase.1.png");
        ImageIcon increase_2 = new ImageIcon("src/res/images/map/editor/button/increase.2.png");
        gameObjects.add(new GameButton(this,"increase",16,1237,874,51,51, increase_1.getImage(), increase_2.getImage()));



        ImageIcon at_sea_1 = new ImageIcon("src/res/images/map/editor/button/at-sea.1.png");
        ImageIcon at_sea_2 = new ImageIcon("src/res/images/map/editor/button/at-sea.2.png");
        gameObjects.add(new DrawToolButton(this,"sea",101,536,890,77,77, at_sea_1.getImage(), at_sea_2.getImage()));

        ImageIcon stone_1 = new ImageIcon("src/res/images/map/editor/button/stone-pile.1.png");
        ImageIcon stone_2 = new ImageIcon("src/res/images/map/editor/button/stone-pile.2.png");
        gameObjects.add(new DrawToolButton(this,"lowLand",102,665,890,77,77, stone_1.getImage(), stone_2.getImage()));

        ImageIcon peaks_1 = new ImageIcon("src/res/images/map/editor/button/peaks.1.png");
        ImageIcon peaks_2 = new ImageIcon("src/res/images/map/editor/button/peaks.2.png");
        gameObjects.add(new DrawToolButton(this,"highLand",103,794,890,77,77, peaks_1.getImage(), peaks_2.getImage()));

        ImageIcon mine_iron_1 = new ImageIcon("src/res/images/map/editor/button/mine-wagon.1.png");
        ImageIcon mine_iron_2 = new ImageIcon("src/res/images/map/editor/button/mine-wagon.2.png");
        gameObjects.add(new DrawToolButton(this,"ironMine",112,923,890,77,77, mine_iron_1.getImage(), mine_iron_2.getImage()));

        ImageIcon mine_gold_1 = new ImageIcon("src/res/images/map/editor/button/minerals.1.png");
        ImageIcon mine_gold_2 = new ImageIcon("src/res/images/map/editor/button/minerals.2.png");
        gameObjects.add(new DrawToolButton(this,"goldMine",113,1052,890,77,77, mine_gold_1.getImage(), mine_gold_2.getImage()));

        ImageIcon farm_1 = new ImageIcon("src/res/images/map/editor/button/farm.1.png");
        ImageIcon farm_2 = new ImageIcon("src/res/images/map/editor/button/farm.2.png");
        gameObjects.add(new DrawToolButton(this,"farmLand",111,536,990,77,77, farm_1.getImage(), farm_2.getImage()));

        ImageIcon beech_1  = new ImageIcon("src/res/images/map/editor/button/beech.1.png");
        ImageIcon beech_2 = new ImageIcon("src/res/images/map/editor/button/beech.2.png");
        gameObjects.add(new DrawToolButton(this,"tree1",114,665,990,77,77, beech_1.getImage(), beech_2.getImage()));

        ImageIcon pine_tree_1 = new ImageIcon("src/res/images/map/editor/button/pine-tree.1.png");
        ImageIcon pine_tree_2 = new ImageIcon("src/res/images/map/editor/button/pine-tree.2.png");
        gameObjects.add(new DrawToolButton(this,"tree2",115,794,990,77,77, pine_tree_1.getImage(), pine_tree_2.getImage()));

        ImageIcon salmon_1 = new ImageIcon("src/res/images/map/editor/button/salmon.1.png");
        ImageIcon salmon_2 = new ImageIcon("src/res/images/map/editor/button/salmon.2.png");
        gameObjects.add(new DrawToolButton(this,"salmon",116,932,990,77,77, salmon_1.getImage(), salmon_2.getImage()));

        ImageIcon piranha_1 = new ImageIcon("src/res/images/map/editor/button/piranha.1.png");
        ImageIcon piranha_2 = new ImageIcon("src/res/images/map/editor/button/piranha.2.png");
        gameObjects.add(new DrawToolButton(this,"piranha",117,1052,990,77,77, piranha_1.getImage(), piranha_2.getImage()));

    }


    @Override
    public void repaint() {
        drawPanel.repaint();
    }

    private void drawIntoGround(int x,int y){
        int i = (x+x0) / Constant.MIN_WIDTH_OF_EACH_GROUND;
        int j = (y+y0) / Constant.MIN_HEIGHT_OF_EACH_GROUND;
        Object [] room = new Object[2];
        switch (selectedDrawTool){
            case (101) :    //sea
                room[0] = Constant.GROUND.SEA;
                room[1] = null;
                ground.get(i).set(j , room);

                if(i>0 && j>0 && ground.get(i-1).get(j-1)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i-1).set(j-1 ,room1);
                }
                if(j>0 && ground.get(i).get(j-1)[0] == Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i).set(j-1 ,room1);
                }
                if(i<maxXMap && j>0 && ground.get(i+1).get(j-1)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i+1).set(j-1 ,room1);
                }
                if(i>0 && ground.get(i-1).get(j)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i-1).set(j ,room1);
                }
                if(i<maxXMap&& ground.get(i+1).get(j)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i+1).set(j ,room1);
                }
                if(i>0 && j<maxYMap && ground.get(i-1).get(j+1)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i-1).set(j+1 ,room1);
                }
                if(j<maxYMap && ground.get(i).get(j+1)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i).set(j+1 ,room1);
                }
                if(i<maxXMap && j<maxYMap && ground.get(i+1).get(j+1)[0]== Constant.GROUND.HIGHLAND){
                    Object [] room1 = {Constant.GROUND.LOWLAND , null};
                    ground.get(i+1).set(j+1 ,room1);
                }


                break;
            case(102):      //lowLand
                room[0] = Constant.GROUND.LOWLAND;
                room[1] = null;
                ground.get(i).set(j , room);
                break;
            case(103):      //highLand
                if(i>0 && j>0 && i<maxXMap && j<maxYMap) {
                    boolean t1 = ground.get(i - 1).get(j - 1)[0] == Constant.GROUND.LOWLAND || ground.get(i - 1).get(j - 1)[0] == Constant.GROUND.HIGHLAND;
                    boolean t2 = ground.get(i).get(j - 1)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j - 1)[0] == Constant.GROUND.HIGHLAND;
                    boolean t3 = ground.get(i + 1).get(j - 1)[0] == Constant.GROUND.LOWLAND || ground.get(i + 1).get(j - 1)[0] == Constant.GROUND.HIGHLAND;
                    boolean t4 = ground.get(i - 1).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i - 1).get(j)[0] == Constant.GROUND.HIGHLAND;
                    boolean t5 = ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND;
                    boolean t6 = ground.get(i + 1).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i + 1).get(j)[0] == Constant.GROUND.HIGHLAND;
                    boolean t7 = ground.get(i - 1).get(j + 1)[0] == Constant.GROUND.LOWLAND || ground.get(i - 1).get(j + 1)[0] == Constant.GROUND.HIGHLAND;
                    boolean t8 = ground.get(i).get(j + 1)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j + 1)[0] == Constant.GROUND.HIGHLAND;
                    boolean t9 = ground.get(i + 1).get(j + 1)[0] == Constant.GROUND.LOWLAND || ground.get(i + 1).get(j + 1)[0] == Constant.GROUND.HIGHLAND;
                    if (t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9) {
                        room[0] = Constant.GROUND.HIGHLAND;
                        room[1] = null;
                        ground.get(i).set(j, room);
                    }
                }
                break;
            case(114):      //tree 1
                if(ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = Constant.OBJECT.TREE1;
                    ground.get(i).set(j, room);
                }
                break;
            case(115):      //tree 2
                if(ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = Constant.OBJECT.TREE2;
                    ground.get(i).set(j, room);
                }
                break;
            case(112):      //iron
                if(ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
                    room[0] = Constant.GROUND.HIGHLAND;
                    room[1] = Constant.OBJECT.IRON_MINE;
                    ground.get(i).set(j, room);
                }
                break;

            case(113):      //gold
                if(ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
                    room[0] = Constant.GROUND.HIGHLAND;
                    room[1] = Constant.OBJECT.IRON_MINE;
                    ground.get(i).set(j, room);
                }
                break;

            case(111):      //farmLand
                if(ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = Constant.OBJECT.FARMLAND;
                    ground.get(i).set(j, room);
                }
                break;

            case(116):      //salmon - fish1
                if(ground.get(i).get(j)[0] == Constant.GROUND.SEA) {
                    room[0] = Constant.GROUND.SEA;
                    room[1] = Constant.OBJECT.FISH1;
                    ground.get(i).set(j, room);
                }
                break;

            case(117):      //piranha - fish2
                if(ground.get(i).get(j)[0] == Constant.GROUND.SEA) {
                    room[0] = Constant.GROUND.SEA;
                    room[1] = Constant.OBJECT.FISH2;
                    ground.get(i).set(j, room);
                }
                break;
        }
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

        else
            drawIntoGround(x,y);

    }

    private int startDraggingX = -1;
    private int startDraggingY = -1;
    private boolean inMovavleArea = false;

    @Override
    public void mouseEnter(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseEntered();
                break;
            }
        }


        if( x <= 5 || x >= Constant.Screen_Width - 5 || y <= 5 || y >= Constant.Screen_Height - 5) {
            if(!inMovablePosition) {
                inMovablePosition = true;
                inMovavleArea = true;
                new Thread(this::moveMap).start();
            }
        }
        else if( ! (movingUp || movingDown || movingRight || movingLeft) ) {
            inMovablePosition = false;
            inMovavleArea = false;
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


        if (!(y >= 1080 - Constant.BOTTOM_FRAME_HEIGHT))
            drawIntoGround(x, y);

        else {
            for (GameObject GO : gameObjects) {
                if (GO.isInArea(x, y)) {
                    if (GO.isInArea(startDraggingX, startDraggingY))
                        GO.mouseClicked();
                    break;
                }
            }
        }

        repaint();
    }

    @Override
    public void mousePress(int x, int y) {
        startDraggingX = x;
        startDraggingY = y;

    }

    @Override
    public void mouseRelease(int x, int y) {
        startDraggingX = -1;
        startDraggingY = -1;

    }


    @Override
    public void pressButton(int code) {
        switch(code){
            case 9:     //erase all
                Object emptyRoom [] = {Constant.GROUND.SEA , null};

                for(int i=0 ; i < ground.size() ; i++)
                    for(int j=0 ; j < ground.get(i).size() ; j++)
                        ground.get(i).set(j , emptyRoom);

                break;


            case 11:    //fast up
                movingUp = true;
                movingDown = false;

                break;
            case 12:    //fast down
                movingDown = true;
                movingUp = false;
                break;
            case 13:    //fast left
                movingLeft = true;
                movingRight = false;
                break;
            case 14:    //fast right
                movingRight = true;
                movingLeft = false;
                break;


            case 7:     //preview
                changeSeason();
                break;

            case 10:    //exit
                menuPanel.chooseMap(ground);
                drawPanel.changeState(GamePanel.STATE.MENU);
                break;


            case(2):       //zoomIn
                changeScale(true);
                break;
            case(3):       //zoomOut
                changeScale(false);
                break;

            case 4:         //save
                saveMap();
                break;
            case 1:         //load
                loadMap();
                break;


        }

        if(!inMovavleArea && (movingUp || movingDown || movingRight || movingLeft)){
                if(!inMovablePosition) {
                    inMovablePosition = true;
                    new Thread(this::moveMap).start();
                }
        }
    }


    private boolean movingUp = false , movingDown = false , movingRight = false , movingLeft = false;
    private boolean inMovablePosition = false;
    private void moveMap() {
        while (isRunningMap && inMovablePosition) {

            int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
            boolean t = false;
            boolean movingFast = movingUp || movingDown || movingRight || movingLeft;
            if ( (x <= 5) && x0 > 0 && !movingFast) {
                x0-=3;
                t=true;

            } else if ( (x >= Constant.Screen_Width - 5) && x0 < maxXMap && !movingFast) {
                x0+=3;
                t=true;
            }

            if ( (y <= 5 ) && y0 > 0 && !movingFast) {
                y0-=3;
                t=true;
            } else if ( (y >= Constant.Screen_Height - 5)  && y0 < maxYMap && !movingFast) {
                y0+=3;
                t=true;
            }

            repaint();

            if(t) {
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e1) {
                }
            }



            if ( (movingLeft) && x0 > 0) {
                x0-=3;
                t=true;

                try {
                    TimeUnit.NANOSECONDS.sleep(x0/4);
                } catch (InterruptedException e1) {
                }
                repaint();
            } else if ( ( movingRight) && x0 < maxXMap) {
                x0+=3;
                t=true;

                try {
                    TimeUnit.NANOSECONDS.sleep((maxXMap-x0)/4);
                } catch (InterruptedException e1) {
                }
                repaint();
            }

            if ( (movingUp ) && y0 > 0) {
                y0-=3;
                t=true;

                try {
                    TimeUnit.NANOSECONDS.sleep(y0/4);
                } catch (InterruptedException e1) {
                }
                repaint();
            } else if ( ( movingDown)  && y0 < maxYMap) {
                y0+=3;
                t=true;

                try {
                    TimeUnit.NANOSECONDS.sleep((maxYMap-y0)/4);
                } catch (InterruptedException e1) {
                }
                repaint();
            }





            if(x0==0)
                movingLeft = false;
            else if(x0==maxXMap)
                movingRight = false;
            if(y0==0)
                movingUp = false;
            else if(y0==maxYMap)
                movingDown = false;


            movingFast = movingUp || movingDown || movingRight || movingLeft;
            if(!movingFast && !inMovavleArea)
                inMovablePosition = false;
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

        if(selectedDrawTool!=-1){
            for(GameObject GO : gameObjects)
                if(GO instanceof  DrawToolButton && ((DrawToolButton)GO).getCode() == selectedDrawTool) {
                    ((DrawToolButton) GO).select(false);
                }
        }

        selectedDrawTool = code;
    }

    public void unSelectDrawTool(){
        selectedDrawTool = -1;
    }


    private int chooseLandImageNumber(Constant.GROUND groundType , int x , int y){

        int k = 0;

        if( y>0 && (ground.get(x).get(y-1)[0] == groundType || ground.get(x).get(y-1)[0] == Constant.GROUND.HIGHLAND))   //up
            k+=1;
        if( x<ground.size()-1 && (ground.get(x+1).get(y) [0] == groundType || ground.get(x+1).get(y)[0] == Constant.GROUND.HIGHLAND))   //right
            k+=2;
        if( y<ground.get(x).size()-1 && (ground.get(x).get(y+1) [0] == groundType || ground.get(x).get(y+1)[0] == Constant.GROUND.HIGHLAND))   //down
            k+=4;
        if( x>0 && (ground.get(x-1).get(y)[0] == groundType || ground.get(x-1).get(y)[0] == Constant.GROUND.HIGHLAND))   //left
            k+=8;


        return k;

    }

    private void changeSeason(){
            season = (season+1)%4;
    }

    private void changeScale(boolean in){   // in = true -> zoomIn  , in = false -> zoomOut
        scale = (in)?(scale+Constant.ONE_SCALE_CHANGING):(scale-Constant.ONE_SCALE_CHANGING);
        if(scale<1)
            scale=1f;
        else if(scale>Constant.MAX_OF_SCALE)
            scale = Constant.MAX_OF_SCALE;

        maxXMap = mapSize.width - Division.division(Constant.DEFAULT_SCREEN_SIZE.width , scale);
        maxYMap = mapSize.height - Division.division(Constant.DEFAULT_SCREEN_SIZE.height + Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/ , scale);

        repaint();
    }


    /**
     * this method save map
     */
    private void saveMap(){
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("save");
            int result = jFileChooser.showSaveDialog(null);
            if(result == 0) {
                FileOutputStream out = new FileOutputStream(jFileChooser.getSelectedFile());
                ObjectOutputStream oout = new ObjectOutputStream(out);
                oout.writeObject(ground);
                oout.close();
            }
        }catch (Exception e) {

        }
    }

    /**
     * this method load map
     */
    private void loadMap(){
        try{
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("load");
            int result = jFileChooser.showOpenDialog(null);
            if(result == 0){
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(jFileChooser.getSelectedFile()));
                ground = (LinkedList<LinkedList<Object[]>>) ois.readObject();
            }
        }catch (Exception e){

        }
    }
}


