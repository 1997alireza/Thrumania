package com.thrumania.src.game.objects;

import com.thrumania.src.gameSpace.*;
import com.thrumania.src.gameSpace.Rectangle;
import res.values.Constant;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by ali on 7/4/2016.
 */

public class Human {

    private Dimension location;
    private Dimension locationInMap;
    private int speed;

    private Area area;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private int imageCounter = 0;
    private boolean isSelected;
    private LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground;
    private LinkedList<LinkedList<Boolean>> binaryMap;
    private LinkedList<LinkedList<Vertex>> ver;

    private boolean isInMove = false;


    private java.util.List<Vertex> path;

    private LinkedList<Integer>  direction;

    public Human(LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground  , int x , int y /* x , y bar asase binaryMap -> any room *= 9 */) {
        this.ground = ground;
        binaryMap = new LinkedList<LinkedList<Boolean>>();
        direction = new LinkedList<Integer>();
        location = new Dimension(x,y);

        locationInMap = new Dimension(location.width*Constant.MIN_WIDTH_OF_EACH_GROUND/3 - 10,location.height*Constant.MIN_HEIGHT_OF_EACH_GROUND/3 - 53);

        area = new Rectangle(locationInMap.width + 40,locationInMap.height + 20 , 20 , 60 );

        vector = VECTOR.LEFT;

        convertMap();
    }

    public static enum PLAYER_STATE {
        STANDING(0), MOVIMG(1), FIGHTING(2), DYING(3);
        private int code;

        PLAYER_STATE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private enum VECTOR{
        UP(0) , UP_RIGHT(1) , RIGHT(2) , DOWN_RIGHT(3) , DOWN(4) , DOWN_LEFT(5) , LEFT(6) , UP_LEFT(7);
        VECTOR(int code){
            this.code = code;
        }

        private int code;

        public int getCode() {
            return code;
        }

    }

    private VECTOR vector;

