package ts.trainticket.databean;

/**
 * Created by liuZOZO on 2018/1/18.
 */
public class Station {
    private String stationName;
    private String cityName;
    private String stationDesc;

    public Station() {
    }

    public Station(String stationName, String cityName, String stationDesc) {
        this.stationName = stationName;
        this.cityName = cityName;
        this.stationDesc = stationDesc;
    }

    public String getStationDesc() {
        return stationDesc;
    }

    public void setStationDesc(String stationDesc) {
        this.stationDesc = stationDesc;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
