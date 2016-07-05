package com.thrumania.src.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by ali on 7/4/2016.
 */
public class GameThread {
    private LinkedList<Object [/*Object ,method ,int ,method*/]> threadList;
    private boolean isRunning;
    private int counter;

    public GameThread(){
        counter = 0;
    }

    public void add(Object instance,String name, int frequency, String getboolean){
        Method method = null;
        Method bool = null;
        try {
            method = instance.getClass().getDeclaredMethod(name,null);
            bool = instance.getClass().getDeclaredMethod(getboolean,null);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object o [] = {instance, method, frequency, bool};
        threadList.add(o);

    }
     void run() {
         isRunning = true;
         while (isRunning) {
             for (int i = 0; i < threadList.size(); i++) {
                 try {
                     if ((boolean) ((Method) threadList.get(i)[3]).invoke(threadList.get(i)[0], null)) {
                         if (counter % (int) threadList.get(i)[2] == 0) {
                             ((Method) threadList.get(i)[1]).invoke(threadList.get(i)[0], null);
                         }
                     } else {
                         threadList.remove(i);
                     }
                 } catch (IllegalAccessException e) {
                     e.printStackTrace();
                 } catch (InvocationTargetException e) {
                     e.printStackTrace();
                 }
             }
             counter++;
         }

     }
    public void stop(){
        isRunning = false;
    }

}
