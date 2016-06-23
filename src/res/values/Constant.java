package res.values;

import java.awt.*;

public class Constant {
    public static Dimension Screen_Dimension = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int Screen_Height = Screen_Dimension.height;
    public static final int Screen_Width = Screen_Dimension.width;
    public static Dimension Mini_Map_Size_Ratio = new Dimension(8,5);

    public enum GROUND{
        SEA(101) , LOWLAND(102) , HIGHLAND(103);


        private int code;

        GROUND(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
    public enum OBJECT{
        FARMLAND(111) , IRON_MINE(112) , GOLD_MINE(113) , TREE1(114) , TREE2(115) , FISH1(116) , FISH2(117) ;

        private int code;

        OBJECT(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

    public static int MIN_WIDTH_OF_EACH_GROUND = 175;
    public static int MIN_HEIGHT_OF_EACH_GROUND = 125;

    public static final int MIN_WIDTH_OF_EACH_SEA= 320;
    public static final int MIN_HEIGHT_OF_EACH_SEA = 240;
    public static final int NUM_OF_SEA_IN_EACH_ROW= 7;
    public static final int NUM_OF_SEA_IN_EACH_COLUMN = 5;
    public static final int BOTTOM_FRAME_HEIGHT = 250;


}
