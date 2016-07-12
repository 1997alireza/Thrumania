package com.thrumania.src.game;

import com.thrumania.src.GraphicHandler;
import com.thrumania.src.Tools.Division;
import com.thrumania.src.game.objects.Human;
import com.thrumania.src.gameSpace.*;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by AliReza on 23/05/2016.
 */
public class Panel  implements GraphicHandler{
    private com.thrumania.src.draw.GamePanel drawPanel;
    private LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground;
    private  Constant.PLAYER_TYPE playerType;
    private int playerNumbers;
    private static boolean isRunningGame;


    private int season = 0; // 0->spring , 1->summer , 2->autumn , 3->winter
    private float scale;
    private static int x0_inDefaultScale, y0_inDefaultScale;
    private static int x0, y0;
    private static Dimension mapSize;  // height / width = 8 / 5
    private static int maxXMap, maxYMap;
    private static int minXMap, minYMap;

    // <-- mini map
    private double scaleScreen;
    private int widthRecFill;
    private int heightRecFill;
    private int xRecFill;
    private int yRecFill;
    // mini map -->

    private LinkedList <Human> humans ;

    private boolean aHumanIsSelected;

    public Panel(LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground , Constant.PLAYER_TYPE playerType , int playerNumbers , com.thrumania.src.draw.GamePanel drawPanel){
        this.drawPanel = drawPanel;
        isRunningGame = true;
        this.drawPanel.game_panel = this;
        this.ground = ground;
        this.playerNumbers = playerNumbers;
        this.playerType = playerType;



        this.mapSize = Constant.MAP_SIZE;

        scaleScreen = (double) mapSize.width / 400;  // mini map

        maxXMap = mapSize.width - Constant.DEFAULT_SCREEN_SIZE.width;
        maxYMap = mapSize.height - (Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/);
        minXMap = -(Constant.Screen_Width - Constant.Screen_Width) / 2;
        minYMap = -(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT)) / 2;

        scale = 1f;
        x0 = 0;
        y0 = 0;
        x0_inDefaultScale = x0;
        y0_inDefaultScale = y0;


        aHumanIsSelected = false;

        humans = new LinkedList<>();

        for(int i=0;i<ground.size();i++)
            for(int j=0;j<ground.get(0).size();j++)
            {
                if(ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_ONE || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_TWO || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_THREE || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_FOUR)
                {
                    humans.add(new Human(ground,i*3 + 1 , j*3 + 1));
                }
            }


