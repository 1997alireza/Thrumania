package com.thrumania.src.menu;
/**
 * Created by AliReza on 23/05/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import com.sun.prism.*;
import com.thrumania.src.GraphicHandler;
import com.thrumania.src.Tools.Cursor;
import com.thrumania.src.draw.GamePanel;
import com.thrumania.src.objects.DragableObject;
import com.thrumania.src.objects.GameButton;
import com.thrumania.src.objects.GameObject;
import res.values.*;
import com.thrumania.src.Tools.*;
public class Panel implements GraphicHandler{

    private com.thrumania.src.draw.GamePanel drawPanel;private com.thrumania.src.mapEditor.Panel mapPanel;private com.thrumania.src.game.Panel gamePanel;

    private int state ;

    private final int background_height = 1080;
    private final int background_width = 1920;


    private LinkedList <GameObject> gameObjects;
    private LinkedList <DragableObject> dragableObjects;
    private final int [][] PT= {
            {110,1,2,6,-1,-1,-1,-1,-1,-1,-1},
            {0,-1,-1,-1,-1,-1,-1,-1,8,-1,101},
            {0,-1,-1,-1,-1,3,-1,5,-1,-1,-1},
            {2,-1,-1,-1,-1,-1,4,-1,8,-1,-1},
            {3,-1,-1,-1,-1,-1,-1,-1,-1,-1,102},
            {2,-1,-1,-1,-1,-1,-1,-1,-1,-1,103},
            {0,-1,-1,-1,7,-1,-1,-1,-1,-1,-1},
            {6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,3,-1}            //state = PT[state][code]
    };
    private final int eventNumber_ForFinishingMapEditing_InAutomata = 9;

    private LinkedList<LinkedList< Object[/* Constant.GROUND , Constant.OBJECT*/] >> ground ;

    public Panel(com.thrumania.src.draw.GamePanel drawPanel,com.thrumania.src.mapEditor.Panel mapPanel,com.thrumania.src.game.Panel gamePanel){
        this.drawPanel = drawPanel;
        this.mapPanel = mapPanel;
        this.gamePanel = gamePanel;
        state = 0;
        gameObjects = new LinkedList<GameObject>();
        dragableObjects = new LinkedList<DragableObject>();

        // <-- map.ground
        ground = new LinkedList<>();

        // first width of map = 22 , first height of map = 20
        for (int i=0;i<22;i++) {
            LinkedList <Object[/* Constant.GROUND , Constant.OBJECT*/]> row = new LinkedList();
            for (int j = 0; j < 20; j++) {
                Object room [] = {Constant.GROUND.SEA,null};
                row.add(room);
            }

            ground.add(row);
        }

        // map.ground -->


    }
    @Override
    public void render(Graphics g) {

        if ((double) Constant.Screen_Width / Constant.Screen_Height <= (double) background_width / background_height) {

            double temp = (double) (background_height * Constant.Screen_Width) / background_width;
            g.drawImage(new ImageIcon("src/res/images/menu/background.jpg").getImage(), 0, (Constant.Screen_Height - (int) temp) / 2, Constant.Screen_Width, (int) temp, null);
        } else {

            double temp = (double) (background_width * Constant.Screen_Height) / background_height;
            g.drawImage(new ImageIcon("src/res/images/menu/background.jpg").getImage(), (Constant.Screen_Width - (int) temp) / 2, 0, (int) temp, Constant.Screen_Height, null);
        }




        if (state == 6){
            int slider_bar_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 4.266)) / 2;
            int slider_bar_right = slider_bar_left;
            int slider_bar_up = Division.division(Constant.Screen_Height, 2.541);
            int slider_bar_height = Division.division(Division.division(Constant.Screen_Width, 4.266), 75);
            ImageIcon slider_bar = new ImageIcon("src/res/images/menu/sound/slider bar.png");

            int sound_text_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 11.294)) / 2;
            int sound_text_right = sound_text_left;
            int sound_text_up = Division.division(Constant.Screen_Height, 3.253);
            int sound_text_height = Division.division(Division.division(Constant.Screen_Width, 11.294), 4.483);
            ImageIcon sound_text = new ImageIcon("src/res/images/menu/sound/sound text.png");
            g.drawImage(slider_bar.getImage(),slider_bar_left,slider_bar_up,Constant.Screen_Width-slider_bar_left-slider_bar_right,slider_bar_height,null);
            g.drawImage(sound_text.getImage(),sound_text_left,sound_text_up,Constant.Screen_Width-sound_text_left-sound_text_right,sound_text_height,null);
        }

        for(GameObject GO : gameObjects){
            g.drawImage(GO.getImage(),GO.getX(),GO.getY(),GO.getWidth(),GO.getHeight(),null);
        }
    
        for(DragableObject DO : dragableObjects){
            g.drawImage(DO.getImage(),DO.getX(),DO.getY(),DO.getWidth(),DO.getHeight(),null);
        }
    }

    @Override
    public void updateComponents() {

        Cursor.setCursor(drawPanel,Cursor.MENU_CUROSR);

        gameObjects = new LinkedList<GameObject>();
        dragableObjects = new LinkedList<DragableObject>();


        int back_left = Division.division(Constant.Screen_Width, 8.97);
        int back_right = Division.division(Constant.Screen_Width,12.97);
        int back_up = Division.division(Constant.Screen_Height, 22.04);
        int back_height = Division.division(Division.division(Constant.Screen_Width,8.97),2.5);
        ImageIcon back_1 = new ImageIcon("src/res/images/menu/button/back 1.png");
        ImageIcon back_2 = new ImageIcon("src/res/images/menu/button/back 2.png");

        int edit_map_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 8)) / 2;
        int edit_map_right = edit_map_left;
        int edit_map_up = Division.division(Constant.Screen_Height, 1.855);
        int edit_map_height = Division.division(Division.division(Constant.Screen_Width, 8),1.80);
        ImageIcon edit_map_1 = new ImageIcon("src/res/images/menu/button/edit map 1.png");
        ImageIcon edit_map_2 = new ImageIcon("src/res/images/menu/button/edit map 2.png");

        switch (state) {
            case 0:
                int single_player_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 6.336)) / 2;
                int single_player_right = single_player_left;
                int single_player_up = Division.division(Constant.Screen_Height, 5.11);
                int single_player_height = Division.division(Division.division(Constant.Screen_Width, 6.336), 2.330);
                ImageIcon single_player_1 = new ImageIcon("src/res/images/menu/button/single player 1.png");
                ImageIcon single_player_2 = new ImageIcon("src/res/images/menu/button/single player 2.png");

                int multi_player_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 6.111)) / 2;
                int multi_player_right = multi_player_left;
                int multi_player_up = Division.division(Constant.Screen_Height, 2.416);
                int multi_player_height = Division.division(Division.division(Constant.Screen_Width, 6.111), 1.849);
                ImageIcon multi_player_1 = new ImageIcon("src/res/images/menu/button/multi player 1.png");
                ImageIcon multi_player_2 = new ImageIcon("src/res/images/menu/button/multi player 2.png");

                int option_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 6.048)) / 2;
                int option_right = option_left;
                int option_up = Division.division(Constant.Screen_Height, 1.545);
                int option_height = Division.division(Division.division(Constant.Screen_Width, 6.048), 2);
                ImageIcon option_1 = new ImageIcon("src/res/images/menu/button/option 1.png");
                ImageIcon option_2 = new ImageIcon("src/res/images/menu/button/option 2.png");

                int quit_left = Division.division(Constant.Screen_Width, 1.151);
                int quit_right = Division.division(Constant.Screen_Width,10.666);
                int quit_up = Division.division(Constant.Screen_Height, 1.15);
                int quit_height = Division.division(Division.division(Constant.Screen_Width,10.666),1.78);
                ImageIcon quit_1 = new ImageIcon("src/res/images/menu/button/quit 1.png");
                ImageIcon quit_2 = new ImageIcon("src/res/images/menu/button/quit 2.png");



                gameObjects.add(new GameButton(this, "single player", 1, single_player_left, single_player_up, Constant.Screen_Width - single_player_left - single_player_right, single_player_height, single_player_1.getImage(), single_player_2.getImage()));
                gameObjects.add(new GameButton(this, "multi player", 2, multi_player_left, multi_player_up, Constant.Screen_Width - multi_player_left - multi_player_right, multi_player_height, multi_player_1.getImage(), multi_player_2.getImage()));
                gameObjects.add(new GameButton(this, "option", 3, option_left, option_up, Constant.Screen_Width - option_left - option_right, option_height, option_1.getImage(), option_2.getImage()));
                gameObjects.add(new GameButton(this, "quit", 0, quit_left, quit_up, quit_right, quit_height, quit_1.getImage(), quit_2.getImage()));
                break;
            case 1:
                int play_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 10.54)) / 2;
                int play_right = play_left;
                int play_up = Division.division(Constant.Screen_Height, 3.5);
                int play_height = Division.division(Division.division(Constant.Screen_Width, 10.54),1.26);
                ImageIcon play_1 = new ImageIcon("src/res/images/menu/button/play 1.png");
                ImageIcon play_2 = new ImageIcon("src/res/images/menu/button/play 2.png");

                gameObjects.add(new GameButton(this, "play", 10, play_left, play_up, Constant.Screen_Width - play_left - play_right, play_height, play_1.getImage(), play_2.getImage()));
                gameObjects.add(new GameButton(this, "edit map", 8, edit_map_left, edit_map_up, Constant.Screen_Width - edit_map_left - edit_map_right, edit_map_height, edit_map_1.getImage(), edit_map_2.getImage()));
                gameObjects.add(new GameButton(this, "back", 0, back_left, back_up, back_right, back_height, back_1.getImage(), back_2.getImage()));
                break;
            case 2:
                int creat_game_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 9.320)) / 2;
                int creat_game_right = creat_game_left;
                int creat_game_up = Division.division(Constant.Screen_Height, 3.5);
                int creat_game_height = Division.division(Division.division(Constant.Screen_Width, 9.320),1.46);
                ImageIcon creat_game_1 = new ImageIcon("src/res/images/menu/button/creat game 1.png");
                ImageIcon creat_game_2 = new ImageIcon("src/res/images/menu/button/creat game 2.png");

                int join_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 10.55)) / 2;
                int join_right = join_left;
                int join_up = Division.division(Constant.Screen_Height, 1.855);
                int join_height = Division.division(Division.division(Constant.Screen_Width, 10.55),1.87);
                ImageIcon join_1 = new ImageIcon("src/res/images/menu/button/join 1.png");
                ImageIcon join_2 = new ImageIcon("src/res/images/menu/button/join 2.png");

                gameObjects.add(new GameButton(this, "creat game", 5, creat_game_left, creat_game_up, Constant.Screen_Width - creat_game_left - creat_game_right, creat_game_height, creat_game_1.getImage(), creat_game_2.getImage()));
                gameObjects.add(new GameButton(this, "join", 7, join_left, join_up, Constant.Screen_Width - join_left - join_right, join_height, join_1.getImage(), join_2.getImage()));
                gameObjects.add(new GameButton(this, "back", 0, back_left, back_up, back_right, back_height, back_1.getImage(), back_2.getImage()));
                break;
            case 3:
                int creat_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 7.320)) / 2;
                int creat_right = creat_left;
                int creat_up = Division.division(Constant.Screen_Height, 3.5);
                int creat_height = Division.division(Division.division(Constant.Screen_Width, 9.320),1.26);
                ImageIcon creat_1 = new ImageIcon("src/res/images/menu/button/creat 1.png");
                ImageIcon creat_2 = new ImageIcon("src/res/images/menu/button/creat 2.png");



                gameObjects.add(new GameButton(this, "creat", 5, creat_left, creat_up, Constant.Screen_Width - creat_left - creat_right, creat_height, creat_1.getImage(), creat_2.getImage()));
                gameObjects.add(new GameButton(this, "edit map", 8, edit_map_left, edit_map_up, Constant.Screen_Width - edit_map_left - edit_map_right, edit_map_height, edit_map_1.getImage(), edit_map_2.getImage()));
                gameObjects.add(new GameButton(this, "back", 0, back_left, back_up, back_right, back_height, back_1.getImage(), back_2.getImage()));
                break;
            case 6:

                gameObjects.add(new GameButton(this, "back", 0, back_left, back_up, back_right, back_height, back_1.getImage(), back_2.getImage()));

                // (-40,0) -> (731,1151)
                int x = (Division.division((PlaySound.getVolume()+40)*420 ,40)) + 730;
                int slider_option_bar_right = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 5.036)) / 2;
                int slider_option_bar_left = (Constant.Screen_Width - Division.division(Constant.Screen_Width, 4.200)) / 2;
                int slider_option_bar_up = Division.division(Constant.Screen_Height, 2.645);
                int minX = slider_option_bar_left;
                int maxX = Constant.Screen_Width-slider_option_bar_right;
                dragableObjects.add(new DragableObject(this,DragableObject.SOUND_OPTION,"sound option",x,slider_option_bar_up,minX,maxX,40,40,(new ImageIcon("src/res/images/menu/sound/kohga.png")).getImage()));
                     break;

        }


    }


    @Override
    public void repaint() {
        drawPanel.repaint();
    }


    @Override
    public void pressButton(int code){
        //check if is a final state
        state = PT[state][code];
        switch (state){
            case 101:   //play game event : single player mode
                drawPanel.changeState(GamePanel.STATE.GAME);
                break;
            case 102:   //play game event : multi player mode as host
                drawPanel.changeState(GamePanel.STATE.GAME);
                break;
            case 103:   //play game event : multi player mode as guest
                drawPanel.changeState(GamePanel.STATE.GAME);
                break;
            case 110:   //exit game event
                System.exit(0);
                break;

            case 8:
                mapPanel = new com.thrumania.src.mapEditor.Panel(drawPanel,this,ground);
                drawPanel.changeState(GamePanel.STATE.MAP);
                break;
            case 9:
                mapPanel = new com.thrumania.src.mapEditor.Panel(drawPanel,this,ground);
                drawPanel.changeState(GamePanel.STATE.MAP);
                break;
            default:
                drawPanel.changeState(GamePanel.STATE.MENU);
        }

    }

    public void editMap(LinkedList<LinkedList< Object[/* Constant.GROUND , Constant.OBJECT*/] >> ground ){
        this.ground = ground;
        pressButton(eventNumber_ForFinishingMapEditing_InAutomata);
    }

    @Override
    public void mouseClick(int x, int y) {
        for(GameObject GO : gameObjects){
            if(GO.isInArea(x,y)){
                GO.mouseClicked();
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
    public void mouseDrag( int x, int y) {
        for(DragableObject DO : dragableObjects) {
            if (DO.isSelected()) {
                DO.changeX((int)MouseInfo.getPointerInfo().getLocation().getX());
                break;
            }
        }
    }

    @Override
    public void mousePress( int x, int y) {
        for(DragableObject DO : dragableObjects){
            if(DO.isInArea(x,y)){
                DO.select(true);
                break;
            }
        }

    }

    @Override
    public void mouseRelease(int x, int y) {
        for(DragableObject DO : dragableObjects){
            if(DO.isInArea(x,y)){
                DO.select(false);
                break;
            }
        }
    }


}
