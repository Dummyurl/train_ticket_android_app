package ts.trainticket.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.BuyTicketActivity;
import ts.trainticket.TravelPathActivity;
import ts.trainticket.R;
import ts.trainticket.databean.ContactPath;
import ts.trainticket.databean.ContactPathPageResponse;
import ts.trainticket.databean.Station;
import ts.trainticket.databean.Ticket;
import ts.trainticket.domain.QueryInfo;
import ts.trainticket.domain.TripResponse;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.CalendarUtil;

/**
 * Created by liuZOZO on 2018/1/21.
 * 车票预订时刻表 Path Deatil
 */
public class TravelPath_Fragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout refreshContactPath = null;

    RecyclerView recyclerView = null;
    List<ContactPath> contactPathTemp = null;
    //前一天 后一天
    private TextView before_dayTv = null;
    private TextView next_dayTv = null;
    private boolean tagDay = false;
    // 日期按钮
    private Button btn_tstart_date = null;
    private Calendar startDate = null;
    // 底部4个按钮
    private Button start_time_fid = null;
    private Button total_time_fid = null;
    private Button arrive_time_fid = null;
    private Button select_fid = null;

    private ImageView animationIV;
    private AnimationDrawable animationDrawable;
    private TextView reserve_tips;
    private ImageView reserve_tips_img;

    //准备数据
    private ContactPathPageResponse cppath = null;

    // 查询路线数据
    String start_city = "";
    String end_city = "";
    String pathStartDate = "";
    String start_time = "";
    String arrive_time = "";
    String seat_type = "";
    String car_typegd = "";
    String car_typez = "";
    String car_typet = "";
    String car_typek = "";


    List<Station> tempList1;
    List<Station> tempList2;
    int cityTotalNum = 0;


    // 请求两次 orderlist 显示
    private int tagNum = 0;
    private List<ContactPath> travel1List;
    private List<ContactPath> travel2List;
    private List<ContactPath> allPathList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_path, container, false);
        initView(view);
        initSearchData();
        initLoading(view);
        return view;
    }

    public void initSearchData() {
        start_city = getArguments().getString("start_city");
        end_city = getArguments().getString("end_city");

        pathStartDate = getArguments().getString("pathStartDate");
        pathStartDate = parseTime(pathStartDate);

        start_time = getArguments().getString("start_time");
        arrive_time = getArguments().getString("arrive_time");

        seat_type = getArguments().getString("seat_type");
        car_typegd = getArguments().getString("car_typegd");
        car_typez = getArguments().getString("car_typez");
        car_typet = getArguments().getString("car_typet");
        car_typek = getArguments().getString("car_typek");

        getOrderListFromServer(start_city, end_city, pathStartDate);
    }

    private void getOrderListFromServer(String start_city, String end_city, String pathStartDate) {
        tagNum = 0;
        travel1List = new ArrayList<>();
        travel2List = new ArrayList<>();
        allPathList = new ArrayList<>();

        String[] getPathUrl = new String[]{UrlProperties.clientIstioIp + UrlProperties.travel1Query, UrlProperties.clientIstioIp + UrlProperties.travel2Query};
        for (int i = 0; i < getPathUrl.length; i++) {
            getDataFromServer(getPathUrl[i], start_city, end_city, pathStartDate);
        }
    }

    public String parseTime(String datdString) {
        //2018年3月25日
        datdString = datdString.replace("年", "-");
        datdString = datdString.replace("月", "-");
        datdString = datdString.replace("日", "");
        String arr[] = datdString.split("-");
        if (arr[1].length() == 1)
            arr[1] = "0" + arr[1];
        datdString = arr[0] + "-" + arr[1] + "-" + arr[2];
        btn_tstart_date.setText(datdString);
        return datdString;
    }

    public void initView(View view) {
        before_dayTv = (TextView) view.findViewById(R.id.before_day_id);
        next_dayTv = (TextView) view.findViewById(R.id.next_day_id);
        before_dayTv.setOnClickListener(this);
        next_dayTv.setOnClickListener(this);

        refreshContactPath = (SwipeRefreshLayout) view.findViewById(R.id.refresh_contact_paths);
        refreshContactPath.setOnRefreshListener(this);
        refreshContactPath.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView = (RecyclerView) view.findViewById(R.id.view_contact_paths);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btn_tstart_date = (Button) view.findViewById(R.id.btn_tstart_date);
        addToBtnController(btn_tstart_date);
        // 初始化界面显示的出发日期
        if (null == startDate) {
            startDate = Calendar.getInstance();
        }
        changeShowDate();
        btn_tstart_date.setOnClickListener(new DateChooseListener());

        initBottomBtn(view);
    }

    private void initLoading(View view) {
        reserve_tips = (TextView) view.findViewById(R.id.reserve_tips);
        reserve_tips_img = (ImageView) view.findViewById(R.id.reserve_tips_img);
        reserve_tips.setText("Loading route desperately...");
        animationIV = (ImageView) view.findViewById(R.id.reserve_animationIV);
        animationIV.setImageResource(R.drawable.pic_loading);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();
    }

    public void initBottomBtn(View view) {
        start_time_fid = (Button) view.findViewById(R.id.early_time_fid);
        total_time_fid = (Button) view.findViewById(R.id.total_time_fid);
        arrive_time_fid = (Button) view.findViewById(R.id.arrive_time_fid);
        select_fid = (Button) view.findViewById(R.id.select_fid);

        start_time_fid.setOnClickListener(new BottomLayoutListener());
        total_time_fid.setOnClickListener(new BottomLayoutListener());
        arrive_time_fid.setOnClickListener(new BottomLayoutListener());
        select_fid.setOnClickListener(new BottomLayoutListener());
        addToBtnController(start_time_fid);
        addToBtnController(total_time_fid);
        addToBtnController(arrive_time_fid);
        addToBtnController(select_fid);
    }

    @Override
    public void onRefresh() {
        // 刷新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 一般会从网络获取数据
                getOrderListFromServer(start_city, end_city, pathStartDate);
                refreshContactPath.setRefreshing(false);// 结束后停止刷新
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.before_day_id:
                tagDay = true;
                getOrderListFromServer(start_city, end_city, getSpecifiedDayBefore(pathStartDate, -1));
                break;
            case R.id.next_day_id:
                tagDay = true;
                getOrderListFromServer(start_city, end_city, getSpecifiedDayBefore(pathStartDate, 1));
                break;
            default:
                break;
        }
    }


    public String getSpecifiedDayBefore(String specifiedDay, int num) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + num);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        pathStartDate = dayBefore;
        btn_tstart_date.setText(pathStartDate);
        return dayBefore;
    }

    // 底部四个按钮
    private class BottomLayoutListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
           // recyclerView.stopScroll();
            if (cppath != null) {
                if (start_time_fid.getId() == v.getId()) {  // 最早发站时刻表
                    Collections.sort(cppath.getContactPathList(), new Comparator<ContactPath>() {
                        @Override
                        public int compare(ContactPath oL, ContactPath oR) {
                            return compareTime(oL.getStartTime(), oR.getStartTime());
                        }
                    });
                } else if (total_time_fid.getId() == v.getId()) {
                    if (cppath != null) {
                        Collections.sort(cppath.getContactPathList(), new Comparator<ContactPath>() {
                            @Override
                            public int compare(ContactPath oL, ContactPath oR) {
                                return compareTime(changeTime(oL.getTotalTime()), changeTime(oR.getTotalTime()));
                            }
                        });
                    }
                } else if (arrive_time_fid.getId() == v.getId()) {
                    if (cppath != null) {
                        Collections.sort(cppath.getContactPathList(), new Comparator<ContactPath>() {
                            @Override
                            public int compare(ContactPath oL, ContactPath oR) {
                                return compareTime(oL.getArriveTime(), oR.getArriveTime());
                            }
                        });
                    }
                } else if (select_fid.getId() == v.getId()) {
                    // 根据车站进行过滤
                    Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), FilterPathPopWindowActivity.class);
