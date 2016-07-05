package com.thrumania.src.game;

import res.values.Constant;

import java.util.LinkedList;

/**
 * Created by AliReza on 03/07/2016.
 */
public class FindCastles {

    private boolean binaryGround [][] ;
    private int player;
    private int points [][];
    public FindCastles(LinkedList<LinkedList<Object[]>> ground , int player){

        binaryGround = new boolean[ground.size()][ground.get(0).size()];
        for(int i=0;i<ground.size();i++)
            for(int j=0;j<ground.get(0).size();j++){
                binaryGround[i][j] = false;
                if(ground.get(i).get(j)[0] == Constant.GROUND.LOWLAND && ground.get(i).get(j)[1] == null){

                    if (j > 0 && (ground.get(i).get(j - 1)[0] == Constant.GROUND.LOWLAND))   //up
                        binaryGround[i][j] = true;
                    else if (i < ground.size() - 1 && (ground.get(i + 1).get(j)[0] == Constant.GROUND.LOWLAND))   //right
                        binaryGround[i][j] = true;
                    else if (j < ground.get(i).size() - 1 && (ground.get(i).get(j + 1)[0] == Constant.GROUND.LOWLAND))   //down
                        binaryGround[i][j] = true;
                    else if (i > 0 && (ground.get(i - 1).get(j)[0] == Constant.GROUND.LOWLAND))   //left
                        binaryGround[i][j] = true;

                }
            }

        this.player = player;

        points  = new int [player][2];
        for(int i=0;i<points.length;i++)
            for(int j=0;j<2;j++)
                points[i][j] = -1;


    }

