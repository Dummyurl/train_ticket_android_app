package ts.trainticket.databean;

import java.io.Serializable;

/**
 * Created by liuZOZO on 2018/2/26.
 */
public class WeatherResponse implements Serializable{

    private static final long serialVersionUID = 6458900339609375582L;
    private Weather data;
    private Integer status;
    private String  desc;

    public Weather getData() {
        return data;
    }

    public void setData(Weather data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
