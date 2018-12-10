package ts.trainticket.domain;

public class PathTimeTable {
    private String stationName;
    private String distance;
    private String stayTime;

    public PathTimeTable() {
    }

    public PathTimeTable(String stationName, String distance, String stayTime) {
        this.stationName = stationName;
        this.distance = distance;
        this.stayTime = stayTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStayTime() {
        return stayTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }
}
