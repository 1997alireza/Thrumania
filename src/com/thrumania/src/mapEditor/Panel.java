package com.thrumania.src.mapEditor;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.Tools.Division;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.game.FindCastles;
import com.thrumania.src.mapEditor.objects.DrawToolButton;
import com.thrumania.src.objects.GameButton;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class Panel implements GraphicHandler {
    private com.thrumania.src.draw.GamePanel drawPanel;
    private com.thrumania.src.menu.Panel menuPanel;

    private LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground;

    private LinkedList<GameObject> gameObjects;



    private int season = 0; // 0->spring , 1->summer , 2->autumn , 3->winter
    private int selectedDrawTool = -1;
    private float scale;
    private static int x0_inDefaultScale, y0_inDefaultScale;
    private static int x0, y0;
    private static Dimension mapSize;  // height / width = 8 / 5
    private static int maxXMap, maxYMap;
    private static int minXMap, minYMap;


    private Stack<ArrayList<Object[]>> undo = new Stack<ArrayList<Object[]>>();
    private Stack<ArrayList<Object[]>> redo = new Stack<ArrayList<Object[]>>();

    private static boolean isRunningMap;

    // <-- mini map
    private double scaleScreen;
    private int widthRecFill;
    private int heightRecFill;
    private int xRecFill;
    private int yRecFill;
    // mini map -->

    public Panel(com.thrumania.src.draw.GamePanel drawPanel, com.thrumania.src.menu.Panel menuPanel, LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground) {


        this.drawPanel = drawPanel;
        this.menuPanel = menuPanel;
        this.drawPanel.map_panel = this;

        isRunningMap = true;


        mapSize = Constant.MIN_MAP_SIZE;

        maxXMap = mapSize.width - Constant.DEFAULT_SCREEN_SIZE.width;
        maxYMap = mapSize.height - (Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/);
        minXMap = -(Constant.Screen_Width - Constant.Screen_Width) / 2;
        minYMap = -(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT)) / 2;

        scale = 1f;
        x0 = 0;
        y0 = 0;
        x0_inDefaultScale = x0;
        y0_inDefaultScale = y0;

        this.ground = ground;

        scaleScreen = (double) mapSize.width / 400;


    }


    @Override
    public void render(Graphics g) {

        x0 = x0_inDefaultScale + (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
        y0 = y0_inDefaultScale + (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(scale, scale);

        for (int i = 0; i < Constant.NUM_OF_SEA_IN_EACH_ROW; i++)
            for (int j = 0; j < Constant.NUM_OF_SEA_IN_EACH_COLUMN; j++) {
                g.drawImage(new ImageIcon("src/res/images/map/ground/sea.jpg").getImage(), -(x0 % Constant.MIN_WIDTH_OF_EACH_SEA) + i * Constant.MIN_WIDTH_OF_EACH_SEA, -(y0 % Constant.MIN_HEIGHT_OF_EACH_SEA) + j * Constant.MIN_HEIGHT_OF_EACH_SEA, Constant.MIN_WIDTH_OF_EACH_SEA, Constant.MIN_HEIGHT_OF_EACH_SEA, null);
            }
        for (int i = x0 / Constant.MIN_WIDTH_OF_EACH_GROUND; i </*Division.division*/ (Constant.DEFAULT_SCREEN_SIZE.width / (scale * Constant.MIN_WIDTH_OF_EACH_GROUND)) + x0 / Constant.MIN_WIDTH_OF_EACH_GROUND + 1 && i < ground.size(); i++)

            for (int j = y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND; j < Division.division(Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/, scale * Constant.MIN_HEIGHT_OF_EACH_GROUND) + Division.division(y0, Constant.MIN_HEIGHT_OF_EACH_GROUND) + 1 + 1/*for tall objects*/ && j < ground.get(i).size(); j++) {
                Constant.GROUND thisGround = (Constant.GROUND) ground.get(i).get(j)[0];
                switch (thisGround) {
                    case SEA:
                        if (ground.get(i).get(j)[1] != null) {
                            switch ((Constant.OBJECT) ground.get(i).get(j)[1]) {
                                case FISH1:
                                    g.drawImage(new ImageIcon("src/res/images/map/fish/fish1.png").getImage(), 10 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, 10 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 144, 120, null);
                                    break;
                                case FISH2:
                                    g.drawImage(new ImageIcon("src/res/images/map/fish/fish2.png").getImage(), 10 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, 10 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 144, 120, null);
                                    break;
                            }
                        }
                        break;
                    case LOWLAND:
                        int imageNum = chooseLandImageNumber(Constant.GROUND.LOWLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + imageNum + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);

                        if (ground.get(i).get(j)[1] != null) {
                            switch ((Constant.OBJECT) ground.get(i).get(j)[1]) {
                                case TREE1:
                                    switch (season) {        // shadow for tree 1
                                        case 0:
                                        case 1:
                                        case 2:
                                            g.drawImage(new ImageIcon("src/res/images/map/tree/1/0,1,2_shadow.png").getImage(), 60 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -45 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);
                                            break;
                                        case 3:
                                            g.drawImage(new ImageIcon("src/res/images/map/tree/1/3_shadow.png").getImage(), 57 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -43 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);
                                    }

                                    g.drawImage(new ImageIcon("src/res/images/map/tree/1/" + season + ".png").getImage(), 20 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -60 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);

                                    break;

                                case TREE2:
                                    g.drawImage(new ImageIcon("src/res/images/map/tree/2/" + season + "_shadow.png").getImage(), 65 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -45 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);
                                    // shadow for tree 2
                                    g.drawImage(new ImageIcon("src/res/images/map/tree/2/" + season + ".png").getImage(), 20 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -60 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 120, null);
                                    break;
                                case FARMLAND:
                                    g.drawImage(new ImageIcon("src/res/images/map/farmLand/farm.png").getImage(), 40 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, 27 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 94, 67, null);
                                    break;
                            }
                        }

                        break;
                    case HIGHLAND:
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + 15 + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        //for the lowLand under the highLand

                        int imageNum2 = chooseLandImageNumber(Constant.GROUND.HIGHLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/highLand/" + imageNum2 + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);


                        if (ground.get(i).get(j)[1] != null) {
                            switch ((Constant.OBJECT) ground.get(i).get(j)[1]) {
                                case IRON_MINE:
                                    g.drawImage(new ImageIcon("src/res/images/map/mine/ironMine.png").getImage(), 40 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 85,92, null);
                                    break;
                                case GOLD_MINE:
                                    g.drawImage(new ImageIcon("src/res/images/map/mine/goldMine.png").getImage(), 40 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND,  - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 120, 111, null);
                                    break;
                            }
                        }

                        break;


                }


            }

        g2d.scale((1f) / scale, (1f) / scale);

        drawMiniMap(g);

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
        gameObjects.add(new GameButton(this, "load", 1, 1498, 844, 67, 67, load_1.getImage(), load_2.getImage()));

        ImageIcon zoom_in_1 = new ImageIcon("src/res/images/map/editor/button/zoom in 1.png");
        ImageIcon zoom_in_2 = new ImageIcon("src/res/images/map/editor/button/zoom in 2.png");
        gameObjects.add(new GameButton(this, "zoom in", 2, 1604, 844, 67, 67, zoom_in_1.getImage(), zoom_in_2.getImage()));

        ImageIcon zoom_out_1 = new ImageIcon("src/res/images/map/editor/button/zoom out 1.png");
        ImageIcon zoom_out_2 = new ImageIcon("src/res/images/map/editor/button/zoom out 2.png");
        gameObjects.add(new GameButton(this, "zoom_out", 3, 1707, 844, 67, 67, zoom_out_1.getImage(), zoom_out_2.getImage()));

        ImageIcon save_1 = new ImageIcon("src/res/images/map/editor/button/save.1.png");
        ImageIcon save_2 = new ImageIcon("src/res/images/map/editor/button/save.2.png");
        gameObjects.add(new GameButton(this, "save", 4, 1812, 844, 67, 67, save_1.getImage(), save_2.getImage()));

        ImageIcon undo_1 = new ImageIcon("src/res/images/map/editor/button/undo.1.png");
        ImageIcon undo_2 = new ImageIcon("src/res/images/map/editor/button/undo.2.png");
        gameObjects.add(new GameButton(this, "undo", 5, 1498, 917, 67, 67, undo_1.getImage(), undo_2.getImage()));

        ImageIcon redo_1 = new ImageIcon("src/res/images/map/editor/button/redo.1.png");
        ImageIcon redo_2 = new ImageIcon("src/res/images/map/editor/button/redo.2.png");
        gameObjects.add(new GameButton(this, "redo", 6, 1812, 917, 67, 67, redo_1.getImage(), redo_2.getImage()));

        ImageIcon preview_1 = new ImageIcon("src/res/images/map/editor/button/season preview 1.png");
        ImageIcon preview_2 = new ImageIcon("src/res/images/map/editor/button/season preview 2.png");
        gameObjects.add(new GameButton(this, "preview", 7, 1498, 991, 67, 67, preview_1.getImage(), preview_2.getImage()));

        ImageIcon erase_1 = new ImageIcon("src/res/images/map/editor/button/erase 1.png");
        ImageIcon erase_2 = new ImageIcon("src/res/images/map/editor/button/erase 2.png");
        gameObjects.add(new DrawToolButton(this, "erase", 8, 1604, 991, 67, 67, erase_1.getImage(), erase_2.getImage()));

        ImageIcon erase_all_1 = new ImageIcon("src/res/images/map/editor/button/erase all 1.png");
        ImageIcon erase_all_2 = new ImageIcon("src/res/images/map/editor/button/erase all 2.png");
        gameObjects.add(new GameButton(this, "erase all", 9, 1707, 991, 67, 67, erase_all_1.getImage(), erase_all_2.getImage()));

        ImageIcon exit_1 = new ImageIcon("src/res/images/map/editor/button/exit 1.png");
        ImageIcon exit_2 = new ImageIcon("src/res/images/map/editor/button/exit 2.png");
        gameObjects.add(new GameButton(this, "exit", 10, 1812, 991, 67, 67, exit_1.getImage(), exit_2.getImage()));

        ImageIcon up_1 = new ImageIcon("src/res/images/map/editor/button/speed_up.1.png");
        ImageIcon up_2 = new ImageIcon("src/res/images/map/editor/button/speed_up.2.png");
        gameObjects.add(new GameButton(this, "up", 11, 1301, 874, 51, 51, up_1.getImage(), up_2.getImage()));

        ImageIcon down_1 = new ImageIcon("src/res/images/map/editor/button/speed_down.1.png");
        ImageIcon down_2 = new ImageIcon("src/res/images/map/editor/button/speed_down.2.png");
        gameObjects.add(new GameButton(this, "down", 12, 1301, 1016, 51, 51, down_1.getImage(), down_2.getImage()));

        ImageIcon left_1 = new ImageIcon("src/res/images/map/editor/button/speed_left.1.png");
        ImageIcon left_2 = new ImageIcon("src/res/images/map/editor/button/speed_left.2.png");
        gameObjects.add(new GameButton(this, "left", 13, 1237, 944, 51, 51, left_1.getImage(), left_2.getImage()));

        ImageIcon right_1 = new ImageIcon("src/res/images/map/editor/button/speed_right.1.png");
        ImageIcon right_2 = new ImageIcon("src/res/images/map/editor/button/speed_right.2.png");
        gameObjects.add(new GameButton(this, "right", 14, 1363, 944, 51, 51, right_1.getImage(), right_2.getImage()));

        ImageIcon decrease_1 = new ImageIcon("src/res/images/map/editor/button/decrease.1.png");
        ImageIcon decrease_2 = new ImageIcon("src/res/images/map/editor/button/decrease.2.png");
        gameObjects.add(new GameButton(this, "decrease", 15, 1237, 874, 51, 51, decrease_1.getImage(), decrease_2.getImage()));

        ImageIcon increase_1 = new ImageIcon("src/res/images/map/editor/button/increase.1.png");
        ImageIcon increase_2 = new ImageIcon("src/res/images/map/editor/button/increase.2.png");
        gameObjects.add(new GameButton(this, "increase", 16, 1363, 1016, 51, 51, increase_1.getImage(), increase_2.getImage()));


        ImageIcon at_sea_1 = new ImageIcon("src/res/images/map/editor/button/at-sea.1.png");
        ImageIcon at_sea_2 = new ImageIcon("src/res/images/map/editor/button/at-sea.2.png");
        gameObjects.add(new DrawToolButton(this, "sea", 101, 536, 890, 77, 77, at_sea_1.getImage(), at_sea_2.getImage()));

        ImageIcon stone_1 = new ImageIcon("src/res/images/map/editor/button/stone-pile.1.png");
        ImageIcon stone_2 = new ImageIcon("src/res/images/map/editor/button/stone-pile.2.png");
        gameObjects.add(new DrawToolButton(this, "lowLand", 102, 665, 890, 77, 77, stone_1.getImage(), stone_2.getImage()));

        ImageIcon peaks_1 = new ImageIcon("src/res/images/map/editor/button/peaks.1.png");
        ImageIcon peaks_2 = new ImageIcon("src/res/images/map/editor/button/peaks.2.png");
        gameObjects.add(new DrawToolButton(this, "highLand", 103, 794, 890, 77, 77, peaks_1.getImage(), peaks_2.getImage()));

        ImageIcon mine_iron_1 = new ImageIcon("src/res/images/map/editor/button/mine-wagon.1.png");
        ImageIcon mine_iron_2 = new ImageIcon("src/res/images/map/editor/button/mine-wagon.2.png");
        gameObjects.add(new DrawToolButton(this, "ironMine", 112, 923, 890, 77, 77, mine_iron_1.getImage(), mine_iron_2.getImage()));

        ImageIcon mine_gold_1 = new ImageIcon("src/res/images/map/editor/button/minerals.1.png");
        ImageIcon mine_gold_2 = new ImageIcon("src/res/images/map/editor/button/minerals.2.png");
        gameObjects.add(new DrawToolButton(this, "goldMine", 113, 1052, 890, 77, 77, mine_gold_1.getImage(), mine_gold_2.getImage()));

        ImageIcon farm_1 = new ImageIcon("src/res/images/map/editor/button/farm.1.png");
        ImageIcon farm_2 = new ImageIcon("src/res/images/map/editor/button/farm.2.png");
        gameObjects.add(new DrawToolButton(this, "farmLand", 111, 536, 990, 77, 77, farm_1.getImage(), farm_2.getImage()));

        ImageIcon beech_1 = new ImageIcon("src/res/images/map/editor/button/beech.1.png");
        ImageIcon beech_2 = new ImageIcon("src/res/images/map/editor/button/beech.2.png");
        gameObjects.add(new DrawToolButton(this, "tree1", 114, 665, 990, 77, 77, beech_1.getImage(), beech_2.getImage()));

        ImageIcon pine_tree_1 = new ImageIcon("src/res/images/map/editor/button/pine-tree.1.png");
        ImageIcon pine_tree_2 = new ImageIcon("src/res/images/map/editor/button/pine-tree.2.png");
        gameObjects.add(new DrawToolButton(this, "tree2", 115, 794, 990, 77, 77, pine_tree_1.getImage(), pine_tree_2.getImage()));

        ImageIcon salmon_1 = new ImageIcon("src/res/images/map/editor/button/salmon.1.png");
        ImageIcon salmon_2 = new ImageIcon("src/res/images/map/editor/button/salmon.2.png");
        gameObjects.add(new DrawToolButton(this, "salmon", 116, 932, 990, 77, 77, salmon_1.getImage(), salmon_2.getImage()));

        ImageIcon piranha_1 = new ImageIcon("src/res/images/map/editor/button/piranha.1.png");
        ImageIcon piranha_2 = new ImageIcon("src/res/images/map/editor/button/piranha.2.png");
        gameObjects.add(new DrawToolButton(this, "piranha", 117, 1052, 990, 77, 77, piranha_1.getImage(), piranha_2.getImage()));

    }


    @Override
    public void repaint() {
        drawPanel.repaint();
    }

    int last_i = -1;
    int last_j = -1;

    private void drawIntoGround(int x, int y) {       // real x and y for mouse

        // <-- START : undo & redo
        int i = (x0 + (int) (x / scale)) / Constant.MIN_WIDTH_OF_EACH_GROUND;
        int j = (y0 + (int) (y / scale)) / Constant.MIN_HEIGHT_OF_EACH_GROUND;

        boolean t = true;
        if (selectedDrawTool == ((Constant.GROUND) ground.get(i).get(j)[0]).getCode())
            t = false;

        else if (ground.get(i).get(j)[1] != null) {
            if (selectedDrawTool == ((Constant.OBJECT) ground.get(i).get(j)[1]).getCode())
                t = false;
        } else if (selectedDrawTool == 8 && ((Constant.GROUND) ground.get(i).get(j)[0]).getCode() == Constant.GROUND.SEA.getCode()
                && ground.get(i).get(j)[1] == null)
            t = false;

        else if ((selectedDrawTool == Constant.OBJECT.GOLD_MINE.getCode() || selectedDrawTool == Constant.OBJECT.IRON_MINE.getCode())
                && ((Constant.GROUND) ground.get(i).get(j)[0]).getCode() != Constant.GROUND.HIGHLAND.getCode())
            t = false;
        else if ((selectedDrawTool == Constant.OBJECT.TREE1.getCode() || selectedDrawTool == Constant.OBJECT.TREE2.getCode() || selectedDrawTool == Constant.OBJECT.FARMLAND.getCode())
                && ((Constant.GROUND) ground.get(i).get(j)[0]).getCode() != Constant.GROUND.LOWLAND.getCode())
            t = false;
        else if ((selectedDrawTool == Constant.OBJECT.FISH1.getCode() || selectedDrawTool == Constant.OBJECT.FISH2.getCode())
                && ((Constant.GROUND) ground.get(i).get(j)[0]).getCode() != Constant.GROUND.SEA.getCode())
            t = false;
        if (selectedDrawTool == Constant.GROUND.HIGHLAND.getCode()) {
            if (i > 0 && j > 0 && i < maxXMap && j < maxYMap) {
                boolean t1 = ground.get(i - 1).get(j - 1)[0] == Constant.GROUND.LOWLAND || ground.get(i - 1).get(j - 1)[0] == Constant.GROUND.HIGHLAND;
                boolean t2 = ground.get(i).get(j - 1)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j - 1)[0] == Constant.GROUND.HIGHLAND;
                boolean t3 = ground.get(i + 1).get(j - 1)[0] == Constant.GROUND.LOWLAND || ground.get(i + 1).get(j - 1)[0] == Constant.GROUND.HIGHLAND;
                boolean t4 = ground.get(i - 1).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i - 1).get(j)[0] == Constant.GROUND.HIGHLAND;
                boolean t5 = ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND;
                boolean t6 = ground.get(i + 1).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i + 1).get(j)[0] == Constant.GROUND.HIGHLAND;
                boolean t7 = ground.get(i - 1).get(j + 1)[0] == Constant.GROUND.LOWLAND || ground.get(i - 1).get(j + 1)[0] == Constant.GROUND.HIGHLAND;
                boolean t8 = ground.get(i).get(j + 1)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j + 1)[0] == Constant.GROUND.HIGHLAND;
                boolean t9 = ground.get(i + 1).get(j + 1)[0] == Constant.GROUND.LOWLAND || ground.get(i + 1).get(j + 1)[0] == Constant.GROUND.HIGHLAND;
                if (!(t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8 && t9)) {
                    t = false;
                }
            }
        }

        if (t) {
            if (dragstate == 0 && (last_i != i || last_j != j) && !undo.isEmpty()) {
                Object o[] = {new Integer(i), new Integer(j), ground.get(i).get(j)};
                undo.peek().add(o);
            } else if (dragstate > 0) {
                redo.removeAllElements();
                ArrayList<Object[]> arr = new ArrayList<Object[]>();
                Object o[] = {new Integer(i), new Integer(j), ground.get(i).get(j)};
                arr.add(o);
                undo.push(arr);
            }
            dragstate = 0;
        }

        last_i = i;
        last_j = j;

        //  END : undo & redo -->

        Object[] room = new Object[2];
        switch (selectedDrawTool) {
            case (101):    //sea
                setSeaInGround(i, j);
                break;
            case (102):      //lowLand
                room[0] = Constant.GROUND.LOWLAND;
                room[1] = null;
                ground.get(i).set(j, room);
                break;
            case (103):      //highLand
                if (i > 0 && j > 0 && i < maxXMap && j < maxYMap) {
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
            case (114):      //tree 1
                if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = Constant.OBJECT.TREE1;
                    ground.get(i).set(j, room);
                }
                break;
            case (115):      //tree 2
                if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = Constant.OBJECT.TREE2;
                    ground.get(i).set(j, room);
                }
                break;
            case (112):      //iron
                if (ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
                    room[0] = Constant.GROUND.HIGHLAND;
                    room[1] = Constant.OBJECT.IRON_MINE;
                    ground.get(i).set(j, room);
                }
                break;

            case (113):      //gold
                if (ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
                    room[0] = Constant.GROUND.HIGHLAND;
                    room[1] = Constant.OBJECT.GOLD_MINE;
                    ground.get(i).set(j, room);
                }
                break;

            case (111):      //farmLand
                if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
                    room[0] = Constant.GROUND.LOWLAND;
                    room[1] = Constant.OBJECT.FARMLAND;
                    ground.get(i).set(j, room);
                }
                break;

            case (116):      //salmon - fish1
                if (ground.get(i).get(j)[0] == Constant.GROUND.SEA) {
                    room[0] = Constant.GROUND.SEA;
                    room[1] = Constant.OBJECT.FISH1;
                    ground.get(i).set(j, room);
                }
                break;

            case (117):      //piranha - fish2
                if (ground.get(i).get(j)[0] == Constant.GROUND.SEA) {
                    room[0] = Constant.GROUND.SEA;
                    room[1] = Constant.OBJECT.FISH2;
                    ground.get(i).set(j, room);
                }
                break;

            case (8):        //erase
                erase(i, j);
                break;
        }

        repaint();
    }

    private long dragstate = 0;

    @Override
    public void mouseClick(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();


        if ((x >= 24 && x <= 24 + 420) && (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 12))
            miniMapMovingRect(x, y);

        else if (y >= Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT) {
            for (GameObject GO : gameObjects) {
                if (GO.isInArea(x, y)) {
                    GO.mouseClicked();
                    break;
                }
            }
        } else {

            if (SwingUtilities.isRightMouseButton(e)) {
                int temp = selectedDrawTool;
                selectedDrawTool = 8;
                drawIntoGround(x, y);
                dragstate++;
                selectedDrawTool = temp;
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                drawIntoGround(x, y);
                dragstate++;
            }
        }
    }

    private int startDraggingX = -1;
    private int startDraggingY = -1;
    private boolean inMovableArea = false;

    @Override
    public void mouseEnter(int x, int y) {
        for (GameObject GO : gameObjects) {
            if (GO.isInArea(x, y)) {
                GO.mouseEntered();
                break;
            }
        }


        if (x <= 5 || x >= Constant.Screen_Width - 5 || y <= 5 || y >= Constant.Screen_Height - 5) {
            if (!inMovablePosition) {
                inMovablePosition = true;
                inMovableArea = true;
                new Thread(this::moveMap).start();
            }
        } else if (!(movingUp || movingDown || movingRight || movingLeft)) {
            inMovablePosition = false;
            inMovableArea = false;
        }
    }

    @Override
    public void mouseExit(int x, int y) {
        for (GameObject GO : gameObjects) {
            if (GO.isInArea(x, y)) {
                GO.mouseExited();
                break;
            }
        }


    }

    @Override
    public void mouseDrag(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        if ((startDraggingX >= 24 && startDraggingX <= 24 + 420) && (startDraggingY >= Constant.Screen_Height - 260 && startDraggingY <= Constant.Screen_Height - 12) && (x >= 24 && x <= 24 + 420) && (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 12))
            miniMapMovingRect(x, y);


        else if ( (startDraggingY < Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT) && (y < Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT)) {
            if (SwingUtilities.isRightMouseButton(e)) {
                int temp = selectedDrawTool;
                selectedDrawTool = 8;
                drawIntoGround(x, y);
                selectedDrawTool = temp;
            } else if (SwingUtilities.isLeftMouseButton(e))
                drawIntoGround(x, y);
        } else if( !((x >= 24 && x <= 24 + 420) && (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 12)) && !((y < Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT))){
            for (GameObject GO : gameObjects) {
                if (GO.isInArea(x, y)) {
                    if (GO.isInArea(startDraggingX, startDraggingY))
                        GO.setDraggingOnIt(true);
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
        dragstate++;

        for (GameObject GO : gameObjects) {
            if (GO.isDraggingOnIt()) {
                GO.setDraggingOnIt(false);
                if (GO.isInArea(x, y))
                    break;
            }
        }


    }

    @Override
    public void mouseWheelMove(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            changeScale(true);
        } else {
            changeScale(false);
        }
    }


    @Override
    public void pressButton(int code) {
        switch (code) {
            case 9:     //erase all

                Object emptyRoom[] = {Constant.GROUND.SEA, null};

                redo.removeAllElements();

                boolean first_time = true;

                for (int i = 0; i < ground.size(); i++)
                    for (int j = 0; j < ground.get(i).size(); j++) {
                        if (ground.get(i).get(j)[0] != Constant.GROUND.SEA) {
                            if (!first_time) {
                                Object o[] = {new Integer(i), new Integer(j), ground.get(i).get(j)};
                                undo.peek().add(o);
                            } else {
                                ArrayList<Object[]> arr = new ArrayList<Object[]>();
                                Object o[] = {new Integer(i), new Integer(j), ground.get(i).get(j)};
                                arr.add(o);
                                undo.push(arr);
                                first_time = false;
                            }
                        }
                        ground.get(i).set(j, emptyRoom);
                    }

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


            case (2):       //zoomIn
                changeScale(true);
                break;
            case (3):       //zoomOut
                changeScale(false);
                break;

            case 4:         //save
                saveMap();
                break;
            case 1:         //load
                loadMap();
                break;

            case 15:        //decrease
                changeMapSize(false);
                break;
            case 16:        //increase
                changeMapSize(true);
                break;
            case 5:         //undo
                if (!undo.isEmpty()) {

                    ArrayList<Object[]> arr = new ArrayList<Object[]>();

                    for (int i = 0; i < undo.peek().size(); i++) {
                        Object o[] = {undo.peek().get(i)[0], undo.peek().get(i)[1], ground.get((Integer) undo.peek().get(i)[0]).get((Integer) undo.peek().get(i)[1])};
                        ground.get((Integer) undo.peek().get(i)[0]).set((Integer) undo.peek().get(i)[1], (Object[]) undo.peek().get(i)[2]);
                        arr.add(o);
                    }

                    undo.pop();
                    redo.push(arr);

                }
                repaint();
                break;
            case 6:     //redo
                if (!redo.isEmpty()) {

                    ArrayList<Object[]> arr = new ArrayList<Object[]>();

                    for (int i = 0; i < redo.peek().size(); i++) {
                        Object o[] = {redo.peek().get(i)[0], redo.peek().get(i)[1], ground.get((Integer) redo.peek().get(i)[0]).get((Integer) redo.peek().get(i)[1])};
                        ground.get((Integer) redo.peek().get(i)[0]).set((Integer) redo.peek().get(i)[1], (Object[]) redo.peek().get(i)[2]);
                        arr.add(o);
                    }

                    redo.pop();
                    undo.push(arr);

                }
                repaint();
                break;

        }

        if (!inMovableArea && (movingUp || movingDown || movingRight || movingLeft)) {
            if (!inMovablePosition) {
                inMovablePosition = true;
                new Thread(this::moveMap).start();
            }
        }
    }


    private boolean movingUp = false, movingDown = false, movingRight = false, movingLeft = false;
    private boolean inMovablePosition = false;


    private void moveMap() {
        while (isRunningMap && inMovablePosition) {

            int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
            boolean t = false;
            boolean movingFast = movingUp || movingDown || movingRight || movingLeft;
            if ((x <= 5) && x0_inDefaultScale > 2 + minXMap && !movingFast) {
                x0_inDefaultScale -= 3;
                t = true;

            } else if ((x >= Constant.Screen_Width - 5) && x0_inDefaultScale < maxXMap - 2 && !movingFast) {
                x0_inDefaultScale += 3;
                t = true;
            }

            if ((y <= 5) && y0_inDefaultScale > 2 + minYMap && !movingFast) {
                y0_inDefaultScale -= 3;
                t = true;
            } else if ((y >= Constant.Screen_Height - 5) && y0_inDefaultScale < maxYMap - 2 && !movingFast) {
                y0_inDefaultScale += 3;
                t = true;
            }

            repaint();

            if (t) {
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e1) {
                }
            }


            if ((movingLeft) && x0_inDefaultScale > 2 + minXMap) {
                x0_inDefaultScale -= 3;
                t = true;

                try {
                    TimeUnit.NANOSECONDS.sleep(x0 / 4);
                } catch (InterruptedException e1) {
                }
                repaint();
            } else if ((movingRight) && x0_inDefaultScale < maxXMap - 2) {
                x0_inDefaultScale += 3;
                t = true;

                try {
                    TimeUnit.NANOSECONDS.sleep((maxXMap - x0_inDefaultScale) / 4);
                } catch (InterruptedException e1) {
                }
                repaint();
            }

            if ((movingUp) && y0_inDefaultScale > 2 + minYMap) {
                y0_inDefaultScale -= 3;
                t = true;

                try {
                    TimeUnit.NANOSECONDS.sleep(y0_inDefaultScale / 4);
                } catch (InterruptedException e1) {
                }
                repaint();
            } else if ((movingDown) && y0_inDefaultScale < maxYMap - 2) {
                y0_inDefaultScale += 3;
                t = true;

                try {
                    TimeUnit.NANOSECONDS.sleep((maxYMap - y0_inDefaultScale) / 4);
                } catch (InterruptedException e1) {
                }
                repaint();
            }


            if (x0_inDefaultScale <= 2 + minXMap) {
                movingLeft = false;
            } else if (x0_inDefaultScale >= maxXMap - 2) {
                movingRight = false;
            }
            if (y0_inDefaultScale <= 2 + minYMap) {
                movingUp = false;
            } else if (y0_inDefaultScale >= maxYMap - 2) {
                movingDown = false;
            }


            movingFast = movingUp || movingDown || movingRight || movingLeft;
            if (!movingFast && !inMovableArea)
                inMovablePosition = false;

            x0_inDefaultScale = x0_inDefaultScale - x0_inDefaultScale % 3;
            y0_inDefaultScale = y0_inDefaultScale - y0_inDefaultScale % 3;

            repaint();


        }


    }

    public static boolean isRunningMap() {
        return isRunningMap;
    }

    public static void setRunningMap(boolean isRunningMap) {
        Panel.isRunningMap = isRunningMap;
    }

    public void selectDrawTool(int code) {

        if (selectedDrawTool != -1) {
            for (GameObject GO : gameObjects)
                if (GO instanceof DrawToolButton && ((DrawToolButton) GO).getCode() == selectedDrawTool) {
                    ((DrawToolButton) GO).select(false);
                }
        }

        selectedDrawTool = code;

        repaint();
    }

    public void unSelectDrawTool() {
        selectedDrawTool = -1;
    }


    private int chooseLandImageNumber(Constant.GROUND groundType, int x, int y) {

        int k = 0;

        if (y > 0 && (ground.get(x).get(y - 1)[0] == groundType || ground.get(x).get(y - 1)[0] == Constant.GROUND.HIGHLAND))   //up
            k += 1;
        if (x < ground.size() - 1 && (ground.get(x + 1).get(y)[0] == groundType || ground.get(x + 1).get(y)[0] == Constant.GROUND.HIGHLAND))   //right
            k += 2;
        if (y < ground.get(x).size() - 1 && (ground.get(x).get(y + 1)[0] == groundType || ground.get(x).get(y + 1)[0] == Constant.GROUND.HIGHLAND))   //down
            k += 4;
        if (x > 0 && (ground.get(x - 1).get(y)[0] == groundType || ground.get(x - 1).get(y)[0] == Constant.GROUND.HIGHLAND))   //left
            k += 8;


        return k;

    }

    private void changeSeason() {
        season = (season + 1) % 4;
        repaint();
    }

    private void changeScale(boolean in) {   // in = true -> zoomIn  , in = false -> zoomOut

        scale = (in) ? (scale + Constant.ONE_SCALE_CHANGING) : (scale - Constant.ONE_SCALE_CHANGING);
        if (scale < 1f)
            scale = 1f;
        else if (scale > Constant.MAX_OF_SCALE)
            scale = Constant.MAX_OF_SCALE;

        minXMap = -(Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
        minYMap = -(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;
        maxXMap = mapSize.width - Constant.DEFAULT_SCREEN_SIZE.width - minXMap;
        maxYMap = mapSize.height - (Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/) - minYMap; // ?

        if (x0_inDefaultScale > maxXMap)
            x0_inDefaultScale = maxXMap;
        if (y0_inDefaultScale > maxYMap)
            y0_inDefaultScale = maxYMap;
        if (x0_inDefaultScale < minXMap)
            x0_inDefaultScale = minXMap;
        if (y0_inDefaultScale < minYMap)
            y0_inDefaultScale = minYMap;

        repaint();
    }

    private void changeMapSize(boolean increase) {

        // one change in map width = 8 ground , one change in map height = 7 ground

        if (increase == false) {       //deccrease
            mapSize = new Dimension(mapSize.width - Constant.ONE_MAP_SIZE_CHANGING.width, mapSize.height - Constant.ONE_MAP_SIZE_CHANGING.height);

            if (Constant.MIN_MAP_SIZE.width > mapSize.width)
                mapSize = Constant.MIN_MAP_SIZE;
            else {

                for (int i = 0; i < Constant.ONE_MAP_NUM_CHANGING.width; i++)
                    ground.removeLast();

                for (int i = 0; i < ground.size(); i++)
                    for (int j = 0; j < Constant.ONE_MAP_NUM_CHANGING.height; j++)
                        ground.get(i).removeLast();
            }

        } else {                      //increase
            mapSize = new Dimension(mapSize.width + Constant.ONE_MAP_SIZE_CHANGING.width, mapSize.height + Constant.ONE_MAP_SIZE_CHANGING.height);

            if (Constant.MAX_MAP_SIZE.width < mapSize.width)
                mapSize = Constant.MAX_MAP_SIZE;
            else {
                Object[] emptyRoom = {Constant.GROUND.SEA, null};

                for (int i = 0; i < ground.size(); i++)
                    for (int j = 0; j < Constant.ONE_MAP_NUM_CHANGING.height; j++)
                        ground.get(i).add(emptyRoom);

                for (int i = 0; i < Constant.ONE_MAP_NUM_CHANGING.width; i++) {
                    LinkedList<Object[]> col = new LinkedList<>();
                    for (int j = 0; j < ground.get(0).size(); j++) {
                        col.add(emptyRoom);
                    }
                    ground.add(col);
                }
            }
        }

        minXMap = -(Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
        minYMap = -(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;
        maxXMap = mapSize.width - Constant.DEFAULT_SCREEN_SIZE.width - minXMap;
        maxYMap = mapSize.height - (Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/) - minYMap; // ?

        if (x0_inDefaultScale > maxXMap)
            x0_inDefaultScale = maxXMap;
        if (y0_inDefaultScale > maxYMap)
            y0_inDefaultScale = maxYMap;
        if (x0_inDefaultScale < minXMap)
            x0_inDefaultScale = minXMap;
        if (y0_inDefaultScale < minYMap)
            y0_inDefaultScale = minYMap;


        scaleScreen = (double) mapSize.width / 400;  // mini map


        Constant.MAP_SIZE = mapSize;

        repaint();
    }


    private static final javax.swing.filechooser.FileFilter FILE_FILTER = new javax.swing.filechooser.FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname == null || pathname.toString() == null || pathname.toString().replaceAll(" ", "") == null)
                return false;
            if (pathname.isDirectory())
                return true;
            return pathname.toString().endsWith(".tmf");    //  Thrumania map file
        }

        @Override
        public String getDescription() {
            return "Thrumania map file (.tmf)";
        }
    };

    /**
     * this method save map
     */
    private void saveMap() {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("save");
            jFileChooser.setFileFilter(FILE_FILTER);
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.setCurrentDirectory(new File("src/res/maps"));
            int result = jFileChooser.showSaveDialog(null);
            if (result == 0) {
                FileOutputStream out = new FileOutputStream(jFileChooser.getSelectedFile());
                ObjectOutputStream oout = new ObjectOutputStream(out);
                oout.writeObject(ground);
                oout.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * this method load map
     */

    private void loadMap() {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("load");
            jFileChooser.setFileFilter(FILE_FILTER);
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.setCurrentDirectory(new File("src/res/maps"));
            int result = jFileChooser.showOpenDialog(null);
            if (result == 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(jFileChooser.getSelectedFile()));
                ground = (LinkedList<LinkedList<Object[]>>) ois.readObject();
            }
        } catch (Exception e) {

        }
    }

    private void erase(int i, int j) {

        Object[] room = new Object[2];

        if (ground.get(i).get(j)[1] != null) {
            room[0] = ground.get(i).get(j)[0];
            room[1] = null;
            ground.get(i).set(j, room);
        } else if (ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
            room[0] = Constant.GROUND.LOWLAND;
            room[1] = null;
            ground.get(i).set(j, room);
        } else if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND) {
            setSeaInGround(i, j);
        }

        repaint();

    }

    private void setSeaInGround(int i, int j) {
        Object[] room = new Object[2];
        room[0] = Constant.GROUND.SEA;
        room[1] = null;
        ground.get(i).set(j, room);

        if (i > 0 && j > 0 && ground.get(i - 1).get(j - 1)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i - 1), new Integer(j - 1), ground.get(i - 1).get(j - 1)};
            undo.peek().add(o);
            ground.get(i - 1).set(j - 1, room1);
        }
        if (j > 0 && ground.get(i).get(j - 1)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i), new Integer(j - 1), ground.get(i).get(j - 1)};
            undo.peek().add(o);
            ground.get(i).set(j - 1, room1);
        }
        if (i < mapSize.width - 1 && j > 0 && ground.get(i + 1).get(j - 1)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i + 1), new Integer(j - 1), ground.get(i + 1).get(j - 1)};
            undo.peek().add(o);
            ground.get(i + 1).set(j - 1, room1);
        }
        if (i > 0 && ground.get(i - 1).get(j)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i - 1), new Integer(j), ground.get(i - 1).get(j)};
            undo.peek().add(o);
            ground.get(i - 1).set(j, room1);
        }
        if (i < mapSize.width - 1 && ground.get(i + 1).get(j)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i + 1), new Integer(j), ground.get(i + 1).get(j)};
            undo.peek().add(o);
            ground.get(i + 1).set(j, room1);
        }
        if (i > 0 && j < mapSize.height - 1 && ground.get(i - 1).get(j + 1)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i - 1), new Integer(j + 1), ground.get(i - 1).get(j + 1)};
            undo.peek().add(o);
            ground.get(i - 1).set(j + 1, room1);
        }
        if (j < mapSize.height - 1 && ground.get(i).get(j + 1)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i), new Integer(j + 1), ground.get(i).get(j + 1)};
            undo.peek().add(o);
            ground.get(i).set(j + 1, room1);
        }
        if (i < mapSize.width - 1 && j < mapSize.height - 1 && ground.get(i + 1).get(j + 1)[0] == Constant.GROUND.HIGHLAND) {
            Object[] room1 = {Constant.GROUND.LOWLAND, null};
            Object o[] = {new Integer(i + 1), new Integer(j + 1), ground.get(i + 1).get(j + 1)};
            undo.peek().add(o);
            ground.get(i + 1).set(j + 1, room1);
        }
    }


    private void miniMapMovingRect(int x, int y) {
        if (x >= 24 && x <= 24 + widthRecFill / 2) {
            if (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 260 + heightRecFill / 2) {
                x0 = 0;
                y0 = 0;
                x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
                y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;
            } else if (y > Constant.Screen_Height - 260 + heightRecFill / 2 && y < Constant.Screen_Height - 12 - heightRecFill / 2) {
                x0 = 0;
                y0 = (int) ((y - (Constant.Screen_Height - 260) - heightRecFill / 2) * scaleScreen);
                x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
                y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

            } else if (y <= Constant.Screen_Height - 12 && y >= Constant.Screen_Height - 12 - heightRecFill / 2) {
                x0 = 0;
                y0 = (int) (mapSize.height - heightRecFill * scaleScreen);
                x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
                y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

            }
        } else if (x >= 24 + 405 - widthRecFill / 2 && x <= 24 + 405) {
            if (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 260 + heightRecFill / 2) {
                x0 = (int) (mapSize.width - widthRecFill * scaleScreen);
                y0 = 0;
                x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
                y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;
            } else if (y > Constant.Screen_Height - 260 + heightRecFill / 2 && y < Constant.Screen_Height - 12 - heightRecFill / 2) {
                x0 = (int) (mapSize.width - widthRecFill * scaleScreen);
                y0 = (int) ((y - (Constant.Screen_Height - 260) - heightRecFill / 2) * scaleScreen);
                x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
                y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

            } else if (y <= Constant.Screen_Height - 12 && y >= Constant.Screen_Height - 12 - heightRecFill / 2) {
                x0 = (int) (mapSize.width - widthRecFill * scaleScreen);
                y0 = (int) (mapSize.height - (heightRecFill * scaleScreen));
                x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
                y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

            }

        } else if (x > 24 + widthRecFill / 2 && x < 24 + 405 - widthRecFill / 2 && y > Constant.Screen_Height - 260 && y < Constant.Screen_Height - 260 + heightRecFill / 2) {
            x0 = (int) ((x - 24 - widthRecFill / 2) * scaleScreen);
            y0 = 0;
            x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
            y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

        } else if (x > 24 + (widthRecFill / 2) && x < 24 + 405 - (widthRecFill / 2) && y < Constant.Screen_Height - 12 && y > Constant.Screen_Height - 12 - heightRecFill / 2) {
            x0 = (int) ((x - 24 - widthRecFill / 2) * scaleScreen);
            y0 = (int) (mapSize.height - (heightRecFill * scaleScreen));
            x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
            y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;
        } else if (x > 24 + widthRecFill / 2 && x < 24 + 405 - widthRecFill / 2 && y > Constant.Screen_Height - 260 + heightRecFill / 2 && y < Constant.Screen_Height - 12 - heightRecFill / 2) {
            x0 = (int) ((x - 24 - widthRecFill / 2) * scaleScreen);
            y0 = (int) ((y - (Constant.Screen_Height - 260) - heightRecFill / 2) * scaleScreen);
            x0_inDefaultScale = x0 - (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
            y0_inDefaultScale = y0 - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

        }
    }

    private void drawMiniMap(Graphics g) {
        //<-- minimap
        int xTransfer = 24;
        int yTransfer = 820;

        Color color = new Color(40, 130, 240);
        g.setColor(color);
        g.fillRect(23, 820, 425, 270);

        for (int i = 0; i < ground.size(); i++) {
            for (int j = 0; j < ground.get(i).size(); j++) {
                Constant.GROUND thisGround = (Constant.GROUND) ground.get(i).get(j)[0];
                switch (thisGround) {
                    case LOWLAND:
                        int imageNum = chooseLandImageNumber(Constant.GROUND.LOWLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + imageNum + ".png").getImage(), (int) (xTransfer + ((double) (i * Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen)), (int) (yTransfer + ((double) (j * Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen)), (int) ((double) Constant.MIN_WIDTH_OF_EACH_GROUND / scaleScreen), (int) ((double) Constant.MIN_HEIGHT_OF_EACH_GROUND / scaleScreen), null);

//                        if (ground.get(i).get(j)[1] != null) {
//                            switch ((Constant.OBJECT) ground.get(i).get(j)[1]) {
//                                case TREE1:
//                                    g.drawImage(new ImageIcon("src/res/images/map/tree/1/" + season + ".png").getImage(), (int) (xTransfer + ((double) (i * Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen)), (int) (yTransfer + ((double) (j * Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen)), (int) (Constant.MIN_WIDTH_OF_EACH_GROUND / scaleScreen), (int) (Constant.MIN_HEIGHT_OF_EACH_GROUND / scaleScreen), null);
//                                    break;
//                                case TREE2:
//                                    g.drawImage(new ImageIcon("src/res/images/map/tree/2/" + season + ".png").getImage(), (int) (xTransfer + ((double) (i * Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen)), (int) (yTransfer + ((double) (j * Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen)), (int) (Constant.MIN_WIDTH_OF_EACH_GROUND / scaleScreen), (int) (Constant.MIN_HEIGHT_OF_EACH_GROUND / scaleScreen), null);
//                                    break;
//                                case FARMLAND:
//                                    break;
//                            }
//                        }
                        break;
                    case HIGHLAND:
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + 15 + ".png").getImage(), (int) (xTransfer + ((double) (i * Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen)), (int) (yTransfer + ((double) (j * Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen)), (int) ((double) (Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen), (int) ((double) (Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen), null);
                        int imageNum2 = chooseLandImageNumber(Constant.GROUND.HIGHLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/highLand/" + imageNum2 + ".png").getImage(), (int) (xTransfer + ((double) (i * Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen)), (int) (yTransfer + ((double) (j * Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen)), (int) ((double) (Constant.MIN_WIDTH_OF_EACH_GROUND) / scaleScreen), (int) ((double) (Constant.MIN_HEIGHT_OF_EACH_GROUND) / scaleScreen), null);
                        break;
                }
            }
        }

        //rectangle
        Color color2 = new Color(220, 20, 40, 130);
        g.setColor(color2);
        xRecFill = (int) (xTransfer + ((double) (x0) / scaleScreen));
        yRecFill = (int) (yTransfer + ((double) (y0) / scaleScreen));
        widthRecFill = (int) (((double) (Constant.Screen_Width) / scaleScreen) / scale);
        heightRecFill = (int) (((double) (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT) / scaleScreen) / scale);
        g.fillRect(xRecFill, yRecFill, widthRecFill, heightRecFill);
        //minimap -->
    }
}


