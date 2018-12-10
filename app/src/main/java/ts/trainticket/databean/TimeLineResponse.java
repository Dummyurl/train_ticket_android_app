package ts.trainticket.databean;

import java.util.List;

/**
 * Created by liuZOZO on 2018/3/4.
 */
public class TimeLineResponse {
    private boolean status;
    private String msg;
    private List<TimeTable> timeTables;

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

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
