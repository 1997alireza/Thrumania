package com.thrumania.src.Tools;

import java.awt.*;

/**
 * Created by AliReza on 20/06/2016.
 */
public class Cursor {
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();

    public static int MENU_CUROSR = 0;

    public static void setCursor(Container container , int type ){

        String path = "src/res/images/cursor/t.png";
        String name = "menu";

        Image image = toolkit.getImage(path);
        java.awt.Cursor c = toolkit.createCustomCursor(image , new Point(0,0), name);
        container.setCursor(c);

    }
}
