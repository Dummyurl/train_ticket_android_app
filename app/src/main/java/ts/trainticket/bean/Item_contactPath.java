package ts.trainticket.bean;

import java.util.Arrays;

/**
 * Created by liuZOZO on 2018/1/21.
 */
public class Item_contactPath {

    private String pathName; // 路线编号 G9418
    private String pathDate;

    private String startStation; // 开始车站
    private int startNumber;
    private String startTime;

    private String durationTime; // 中间多长时间

    private String endStation;  // 目的车站
    private int endNumber;
    private String endTime;

    private String []leftTickets = new String[10];
    private String[] seats = new String[10];

    public Item_contactPath() {
    }

    public Item_contactPath(String pathName, String pathDate, String startStation, int startNumber, String startTime, String durationTime, String endStation, int endNumber, String endTime, String[] seats,String[] leftTickets) {
        this.pathName = pathName;
        this.pathDate = pathDate;
        this.startStation = startStation;
        this.startNumber = startNumber;
        this.startTime = startTime;
        this.durationTime = durationTime;
        this.endStation = endStation;
        this.endNumber = endNumber;
        this.endTime = endTime;
        this.leftTickets = leftTickets;
        this.seats = seats;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathDate() {
        return pathDate;
    }

    public void setPathDate(String pathDate) {
        this.pathDate = pathDate;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }

    public String[] getLeftTickets() {
        return leftTickets;
    }

    public void setLeftTickets(String[] leftTickets) {
        this.leftTickets = leftTickets;
    }

    public String[] getSeats() {
        return seats;
    }

    public void setSeats(String[] seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Item_contactPath{" +
                "pathName='" + pathName + '\'' +
                ", pathDate='" + pathDate + '\'' +
                ", startStation='" + startStation + '\'' +
                ", startNumber=" + startNumber +
                ", startTime='" + startTime + '\'' +
                ", durationTime='" + durationTime + '\'' +
                ", endStation='" + endStation + '\'' +
                ", endNumber=" + endNumber +
                ", endTime='" + endTime + '\'' +
                ", leftTickets=" + Arrays.toString(leftTickets) +
                ", seats=" + Arrays.toString(seats) +
                '}';
    }
}
