package ts.trainticket.databean;

import java.io.Serializable;

/**
 * Created by liuZOZO on 2018/2/26.
 */
// 昨日的天气
public class Yesterday implements Serializable{

    private static final long serialVersionUID = 300639608376607622L;
    private String date;
    private String high;
    private String fx;
    private String low;
    private String fl;
    private String type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }
}
