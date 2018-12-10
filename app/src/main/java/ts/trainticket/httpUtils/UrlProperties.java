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

    public final static String findContacts = "/contacts/findContacts";


    public final static String orderQuery = "/order/query";
    public final static String otherOrderQuery = "/orderOther/query";


    public final static String createContacts = "/contacts/create";
    public final static String deleteContacts = "/contacts/deleteContacts";

    // 提交订单 g/d
    public final static String gdPreserve = "/preserve";
    public final static String otherPreserve = "/preserveOther";

    // pay
    public final static String inside_payment = "/inside_payment/pay";
    // cancel order
    public final static String cancelOrder = "/cancelOrder";


    // 查询所有路线
    public final static String queryAllRoute = "/route/queryAll";
    // 查询每个车站停留时间
    public final static String  queryStation = "/station/query";

    public final static String   getTripTimeTable =  "/travelPlan/getTripTimeTable";

    // 查询G123 经过的路线
    public final static String  getStopAtStation = "/travelPlan/getAll";

    public final static String  getTravelAll = "/travel/queryAll";
    // cityStation
    public final static String csUri = "http://10.141.211.161:31380";
    // public final static String getAllCity = "/city/getAllCity";

    public final static String getCityStation = "/station/findStationByCityName";

    //%E5%90%88%E8%82%A5
    public final static String findCity = "/city/findCity";

    // ps 相关
    public final static String psUri = "http://115.159.107.170:8084";
    public final static String getCPSByStation = "/pathStation/getCPSByStation";
    public final static String queryPathByTwoCity = "/pathStation/queryPathByTwoCity";
    //路线详细详细
    public final static String getTimeTable = "/pathStation/getTimeTable";

    // http://liucoding.cn:8084/pathStation/queryPathByTwoCity/%E4%B8%8A%E6%B5%B7/%E5%90%88%E8%82%A5/12/12/12/12

    // 通过用户名，密码进行登录
    public final static String loginUrl = "http://liucoding.cn:8081/user/getUserByUserNameAndPwd";

//    public final static String loginUrl = "http://172.16.21.26:8081/user/getUserByUserNameAndPwd";

    // 上传头像
//    public final static String headPicUrl = "http://172.16.21.26:8081/user/addUserHeadPic";


    public final static String headPicUrl = "http://liucoding.cn:8081/user/addUserHeadPic";

    public final static String addUser = "http://liucoding.cn:8081/user/addUser";

    public final static String getContacts = "http://liucoding.cn:8081/contacts/findAllContactsByUserName";
    public final static String delConatactsById = "http://liucoding.cn:8081/contacts/deleteContacts";

    // 得到所有的 order
    public final static String getAllOrdersByUserName = "http://liucoding.cn:8080/orders/getAllOrdersByUserName";
    // 添加一个常用联系人
    public final static String addContacts = "http://liucoding.cn:8081/contacts/addContacts";


    // 提交订单
    public final static String addOrders = "http://liucoding.cn:8080/orders/addOrders";
    // 退改签订单
    public final static String modifyOrders = "http://liucoding.cn:8080/orders/modifyOrders";


    // 减少一张车票
    public final static String reduceTiccketNum = "http://liucoding.cn:8083/ticket/reduceNum";


    // 从服务器查询订单信息
    public final static String getAllCOrdersByUserName = "http://liucoding.cn:8080/orders/getAllCOrdersByUserName";
    public final static String findISOrderOrNot = "http://liucoding.cn:8080/orders/findISOrderOrNot";

    // 改变订单状态
    public final static String changeOrderStatus = "http://liucoding.cn:8080/orders/changeOrderStatus";
    public final static String changeOrderStatus2 = "http://liucoding.cn:8080/orders/changeOrderStatus2";

    public final static String getOneOrderByOrderId = "http://liucoding.cn:8080/orders/getOneOrderByOrderId";

    public final static String getWeather = "http://liucoding.cn:8087/weather/cityName";


    public final static String findStationByCityName = "http://liucoding.cn:8082/station/findStationByCityName";

    // 更新单个路线车票
    public final static String getOnePathTicket = "http://liucoding.cn:8083/ticket/getOnePathTicket";

    public final static String modifyUSerInfo = "http://liucoding.cn:8081/user/modifyUSerInfo";
}
