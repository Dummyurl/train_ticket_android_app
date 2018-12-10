package ts.trainticket.fragement;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.LoginActivity;
import ts.trainticket.Meituan.MeiTuanListView;
import ts.trainticket.R;
import ts.trainticket.TicketReserveActivity;
import ts.trainticket.databean.ContactPath;
import ts.trainticket.databean.PathStation;
import ts.trainticket.databean.Ticket;
import ts.trainticket.databean.TicketPageResponse;
import ts.trainticket.databean.TimeTable;
import ts.trainticket.databean.TimeTableResponse;
import ts.trainticket.domain.GetRoutesListlResult;
import ts.trainticket.domain.PathTimeTable;
import ts.trainticket.domain.QueryInfo;
import ts.trainticket.domain.Route;
import ts.trainticket.domain.Station;
import ts.trainticket.domain.TravelAdvanceResult;
import ts.trainticket.domain.TravelAdvanceResultUnit;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;
import ts.trainticket.utils.CalendarUtil;

/**
 * Created by liuZOZO on 2018/1/21.
 * 车次详情
 */
public class PathDetailFragment extends BaseFragment implements MeiTuanListView.OnMeiTuanRefreshListener {

    // 日期选择按钮
    private Button btnDate = null;
    private Calendar startDate = null;

    private MeiTuanListView mListView;
    private MeituanAdapter mAdapter;
    private final static int REFRESH_COMPLETE = 0;
    List<TicketRes_Item> mDatas;
    private ListView timeTableListView2 = null;

    private LinearLayout time_table_detail = null;
    private List<TimeTable> timeTableList = null;

    private ImageView animationIV;
    private AnimationDrawable animationDrawable;

    /**
     * mInterHandler是一个私有静态内部类继承自Handler，内部持有MainActivity的弱引用，
     * 避免内存泄露
     */
    private InterHandler mInterHandler = new InterHandler(this);

    private View inflate;
    private TextView test;
    private Dialog dialog = null;

    TextView by_pathName = null;
    TextView timeTable_pathName = null;

    TextView startStation = null;
    TextView startTime = null;

    TextView endStation = null;
    TextView endTime = null;
    ContactPath path = null;


    // 查询所有的停靠站和距离
    int callTimeTag = 0;
    GetRoutesListlResult queryRoutes = null;
    List<Station> queryStationList = null;
    List<PathTimeTable> pathTimeTableList = null;
    TravelAdvanceResult travelAdvanceResult = new TravelAdvanceResult();

    private static class InterHandler extends Handler {
        private WeakReference<PathDetailFragment> mActivity;

        public InterHandler(PathDetailFragment activity) {
            mActivity = new WeakReference<PathDetailFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PathDetailFragment activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        activity.mListView.setOnRefreshComplete();
                        activity.mAdapter.notifyDataSetChanged();
                        activity.mListView.setSelection(0);
                        break;
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_buy_ticket, container, false);
        initView(view);
        initData(getArguments().getString("pathInfo"));
        initRefresh(view);
        return view;
    }

    private void initView(View view) {
        by_pathName = (TextView) view.findViewById(R.id.by_pathname);

        btnDate = (Button) view.findViewById(R.id.btn_byticket_date);
        btnDate.setOnClickListener(new DateChooseListener());
        addToBtnController(btnDate);
        // 初始化界面显示的出发日期
        if (null == startDate) {
            startDate = Calendar.getInstance();
        }
        changeShowDate();

        time_table_detail = (LinearLayout) view.findViewById(R.id.time_table_detail);
        time_table_detail.setOnClickListener(new ShowTimeTableDetail());

        startStation = (TextView) view.findViewById(R.id.by_startStation);
        startTime = (TextView) view.findViewById(R.id.by_startTime);

        endStation = (TextView) view.findViewById(R.id.by_arriveStation);
        endTime = (TextView) view.findViewById(R.id.by_endTime);
    }