    public void convertMap() {
        binaryMap = new LinkedList<>();
        for(int i=0;i<ground.size()*3;i++) {
            LinkedList <Boolean>  row = new LinkedList();
            for(int j=0;j<ground.get(0).size()*3;j++)
                row.add(false);
            binaryMap.add(row);

        }
        for (int i = 0; i < ground.size(); i++) {
            for (int j = 0; j < ground.get(i).size(); j++) {

                if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND && (ground.get(i).get(j)[1] == null  || ground.get(i).get(j)[1] == Constant.OBJECT.FARMLAND || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_ONE || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_TWO || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_THREE || ground.get(i).get(j)[1] == Constant.OBJECT.CASTLE_FOUR)) {
                    for (int k = 3 * i; k < 3 * i + 3; k++) {
                        for (int l = 3 * j; l < 3 * j + 3; l++) {
                            binaryMap.get(k).set(l, true);
                        }
                    }
                }

                else if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND && ( ground.get(i).get(j)[1] == Constant.OBJECT.TREE1 || ground.get(i).get(j)[1] == Constant.OBJECT.TREE2 )) {
                    for (int k = 3 * i; k < 3 * i + 3; k++) {
                        for (int l = 3 * j; l < 3 * j + 3; l++) {
                            binaryMap.get(k).set(l, true);
                        }
                        binaryMap.get(3*i+1).set(3*j+1,false);
                    }
                    binaryMap.get(3 * i + 1).set(3 * j + 1, false);
                }

                else if (ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
                    if (/*kohnavard*/ false) {///sharrt
                        for (int k = 3 * i; k < 3 * i + 3; k++) {
                            for (int l = 3 * j; l < 3 * j + 3; l++) {
                                binaryMap.get(k).set(l, true);
                            }
                        }
                    }
                }
            }
        }
    }

    public void findWay(int x, int y) {
        ver = new LinkedList<LinkedList<Vertex>>();


        for (int i = 0; i < binaryMap.size(); i++) {
            LinkedList row = new LinkedList();

            for(int j=0;j<binaryMap.get(0).size();j++)
                row.add(null);
            ver.add(row);
            for (int j = 0; j < binaryMap.get(i).size(); j++) {
                ver.get(i).set(j, new Vertex(i + " " + j));
            }
        }



        for (int i = 0; i < binaryMap.size(); i++) {
            for (int j = 0; j < binaryMap.get(i).size(); j++) {
                ArrayList <Edge> e = new ArrayList<>();
                if(binaryMap.get(i).get(j)) {
                    if (i > 0 && binaryMap.get(i - 1).get(j)) {
                        e.add(new Edge(ver.get(i - 1).get(j), 1));
                    }
                    if (i < binaryMap.size() - 1 && binaryMap.get(i + 1).get(j)) {
                        e.add(new Edge(ver.get(i + 1).get(j), 1));
                    }
                    if (j > 0 && binaryMap.get(i).get(j - 1)) {
                        e.add(new Edge(ver.get(i).get(j - 1), 1));
                    }
                    if (j < binaryMap.get(0).size() - 1 && binaryMap.get(i).get(j + 1)) {
                        e.add(new Edge(ver.get(i).get(j + 1), 1));
                    }
                    if (i > 0 && j > 0 && binaryMap.get(i - 1).get(j - 1) && ( binaryMap.get(i-1).get(j) || binaryMap.get(i).get(j-1) || ground.get((i-1)/3).get((j)/3)[0]== Constant.GROUND.HIGHLAND || ground.get((i)/3).get((j-1)/3)[0]== Constant.GROUND.HIGHLAND)) {
                        e.add(new Edge(ver.get(i - 1).get(j - 1), Math.sqrt(2)));
                    }
                    if (i > 0 && j < binaryMap.get(0).size() - 1 && binaryMap.get(i - 1).get(j + 1) && ( binaryMap.get(i-1).get(j) || binaryMap.get(i).get(j+1) || ground.get((i-1)/3).get((j)/3)[0]== Constant.GROUND.HIGHLAND || ground.get((i)/3).get((j+1)/3)[0]== Constant.GROUND.HIGHLAND)) {
                        e.add(new Edge(ver.get(i - 1).get(j + 1), Math.sqrt(2)));
                    }
                    if (i < binaryMap.size() - 1 && j > 0 && binaryMap.get(i + 1).get(j - 1) && ( binaryMap.get(i+1).get(j) || binaryMap.get(i).get(j-1) || ground.get((i+1)/3).get((j)/3)[0]== Constant.GROUND.HIGHLAND || ground.get((i)/3).get((j-1)/3)[0]== Constant.GROUND.HIGHLAND)) {
                        e.add(new Edge(ver.get(i + 1).get(j - 1), Math.sqrt(2)));
                    }
                    if (i < binaryMap.size() - 1 && j < binaryMap.get(0).size() - 1 && binaryMap.get(i + 1).get(j + 1)  && ( binaryMap.get(i+1).get(j) || binaryMap.get(i).get(j+1) || ground.get((i+1)/3).get((j)/3)[0]== Constant.GROUND.HIGHLAND || ground.get((i)/3).get((j+1)/3)[0]== Constant.GROUND.HIGHLAND) ) {
                        e.add(new Edge(ver.get(i + 1).get(j + 1), Math.sqrt(2)));
                    }



                    if (e.size() == 0) {
                        ver.get(i).get(j).adjacencies = null;
                    }
                    else {
                        Edge[] arrayOfEdge = new Edge[e.size()];
                        for (int k = 0; k < e.size(); k++)
                            arrayOfEdge[k] = e.get(k);
                        ver.get(i).get(j).adjacencies = arrayOfEdge;
                    }


                }
            }
        }
        Dijkstra.computePaths(ver.get(location.width).get(location.height));
        List<Vertex> thisPath;
        thisPath = Dijkstra.getShortestPathTo(ver.get(x).get(y));

        if(thisPath.get(0).minDistance != Double.POSITIVE_INFINITY)
            path = thisPath;

        if(path != null && thisPath.get(0).minDistance != Double.POSITIVE_INFINITY)
            new Thread(this :: move).start();
        else
            isInMove = false;


    }

    public void move() {

        isInMove = false;
        synchronized (this) {
            isInMove = true;
            new Thread(this :: imageCount).start();
            path.remove(0);
            while (isInMove && !path.isEmpty()) {

                Vertex nextVertex = path.get(0);
                path.remove(0);
                int dx = nextVertex.getX() - nextVertex.previous.getX();
                int dy = nextVertex.getY() - nextVertex.previous.getY();

                if (dx == -1 && dy == -1)
                    vector = VECTOR.UP_LEFT;
                else if (dx == -1 && dy == 0)
                    vector = VECTOR.LEFT;
                else if (dx == -1 && dy == 1)
                    vector = VECTOR.DOWN_LEFT;
                else if (dx == 0 && dy == -1)
                    vector = VECTOR.UP;
                else if (dx == 0 && dy == 1)
                    vector = VECTOR.DOWN;
                else if (dx == 1 && dy == -1)
                    vector = VECTOR.UP_RIGHT;
                else if (dx == 1 && dy == 0)
                    vector = VECTOR.RIGHT;
                else if (dx == 1 && dy == 1)
                    vector = VECTOR.DOWN_RIGHT;

                location = new Dimension(location.width + dx, location.height + dy);
                boolean keepMoving = true;
                while (keepMoving) {

                    keepMoving = false;
                    if(!(locationInMap.width - (location.width * Constant.MIN_WIDTH_OF_EACH_GROUND / 3 - 10) < 5 &&  (location.width * Constant.MIN_WIDTH_OF_EACH_GROUND / 3 - 10) - locationInMap.width  < 5)) {
                        locationInMap.width += dx * 7;
                        keepMoving = true;
                    }
                    if( !( locationInMap.height - (location.height * Constant.MIN_HEIGHT_OF_EACH_GROUND / 3 - 53) < 5 &&  (location.height * Constant.MIN_HEIGHT_OF_EACH_GROUND / 3 - 53) - locationInMap.height < 5 )){
                        locationInMap.height += dy * 5;
                        keepMoving = true;
                    }
                    area = new Rectangle(locationInMap.width + 40,locationInMap.height + 20 , 20 , 60 );
                    try {
                        Thread.sleep(40 + dx*dx*20 + dy*dy*20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            isInMove = false;
        }

    }


    public java.util.List<Vertex> getPath() {
        return path;    // return null if there are not any path
    }

    public Image getImage(){
        BufferedImage characterImage = null;

        String vectorS = "";

        switch (vector){
            case UP :
                vectorS = "n";
                break;
            case UP_RIGHT:
                vectorS = "ne";
                break;
            case RIGHT:
                vectorS = "e";
                break;
            case DOWN_RIGHT:
                vectorS = "se";
                break;
            case DOWN:
                vectorS = "s";
                break;
            case DOWN_LEFT:
                vectorS = "sw";
                break;
            case LEFT:
                vectorS = "w";
                break;
            case UP_LEFT:
                vectorS = "nw";
                break;
        }
        try {
            BufferedImage tempImage = ImageIO.read(new File("src/res/images/human/test/walking/walking "+vectorS+"000"+imageCounter+".bmp"));

            characterImage = new BufferedImage(tempImage.getWidth(),tempImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
            characterImage.getGraphics().drawImage(tempImage, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // to removing background in image
        for(int i=0;i<characterImage.getWidth();i++)
            for(int j=0;j<characterImage.getHeight();j++)
            {
                if(characterImage.getRGB(i, j)==-10599895/*background rgb color */){
                    characterImage.setRGB(i,j, (0 << 24) | 0x00ffffff);
                }
            }


        if(isSelected) {
            try {
                BufferedImage shadow = ImageIO.read(new File("src/res/images/human/test/selected.png"));
                shadow.getGraphics().drawImage(characterImage,0,0,null);
                characterImage = shadow;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return characterImage;

    }
    public Dimension getlocation() {
        return location;
    }

    public int getX(){
        return ( locationInMap.width );
    }
    public int getY(){
        return ( locationInMap.height );
    }


    public boolean isInArea(int x,int y){
        return area.isInArea(x,y);
    }


    private void imageCount() {
        while(isInMove) {
            imageCounter = (imageCounter + 1) % 8;
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
