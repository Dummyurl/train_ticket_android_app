package ts.trainticket.httpUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.okhttp.RequestBody;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by liuZOZO on 2018/1/14.
 */
public class RxHttpUtils {


    public static Observable getPostData(final String url, final RequestBody jsonstr , final Context context) {
        // 创建被观察者
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String > subscriber) {
                if (null == url || !isNetWorkConnected(context)) {
                    subscriber.onError(new Throwable("likely not connect to network"));
                    return;
                }
                try {
                    System.out.println(jsonstr +"--==--=-");
                    String temp = HttpUtils.postData(url,jsonstr);
                    subscriber.onNext(temp);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 抛出异常
                    subscriber.onError(new Throwable("likely not connect to server"));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable getDataPost(final String url, final RequestBody jsonstr , final Context context) {
        // 创建被观察者
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String > subscriber) {
                if (null == url || !isNetWorkConnected(context)) {
                    subscriber.onError(new Throwable("likely not connect to network"));
                    return;
                }
                try {
                    System.out.println(jsonstr +"--==--=-");
                    String temp = HttpUtils.postData(url,jsonstr);
                    subscriber.onNext(temp);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 抛出异常
                    subscriber.onError(new Throwable("likely not connect to server"));
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable getDataByUrl(final String url, final Context context) {
        // 创建被观察者
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String > subscriber) {
                if (null == url || !isNetWorkConnected(context)) {
                    subscriber.onError(new Throwable("likely not connect to network"));
                    return;
                }
                try {
                    String temp = HttpUtils.sendGetRequest(url);
                    subscriber.onNext(temp);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 抛出异常
                    subscriber.onError(new Throwable("likely not connect to server"));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    // post   传递 loginId  loginToken 的请求头
    public static Observable postWithHeader(final String url, final String loginId,final String loginToken, final RequestBody jsonstr , final Context context) {
        // 创建被观察者
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String > subscriber) {
                if (null == url || !isNetWorkConnected(context)) {
                    subscriber.onError(new Throwable("likely not connect to network"));
                    return;
                }
                try {
                    System.out.println(jsonstr +"--==--=-");
                    String temp = HttpUtils.postDataWithHeader(url,loginId, loginToken, jsonstr);
                    subscriber.onNext(temp);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 抛出异常
                    subscriber.onError(new Throwable("likely not connect to server"));
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    // get   传递 loginId  loginToken 的请求头
    public static Observable getWithHeader(final String url, final String loginId,final String loginToken, final Context context) {
        // 创建被观察者
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String > subscriber) {
                if (null == url || !isNetWorkConnected(context)) {
                    subscriber.onError(new Throwable("likely not connect to network"));
                    return;
                }
                try {
                    String temp = HttpUtils.getContacts(url,loginId, loginToken);
                    subscriber.onNext(temp);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 抛出异常
                    subscriber.onError(new Throwable("likely not connect to server"));
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    private static boolean isNetWorkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


}
