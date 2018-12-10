package ts.trainticket.utils;

public class ServerConstValues {

    /**
     * 乘客类型
     */
//    public static final String[] PASSENGER_TYPES = {
//            "儿童",
//            "成人",
//            "学生",
//            "残疾军人、伤残人民警察"
//    };


    /**
     * 乘客类型
     */

    public static final String[] EASY_PASSENGER_TYPES = {
            "Children",
            "Adult",
            "Student",
            "The remnant Army"
    };

//    public static final String[] EASY_PASSENGER_TYPES = {
//            "儿童",
//            "成人",
//            "学生",
//            "残军"
//    };


    /**
     * 订单状态
     */
    public static final String[] STATUSES = new String[]{
            "未购买", "已购买", "已退票", "已改签", "已过期"
    };


    /**
     * 列车所有类型
     */
    public static final String[] TRAIN_TYPES = {
            "G", "D", "Z", "T", "K"
    };


    public static final String[]  SEAT_TYPES = {
            "no site",
            "1st-class",
            "2rd-class",
            "hard seat",
            "soft seat",
            "hard sleeper",
            "Soft sleeper",
            "high-grade soft sleeper",
            "special-class seat",
            "business seat"
    };

    /**
     * 列车所有座位类型
     */
//    public static final String[] SEAT_TYPES = {
//            "无座",
//            "硬座",
//            "软座",
//            "硬卧",
//            "软卧",
//            "高级软卧",
//            "二等座",
//            "一等座",
//            "特等座",
//            "商务座"
//    };


    /**
     * 列车所有座位类型 简易写法
     */

    public static final String[]  EASY_SEAT_TYPES = {
            "no site",
            "1st-class",
            "2rd-class",
            "hard seat",
            "soft seat",
            "hard sleeper",
            "Soft sleeper",
            "high-grade soft sleeper",
            "special-class",
            "business"
    };


    public static final int getSeatType(String seatType) {
        seatType = seatType.trim();
        System.out.println(seatType+"--==-");
        if ("no site".equals(seatType))
            return 0;
        if ("hard seat".equals(seatType))
            return 1;
        if ("1st-class".equals(seatType))
            return 2;
        if ("2rd-class".equals(seatType))
            return 3;
        if ("soft seat".equals(seatType))
            return 4;
        if ("hard sleeper".equals(seatType))
            return 5;
        if ("Soft sleeper".equals(seatType))
            return 6;
        if ("high-grade soft sleeper".equals(seatType))
            return 7;
        if ("special-class".equals(seatType))
            return 8;
        if ("business".equals(seatType))
            return 9;
        return 0;
    }


//    public static final String[] EASY_SEAT_TYPES = {
//            "无座",
//            "硬座",
//            "软座",
//            "硬卧",
//            "软卧",
//            "高软",
//            "二等",
//            "一等",
//            "特等",
//            "商务"
//    };


//    public static final int getSeatType(String seatType) {
//        seatType = seatType.trim();
//        System.out.println(seatType+"--==-");
//        if ("无座".equals(seatType))
//            return 0;
//        if ("硬座".equals(seatType))
//            return 1;
//        if ("软座".equals(seatType))
//            return 2;
//        if ("硬卧".equals(seatType))
//            return 3;
//        if ("软卧".equals(seatType))
//            return 4;
//        if ("高软".equals(seatType))
//            return 5;
//        if ("二等".equals(seatType))
//            return 6;
//        if ("一等".equals(seatType))
//            return 7;
//        if ("特等".equals(seatType))
//            return 8;
//        if ("商务".equals(seatType))
//            return 9;
//        return 0;
//    }

    /**
     * 年月日格式
     */
    public static final String TEMPLATE = "yyyy-MM-dd";
}
