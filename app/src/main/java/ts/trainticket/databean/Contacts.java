package ts.trainticket.databean;

/**
 * Created by liuZOZO on 2018/1/16.
 */
public class Contacts {
    private String userName;// 账户名  必须对应accout 表下的 userName ，隶属关系
    private String contactRealName; // 常用联系人真实姓名
    private String contactRealIcard;// 常用联系人真实身份证号
    private String contactPhone;// 常用联系人手机号
    private Integer conatctType;// 常用联系人类型
    private String orderId;

    private String seatPrice;

    public Contacts(String userName, String contactRealName, String contactRealIcard, String contactPhone, Integer conatctType) {
        this.userName = userName;
        this.contactRealName = contactRealName;
        this.contactRealIcard = contactRealIcard;
        this.contactPhone = contactPhone;
        this.conatctType = conatctType;
    }


    public Contacts(String userName, String contactRealName, String contactRealIcard, Integer conatctType,String orderId) {
        this.userName = userName;
        this.contactRealName = contactRealName;
        this.contactRealIcard = contactRealIcard;
        this.conatctType = conatctType;
        this.orderId = orderId;
    }

    public Contacts(String userName, String contactRealName, String contactRealIcard, Integer conatctType, String orderId, String seatPrice) {
        this.userName = userName;
        this.contactRealName = contactRealName;
        this.contactRealIcard = contactRealIcard;
        this.conatctType = conatctType;
        this.orderId = orderId;
        this.seatPrice = seatPrice;
    }

    public Contacts(String userName, String contactRealName, String contactRealIcard, String contactPhone, Integer conatctType, String orderId, String seatPrice) {
        this.userName = userName;
        this.contactRealName = contactRealName;
        this.contactRealIcard = contactRealIcard;
        this.contactPhone = contactPhone;
        this.conatctType = conatctType;
        this.orderId = orderId;
        this.seatPrice = seatPrice;
    }

    public String getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(String seatPrice) {
        this.seatPrice = seatPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactRealName() {
        return contactRealName;
    }

    public void setContactRealName(String contactRealName) {
        this.contactRealName = contactRealName;
    }

    public String getContactRealIcard() {
        return contactRealIcard;
    }

    public void setContactRealIcard(String contactRealIcard) {
        this.contactRealIcard = contactRealIcard;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getConatctType() {
        return conatctType;
    }

    public void setConatctType(Integer conatctType) {
        this.conatctType = conatctType;
    }
}
