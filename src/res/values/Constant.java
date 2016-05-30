package res.values;

import java.awt.*;

public class Constant {
    public static Dimension Screen_Dimension = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int Screen_Height = Screen_Dimension.height;
    public static final int Screen_Width = Screen_Dimension.width;
    public static Dimension Mini_Map_Size_Ratio = new Dimension(8,5);

    public enum GROUND{
        SEA , LOWLAND , HIGHLAND;
    }
    public enum OBJECT{
        TREE , FARMLAND , IRON_MINE , GOLD_MINE;
    }


}
