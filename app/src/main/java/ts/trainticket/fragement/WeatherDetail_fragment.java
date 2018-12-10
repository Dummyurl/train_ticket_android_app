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

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.R;
import ts.trainticket.bean.Item_contactPath;
import ts.trainticket.databean.Forecast;
import ts.trainticket.databean.WeatherResponse;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;

/**
 * Created by liuZOZO on 2018/3/24.
 */
public class WeatherDetail_fragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tipsTv = null;
    private TextView airTv = null;
    private TextView airdegreeTv = null;
    private TextView cityNameTv = null;
    private TextView temptureTv = null;


    private ImageView animationIV;
    private AnimationDrawable animationDrawable;
    private ImageView reserve_tipsImg2 = null;
    private TextView reserve_tipsTv2 = null;

    private SwipeRefreshLayout refreshWeather = null;
    RecyclerView recyclerView = null;
    List<Item_contactPath> contactPaths = null;
    // 晴  多云 阴 小雨 大雨 雾 小雪  大雪 雨夹雪  雷雨
    private int[] weatherIcon = new int[]{R.drawable.hb_sunshine_blue,
            R.drawable.hb_cloudy_blue,
            R.drawable.hb_overcas_bluet,
            R.drawable.hb_lightrain_blue,
            R.drawable.hb_moderaterain_blue,
            R.drawable.hb_fog_blue,
            R.drawable.hb_lightsnown_blue,
            R.drawable.hb_heavysnow_blue,
            R.drawable.hb_rainsnown_blue,
            R.drawable.hb_thundershower_blue
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weatherdetail, container, false);
        initView(view);
        getData();
        return view;
    }

    public void getData() {
        String cityName = getArguments().getString("search_city");
        if(cityName == null || "".equals(cityName))
            cityName = "合肥";
        getDataFromServer(cityName);
    }

    private void initView(View view) {

        tipsTv = (TextView) view.findViewById(R.id.tips_wid);
        airTv = (TextView) view.findViewById(R.id.air_wid);
        airdegreeTv = (TextView) view.findViewById(R.id.airdegree_wid);
        cityNameTv = (TextView) view.findViewById(R.id.cityName_wid);
        temptureTv = (TextView) view.findViewById(R.id.tempture_wid);


        animationIV = (ImageView) view.findViewById(R.id.animationIV_wes2);
        animationIV.setImageResource(R.drawable.pic_loading);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();

        reserve_tipsImg2 = (ImageView) view.findViewById(R.id.reserve_wes_img_2);
        reserve_tipsTv2 = (TextView) view.findViewById(R.id.reserve_wes_2);


        refreshWeather = (SwipeRefreshLayout) view.findViewById(R.id.refresh_weather_id);
        recyclerView = (RecyclerView) view.findViewById(R.id.weather_recycleView);
        // date  图标  多云(type)   风向(fengxiang) 风力(fengli)   高温(high)  低温(low)
        refreshWeather.setOnRefreshListener(this);
        refreshWeather.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showContactPath(WeatherResponse wrs) {
        temptureTv.setText(wrs.getData().getWendu() + "℃");
        cityNameTv.setText("(" + wrs.getData().getCity() + ")");
        String api = wrs.getData().getAqi();
        airTv.setText(api);
        airdegreeTv.setText(getAqiDegree(api));
        tipsTv.setText(wrs.getData().getGanmao());

        animationDrawable.stop();
        animationIV.setVisibility(View.GONE);

        List<Forecast> weatherList = wrs.getData().getForecast();
        WeatherListAdapter myAdapter = new WeatherListAdapter(weatherList);
        recyclerView.setAdapter(myAdapter);
    }

    public String getAqiDegree(String api) {
        int num = Integer.parseInt(api);
        if (0 <= num && num <= 50)
            return "(优)";
        else if (51 <= num && num <= 100)
            return "(良)";
        else if (101 <= num && num <= 150)
            return "(轻度污染)";
        else if (151 <= num && num <= 200)
            return "(中度污染)";
        else if (201 <= num && num <= 300)
            return "(重度污染)";
        else if (301 <= num)
            return "(严重污染)";
        else
            return "(未知等级)";
    }


    public void getDataFromServer(String cityName) {
        // 准备数据
        try {
            cityName = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 请求连接
        String listStationUri = UrlProperties.getWeather + "/" + cityName;
        subscription = RxHttpUtils.getDataByUrl(listStationUri, getContext())
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

                        if (responseResult != null && !responseResult.equals("")) {
                            Gson gson = new Gson();
                            WeatherResponse wrs = gson.fromJson(responseResult, WeatherResponse.class);
                            if (wrs.getStatus() == 1000) {
                                showContactPath(wrs);
                            } else {
                                animationDrawable.stop();
                                animationIV.setVisibility(View.GONE);
                               // Toast.makeText(getActivity(), "返回异常数据", Toast.LENGTH_SHORT).show();
                                reserve_tipsTv2.setVisibility(View.VISIBLE);
                                reserve_tipsImg2.setVisibility(View.VISIBLE);
                            }
                        } else {
                            animationDrawable.stop();
                            animationIV.setVisibility(View.GONE);
                            reserve_tipsTv2.setVisibility(View.VISIBLE);
                            reserve_tipsImg2.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // 晴  多云 阴 小雨 大雨 雾 小雪  大雪 雨夹雪  雷阵雨
    private int getIconImg(String type){
        if(type.contains("晴"))
            return weatherIcon[0];
        if(type.contains("多云"))
            return weatherIcon[1];
        if(type.contains("阴"))
            return weatherIcon[2];
        if(type.contains("小雨") || type.contains("阵雨"))
            return weatherIcon[3];
        if(type.contains("大雨"))
            return weatherIcon[4];
        if(type.contains("雾"))
            return weatherIcon[5];
        if(type.contains("小雪"))
            return weatherIcon[6];
        if(type.contains("大雪"))
            return weatherIcon[7];
        if(type.contains("雨夹雪"))
            return weatherIcon[8];
        if(type.contains("雷阵雨"))
            return weatherIcon[9];
        else
            return  weatherIcon[1];
    }
    private String setText(String text){
        if(text.length() == 1)
            return text+"      "+" |  ";
        if(text.length() == 2)
            return text+"  "+" |  ";
        else
            return text+" |  ";
    }
    class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.WeatherHolder> {
        private List<Forecast> list;

        public WeatherListAdapter(List<Forecast> list) {
            this.list = list;
        }

        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_weather, viewGroup, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherHolder holder, int i) {
            holder.setIsRecyclable(false);
            holder.dateTv.setText(list.get(i).getDate());
            holder.typeTv.setText(setText(list.get(i).getType()));
            holder.typeImg.setBackgroundResource(getIconImg(list.get(i).getType()));
            holder.fengxiangTv.setText(list.get(i).getFengxiang()+" ");
            // ![CDATA[3-4级]]
            String fenglili = list.get(i).getFengli().substring(9);
            holder.fengliTv.setText(fenglili.substring(0, fenglili.length() - 3));
            holder.highTv.setText(list.get(i).getHigh().substring(3));
            //低温 11℃
            String lowtp = list.get(i).getLow().substring(3);
            holder.lowTv.setText(lowtp.substring(0, lowtp.length() - 1));
            if (i == list.size())
                holder.weatherLine.setVisibility(View.GONE);
            else
                holder.weatherLine.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class WeatherHolder extends RecyclerView.ViewHolder {
            // date  图标  多云(type)   风向(fengxiang) 风力(fengli)   高温(high)  低温(low)-->
            private TextView dateTv;
            private ImageView typeImg;
            private TextView typeTv;
            private TextView fengxiangTv;
            private TextView fengliTv;
            private TextView highTv;
            private TextView lowTv;
            private View weatherLine;

            public WeatherHolder(View itemView) {
                super(itemView);
                dateTv = (TextView) itemView.findViewById(R.id.weather_date_id);
                typeImg = (ImageView) itemView.findViewById(R.id.weather_typeimg_id);
                typeTv = (TextView) itemView.findViewById(R.id.weather_type_id);
                fengxiangTv = (TextView) itemView.findViewById(R.id.weather_fengxiang_id);

                fengliTv = (TextView) itemView.findViewById(R.id.weather_fengli_id);
                highTv = (TextView) itemView.findViewById(R.id.weather_high_id);
                lowTv = (TextView) itemView.findViewById(R.id.weather_low_id);

                weatherLine = itemView.findViewById(R.id.weather_line_id);
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 一般会从网络获取数
                refreshWeather.setRefreshing(false);
                unlockClick();
            }
        }, 3000);
    }
}
