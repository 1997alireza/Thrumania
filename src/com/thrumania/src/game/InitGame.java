package com.thrumania.src.game;

import res.values.Constant;

import javax.swing.*;
import java.util.LinkedList;

/**
 * Created by AliReza on 04/07/2016.
 */
public class InitGame {
    private LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground;
    private Constant.PLAYER_TYPE playerType ;
    private int playerNumbers;


    private boolean isSuccessful;
    public InitGame(LinkedList<LinkedList<Object[/* Constant.GROUND , Constant.OBJECT*/]>> ground , Constant.PLAYER_TYPE playerType , int playerNumbers ,com.thrumania.src.draw.GamePanel drawPanel ){
        this.ground = ground;
        this.playerType = playerType;
        this.playerNumbers = playerNumbers;

        switch (playerType){
            case SINGLE:
                isSuccessful = setCastles();
                if(isSuccessful)
                    new com.thrumania.src.game.Panel(ground, playerType, playerNumbers, drawPanel);
                break;
            case MULTI_HOST:
                isSuccessful = setCastles();
                //send ground and playerNum to clints
                break;
            case MULTI_CLIENT:
                // receive ground and playerNum

                isSuccessful = true;
                break;
        }


    }

    private boolean setCastles(){
        FindCastles findCastles = new FindCastles(ground , playerNumbers);
        int castlesPoints [][] ;
        castlesPoints = findCastles.find();
        boolean t = true;
        for(int i=0;i<castlesPoints.length;i++)
            for(int j=0;j<2;j++)
                if(castlesPoints[i][j]<0)
                    t = false;

        if(t) {
            for (int i = 0; i < playerNumbers; i++) {
                Object[] thisRoom = new Object[2];
                thisRoom[0] = ground.get(castlesPoints[i][0]).get(castlesPoints[i][1])[0];
                switch (i) {
                    case 0:
                        thisRoom[1] = Constant.OBJECT.CASTLE_ONE;
                        break;
                    case 1:
                        thisRoom[1] = Constant.OBJECT.CASTLE_TWO;
                        break;
                    case 2:
                        thisRoom[1] = Constant.OBJECT.CASTLE_THREE;
                        break;
                    case 3:
                        thisRoom[1] = Constant.OBJECT.CASTLE_FOUR;
                        break;
                }

                ground.get(castlesPoints[i][0]).set(castlesPoints[i][1], thisRoom);
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Map is so small");
        }
        return t;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

}
