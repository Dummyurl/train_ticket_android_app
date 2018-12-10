package ts.trainticket.httpUtils;


public class UserUtils {
	// post 数据不用拼接url
	// get 要把数拼接到url上
//	public static String postRegistUser(RegistData rd) {
	//	String uri = UrlProperties.uri+UrlProperties.postRegistUrl;
		//return HttpUtils.sendPostRequest(uri,new Gson().toJson(rd));
//	}
	public static String getData(String url){
		return HttpUtils.sendGetRequest(url);
	}
}