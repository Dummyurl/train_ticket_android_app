package ts.trainticket.databean;

/**
 * Created by liuZOZO on 2018/1/30.
 */
public class Ticket {

    public static final String[] EASY_SEAT_TYPES = {
            "no seat",
            "hard seat",
            "soft seat",
            "hard sleeper",
            "Soft sleeper",
            "high-grade soft sleeper",
            "2rd-class",
            "1st-class",
            "special-class",
            "business"
    };

//    public static final String[]  EASY_SEAT_TYPES = {
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

    public static final String[] FULL_SEAT_TYPES = {
            "no site",
            "hard seat",
            "soft seat",
            "hard sleeper",
            "Soft sleeper",
            "high-grade soft sleeper",
            "second-class seat",
            "first-class seat",
            "special-class seat",
            "business seat"
    };

    //    public static final String[]  FULL_SEAT_TYPES = {
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
    public static final String[] ORDER_STATE = {
            "Not Paid",
            "Paid & Not Collected",
            "Collected",
            "Cancel & Rebook",
            "Cancel",
            "Refunded",
            "Used",
            "other"
    };

//    public static final String[] ORDER_STATE = {
//            "待支付",
//            "待完成",
//            "已取消",
//            "改退签",
//            "已完成"
//    };

    private String pathName; //路线名
    private String pathStartDate; // 路线出发日期
    private int stationNumber; // 第几停靠站
    private int seatType; // 座位类型
    private int leftTickets; // 剩余票数

    public Ticket() {
    }

    public Ticket(String pathName, String pathStartDate, int stationNumber, int seatType, int leftTickets) {
        this.pathName = pathName;
        this.pathStartDate = pathStartDate;
        this.stationNumber = stationNumber;
        this.seatType = seatType;
        this.leftTickets = leftTickets;
    }


    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathStartDate() {
        return pathStartDate;
    }

    public void setPathStartDate(String pathStartDate) {
        this.pathStartDate = pathStartDate;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public int getSeatType() {
        return seatType;
    }

    public void setSeatType(int seatType) {
        this.seatType = seatType;
    }

    public int getLeftTickets() {
        return leftTickets;
    }

    public void setLeftTickets(int leftTickets) {
        this.leftTickets = leftTickets;
    }
}
