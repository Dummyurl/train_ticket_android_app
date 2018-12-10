package ts.trainticket.databean;

import java.util.List;

/**
 * Created by liuZOZO on 2018/1/18.
 */
public class StationList {

    List<Station> stationList;

    public StationList(List<Station> stationList) {
        this.stationList = stationList;
    }

    public List<Station> getStationList() {
        return stationList;
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
    }

}
