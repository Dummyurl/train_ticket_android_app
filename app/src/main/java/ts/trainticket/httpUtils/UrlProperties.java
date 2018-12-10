package ts.trainticket.httpUtils;

public class UrlProperties {

    public final static String clientIstioIp = "http://10.141.211.161:31380";
    // get all city name
    public final static String getAllCity = "/station/query";


    // All  G /D   other
    public final static String travel1Query = "/travel/query";
    public final static String travel2Query = "/travel2/query";

    // login
    // http://10.141.211.161:31380/login
    public final static String login = "/login";
    public final static String register ="/register";

    public final static String findContacts = "/contacts/findContacts";


    public final static String orderQuery = "/order/query";

    public final static String createContacts = "/contacts/create";
    public final static String deleteContacts = "/contacts/deleteContacts";

    // submit order g/d
    public final static String gdPreserve = "/preserve";
    public final static String otherPreserve = "/preserveOther";

    // pay
    public final static String inside_payment = "/inside_payment/pay";
    // cancel order
    public final static String cancelOrder = "/cancelOrder";


    // search all trip line
    public final static String queryAllRoute = "/route/queryAll";

    //
    public final static String  getStopAtStation = "/travelPlan/getAll";

    public final static String  getTravelAll = "/travel/queryAll";



    public final static String headPicUrl = "/user/addUserHeadPic";

    public final static String addUser = "/user/addUser";



    // change ticket status
    public final static String getOneOrderByOrderId = "/orders/getOneOrderByOrderId";
    public final static String getWeather = "/weather/cityName";



    // update single line ticket
    public final static String getOnePathTicket = "ticket/getOnePathTicket";

    public final static String modifyUSerInfo = "/user/modifyUSerInfo";
}