    private void initData(String gsonStr) {
        Gson gson = new Gson();
        path = gson.fromJson(gsonStr, ContactPath.class);

        btnDate.setText(path.getPathDate());
        by_pathName.setText(path.getPathName());
        startStation.setText(path.getStartStation());
        startTime.setText(path.getStartTime().substring(0, 5));
        endStation.setText(path.getArriveStation());
        endTime.setText(path.getArriveTime().substring(0, 5));
        List<TicketRes_Item> tempDatas = new ArrayList<>();
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tempDatas.add(new TicketRes_Item(Ticket.EASY_SEAT_TYPES[i], path.getPrices()[i], path.getSeats()[i]));
        }
        Collections.sort(tempDatas, new SortByTicketsNum());
        for (int i = 0; i < tempDatas.size(); i++) {
            if (tempDatas.get(i).getLeftTickets() > 0) {
                mDatas.add(tempDatas.get(i));
            }
        }
    }

    private void initRefresh(View view) {
        mListView = (MeiTuanListView) view.findViewById(R.id.listview);
        showItemTicket(mDatas);
    }

    private void showItemTicket(List<TicketRes_Item> mDatas1) {
        mAdapter = new MeituanAdapter(mDatas1);
        mListView.setAdapter(mAdapter);
        mListView.setOnMeiTuanRefreshListener(this);
    }

    // time table
    class ShowTimeTableDetail implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            initDialogTimeTable();
            //展示数据queryAllRoute
            String[] url = new String[]{UrlProperties.clientIstioIp + UrlProperties.queryAllRoute,
                    UrlProperties.clientIstioIp + UrlProperties.getStopAtStation};
            for (int i = 0; i < url.length; i++)
                getStopStation(url[i], i);

            //  getTableLineFromServre(by_pathName.getText().toString());
            timeTable_pathName.setText(by_pathName.getText().toString());
        }
    }

    public void getStopStation(String url, final int callTag) {
        System.out.println(callTag + "======34"+ url);
        // 查询G123 经过那些站
        if (callTag == 1) {
            QueryInfo queryInfo = new QueryInfo(startStation.getText().toString(),endStation.getText().toString(), btnDate.getText().toString());

            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(queryInfo));


            String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
            String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");
            System.out.println("094343===" + new Gson().toJson(queryInfo));
            subscription = RxHttpUtils.postWithHeader(url, loginId, token, requestBody, getContext())
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
                            System.out.println(responseResult + "=32=930430");
                            if (responseResult != null && !responseResult.equals("")) {
                                Gson gson = new Gson();
                                System.out.println(responseResult + "=32220");
                                travelAdvanceResult = gson.fromJson(responseResult, TravelAdvanceResult.class);
                                System.out.println(responseResult + "=3222220");
                                showTimeTable();
                            } else {
                                Toast.makeText(getActivity(), "request data error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            subscription = RxHttpUtils.getDataByUrl(url, getContext())
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
                            System.out.println(responseResult + "==930430");
                            if (responseResult != null && !responseResult.equals("")) {
                                Gson gson = new Gson();
                                if (callTag == 0) {
                                    queryRoutes = gson.fromJson(responseResult, GetRoutesListlResult.class);
                                    showTimeTable();
                                } else {
                                    queryStationList = new ArrayList<Station>();
                                    Type listType = new TypeToken<List<Station>>() {
                                    }.getType();
                                    queryStationList = gson.fromJson(responseResult, listType);
                                    System.out.println(queryStationList.size() + "====-size");
                                    showTimeTable();
                                }
                            } else {
                                Toast.makeText(getActivity(), "request data error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    //展示详细信息
    public void showTimeTable() {
        callTimeTag++;
        System.out.println(callTimeTag + "-900932");
        if (callTimeTag != 0 && callTimeTag%2 == 0) {
            // queryRoutes
            // queryStationList
            String sStation = startStation.getText().toString().toLowerCase().replaceAll(" ", "");
            String eStation = endStation.getText().toString().toLowerCase().replaceAll(" ", "");

            System.out.println(sStation + "---23-" + eStation);

            pathTimeTableList = new ArrayList<>();

            List<TravelAdvanceResultUnit> tau = travelAdvanceResult.getTravelAdvanceResultUnits();
            for (int i = 0; i < tau.size(); i++) {
                System.out.println(timeTable_pathName.getText().toString() + "---09" + tau.get(i).getTripId());

                if (timeTable_pathName.getText().toString().equals(tau.get(i).getTripId())) { // 找到对应的路线G236

                    ArrayList<String> stopStations = tau.get(i).getStopStations();
                    System.out.println(stopStations.size() + "---=-==0");
                    int stopAtStationsNum = stopStations.size();

                    ArrayList<Route> routesList = queryRoutes.getRoutes(); // 查找经过的车站的距离
                    ArrayList<Integer> distances = new ArrayList<>();
                    for (int k = 0; k < routesList.size(); k++) {
                        if (routesList.get(k).getStations().size() == stopAtStationsNum
                                && routesList.get(k).getStartStationId().equals(sStation.replaceAll(" ", "").toLowerCase())
                                && routesList.get(k).getTerminalStationId().equals(eStation.replaceAll(" ", "").toLowerCase())) {
                            distances = routesList.get(k).getDistances();
                        }
                    }
                    System.out.println("distance--" + distances.size());

                    List<Object> stationStayTime = ApplicationPreferences.getStayTimeStation(getContext());
                    System.out.println(stationStayTime.size() + "===-032-2-303-032-30-32");
                    Map<String, String> stationStayTimes = new HashMap<>();
                    for (int u = 0; u < stationStayTime.size(); u++) {
                        String stationTime = stationStayTime.get(u).toString();
                        if (!stationStayTimes.containsKey(stationTime.split("_")[0])) {
                            stationStayTimes.put(stationTime.split("_")[0], stationTime.split("_")[1]);
                        }
                    }

                    System.out.println("-----4343----");
                    for (int j = 0; j < stopAtStationsNum; j++) {
                        System.out.println("-00-934---");
                        PathTimeTable tempPathTimeTable = new PathTimeTable();
                        tempPathTimeTable.setStationName(stopStations.get(j));  // station
                        System.out.println(stopStations.get(j) + "--==0---" + distances.get(j));
                        tempPathTimeTable.setDistance(distances.get(j) + "");
                        tempPathTimeTable.setStayTime(stationStayTimes.get(stopStations.get(j).replaceAll(" ", "").toLowerCase()));
                        pathTimeTableList.add(tempPathTimeTable);
                    }
                }
            }
            System.out.println(pathTimeTableList.size() + "---909");
            if (pathTimeTableList.size() > 0) {
                animationDrawable.stop();
                animationIV.setVisibility(View.GONE);

                TimeTableAdapter ttb = new TimeTableAdapter(pathTimeTableList);
                timeTableListView2.setAdapter(ttb);
            } else {
                animationDrawable.stop();
            }
            callTimeTag = 0;
        }
    }


    public void getOneTicketFromServre(String pathName, String startTime, String startNum, String endNum) {
        // 准备数据
        try {
            pathName = URLEncoder.encode(pathName, "UTF-8");
            startTime = URLEncoder.encode(startTime, "UTF-8");
            startNum = URLEncoder.encode(startNum, "UTF-8");
            endNum = URLEncoder.encode(endNum, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 请求连接
        String listStationUri = UrlProperties.getOnePathTicket + "/" + pathName + "/" + startTime + "/" + startNum + "/" + endNum;
        subscription = RxHttpUtils.getDataByUrl(listStationUri, getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        unlockClick();
                        // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String responseResult) {
                        unlockClick();
                        if (responseResult != null && !responseResult.equals("")) {
                            Gson gson = new Gson();
                            TicketPageResponse oneTicket = gson.fromJson(responseResult, TicketPageResponse.class);
                            if (oneTicket != null) {
                                System.out.println(oneTicket.getMsg() + "-09-0-0-" + oneTicket.getTicketList().size());
                                List<Ticket> tempTicket = oneTicket.getTicketList();

                                List<TicketRes_Item> tempItem = new ArrayList<TicketRes_Item>();

                                for (int k = 0; k < mDatas.size(); k++) {
                                    for (int kl = 0; kl < tempTicket.size(); kl++) {
                                        System.out.println(mDatas.get(k).getSeatType() + "0-0-" + Ticket.EASY_SEAT_TYPES[tempTicket.get(kl).getSeatType()]);

                                        if (mDatas.get(k).getSeatType().equals(Ticket.EASY_SEAT_TYPES[tempTicket.get(kl).getSeatType()])) {
                                            mDatas.get(k).setLeftTickets(tempTicket.get(kl).getLeftTickets());
                                        }
                                    }
                                }
                                showItemTicket(mDatas);
                            } else {
                                System.out.println("-=============");
                            }
                        } else {
                            Toast.makeText(getActivity(), "request data error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initDialogTimeTable() {
        dialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        inflate = LayoutInflater.from(getContext()).inflate(R.layout.time_table_detail, null);

        animationIV = (ImageView) inflate.findViewById(R.id.animationIV_ttd);
        animationIV.setImageResource(R.drawable.pic_loading);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();

        timeTableListView2 = (ListView) inflate.findViewById(R.id.timeTableList2);
        timeTable_pathName = (TextView) inflate.findViewById(R.id.timeTable_pathName);

        // 初始化控件
        dialog.setContentView(inflate);
        // 获取当前Activity所在窗口
        Window dialogWindow = dialog.getWindow();
        // 设置dialog 从窗底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    class TimeTableAdapter extends BaseAdapter {
        private List<PathTimeTable> list;

        public TimeTableAdapter(List<PathTimeTable> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            TimeTableViewHolder ttVH = null;
            if (view == null) {
                ttVH = new TimeTableViewHolder();
                inflate = LayoutInflater.from(getContext()).inflate(R.layout.time_table_detail, null);
                view = LayoutInflater.from(inflate.getContext()).inflate(R.layout.item_time_table, null);
                if (view != null) {
                    ttVH.pathstationNum = (TextView) view.findViewById(R.id.timeTable_num);
                    ttVH.stationName = (TextView) view.findViewById(R.id.timeTable_station);
                    ttVH.arriveTime = (TextView) view.findViewById(R.id.timeTable_arriveTime);
//                    ttVH.startTime = (TextView) view.findViewById(R.id.timeTable_startTime);
                    ttVH.totalTime = (TextView) view.findViewById(R.id.timeTable_stopTime);
                    view.setTag(ttVH);
                }
            } else {
                ttVH = (TimeTableViewHolder) view.getTag();
            }
            ttVH.pathstationNum.setText(position + "");
            ttVH.stationName.setText(list.get(position).getStationName());
            ttVH.arriveTime.setText(list.get(position).getDistance());
            ttVH.totalTime.setText(list.get(position).getStayTime());


            //    ttVH.totalTime.setText(list.get(position).getTotalTime());
            if (position == 0 || position == list.size() - 1) {
                ttVH.pathstationNum.setTextColor(Color.parseColor("#EE7621"));
                ttVH.stationName.setTextColor(Color.parseColor("#EE7621"));
                ttVH.arriveTime.setTextColor(Color.parseColor("#EE7621"));
                ttVH.totalTime.setTextColor(Color.parseColor("#EE7621"));
            } else {
                ttVH.pathstationNum.setTextColor(Color.parseColor("#000000"));
                ttVH.stationName.setTextColor(Color.parseColor("#000000"));
                ttVH.arriveTime.setTextColor(Color.parseColor("#000000"));
                ttVH.totalTime.setTextColor(Color.parseColor("#000000"));
            }
            return view;
        }

        class TimeTableViewHolder {
            private TextView pathstationNum;
            private TextView stationName;
            private TextView arriveTime;
            private TextView totalTime;
        }
    }

    class MeituanAdapter extends BaseAdapter {

        private List<TicketRes_Item> list;

        public MeituanAdapter(List<TicketRes_Item> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            MeituanViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new MeituanViewHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_seat_type_price, null);
                viewHolder.seatType = (TextView) view.findViewById(R.id.res_ticketType);
                viewHolder.seatPrice = (TextView) view.findViewById(R.id.res_tickePrice);
                viewHolder.leftTickets = (TextView) view.findViewById(R.id.res_leftticket);
                viewHolder.reserveBtn = (Button) view.findViewById(R.id.res_btn);
                view.setTag(viewHolder);
            } else {
                viewHolder = (MeituanViewHolder) view.getTag();
            }
            viewHolder.seatType.setText(list.get(position).getSeatType());
            viewHolder.seatPrice.setText("¥" + (int) (list.get(position).getSeatPrice()));
            viewHolder.leftTickets.setText(list.get(position).getLeftTickets() + "p");
            viewHolder.reserveBtn.setText("reserve");
            addToBtnController(viewHolder.reserveBtn);

            viewHolder.reserveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isLogin = ApplicationPreferences.isUserOnLine(getContext());
                    System.out.println(isLogin + "----=======900000000");
                    Intent intent = null;
                    if (isLogin) {
                        intent = new Intent(getActivity(), TicketReserveActivity.class);
                        Gson gson = new Gson();
                        intent.putExtra("ticket_detail", gson.toJson(path));
                        intent.putExtra("seatType", list.get(position).getSeatType());
                        intent.putExtra("seatPrice", "¥" + (int) (list.get(position).getSeatPrice()));
                    } else {
                        Toast.makeText(getContext(), "您没有登录，请先登录", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), LoginActivity.class);
                    }
                    startActivity(intent);
                }
            });
            return view;
        }

        class MeituanViewHolder {
            private TextView seatType;
            private TextView seatPrice;
            private TextView leftTickets;
            private Button reserveBtn;
        }
    }

    class TicketRes_Item {
        String seatType;
        double seatPrice;
        int leftTickets;

        public String getSeatType() {
            return seatType;
        }

        public void setSeatType(String seatType) {
            this.seatType = seatType;
        }

        public double getSeatPrice() {
            return seatPrice;
        }

        public void setSeatPrice(int seatPrice) {
            this.seatPrice = seatPrice;
        }

        public int getLeftTickets() {
            return leftTickets;
        }

        public void setLeftTickets(int leftTickets) {
            this.leftTickets = leftTickets;
        }

        public TicketRes_Item(String seatType, double seatPrice, int leftTickets) {
            this.seatType = seatType;
            this.seatPrice = seatPrice;
            this.leftTickets = leftTickets;
        }
    }

    class SortByTicketsNum implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            TicketRes_Item s1 = (TicketRes_Item) lhs;
            TicketRes_Item s2 = (TicketRes_Item) rhs;
            return s2.getLeftTickets() - s1.getLeftTickets();
        }
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    System.out.println(path.getPathName() + "--" + path.getPathDate() + "--" + path.getStartNumber() + "--" + path.getArriveNumber());
                    getOneTicketFromServre(path.getPathName(), path.getPathDate(), path.getStartNumber() + "", path.getArriveNumber() + "");
                    mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


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
            datePicker.show(getActivity().getFragmentManager(), "" + btnDate.getId());
        }
    }

    // change date
    private void changeShowDate() {
        SimpleDateFormat tempFromat = new SimpleDateFormat("yyyy年MM月dd日");
        // String dateStr = SimpleDateFormat.getDateInstance().format(startDate.getTime());
        String dateStr = tempFromat.format(startDate.getTime());
        if (dateStr.charAt(0) == '0') // 去掉第一个0
            btnDate.setText(dateStr.replaceFirst("0", ""));
        else
            btnDate.setText(dateStr);
    }
}
