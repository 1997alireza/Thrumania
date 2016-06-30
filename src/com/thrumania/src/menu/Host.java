package com.thrumania.src.menu;

import res.values.Constant;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by AliReza on 30/06/2016.
 */
public class Host {
    private static final int maxClients = 3;
    private ArrayList <Client> clientList = new ArrayList<>();

    private boolean isBroadcasting ;
    DatagramPacket DP;
    DatagramSocket DS;

    public Host(){
        try {
            DS = new DatagramSocket(Constant.HOST_ENTERING_PORT_NUMBER);
            DP = new DatagramPacket(Constant.HOST_BROADCAST_MESSAGE.getBytes(), 6);
            DP.setPort(Constant.HOST_SENDING_PORT_NUMBER);
        } catch (SocketException e) {
        }
    }

    public void broadcast(){

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
        }
    }

    public void searchingClient(){
        while(isBroadcasting){
            DatagramPacket DP = new DatagramPacket(new byte[100],100);
            try {
                DS.receive(DP);
            } catch (IOException e) {
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
                        addClient(new Client(clientName,DP.getAddress()));
                    }
                }
            }
        }
    }

    public void addClient(Client client){
        clientList.add(client);
    }

    public void setBroadcasting(boolean broadcasting) {
        isBroadcasting = broadcasting;
    }


}


class Client{


    private String name;


    InetAddress inetAddress;
    public Client(String name , InetAddress inetAddress){
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
