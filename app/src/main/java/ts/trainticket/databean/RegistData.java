package ts.trainticket.databean;

public class RegistData {
    private String userName;
    private String userPassword;
    private String userPhone;
    private String realName;
    private String realIcard;
    public RegistData(){

    }
    
    public RegistData(String userName, String userPassword, String userPhone, String realName, String realIcard) {
		super();
		this.userName = userName;
		this.userPassword = userPassword;
		this.userPhone = userPhone;
		this.realName = realName;
		this.realIcard = realIcard;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealIcard() {
        return realIcard;
    }

    public void setRealIcard(String realIcard) {
        this.realIcard = realIcard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