    public int [][] find() {


        // <-- find 2 points that have the furthest path

        int maxDistance = -1;
        for (int i = 0; i < binaryGround.length; i++)
            for (int j = 0; j < binaryGround[0].length; j++)
                for (int iP = 0; iP < binaryGround.length; iP++)
                    for (int jP = 0; jP < binaryGround[0].length; jP++)
                        if ((i != iP || j != jP) && binaryGround[i][j] && binaryGround[iP][jP]) {
                            int distance = distanceP2(i, j, iP, jP);
                            if (distance > maxDistance) {
                                maxDistance = distance;
                                points[0][0] = i;
                                points[0][1] = j;
                                points[1][0] = iP;
                                points[1][1] = jP;
                            }

                        }
        // find 2 points that have the furthest path -->

        maxDistance = -1;
        int minDifDistance = binaryGround.length * binaryGround.length + binaryGround[0].length * binaryGround[0].length;
        if (player >= 3) {
            for (int i = 0; i < binaryGround.length; i++)
                for (int j = 0; j < binaryGround[0].length; j++) {
                    if (binaryGround[i][j]) {
                        boolean t = true;
                        for (int k = 0; k < 2; k++) {
                            if (points[k][0] == i && points[k][1] == j)
                                t = false;
                        }

                        if (t) {
                            int distance = distanceP2(points[0][0], points[0][1], i, j) + distanceP2(points[1][0], points[1][1], i, j);
                            int difDistance = distanceP2(points[0][0], points[0][1], i, j) - distanceP2(points[1][0], points[1][1], i, j);
                            difDistance = (difDistance > 0) ? difDistance : -1 * difDistance;
                            if (minDifDistance > difDistance && distance > maxDistance) {
                                minDifDistance = difDistance;
                                maxDistance = distance;
                                points[2][0] = i;
                                points[2][1] = j;

                            } else if (minDifDistance > difDistance) {
                                minDifDistance = difDistance;
                                maxDistance = distance;
                                points[2][0] = i;
                                points[2][1] = j;
                            } else if ((minDifDistance - difDistance) * (minDifDistance - difDistance) < 4 && distance > maxDistance) {
                                minDifDistance = difDistance;
                                maxDistance = distance;
                                points[2][0] = i;
                                points[2][1] = j;
                            }
                        }
                    }
                }
        }

        if (player == 4) {
            maxDistance = -1;
            for (int i = 0; i < binaryGround.length; i++)
                for (int j = 0; j < binaryGround[0].length; j++) {
                    if (binaryGround[i][j]) {
                        boolean t = true;
                        for (int k = 0; k < 3; k++) {
                            if (points[k][0] == i && points[k][1] == j)
                                t = false;
                        }
                        if (t) {
                            int distance = distanceP2(points[0][0], points[0][1], i, j) + distanceP2(points[1][0], points[1][1], i, j) + distanceP2(points[2][0], points[2][1], i, j);
                            if (distance > maxDistance) {
                                maxDistance = distance;
                                points[3][0] = i;
                                points[3][1] = j;

                            }
                        }
                    }
                }
        }

//        if(player>=3) {
//
//            int i=0;
//            while (checkNeighbor(0));
//            while (checkNeighbor(1)) ;
//            while (checkNeighbor(2)) ;
//            if (player == 4)
//                while (checkNeighbor(3)) ;
//        }



        return points;
    }
    private int distanceP2(int i1,int j1,int i2,int j2){
        int dx = i2 - i1;
        int dy = j2 - j1;
        return ( dx*dx + dy*dy);

    }


//    private boolean checkNeighbor (int thisK){
//        boolean t = false;
//        int mainDistance = 0 ;
//        int mainI = points[thisK][0];
//        int mainJ = points[thisK][1];
//        for(int i=0;i<player;i++){
//            if(i!=thisK){
//                mainDistance += distanceP2(mainI,mainJ,points[i][0],points[i][1]);
//            }
//        }
//
//        if( mainI>0 && binaryGround[mainI-1][mainJ]){   // left
//            int thisDistance = 0 ;
//            for(int i=0;i<player;i++){
//                if(i!=thisK){
//                    thisDistance += distanceP2(mainI-1,mainJ,points[i][0],points[i][1]);
//                }
//            }
//
//            if(thisDistance > mainDistance){
//                mainDistance = thisDistance;
//                points[thisK][0] = mainI - 1;
//                points[thisK][1] = mainJ;
//                t=true;
//            }
//
//        }
//
//        if( mainI<binaryGround.length-1 && binaryGround[mainI+1][mainJ]){   // right
//            int thisDistance = 0 ;
//            for(int i=0;i<player;i++){
//                if(i!=thisK){
//                    thisDistance += distanceP2(mainI+1,mainJ,points[i][0],points[i][1]);
//                }
//            }
//
//            if(thisDistance > mainDistance){
//                mainDistance = thisDistance;
//                points[thisK][0] = mainI + 1;
//                points[thisK][1] = mainJ;
//                t=true;
//            }
//
//        }
//
//        if( mainJ>0 && binaryGround[mainI][mainJ-1]){   // up
//            int thisDistance = 0 ;
//            for(int i=0;i<player;i++){
//                if(i!=thisK){
//                    thisDistance += distanceP2(mainI,mainJ-1,points[i][0],points[i][1]);
//                }
//            }
//
//            if(thisDistance > mainDistance){
//                mainDistance = thisDistance;
//                points[thisK][0] = mainI;
//                points[thisK][1] = mainJ - 1;
//                t=true;
//            }
//
//        }
//
//        if( mainJ<binaryGround[0].length - 1 && binaryGround[mainI][mainJ+1]){   // down
//            int thisDistance = 0 ;
//            for(int i=0;i<player;i++){
//                if(i!=thisK){
//                    thisDistance += distanceP2(mainI,mainJ+1,points[i][0],points[i][1]);
//                }
//            }
//
//            if(thisDistance > mainDistance){
//                mainDistance = thisDistance;
//                points[thisK][0] = mainI;
//                points[thisK][1] = mainJ + 1;
//                t=true;
//            }
//
//        }
//
//        return t;
//
//
//
//    }





}
