package res.values;

import java.awt.*;

public class Constant {

    public static final Dimension Screen_Dimension = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int Screen_Height = Screen_Dimension.height;
    public static final int Screen_Width = Screen_Dimension.width;

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
        FARMLAND(111) , IRON_MINE(112) , GOLD_MINE(113) , TREE1(114) , TREE2(115) , FISH1(116) , FISH2(117) , CASTLE_ONE(121) ,CASTLE_TWO(122) ,CASTLE_THREE(123) ,CASTLE_FOUR(124) ;

        private int code;

        OBJECT(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

    public enum PLAYER_TYPE{
        SINGLE , MULTI_HOST , MULTI_CLIENT
    }

    public static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1920 , 1080);

    public static final Dimension MIN_MAP_SIZE = new Dimension(1920 * 2, 1200 * 2);    // height / width = 8 / 5
    public static final Dimension ONE_MAP_NUM_CHANGING= new Dimension(8,7);
    public static final Dimension ONE_MAP_SIZE_CHANGING = new Dimension(ONE_MAP_NUM_CHANGING.width*Constant.MIN_WIDTH_OF_EACH_GROUND ,ONE_MAP_NUM_CHANGING.height*Constant.MIN_HEIGHT_OF_EACH_GROUND);  // height / width = 8 / 5
    public static final Dimension MAX_MAP_SIZE = new Dimension(MIN_MAP_SIZE.width + ONE_MAP_SIZE_CHANGING.width * 4 , MIN_MAP_SIZE.height + ONE_MAP_SIZE_CHANGING.height * 4);
    public static final Dimension Mini_Map_Size_Ratio = new Dimension(8,5);
    public static Dimension MAP_SIZE = MIN_MAP_SIZE;

    public static final int MIN_WIDTH_OF_EACH_GROUND = 175;
    public static final int MIN_HEIGHT_OF_EACH_GROUND = 125;

    public static final int MIN_WIDTH_OF_EACH_SEA= 320;
    public static final int MIN_HEIGHT_OF_EACH_SEA = 240;
    public static final int NUM_OF_SEA_IN_EACH_ROW= 7;
    public static final int NUM_OF_SEA_IN_EACH_COLUMN = 5;
    public static final int BOTTOM_FRAME_HEIGHT = 250;

    public static final float MAX_OF_SCALE = 2f;
    public static final float ONE_SCALE_CHANGING = 0.05f;

    public static String PLAYER_NAME ;

    public static final String HOST_BROADCAST_MESSAGE = "Thrumania host";
    public static final String CLIENT_CONNECTING_MESSAGE = "Thrumania client"; // Message = CLIENT_CONNECTING_MESSAGE+":"+ClientName
    public static final String HOST_CHECK_MESSAGE = "Are you there?";
    public static final String CLIENT_ANSWER_CHECK = "I am alive";
    public static final int HOST_SENDING_PORT_NUMBER /*client entering port number*/ = 4040;  //port to sending data from host to client
    public static final int HOST_ENTERING_PORT_NUMBER /*client sending port number*/ = 4050 ; //port to entering data from client to host
    public static final int HOST_ENTERING_CHECK_PORT = 4060;
    public static final int HOST_SENDING_CHECK_PORT = 4070;
    public static final int HOST_SENDING_UPDATE_PORT /*client entering port for update list*/= 4080;

}
