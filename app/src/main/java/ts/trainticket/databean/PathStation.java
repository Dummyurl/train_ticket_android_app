package ts.trainticket.databean;

/**
 * Created by liuZOZO on 2018/1/18.
 */
public class PathStation {

    private String pathName;
    private String stationName;
    private int pathstationNum;
    private String arriveTime;
    private String startTime;

    private String totalTime;

    public PathStation() {
    }

    public PathStation(String pathName, String stationName, int pathstationNum, String arriveTime, String startTime) {
        this.pathName = pathName;
        this.stationName = stationName;
        this.pathstationNum = pathstationNum;
        this.arriveTime = arriveTime;
        this.startTime = startTime;
    }

    public PathStation(String pathName, String totalTime, String startTime, String arriveTime, int pathstationNum, String stationName) {
        this.pathName = pathName;
        this.totalTime = totalTime;
        this.startTime = startTime;
        this.arriveTime = arriveTime;
        this.pathstationNum = pathstationNum;
        this.stationName = stationName;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getPathstationNum() {
        return pathstationNum;
    }

    public void setPathstationNum(int pathstationNum) {
        this.pathstationNum = pathstationNum;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
