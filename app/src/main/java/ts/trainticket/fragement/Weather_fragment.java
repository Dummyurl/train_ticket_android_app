package ts.trainticket.fragement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ts.trainticket.R;
import ts.trainticket.WeatherDetailActivty;

/**
 * Created by liuZOZO on 2018/3/24.
 */
public class Weather_fragment extends BaseFragment {

    private LinearLayout search_weatherBtn = null;
    private TextView position_provenceTv = null;
    private EditText search_city = null;
    private LocationManager locationManager;
    private double latitude = 0;
    private TextView positionCity = null;
    private double longitude = 0;
    String latLongString;
    String provence = "";

    private TextView tiem_dateBTv;
    private TextView tiem_dateETV;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            double[] data = (double[]) msg.obj;

            List<Address> addList = null;
            Geocoder ge = new Geocoder(getContext());
            try {
                addList = ge.getFromLocation(data[0], data[1], 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (addList != null && addList.size() > 0) {
                for (int i = 0; i < addList.size(); i++) {
                    Address ad = addList.get(i);
                    provence  = ad.getAdminArea();
                    latLongString = ad.getLocality();
                }
            }
            position_provenceTv.setText("("+provence+")");
            positionCity.setText(latLongString);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initView(view);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        new Thread() {
            public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

            @Override
            public void run() {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
                    //请求权限
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                //判断是否需要 向用户解释，为什么要申请该权限
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_CONTACTS);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
                    //请求权限
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                //判断是否需要 向用户解释，为什么要申请该权限
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_CONTACTS);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    latitude = location.getLatitude(); // 经度
                    longitude = location.getLongitude(); // 纬度
                    double[] data = {latitude, longitude};
                    Message msg = handler.obtainMessage();
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            }

        }.start();
        return view;
    }

    private void initView(View view) {
        positionCity = (TextView) view.findViewById(R.id.position_city);
        search_weatherBtn = (LinearLayout) view.findViewById(R.id.search_weather_id);
        search_city = (EditText) view.findViewById(R.id.search_city_id);
        position_provenceTv = (TextView) view.findViewById(R.id.position_provence);

        tiem_dateBTv = (TextView) view.findViewById(R.id.tiem_date_bid);
        tiem_dateBTv.setText(getFetureDate(0));
        tiem_dateETV = (TextView) view.findViewById(R.id.time_date_eid);
        tiem_dateETV.setText(getFetureDate(4));
        search_weatherBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String city = search_city.getText().toString();
                if (city == null) {
                    Toast.makeText(getContext(), "请输入城市", Toast.LENGTH_SHORT).show();
                } else {
                    // 跳转
                    Intent cityIntent = new Intent(getContext(), WeatherDetailActivty.class);
                    cityIntent.putExtra("search_city", city);
                    startActivity(cityIntent);
                }
            }
        });
    }

    public static  String  getFetureDate(int  time){
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,time);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        System.out.println(dateString);
        return dateString;
    }

}
