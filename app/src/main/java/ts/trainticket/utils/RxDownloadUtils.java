package ts.trainticket.utils;

import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import ts.trainticket.httpUtils.HttpUtils;
import ts.trainticket.httpUtils.ResponseResult;
import ts.trainticket.httpUtils.UrlProperties;

/**
 * Created by liuZOZO on 2018/1/14.
 */
public class RxDownloadUtils {
    public static Observable getAllCities(final String url) {
        // 创建被观察者
        return Observable.create(new Observable.OnSubscribe<ResponseResult>() {

            @Override
            public void call(Subscriber<? super ResponseResult> subscriber) {
                if (null == url) {
                    subscriber.onError(new Throwable("likely not connect to Network!"));
                    return;
                }
                try {
                    String uri = UrlProperties.clientIstioIp + UrlProperties.getAllCity;
                    String temp = HttpUtils.sendGetRequest(uri);
                    subscriber.onNext(new Gson().fromJson(temp, ResponseResult.class));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 抛出异常
                    subscriber.onError(new Throwable("likely not connect to Server!"));
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
