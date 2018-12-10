package ts.trainticket.databean;

import java.io.Serializable;

/**
 * Created by liuZOZO on 2018/2/26.
 */
// 未来的天气
public class Forecast implements Serializable{

    private static final long serialVersionUID = -7721485655996804090L;
    private String date;
    private String high;
    private String fengli;
    private String low;
    private String fengxiang;
    private String type;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
