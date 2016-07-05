package com.thrumania.src.game.objects;

import res.values.Constant;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by ali on 7/4/2016.
 */
public class Human {
    public Dimension getLoction() {
        return loction;
    }

    private Dimension loction;
    private int speed;
    private LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground;
    private LinkedList<LinkedList<Boolean>> binaryMap;
    //private Vertex ver [][];
    private LinkedList<LinkedList<Vertex>> ver;


    private LinkedList<Integer>  direction;

    public Human(LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground  , int x , int y) {
        this.ground = ground;
        binaryMap = new LinkedList<LinkedList<Boolean>>();
        direction = new LinkedList<Integer>();
        loction = new Dimension(x,y);
    }

    private enum PLAYER_STATE {
        STANDING(0), MOVIMG(1), FIGHTING(2), DYING(3);
        private int code;

        PLAYER_STATE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public void ConvertMap() {
        for (int i = 0; i < ground.size(); i++) {
            for (int j = 0; j < ground.get(i).size(); j++) {
                if (ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND || ground.get(i).get(j)[1] == Constant.OBJECT.FARMLAND) {
                    for (int k = 3 * i; k < 3 * i + 3; k++) {
                        for (int l = 3 * j; l < 3 * j + 3; l++) {
                            binaryMap.get(k).set(l, true);
                        }
                    }
                } else if (ground.get(i).get(j)[1] == Constant.OBJECT.TREE1 || ground.get(i).get(j)[1] == Constant.OBJECT.TREE2) {
                    for (int k = 3 * i; k < 3 * i + 3; k++) {
                        for (int l = 3 * j; l < 3 * j + 3; l++) {
                            binaryMap.get(k).set(l, true);
                        }
                    }
                    binaryMap.get(3 * i + 1).set(3 * j + 1, false);
                } else if (ground.get(i).get(j)[0] == Constant.GROUND.HIGHLAND) {
                    if (/*kohnavard*/ true) {///sharrt
                        for (int k = 3 * i; k < 3 * i + 3; k++) {
                            for (int l = 3 * j; l < 3 * j + 3; l++) {
                                binaryMap.get(k).set(l, true);
                            }
                        }
                    } else {
                        for (int k = 3 * i; k < 3 * i + 3; k++) {
                            for (int l = 3 * j; l < 3 * j + 3; l++) {
                                binaryMap.get(k).set(l, false);
                            }
                        }
                    }
                } else {
                    for (int k = 3 * i; k < 3 * i + 3; k++) {
                        for (int l = 3 * j; l < 3 * j + 3; l++) {
                            binaryMap.get(k).set(l, false);
                        }
                    }
                }
            }
        }
    }

    public void findWay(int x, int y) {
        // ver = new Vertex[binaryMap.size()][binaryMap.get(0).size()];
        ver = new LinkedList<LinkedList<Vertex>>();
        for (int i = 0; i < binaryMap.size(); i++) {
            for (int j = 0; j < binaryMap.get(i).size(); j++) {
                if (binaryMap.get(i).get(j)) {
                    ver.get(i).set(j, new Vertex(i + " " + j));
                }
            }
        }

        for (int i = 0; i < binaryMap.size(); i++) {
            for (int j = 0; j < binaryMap.get(i).size(); j++) {
                int temp = 0;
                boolean arr[] = {false, false, false, false, false, false, false, false};
                if (i > 0 && binaryMap.get(i - 1).get(j)) {
                    temp++;
                    arr[0] = true;
                }
                if (i < binaryMap.size() && binaryMap.get(i + 1).get(j)) {
                    temp++;
                    arr[1] = true;
                }
                if (j > 0 && binaryMap.get(i).get(j - 1)) {
                    temp++;
                    arr[2] = true;
                }
                if (j < binaryMap.get(0).size() && binaryMap.get(i).get(j + 1)) {
                    temp++;
                    arr[3] = true;
                }
                if (i > 0 && j > 0 && binaryMap.get(i - 1).get(j - 1)) {
                    temp++;
                    arr[4] = true;
                }
                if (i > 0 && j < binaryMap.get(0).size() && binaryMap.get(i - 1).get(j + 1)) {
                    temp++;
                    arr[5] = true;
                }
                if (i < binaryMap.size() && j > 0 && binaryMap.get(i + 1).get(j - 1)) {
                    temp++;
                    arr[6] = true;
                }
                if (i < binaryMap.size() && j < binaryMap.get(0).size() && binaryMap.get(i + 1).get(j + 1)) {
                    temp++;
                    arr[7] = true;
                }
                Edge e[] = new Edge[temp];
                for (int k = 0, l = 0; k < 4; k++) {
                    if (arr[k] == true) {
                        switch (k) {
                            case 0:
                                e[l] = new Edge(ver.get(i - 1).get(j), 1);
                                break;
                            case 1:
                                e[l] = new Edge(ver.get(i + 1).get(j), 1);
                                break;
                            case 2:
                                e[l] = new Edge(ver.get(i).get(j - 1), 1);
                                break;
                            case 3:
                                e[l] = new Edge(ver.get(i).get(j + 1), 1);
                                break;
                            case 4:
                                e[l] = new Edge(ver.get(i - 1).get(j - 1), Math.sqrt(2));
                                break;
                            case 5:
                                e[l] = new Edge(ver.get(i - 1).get(j + 1), Math.sqrt(2));
                                break;
                            case 6:
                                e[l] = new Edge(ver.get(i + 1).get(j - 1), Math.sqrt(2));
                                break;
                            case 7:
                                e[l] = new Edge(ver.get(i + 1).get(j + 1), Math.sqrt(2));
                                break;
                            default:
                                break;
                        }
                        l++;
                    }
                }
                ver.get(i).get(j).adjacencies = e;
            }
        }
        Dijkstra.computePaths(ver.get(loction.width).get(loction.height));
        java.util.List<Vertex> path = Dijkstra.getShortestPathTo(ver.get(x).get(y));


//        if(s1[0].equals(s2[0])){
//            if(Integer.parseInt(s1[1]) > Integer.parseInt(s2[1]))
//                vector = 2;
        for (int i = 0; i < path.size() - 1; i++) {
            String s1[] = path.get(0).toString().split(" ");
            String s2[] = path.get(1).toString().split(" ");
            int vector = 0;
            if (Integer.parseInt(s1[0]) == Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) > Integer.parseInt(s2[1])) {//up
                vector = 0;
            } else if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) > Integer.parseInt(s2[1])) {//left up
                vector = 1;
            } else if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) == Integer.parseInt(s2[1])) {//left
                vector = 2;
            } else if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) < Integer.parseInt(s2[1])) {//left down
                vector = 3;
            } else if (Integer.parseInt(s1[0]) == Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) < Integer.parseInt(s2[1])) {//down
                vector = 4;
            } else if (Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) < Integer.parseInt(s2[1])) {//right down
                vector = 5;
            } else if (Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) == Integer.parseInt(s2[1])) {//right
                vector = 6;
            } else if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]) && Integer.parseInt(s1[1]) > Integer.parseInt(s2[1])) {//right up
                vector = 7;
            }
            direction.add(vector);
        }
    }

    public void move() {


    }

}
