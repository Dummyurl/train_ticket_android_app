package ts.trainticket.databean;

import java.util.List;

/**
 * Created by liuZOZO on 2018/1/16.
 */
public class TimeTableResponse {

    private boolean status;
    private String msg;
    private List<PathStation> pathStationList;

    public TimeTableResponse(boolean status, String msg, List<PathStation> pathStationList) {
        this.status = status;
        this.msg = msg;
        this.pathStationList = pathStationList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PathStation> getPathStationList() {
        return pathStationList;
    }

    public void setPathStationList(List<PathStation> pathStationList) {
        this.pathStationList = pathStationList;
    }
}