//                    System.out.println(tempList1.size() + "-=0-90989" + tempList2.size());
//                    System.out.println(tempList1.toString() + "-=0-909489" + tempList2);
//                    Gson gson = new Gson();
//                    if (tempList1 != null && tempList2 != null) {
//                        intent.putExtra("startCity", gson.toJson(tempList1));
//                        intent.putExtra("arriveCity", gson.toJson(tempList2));
//                    }
//                    /// startActivity(intent);
//                    startActivityForResult(intent, TravelPathActivity.FILTE_RPATH_REQUEST_CODE);
                }
                try {
                    showNewContactPath(cppath.getContactPathList()); // 展示数据
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "likely not get any data", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showNewContactPath(List<ContactPath> contactPaths) throws ParseException {
            ContactPathAdapter myAdapter = new ContactPathAdapter(contactPaths);
            recyclerView.setAdapter(myAdapter);
    }

    public void initStation(List<Station> tempList, int tag) {
        if (tag == 1) {
            tempList1 = tempList;
        } else {
            tempList2 = tempList;
        }
    }


    private String changeTime(String time) {
        // 2时45分 2：45：00
        // TODO: 2018/3/25   // 23分
        time = time.replace("h", ":");
        time = time.replace("m", "");
        time = time + ":00";
        return time;
    }

    private int compareTime(String l, String r) {
        Calendar ll = CalendarUtil.getCalendarByStr(l);
        Calendar rr = CalendarUtil.getCalendarByStr(r);
        return CalendarUtil.compareDate(ll, rr);
    }

    // 日期监听器类
    private class DateChooseListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            lockClick();
            Calendar now = CalendarUtil.getToday();

            // 初始化日期选择器
            DatePickerDialog datePicker = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                            startDate.set(year, monthOfYear, dayOfMonth);
                            changeShowDate();
                        }
                    },
                    startDate.get(Calendar.YEAR),
                    startDate.get(Calendar.MONTH),
                    startDate.get(Calendar.DAY_OF_MONTH)
            );
            // 最远买票日期
            Calendar lastDay = CalendarUtil.getLastDay();
            // 设置最小日期与最大日期
            datePicker.setMinDate(now);
            datePicker.setMaxDate(lastDay);
            // 关闭解锁
            datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    unlockClick();
                }
            });
            datePicker.show(getActivity().getFragmentManager(), "" + btn_tstart_date.getId());
        }
    }

    // change date
    private void changeShowDate() {
        SimpleDateFormat tempFromat = new SimpleDateFormat("MM月dd日 E ");
        // String dateStr = SimpleDateFormat.getDateInstance().format(startDate.getTime());
        String dateStr = tempFromat.format(startDate.getTime());
        if (dateStr.charAt(0) == '0') // 去掉第一个0
            btn_tstart_date.setText(dateStr.replaceFirst("0", ""));
        else
            btn_tstart_date.setText(dateStr);
    }

    public void getDataFromServer(String getPathUrl, String start_city, String end_city, String pathStartDate) {
        // 准备数据
        // 请求连接
        QueryInfo queryInfo = new QueryInfo(start_city, end_city, pathStartDate );

        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(queryInfo));


        subscription = RxHttpUtils.getDataPost(getPathUrl, requestBody, getContext())
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

                            ArrayList<TripResponse> tripResponseArrayList = new ArrayList<>();

                            JSONArray jsonArray = JSON.parseArray(responseResult);
                            Iterator it = jsonArray.iterator();
                            while (it.hasNext()) {
                                JSONObject tripObj = (JSONObject) it.next();
                                JSONObject tripIdObj = tripObj.getJSONObject("tripId");
                                String tripId = tripIdObj.getString("type") + tripIdObj.getString("number");

                                TripResponse tp = new TripResponse(tripId, tripObj.getString("trainTypeId"),
                                        tripObj.getString("startingStation"), tripObj.getString("terminalStation"),
                                        tripObj.getString("startingTime"), tripObj.getString("endTime"),
                                        tripObj.getString("economyClass"), tripObj.getString("confortClass"),
                                        tripObj.getString("priceForEconomyClass"), tripObj.getString("priceForConfortClass"));

                                tripResponseArrayList.add(tp);
                            }

                            // 转换数据
                            List<ContactPath> contactPathList = new ArrayList<>();
                            for (int i = 0; i < tripResponseArrayList.size(); i++) {
                                ContactPath cp = new ContactPath();
                                cp.setPathName(tripResponseArrayList.get(i).getTripId());
                                // todo 系统时间是2013年
                                cp.setPathDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                // 取最后时分秒
                                cp.setStartTime(CalendarUtil.getHMS(tripResponseArrayList.get(i).getStartingTime()));
                                cp.setStartStation(tripResponseArrayList.get(i).getStartingStation());

                                cp.setArriveTime(CalendarUtil.getHMS(tripResponseArrayList.get(i).getEndTime()));
                                cp.setArriveStation(tripResponseArrayList.get(i).getTerminalStation());

                                // 两个时间戳相减计算差值
                                cp.setTotalTime(CalendarUtil.getDistanceTime(CalendarUtil.getHMS(tripResponseArrayList.get(i).getStartingTime()), CalendarUtil.getHMS(tripResponseArrayList.get(i).getEndTime())));

                                // 10种座位，10种座位数
                                cp.setSeats(new int[]{0, -1, -1, -1, -1, -1, Integer.parseInt(tripResponseArrayList.get(i).getEconomyClass()), Integer.parseInt(tripResponseArrayList.get(i).getConfortClass()), -1, -1});
                                cp.setPrices(new double[]{0, 0, 0, 0, 0, 0, Double.parseDouble(tripResponseArrayList.get(i).getPriceForEconomyClass()), Double.parseDouble(tripResponseArrayList.get(i).getPriceForConfortClass()), 0, 0});
                                contactPathList.add(cp);
                            }

                            try {
                                showContactPath(contactPathList);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            // showState(false);
                            //  Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                        }
//                        animationDrawable.stop();
//                        animationIV.setVisibility(View.GONE);
                    }
                });
    }


    private void showState(boolean tag) {
        if (tag) {
            reserve_tips.setVisibility(View.GONE);
            reserve_tips_img.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            animationDrawable.stop();
            animationIV.setVisibility(View.GONE);
        } else {
            reserve_tips_img.setVisibility(View.VISIBLE);
            reserve_tips.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            animationDrawable.stop();
            animationIV.setVisibility(View.GONE);
            reserve_tips.setText("no that trip line！");
        }
    }

    public String getNow() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        return dateFormat.format(now);
    }

    private void showContactPath(List<ContactPath> contactPaths) throws ParseException {
        tagNum++;
        if (tagNum == 1)
            travel1List.addAll(contactPaths);
        if (tagNum == 2)
            travel2List.addAll(contactPaths);
        if (tagNum == 2) {
            allPathList.addAll(travel1List);
            allPathList.addAll(travel2List);
            cppath = new ContactPathPageResponse(true,"Success",allPathList);

            // 根据首页条件过滤数据
            allPathList = filterPathList(allPathList);
            tagDay = false;
            if (allPathList.size() == 0) {
                showState(false);
            } else {
                showState(true);
            }
            contactPathTemp = allPathList;
            ContactPathAdapter myAdapter = new ContactPathAdapter(allPathList);
            recyclerView.setAdapter(myAdapter);
        }
    }

    public List<ContactPath> filterPathList(List<ContactPath> contactPaths) throws ParseException {
        List<ContactPath> tempList = new ArrayList<>();
        // 这里判断传过来的过滤条件
        String g = "g";
        String d = "d";
        String z = "z";
        String t = "t";
        String k = "k";
        if (car_typegd.equals("GD")) {
            g = "G";
            d = "D";
        }
        if (car_typez.equals("Z"))
            z = "Z";
        if (car_typet.equals("T"))
            t = "T";
        if (car_typek.equals("K"))
            k = "K";
        if (g == "g" && d == "d" && z == "z" && t == "t" && k == "k") {
            // 如果没有选择过滤车辆，默认全部
            g = "G";
            d = "D";
            z = "Z";
            t = "T";
            k = "K";
        }
        String pathName = "";
        for (int i = 0; i < contactPaths.size(); i++) {
            pathName = contactPaths.get(i).getPathName();
            if (pathName.contains(g) || pathName.contains(d) || pathName.contains(z) || pathName.contains(t) || pathName.contains(k)) {
                if (isEffectiveDate(contactPaths.get(i).getStartTime(), start_time + ":00", arrive_time + ":00"))
                    tempList.add(contactPaths.get(i));
            }
        }
        return tempList;
        // start_time = getArguments().getString("start_time");
        //  arrive_time = getArguments().getString("arrive_time");
        //  seat_type = getArguments().getString("seat_type");
    }

    public static boolean isEffectiveDate(String nowTime1, String startTime1, String endTime1) throws ParseException {
        String format = "HH:mm:ss";
        Date nowTime = new SimpleDateFormat(format).parse(nowTime1);
        Date startTime = new SimpleDateFormat(format).parse(startTime1);
        Date endTime = new SimpleDateFormat(format).parse(endTime1);
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public List<ContactPath> filterTimeUnUsered(List<ContactPath> contactPaths) {
        List<ContactPath> tempContacts = new ArrayList<>();
        for (int i = 0; i < contactPaths.size(); i++) {
            if (timeCompare(contactPaths.get(i).getStartTime()) < 0) {
                tempContacts.add(contactPaths.get(i));
            }
        }
        return tempContacts;
    }

    public static int timeCompare(String startTime) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateNow = formatter.format(currentTime);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(formatter.parse(dateNow));
            c2.setTime(formatter.parse(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = c1.compareTo(c2);
        return result;
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshContactPath.setRefreshing(false);
        unlockClick();
    }

    // 每个item 对应一个adapter , 一个adapter 对应一个viewholder
    class ContactPathAdapter extends RecyclerView.Adapter<ContactPathAdapter.ContactPathViewHolder> {

        private List<ContactPath> list;

        public ContactPathAdapter(List<ContactPath> list) {
            this.list = list;
        }

        @Override
        public ContactPathViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contactpath, viewGroup, false);
            return new ContactPathViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactPathViewHolder cpholder, int i) {
            cpholder.setIsRecyclable(false);
            cpholder.pathName.setText(list.get(i).getPathName());

            cpholder.startStation.setText(list.get(i).getStartStation());
            cpholder.startTime.setText(list.get(i).getStartTime().substring(0, 5));
            cpholder.durationTime.setText(list.get(i).getTotalTime());

            cpholder.endStation.setText(list.get(i).getArriveStation());
            cpholder.endTime.setText(list.get(i).getArriveTime().substring(0, 5));

            String[] seatMap = arrangeSeatsNum(list.get(i).getSeats());

            String[] seatIndex = new String[10];
            String[] seatValues = new String[10];

            for (int jj = 0; jj < seatMap.length; jj++) {
                String[] array = seatMap[jj].split("=");
                seatIndex[jj] = array[0];
                seatValues[jj] = array[1];
            }

            String showPrice = String.valueOf((int) (list.get(i).getPrices()[Integer.parseInt(seatIndex[0])]));
            cpholder.first_money_ct.setText("¥" + showPrice);

            for (int k = 0; k < 4; k++) {
                if (Integer.parseInt(seatValues[k]) < 100) {
                    cpholder.seatValue[k].setTextColor(Color.RED);
                }
                if (!seatValues[k].equals("-1")) {
                    cpholder.seatType[k].setText(Ticket.EASY_SEAT_TYPES[Integer.parseInt(seatIndex[k])] + ":");
                    cpholder.seatValue[k].setText(seatValues[k] + "p");
                }
            }

            final int position = i;
            cpholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击每一个路线进行跳转
                    Intent intent = new Intent(getActivity(), BuyTicketActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("item_contact_path", gson.toJson(list.get(position)));
                    // Toast.makeText(getActivity(), gson.toJson(list.get(position)), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        // 对应item 里面的每一个元素
        class ContactPathViewHolder extends RecyclerView.ViewHolder {

            private TextView pathName;

            private TextView startStation;
            private TextView startTime;

            private TextView durationTime;

            private TextView endStation;
            private TextView endTime;

            private TextView first_money_ct;

            private TextView[] seatType = new TextView[4];
            private TextView[] seatValue = new TextView[4];

            public ContactPathViewHolder(View itemView) {
                super(itemView);
                pathName = (TextView) itemView.findViewById(R.id.pathname_ct);

                startStation = (TextView) itemView.findViewById(R.id.startStaiton_ct);
                startTime = (TextView) itemView.findViewById(R.id.startTime_ct);

                durationTime = (TextView) itemView.findViewById(R.id.lastTime_ct);

                endStation = (TextView) itemView.findViewById(R.id.endStation_ct);
                endTime = (TextView) itemView.findViewById(R.id.endTime_ct);

                first_money_ct = (TextView) itemView.findViewById(R.id.first_money_ct);
                int seatTypes[] = new int[]{R.id.first_seattype_ct, R.id.second_seattype_ct, R.id.third_seattype_ct, R.id.four_seattype_ct};
                int seatValues[] = new int[]{R.id.first_seatvalue_ct, R.id.second_seatvalue_ct, R.id.third_seatvalue_ct, R.id.four_seatvalue_ct};
                for (int i = 0; i < 4; i++) {
                    seatType[i] = (TextView) itemView.findViewById(seatTypes[i]);
                    seatValue[i] = (TextView) itemView.findViewById(seatValues[i]);
                }
            }
        }
    }

    // 监听返回回来的值的变化---从而改变界面上的值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (TravelPathActivity.PATH_CHOOSE_RESULT == resultCode && TravelPathActivity.FILTE_RPATH_REQUEST_CODE == requestCode) {
            // 选择的城市返回了
            unlockClick();
            if (null == data) {
                return;
            }
            // // TODO: 2018/3/30
            //[{"cityName":"合肥 ","stationDesc":"普通列车停靠","stationName":"合肥北城"},{"cityName":"合肥 ","stationDesc":"高铁,动车停靠","stationName":"合肥南"}]=======
            // [{"cityName":"上海 ","stationDesc":"普通列车停靠","stationName":"上海南"},{"cityName":"上海 ","stationDesc":"高铁,动车停靠","stationName":"上海虹桥"}]


            String result1 = "";
            Gson gson = new Gson();
            ArrayList<Station> list1 = new ArrayList<Station>();
            ArrayList<Station> list2 = new ArrayList<Station>();
            Type listType = new TypeToken<List<Station>>() {
            }.getType();
            list1 = gson.fromJson(data.getStringExtra("startCity"), listType);
            list2 = gson.fromJson(data.getStringExtra("arriveCity"), listType);



            List<ContactPath> item_contactPaths = new ArrayList<>();
            for (int i = 0; i < contactPathTemp.size(); i++) {
                boolean startTag = false, endTag = false;
                for (int s = 0; s < list1.size(); s++) {
                    if (contactPathTemp.get(i).getStartStation().equals(list1.get(s).getStationName())) {
                        startTag = true;
                    }
                }

                for (int e = 0; e < list2.size(); e++) {
                    if (contactPathTemp.get(i).getArriveStation().equals(list2.get(e).getStationName())) {
                        endTag = true;
                    }
                }

                if (list1.size() == 0 && list2.size() == 0) {
                    item_contactPaths.add(contactPathTemp.get(i));
                }
                if (endTag && startTag) {
                    item_contactPaths.add(contactPathTemp.get(i));
                }
            }
            if (item_contactPaths.size() != 0)
                showState(true);
            try {
                showContactPath(item_contactPaths);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private String[] arrangeSeatsNum(int seats[]) {
        Map<String, String> seatNum = new HashMap<>();
        for (int i = 0; i < seats.length; i++) {
            seatNum.put(i + "", seats[i] + "");
        }
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(seatNum.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return (rhs.getValue().compareTo(lhs.getValue()));
            }
        });
        String[] newSeats = new String[10];
        for (int i = 0; i < infoIds.size(); i++) {
            String id = infoIds.get(i).toString();
            newSeats[i] = id;
        }
        return newSeats;
    }

}
