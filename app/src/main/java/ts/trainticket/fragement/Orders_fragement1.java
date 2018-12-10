package ts.trainticket.fragement;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.R;
import ts.trainticket.adapter.Orders1Adapter;
import ts.trainticket.databean.Orders;
import ts.trainticket.databean.OrdersPageResponse;
import ts.trainticket.domain.Order;
import ts.trainticket.domain.OrderList;
import ts.trainticket.domain.OrderQueryInfo;
import ts.trainticket.domain.PreserveOrderResult;
import ts.trainticket.domain.TripResponse;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;
import ts.trainticket.utils.CalendarUtil;

/**
 * Created by liuZOZO on 2018/3/13.
 */
public class Orders_fragement1 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swiper = null; // 下拉刷新控件
    private RecyclerView recyclerView = null; // 常用联系人列表

    private ImageView animationIV;
    private AnimationDrawable animationDrawable;
    private ImageView reserve_tipsImg = null;
    private TextView reserve_tipsTv = null;

    // 请求两次 orderlist 显示
    private int tagNum = 0;
    private List<OrderList> orderList;
    private List<OrderList> otherOrderList;
    private List<OrderList> allOrderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_orders1, container, false);

        initViews(view);

        getOrderListFromServer();

        return view;
    }

    private void initViews(View view) {
        swiper = (SwipeRefreshLayout) view.findViewById(R.id.refresh_orders1_table);
        swiper.setOnRefreshListener(this);
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView = (RecyclerView) view.findViewById(R.id.view_orders1_table);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        animationIV = (ImageView) view.findViewById(R.id.animationIV_os1);
        animationIV.setImageResource(R.drawable.pic_loading);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();

        reserve_tipsImg = (ImageView) view.findViewById(R.id.reserve_tips_img);
        reserve_tipsTv = (TextView) view.findViewById(R.id.reserve_tips);
    }


    private void getOrderListFromServer() {
        tagNum = 0;
        orderList = new ArrayList<>();
        otherOrderList = new ArrayList<>();
        allOrderList = new ArrayList<>();

        // , UrlProperties.clientIstioIp + UrlProperties.otherOrderQuery
        String[] ordersUrl = new String[]{UrlProperties.clientIstioIp + UrlProperties.orderQuery};
        for (int i = 0; i < ordersUrl.length; i++) {
            System.out.println("==============0000--0-");
            getOrderDataFromServer(ordersUrl[i]);
        }
    }


    public void getOrderDataFromServer(String ordersUrl) {
        // 基于请求头的header 和 tokin  id 进行查询
        OrderQueryInfo orderQueryInfo = new OrderQueryInfo(null, null, null, null, 0, false, false, false);

        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(orderQueryInfo));


        String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");


        subscription = RxHttpUtils.postWithHeader(ordersUrl, loginId, token, requestBody, getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        unlockClick();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String responseResult) {
                        unlockClick();
                        System.out.println(responseResult + "=09889");
                        if (responseResult != null && !responseResult.equals("")) {
                            Gson gson = new Gson();
                            List<OrderList> orderLists = new ArrayList<OrderList>();
                            Type listType = new TypeToken<List<OrderList>>() {
                            }.getType();
                            orderLists = gson.fromJson(responseResult, listType);
                            System.out.println(orderLists.size() + "0-9323290");

                            JSONArray jsonArray = JSON.parseArray(responseResult);
                            Iterator it = jsonArray.iterator();

                            showTable(orderLists);
                        } else {
                            showStates();
                            // Toast.makeText(getActivity(), "没有订单信息", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 展示路线时刻表
    private void showTable(List<OrderList> ordersList1) {
        tagNum++;
        // orderList  和 orderOtherList
        if (tagNum == 1)
            orderList.addAll(ordersList1);
//        if (tagNum == 2)
//            otherOrderList.addAll(ordersList1);
//        System.out.println(allOrderList.size() + "=--99" + tagNum);
        if (tagNum == 1) {
            allOrderList.addAll(orderList);
            //allOrderList.addAll(otherOrderList);
            System.out.println(allOrderList.size() + "=--9999999999999999999");

            if (allOrderList.size() == 0) {
                showStates();
            } else {
                animationDrawable.stop();
                animationIV.setVisibility(View.GONE);
            }
            Orders1Adapter myAdapter = new Orders1Adapter(allOrderList, getContext());
            myAdapter.setHasStableIds(true);
            recyclerView.setAdapter(myAdapter);
        }
    }

    private void showStates() {
        reserve_tipsTv.setVisibility(View.VISIBLE);
        reserve_tipsImg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        animationDrawable.stop();
        animationIV.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        // 刷新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 一般会从网络获取数
                getOrderListFromServer();
                swiper.setRefreshing(false);// 结束后停止刷新
            }
        }, 3000);
    }

}
