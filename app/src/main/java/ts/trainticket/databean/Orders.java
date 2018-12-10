package ts.trainticket.databean;

/**
 * Created by liuZOZO on 2018/3/10.
 */
public class Orders {

    private String orderId;
    private String userName;
    private String pasIdCard;
    private String pathName;
    private String pathStartDate;
    private String pathArriveDate;
    private String buyTime;
    private String takeTime;
    private String arriveTime;
    private String startStationName;
    private String arriveStationName;
    private String pasType;
    private String seatType;
    private String ticketPrice;
    private String status;

    public Orders() {
    }


    public Orders(String userName, String pasIdCard, String pathName, String pathStartDate, String pathArriveDate, String buyTime, String takeTime, String arriveTime, String startStationName, String arriveStationName, String pasType, String seatType, String ticketPrice, String status) {
        this.userName = userName;
        this.pasIdCard = pasIdCard;
        this.pathName = pathName;
        this.pathStartDate = pathStartDate;
        this.pathArriveDate = pathArriveDate;
        this.buyTime = buyTime;
        this.takeTime = takeTime;
        this.arriveTime = arriveTime;
        this.startStationName = startStationName;
        this.arriveStationName = arriveStationName;
        this.pasType = pasType;
        this.seatType = seatType;
        this.ticketPrice = ticketPrice;
        this.status = status;
    }

    public Orders(String orderId, String userName, String pasIdCard, String pathName, String pathStartDate, String pathArriveDate, String buyTime, String takeTime, String arriveTime, String startStationName, String arriveStationName, String pasType, String seatType, String ticketPrice, String status) {
        this.orderId = orderId;
        this.userName = userName;
        this.pasIdCard = pasIdCard;
        this.pathName = pathName;
        this.pathStartDate = pathStartDate;
        this.pathArriveDate = pathArriveDate;
        this.buyTime = buyTime;
        this.takeTime = takeTime;
        this.arriveTime = arriveTime;
        this.startStationName = startStationName;
        this.arriveStationName = arriveStationName;
        this.pasType = pasType;
        this.seatType = seatType;
        this.ticketPrice = ticketPrice;
        this.status = status;
    }

    public String getPathArriveDate() {
        return pathArriveDate;
    }

    public void setPathArriveDate(String pathArriveDate) {
        this.pathArriveDate = pathArriveDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasIdCard() {
        return pasIdCard;
    }

    public void setPasIdCard(String pasIdCard) {
        this.pasIdCard = pasIdCard;
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

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getArriveStationName() {
        return arriveStationName;
    }

    public void setArriveStationName(String arriveStationName) {
        this.arriveStationName = arriveStationName;
    }

    public String getPasType() {
        return pasType;
    }

    public void setPasType(String pasType) {
        this.pasType = pasType;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}