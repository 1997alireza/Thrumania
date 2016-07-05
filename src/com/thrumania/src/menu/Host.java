package com.thrumania.src.menu;

import com.thrumania.src.menu.objects.PlayerName;
import com.thrumania.src.objects.GameObject;
import res.values.Constant;

import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by AliReza on 30/06/2016.
 */
public class Host {
    private static final int maxClients = 3;

    private ArrayList <ClientMember> clientList = new ArrayList<>();

    private boolean isBroadcasting ;
    DatagramPacket DP;
    DatagramSocket DS;

    DatagramSocket DS_Update = null;

    LinkedList <GameObject> gameObjects ;
    private int x ;
    private int y0 ;
    private int deltaY ;
    private int width ;
    private int height;
    com.thrumania.src.menu.Panel menuPanel;
    public Host(com.thrumania.src.menu.Panel menuPanel,LinkedList <GameObject> gameObjects , int x , int y0 , int deltaY , int width , int height){
        this.menuPanel = menuPanel;
        this.gameObjects = gameObjects;
        this.x = x;
        this.y0 = y0;
        this.deltaY = deltaY;
        this.width = width;
        this.height = height;

        try {
            if(DS == null)
                DS = new DatagramSocket(Constant.HOST_ENTERING_PORT_NUMBER);
            DP = new DatagramPacket(Constant.HOST_BROADCAST_MESSAGE.getBytes(), Constant.HOST_BROADCAST_MESSAGE.length());
            DP.setPort(Constant.HOST_SENDING_PORT_NUMBER);

            DS_Update = new DatagramSocket();

        } catch (SocketException e) {
        }

        gameObjects.add(new PlayerName(Constant.PLAYER_NAME,x,y0,width,height,true));
        menuPanel.repaint();

        new Thread(this :: startBroadcast).start();
    }

    public void stopBroadcast(){
        isBroadcasting = false;
        DS.close();
        DS_Update.close();
    }

    public void startBroadcast(){

        isBroadcasting = true;
        new Thread(this :: broadcasting).start();
        new Thread(this :: searchingClient).start();
        //new Thread(this :: checkClientConnection).start();  // ?

    }

    private void broadcasting(){
        byte b[] = new byte [4];
        try {
            b = InetAddress.getLocalHost().getAddress();
        } catch (UnknownHostException e1) {
        }

        while(isBroadcasting){

            try {
                for(int i=0;i<256;i++)
                {
                    b[3] = (byte)i;
                    DP.setAddress(InetAddress.getByAddress(b));
                    DS.send(DP);
                }
            }catch(Exception e){}

            try {
                Thread.sleep(2000);     // ??
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    private void searchingClient(){
        while(isBroadcasting){
            System.out.println("kkkkkiiiyt");
            DatagramPacket DP = new DatagramPacket(new byte[100],100);
            try {
                if(DS == null)
                    DS =  new DatagramSocket(Constant.HOST_ENTERING_PORT_NUMBER);
                DS.receive(DP);
            }
            catch (SocketException e){
                System.out.println("Socket closed");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(DP.getData()!=null) {
                String message = new String(DP.getData()).trim();
                int i = message.indexOf(":");
                if(i>0 && message.length()>i+1) {
                    String messageTitle = message.substring(0, i);
                    String clientName = message.substring(i+1);
                    if (messageTitle.equals(Constant.CLIENT_CONNECTING_MESSAGE))
                    {
                        addClient(new ClientMember(clientName,DP.getAddress()));
                    }
                }
            }
        }
    }

    private void addClient(ClientMember client){
        if(clientList.size()<maxClients) {
            clientList.add(client);

            System.out.println("this");
            updateListToClients();
        }
    }

    private void updateListToClients (){


        String clientNames = Constant.PLAYER_NAME+",";
        for(ClientMember client : clientList)
        {
            clientNames += client.getName() + ",";
        }
        DatagramPacket DP_Update = new DatagramPacket(clientNames.getBytes() , clientNames.length());
        DP_Update.setPort(Constant.HOST_SENDING_UPDATE_PORT);

        for(ClientMember client : clientList){
            DP_Update.setAddress(client.getInetAddress());
            try {
                DS_Update.send(DP_Update);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // <-- show player name in menu
        for(int i=0;i<gameObjects.size();i++){
            GameObject GO = gameObjects.get(i);
            if (GO instanceof PlayerName)
                gameObjects.remove(GO);
        }

        gameObjects.add(new PlayerName(Constant.PLAYER_NAME,x,y0,width,height,true));

        for(int i=0;i<clientList.size();i++){
            ClientMember client = clientList.get(i);
            gameObjects.add(new PlayerName(client.getName() , x, y0 + deltaY*(i+1) ,width,height , false));
        }
        // show player name in menu -->


        menuPanel.repaint();
    }



//    private void checkClientConnection(){
//        DatagramPacket DP_Check = new DatagramPacket(Constant.HOST_CHECK_MESSAGE.getBytes() , Constant.HOST_CHECK_MESSAGE.length());
//        DatagramSocket DS_Check = null;
//        try {
//            DS_Check = new DatagramSocket(Constant.HOST_ENTERING_CHECK_PORT);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        while(isBroadcasting) {
//            System.out.println("cccc");
//            if (clientList.size() == 0)
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                }
//            else {
//                for (int i = 0; i < clientList.size(); i++) {
//                    System.out.println("raft tu fore clientha");
//                    ClientMember client = clientList.get(i);
//
//                    DP_Check.setAddress(client.getInetAddress());
//                    DP_Check.setPort(Constant.HOST_SENDING_CHECK_PORT);
//                    try {
//                        DS_Check.send(DP_Check);
//                        DS_Check.setSoTimeout(4000);//?
//                        DatagramPacket DP_ClientAns;
//                        long startTime = System.currentTimeMillis();
//                        boolean isClientAlive = false;
//                        while (startTime + 4000 > System.currentTimeMillis()) {
//                            DP_ClientAns = new DatagramPacket(new byte[100], 100);
//                            System.out.println("kkkk");
//                            DS_Check.receive(DP_ClientAns);
//                            System.out.println(new String(DP_ClientAns.getData()).trim());
//                            if (DP_ClientAns.getAddress().equals(client.getInetAddress()) && Constant.CLIENT_ANSWER_CHECK.equals(DP_ClientAns.getData().toString().trim())) {
//                                isClientAlive = true;
//                                break;
//                            }
//
//                        }
//                        if(!isClientAlive)
//                            clientList.remove(client);
//
//                    } catch (SocketTimeoutException e) {
//                        clientList.remove(client);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }
//    }


    public ArrayList<ClientMember> getClientList() {
        return clientList;
    }

}


class ClientMember{


    private String name;


    InetAddress inetAddress;
    public ClientMember(String name , InetAddress inetAddress){
        this.name = name;
        this.inetAddress = inetAddress;

    }

    public String getName() {
        return name;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

}
