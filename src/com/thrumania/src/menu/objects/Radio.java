package com.thrumania.src.menu.objects;

/**
 * Created by AliReza on 28/06/2016.
 */
public class Radio {
    private String decribtion;
    private int value;
    private RadioButton[] buttons;

    public Radio(String decribtion , RadioButton button1 , RadioButton button2 , RadioButton button3){
        this.decribtion = decribtion;
        value = 2;
        buttons = new RadioButton[3];

        buttons[0] = button1;
        button1.setParent(this);
        buttons[1] = button2;
        button2.setParent(this);
        buttons[2] = button3;
        button3.setParent(this);

        button1.select();
    }

    public void select(int value){

        for(int i=0;i<buttons.length;i++)
            buttons[i].unSelect();

        buttons[value-2].select();

        this.value = value;

    }

    public int getValue(){
        return value;
    }
}