        new Thread(this :: changeSeason).start();
    }

    @Override
    public void render(Graphics g) {

        x0 = x0_inDefaultScale + (Constant.Screen_Width - Division.division(Constant.Screen_Width, scale)) / 2;
        y0 = y0_inDefaultScale + (Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT - Division.division(Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT, scale)) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(scale, scale);


        // for SEA ground
        for (int i = 0; i < Constant.NUM_OF_SEA_IN_EACH_ROW; i++)
            for (int j = 0; j < Constant.NUM_OF_SEA_IN_EACH_COLUMN; j++) {
                g.drawImage(new ImageIcon("src/res/images/map/ground/sea.jpg").getImage(), -(x0 % Constant.MIN_WIDTH_OF_EACH_SEA) + i * Constant.MIN_WIDTH_OF_EACH_SEA, -(y0 % Constant.MIN_HEIGHT_OF_EACH_SEA) + j * Constant.MIN_HEIGHT_OF_EACH_SEA, Constant.MIN_WIDTH_OF_EACH_SEA, Constant.MIN_HEIGHT_OF_EACH_SEA, null);
            }


        // for LOWLAND and HIGHLAND ground
        for (int i = x0 / Constant.MIN_WIDTH_OF_EACH_GROUND; i </*Division.division*/ (Constant.DEFAULT_SCREEN_SIZE.width / (scale * Constant.MIN_WIDTH_OF_EACH_GROUND)) + x0 / Constant.MIN_WIDTH_OF_EACH_GROUND + 1 && i < ground.size(); i++)
            for (int j = y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND; j < Division.division(Constant.DEFAULT_SCREEN_SIZE.height - Constant.BOTTOM_FRAME_HEIGHT/* for bottom panel*/, scale * Constant.MIN_HEIGHT_OF_EACH_GROUND) + Division.division(y0, Constant.MIN_HEIGHT_OF_EACH_GROUND) + 1 + 1/*for tall objects*/ && j < ground.get(i).size(); j++) {
                Constant.GROUND thisGround = (Constant.GROUND) ground.get(i).get(j)[0];
                switch (thisGround) {
                    case LOWLAND:
                        int imageNum = chooseLandImageNumber(Constant.GROUND.LOWLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + imageNum + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        break;

                    case HIGHLAND:
                        g.drawImage(new ImageIcon("src/res/images/map/ground/lowLand/" + season + "-" + 15 + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        //for the lowLand under the highLand

                        int imageNum2 = chooseLandImageNumber(Constant.GROUND.HIGHLAND, i, j);
                        g.drawImage(new ImageIcon("src/res/images/map/ground/highLand/" + imageNum2 + ".png").getImage(), -x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, Constant.MIN_WIDTH_OF_EACH_GROUND, Constant.MIN_HEIGHT_OF_EACH_GROUND, null);
                        break;

                }
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

                        ArrayList <Human> thisHumans = getHuman(i,j,false);
                        for(Human human : thisHumans)
                            g.drawImage(human.getImage() , human.getX() - x0, human.getY() - y0 , null);

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

                                case CASTLE_ONE:
                                    g.drawImage(new ImageIcon("src/res/images/map/castle/1.png").getImage(), 35 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -7 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 133, 120, null);
                                    break;
                                case CASTLE_TWO:
                                    g.drawImage(new ImageIcon("src/res/images/map/castle/2.png").getImage(), 35 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -15 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 125, 125, null);
                                    break;
                                case CASTLE_THREE:
                                    g.drawImage(new ImageIcon("src/res/images/map/castle/3.png").getImage(), 35 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -10 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 125, 125, null);
                                    break;
                                case CASTLE_FOUR:
                                    g.drawImage(new ImageIcon("src/res/images/map/castle/4.png").getImage(), 35 - x0 % Constant.MIN_WIDTH_OF_EACH_GROUND + (i - x0 / Constant.MIN_WIDTH_OF_EACH_GROUND) * Constant.MIN_WIDTH_OF_EACH_GROUND, -10 - y0 % Constant.MIN_HEIGHT_OF_EACH_GROUND + (j - y0 / Constant.MIN_HEIGHT_OF_EACH_GROUND) * Constant.MIN_HEIGHT_OF_EACH_GROUND, 129, 100, null);
                                    break;
                            }

                        }

                        thisHumans = getHuman(i,j,true);
                        for(Human human : thisHumans)
                            g.drawImage(human.getImage() , human.getX() - x0, human.getY() - y0 , null);


                        break;
                    case HIGHLAND:

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



        repaint();

    }

    @Override
    public void updateComponents() {

        repaint();

    }


    @Override
    public void repaint() {
        drawPanel.repaint();
    }


    @Override
    public void mouseClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();


        if ((x >= 24 && x <= 24 + 420) && (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 12))
            miniMapMovingRect(x, y);

        else if (y >= Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT) {
//            for (GameObject GO : gameObjects) {
//                if (GO.isInArea(x, y)) {
//                    GO.mouseClicked();
//                    break;
//                }
//            }
        }
        else {

            int xInMAp = (x0 + (int) (x / scale)) ;
            int yInMap = (y0 + (int) (y / scale)) ;

            boolean t = false;
            for(Human human : humans){
                if(human.isInArea(xInMAp,yInMap))
                {
                    if(human.isSelected()){
                        human.setSelected(false);
                        aHumanIsSelected = false;
                    }

                    else{

                        for(Human human2 : humans){
                            human2.setSelected(false);
                        }

                        human.setSelected(true);
                        aHumanIsSelected = true;
                    }
                    t = true;
                    break;
                }

            }

            if(!t && aHumanIsSelected){
                for(Human human : humans){
                    if(human.isSelected()){
                        human.findWay(xInMAp*3/Constant.MIN_WIDTH_OF_EACH_GROUND,yInMap*3/Constant.MIN_HEIGHT_OF_EACH_GROUND);
                        break;
                    }
                }
            }



        }

    }

    @Override
    public void mouseEnter(int x, int y) {
        if (x <= 5 || x >= Constant.Screen_Width - 5 || y <= 5 || y >= Constant.Screen_Height - 5) {
            if (!inMovablePosition) {
                inMovablePosition = true;
                inMovableArea = true;
                new Thread(this::moveMap).start();
            }
        } else {
            inMovablePosition = false;
            inMovableArea = false;
        }

    }

    @Override
    public void mouseExit(int x, int y) {

    }
    @Override
    public void mouseDrag(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if ((startDraggingX >= 24 && startDraggingX <= 24 + 420) && (startDraggingY >= Constant.Screen_Height - 260 && startDraggingY <= Constant.Screen_Height - 12) && (x >= 24 && x <= 24 + 420) && (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 12))
            miniMapMovingRect(x, y);


//        else if ( (startDraggingY < Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT) && (y < Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT)) {
//            if (SwingUtilities.isRightMouseButton(e)) {
//                int temp = selectedDrawTool;
//                selectedDrawTool = 8;
//                drawIntoGround(x, y);
//                selectedDrawTool = temp;
//            } else if (SwingUtilities.isLeftMouseButton(e))
//                drawIntoGround(x, y);
//        } else if( !((x >= 24 && x <= 24 + 420) && (y >= Constant.Screen_Height - 260 && y <= Constant.Screen_Height - 12)) && !((y < Constant.Screen_Height - Constant.BOTTOM_FRAME_HEIGHT))){
//            for (GameObject GO : gameObjects) {
//                if (GO.isInArea(x, y)) {
//                    if (GO.isInArea(startDraggingX, startDraggingY))
//                        GO.setDraggingOnIt(true);
//                    break;
//                }
//            }
//        }

        repaint();

    }

    @Override
    public void mousePress( int x, int y) {
        startDraggingX = x;
        startDraggingY = y;

    }
    @Override
    public void mouseRelease(int x, int y) {
        startDraggingX = -1;
        startDraggingY = -1;

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
    public void pressButton(int code) { // inja bar asase codi ke be buttone tuye panele payino rast dadim migim chikar kone

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


    private boolean inMovablePosition = false;

    private int startDraggingX = -1;
    private int startDraggingY = -1;
    private boolean inMovableArea = false;
    
    private void moveMap() {
        while (isRunningGame && inMovablePosition) {

            int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
            int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
            boolean t = false;
            if ((x <= 5) && x0_inDefaultScale > 2 + minXMap) {
                x0_inDefaultScale -= 3;
                t = true;

            } else if ((x >= Constant.Screen_Width - 5) && x0_inDefaultScale < maxXMap - 2) {
                x0_inDefaultScale += 3;
                t = true;
            }

            if ((y <= 5) && y0_inDefaultScale > 2 + minYMap) {
                y0_inDefaultScale -= 3;
                t = true;
            } else if ((y >= Constant.Screen_Height - 5) && y0_inDefaultScale < maxYMap - 2) {
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



            if (!inMovableArea)
                inMovablePosition = false;

            x0_inDefaultScale = x0_inDefaultScale - x0_inDefaultScale % 3;
            y0_inDefaultScale = y0_inDefaultScale - y0_inDefaultScale % 3;

            repaint();


        }


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


    public static boolean isRunningGame() {
        return isRunningGame;
    }

    public static void setRunningGame(boolean isRunningGame) {
        Panel.isRunningGame = isRunningGame;
    }

    private void changeSeason(){
        while(isRunningGame) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            season = (season + 1) % 4;

            repaint();
        }
    }

    private ArrayList<Human> getHuman(int i,int j , boolean isDownerThanTree) {
        i *= 3;
        j *= 3;

        ArrayList<Human> thisHumans = new ArrayList<>();

        for (Human human : humans) {
            if (isDownerThanTree && ((human.getlocation().width == i || human.getlocation().width == i + 1 || human.getlocation().width == i + 2) && human.getlocation().height == j + 2))
                thisHumans.add(human);
            else if (!isDownerThanTree && ((human.getlocation().width == i || human.getlocation().width == i + 1 || human.getlocation().width == i + 2) && (human.getlocation().height == j || human.getlocation().height == j + 1)))
                thisHumans.add(human);
        }

        return thisHumans;
    }

}
