package ts.trainticket.fragement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.MainActivity;
import ts.trainticket.Meituan.RightMarkView;
import ts.trainticket.R;
import ts.trainticket.bean.CustomDialog;
import ts.trainticket.databean.COrders;
import ts.trainticket.databean.COrdersPageResponse;
import ts.trainticket.databean.Contacts;
import ts.trainticket.domain.CancelOrderInfo;
import ts.trainticket.domain.CancelOrderResult;
import ts.trainticket.domain.Order;
import ts.trainticket.domain.OrderList;
import ts.trainticket.domain.OrderTicketsResult;
import ts.trainticket.domain.PaymentInfo;
import ts.trainticket.domain.PreserveOrderResult;
import ts.trainticket.httpUtils.ResponseResult;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;
import ts.trainticket.utils.CalendarUtil;
import ts.trainticket.utils.ServerConstValues;

/**
 * Created by liuZOZO on 2018/1/27.
 * 订单详情页
 */
public class OrderDetail_Fragement extends BaseFragment implements View.OnClickListener {

    //    一组状态
    private ImageView show_tag_picImg = null;
    private TextView show_top_tipsTv = null;
    private TextView show_tips2Tv = null;
    private LinearLayout count_time_layout = null;
    private RightMarkView markView = null;
    private TextView cancel_order;
    private Button pay_btn = null;
    private TextView add_accountTv = null;

    private LinearLayout order_detailLayout = null;

    // 待支付0  状态1（待出发）  2（已取消 超时）  3（退改签） 4 已完成
    private String[] tipText = new String[]{"Not Paid", "Paid & Not Collected", "Collected", "Cancel & Rebook", "Cancel", "Refunded", "Used", "Other"};

    private int[] imagesTip = new int[]{
            R.drawable.hb_youji_icon,
            R.drawable.tab_ticket_selected_new,
            R.drawable.cb_checked,
            R.drawable.hb_yiquxiao_icon,
            R.drawable.delete_bg,
            R.drawable.delete_bg,
            R.drawable.cb_checked,
            R.drawable.hb_yiquxiao_icon,
    };
    // 待支付0 -> 取消订单   状态1（待出发） ->  退票
    private String[] cancelBtnTips = new String[]{"Cancel", "Refound", "waiting", "To refund", "Cancelled ,to re-book", "Refund, to re-book", "To re-book", "To re-book"};
    private String[] tipBottomBtnText = new String[]{"Pay", "Paid", "Paid", "Paid", "Canceled", "Refounded", "Completed", "unkonwn"};
    public static final double[] PASSENGER_TYPES_RATIOS = {1, 0.7, 0.5, 0.5};

    private TextView countTime = null;
    private TextView beginDate = null;
    private TextView endDate = null;

    private TextView coachNumber = null;

    private TextView beginPlace = null;
    private TextView pathName = null;
    private TextView duraTime = null;

    private TextView seatNumber = null;

    private TextView endPlace = null;

    private ListView selected_psg_list = null;
    List<Contacts> contacts = null;
    private TextView total_Money;

    private String seatTypeValue;
    private double seatPrice = 0;

    LinearLayout show_toPayPanel = null;

    CustomDialog dialog = null;

    String reserve_result = "";
    String orders_Result = "";

    String presentState = "";
    String takeTime = "";
    String takeDate = "";

    // 预定后返回的订单信息
    PreserveOrderResult preOrderResult = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_orderdetail, container, false);
        initCircleView(view);
        initView(view);
        showData();
        return view;
    }

    public void showData() {

        reserve_result = getArguments().getString("reserve_result"); //  新预定的
        orders_Result = getArguments().getString("ordersResult");  // 从老页面传过来的
        int temp_tag = 0;

        System.out.println(reserve_result + "-93028" + orders_Result);
        Gson gson = new Gson();
        if (reserve_result != null && reserve_result != "") {
            JSONObject orderPreObj = JSON.parseObject(reserve_result);
            if ("Success".equals(orderPreObj.getString("message"))) {
                // 把订单信息的界面显示出来
                JSONObject orderObj = orderPreObj.getJSONObject("order");
                preOrderResult = gson.fromJson(orderObj.toString(), PreserveOrderResult.class);
                showOrderResult(preOrderResult);
            }
        } else if (orders_Result != null && orders_Result != "") {

            dialog = new CustomDialog(getContext(), R.style.CustomDialog);
            dialog.show();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 500);

            Gson gson2 = new Gson();
            preOrderResult = gson2.fromJson(orders_Result, PreserveOrderResult.class);
            System.out.println(preOrderResult.toString() + "-09089897");
            showOrderResult(preOrderResult);
        }
        // dialog.dismiss();
    }

    public void showOrderResult(PreserveOrderResult preserveOrderResult) {
        System.out.println(preOrderResult + "---00000233");
        order_detailLayout.setVisibility(View.VISIBLE);

        String tempidcard = "";
        char[] idcards = preserveOrderResult.getAccountId().toCharArray();
        for (int j = 0; j < idcards.length; j++) {
            if (j > 12 && j < 23)
                tempidcard = tempidcard + "";
            else
                tempidcard = tempidcard + idcards[j] + "";
        }
        add_accountTv.setText(tempidcard);

        beginDate.setText(CalendarUtil.dateToWeek(CalendarUtil.getDate(preOrderResult.getTravelDate())));
        // 放出发时间
        endDate.setText(CalendarUtil.getHM(preOrderResult.getTravelTime()));
        pathName.setText(preOrderResult.getTrainNumber() + "");

        // beginTime.setText(orderObj.getString("from"));
        beginPlace.setText(preOrderResult.getFrom());
        coachNumber.setText("coachNum:" + preOrderResult.getCoachNumber());
        // endTime.setText(orderObj.getString("to"));

        endPlace.setText(preOrderResult.getTo());
        seatNumber.setText("sNum:" + preOrderResult.getSeatNumber());

        // 下面俩用来控制按钮状态
        System.out.println(preOrderResult.getStatus() + "==0-9088080");
        presentState = preOrderResult.getStatus() + "";
        takeTime = CalendarUtil.getHMS(preOrderResult.getTravelTime());
        // 显示状态
        showState(preOrderResult.getStatus(), takeTime);
        setLeftTime(CalendarUtil.getDate(preOrderResult.getBoughtDate()));  // 倒计时

        seatTypeValue = preOrderResult.getSeatClass() + "";
        contacts = new ArrayList<>();
        // 显示passenger
        Contacts contact = new Contacts(preOrderResult.getContactsName(), preOrderResult.getContactsName(), preOrderResult.getContactsDocumentNumber(),
                "1", 1, preOrderResult.getId(), preOrderResult.getPrice());
        contacts.add(contact);
        // 计算总金额
        total_Money.setText("¥" + preOrderResult.getPrice());
        showPesger(contacts);
    }

    // 状态不为0 的时候,调用该方法,显示状态
    private void showState(int status, String takeTime) {
        show_tag_picImg.setBackgroundResource(imagesTip[status]);
        show_top_tipsTv.setText(tipText[status]);

        if (status != 0) {
            // 非待支付状态  画图消失,倒计时消失
            count_time_layout.setVisibility(View.GONE);
            markView.setVisibility(View.GONE);
            // 倒计时处显示提示, 画图处显示提示图片
            show_tips2Tv.setVisibility(View.VISIBLE);
            show_tag_picImg.setVisibility(View.VISIBLE);
        }
        System.out.println(takeTime + "=------===" + takeDate);
        if (CalendarUtil.compareTimeDate(takeTime) && CalendarUtil.compare_date(takeDate) && status == 1)
            cancel_order.setText("已发车");
        else
            cancel_order.setText(cancelBtnTips[status]);

        // 支付按钮的状态
        pay_btn.setText(tipBottomBtnText[status]);
    }

    private void initCircleView(View view) {
        markView = (RightMarkView) view.findViewById(R.id.activity_right_mark_rmv);
        // 设置开始和结束两种颜色
        markView.setColor(Color.parseColor("#FF4081"), Color.YELLOW);
        // 设置画笔粗细
        markView.setStrokeWidth(8f);
        markView.start();
    }

    private void setLeftTime(final String buyTimeStr) {
        Calendar now = Calendar.getInstance();
        Calendar buyTime = CalendarUtil.getCalendarByDateTime(buyTimeStr);
        buyTime.add(Calendar.MINUTE, 30);
        final long leftTime = (buyTime.getTimeInMillis() - now.getTimeInMillis()) / 1000;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        long showLeft = leftTime - aLong;
                        if (showLeft > 0) {
                            String showLeftTime = "";
                            if (showLeft % 60 < 10)
                                showLeftTime = (showLeft / 60) + ":0" + (showLeft % 60);
                            else
                                showLeftTime = (showLeft / 60) + ":" + (showLeft % 60);
                            countTime.setText(showLeftTime);
                        } else {
                            //  pageStateController.changeToWait();
                            //   getIndentDetailFromServer();
                            this.unsubscribe();
                        }
                    }
                });
    }

    private void initView(View view) {
        show_tag_picImg = (ImageView) view.findViewById(R.id.show_tag_pic);
        show_top_tipsTv = (TextView) view.findViewById(R.id.show_top_tips);
        show_tips2Tv = (TextView) view.findViewById(R.id.show_tips2_id);
        count_time_layout = (LinearLayout) view.findViewById(R.id.count_time_layoutId);
        add_accountTv = (TextView) view.findViewById(R.id.odt_account_id);

        cancel_order = (TextView) view.findViewById(R.id.cancel_order_sid);
        cancel_order.setOnClickListener(this);
        pay_btn = (Button) view.findViewById(R.id.pay_btn_id);
        pay_btn.setOnClickListener(this);

        show_toPayPanel = (LinearLayout) view.findViewById(R.id.show_to_payPanel);
        order_detailLayout = (LinearLayout) view.findViewById(R.id.order_detail_id);

        selected_psg_list = (ListView) view.findViewById(R.id.selected_psg_list);
        countTime = (TextView) view.findViewById(R.id.odt_time_id);

        beginDate = (TextView) view.findViewById(R.id.odt_beginDate_id);
        endDate = (TextView) view.findViewById(R.id.odt_endDate_id);

        coachNumber = (TextView) view.findViewById(R.id.odt_coachNumber_id);

        beginPlace = (TextView) view.findViewById(R.id.odt_beginPlace_id);
        pathName = (TextView) view.findViewById(R.id.odt_pathName_id);
        duraTime = (TextView) view.findViewById(R.id.odt_duraTime_id);

        seatNumber = (TextView) view.findViewById(R.id.odt_seatNumber_id);

        endPlace = (TextView) view.findViewById(R.id.odt_endPlace_id);
        total_Money = (TextView) view.findViewById(R.id.totalMoney_id);

    }

    private void showPesger(List<Contacts> contacts) {
        selected_psg_list.setAdapter(new SelectedPsgerAdapter(getContext(), contacts));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_order_sid:
                orderOrNot(presentState);
                break;
            case R.id.pay_btn_id:
                gotoPay(presentState);
                break;
            default:
                break;
        }
    }

    // 待支付0  状态1（待出发）  2（已取消 超时）  3（退改签） 4 已完成
    public void gotoPay(String satus) {
        System.out.println(satus + "===--009" + contacts.size());
        switch (satus) {
            case "0":
                payOrder(preOrderResult.getId(), preOrderResult.getTrainNumber());

                break;
            //    private String[] cancelBtnTips = new String[]{"取消订单", "退票", "等待出发", "退票","已取消,重新预订", "已退票,重新预订", "重新预订","重新预订"};
            //    private String[] tipText = new String[]{"待支付", "待出发", "已验票", "已重新预订", "已取消", "已退款", "已完成", "其他类型"};

            case "1":
                Toast.makeText(getContext(), "You have purchased tickets, please do not repeat the operation.", Toast.LENGTH_SHORT).show();
                break;
            case "2":
                Toast.makeText(getContext(), "You have checked the tickets. Do not repeat the operation.", Toast.LENGTH_SHORT).show();
                break;
            case "3":
                Toast.makeText(getContext(), "You have reserved, please do not repeat the operation.", Toast.LENGTH_SHORT).show();
                break;
            case "4":
                Toast.makeText(getContext(), "You have cancelled, please click to reserve", Toast.LENGTH_SHORT).show();
                break;
            case "5":
                Toast.makeText(getContext(), "You have cancelled, please click to reserve", Toast.LENGTH_SHORT).show();
                break;
            case "6":
                Toast.makeText(getContext(), "You have finished, please click Reservation", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "Unknown click operation error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // 待支付0  状态1（待出发）  2（已取消 超时）  3（退改签） 4 已完成
    // 待支付0 -> 取消订单   状态1（待出发） ->  退票
    // 取消订单", "退票", "已超时,重新预订", "已退票,重新预订", "重新预订"};
    public void orderOrNot(String status) {

        //     private String[] cancelBtnTips = new String[]{"取消订单", "退票", "等待出发", "退票","已取消,重新预订", "已退票,重新预订", "重新预订","重新预订"};
        //    private String[] tipText = new String[]{"待支付", "待出发", "已验票", "已重新预订", "已取消", "已退款", "已完成", "其他类型"};
        System.out.println(status + "-----status");
        switch (status) {
            case "0":
                // 退票
                AlertDialogPro.Builder builder2 = new AlertDialogPro.Builder(getContext());
                builder2.setMessage("Are you sure you want to cancel the order?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 退票
                        cancelOrder(preOrderResult.getId());

//                        presentState = "4";
//                        showState(Integer.parseInt(presentState), takeTime);
                    }
                }).setNegativeButton("Cancel", null).show();
                break;
            case "1":
                // 退票
                AlertDialogPro.Builder builder3 = new AlertDialogPro.Builder(getContext());
                builder3.setMessage("Are you sure you want a refund?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 退票
                        cancelOrder(preOrderResult.getId());
//                        presentState = "5";
//                        showState(Integer.parseInt(presentState), takeTime);
                    }
                }).setNegativeButton("Cancel", null).show();
                break;
            case "2":
            case "3":
            case "4":
            case "5":
                Intent intent = new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    public void payOrder(String orderId, String tripId) {

        // 请求连接
        PaymentInfo paymentInfo = new PaymentInfo(orderId, tripId);

        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(paymentInfo));

        String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");

        String insidePayUrl = UrlProperties.clientIstioIp + UrlProperties.inside_payment;
        dialog = new CustomDialog(getContext(), R.style.CustomDialog);
        dialog.show();


        subscription = RxHttpUtils.postWithHeader(insidePayUrl, loginId, token, requestBody, getContext())
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
                        System.out.println(responseResult + "----==-099");
                        if ("true".equals(responseResult)) {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Request data failed", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        presentState = "1";
                        showState(Integer.parseInt(presentState), takeTime);
                    }
                });
    }

    public void cancelOrder(String orderId) {

        // 请求连接
        final CancelOrderInfo cancelOrderInfo = new CancelOrderInfo(orderId);

        System.out.println(new Gson().toJson(cancelOrderInfo) + "=00-9090");
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(cancelOrderInfo));

        String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");

        String cancelOrderUrl = UrlProperties.clientIstioIp + UrlProperties.cancelOrder;

        dialog = new CustomDialog(getContext(), R.style.CustomDialog);
        dialog.show();
        subscription = RxHttpUtils.postWithHeader(cancelOrderUrl, loginId, token, requestBody, getContext())
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
                        System.out.println(responseResult + "----=23=-099");
                        dialog.dismiss();
                        if (responseResult != null && responseResult != "") {
                            // {"status":true,"message":"Success."}
                            Gson gson = new Gson();
                            CancelOrderResult cancelOrderResult = gson.fromJson(responseResult, CancelOrderResult.class);
                            if (cancelOrderResult.isStatus()) {
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Request data failed", Toast.LENGTH_SHORT).show();
                        }
                        presentState = "5";
                        showState(Integer.parseInt(presentState), takeTime);
                    }
                });
    }


    public class SelectedPsgerAdapter extends BaseAdapter {

        private List<Contacts> data;
        private LayoutInflater layoutInflater;
        private Context context;

        public SelectedPsgerAdapter(Context context, List<Contacts> data) {
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            SelectedPsger addPsger = null;
            if (convertView == null) {
                addPsger = new SelectedPsger();
                //  获得组件， 实例化组件
                convertView = layoutInflater.inflate(R.layout.item_selected_passenger, null);
                addPsger.psName = (TextView) convertView.findViewById(R.id.pas_name_sid);
                addPsger.psType = (TextView) convertView.findViewById(R.id.pas_type_sid);
                addPsger.psIdcard = (TextView) convertView.findViewById(R.id.pas_idcard_sid);
                addPsger.seatType = (TextView) convertView.findViewById(R.id.pas_seatType_sid);
                addPsger.seatPrice = (TextView) convertView.findViewById(R.id.pas_price_sid);
                addPsger.odt_status = (TextView) convertView.findViewById(R.id.odt_status_id);
                addPsger.odt_ordernum = (TextView) convertView.findViewById(R.id.odt_ordernum_id);
                convertView.setTag(addPsger);
            } else {
                addPsger = (SelectedPsger) convertView.getTag();
            }
            // 绑定数据
            addPsger.psName.setText(data.get(position).getContactRealName());
            addPsger.psType.setText(ServerConstValues.EASY_PASSENGER_TYPES[data.get(position).getConatctType()]);

            String tempidcard = "";
            char[] idcards = data.get(position).getContactRealIcard().toCharArray();
            for (int j = 0; j < idcards.length; j++) {
                if (j > 7 && j < 14)
                    tempidcard = tempidcard + "*";
                else
                    tempidcard = tempidcard + idcards[j] + "";
            }
            addPsger.psIdcard.setText(tempidcard);
            addPsger.seatType.setText(ServerConstValues.SEAT_TYPES[Integer.parseInt(seatTypeValue)]);
            addPsger.seatPrice.setText("¥" + data.get(position).getSeatPrice());

            String tempidcard2 = "";
            char[] idcards2 = data.get(position).getOrderId().toCharArray();
            for (int j = 0; j < idcards2.length; j++) {
                if (j > 12 && j < 23)
                    tempidcard2 = tempidcard2 + "";
                else
                    tempidcard2 = tempidcard2 + idcards2[j] + "";
            }
            addPsger.odt_ordernum.setText(tempidcard2);
            return convertView;
        }

        public final class SelectedPsger {
            private TextView psName;
            private TextView psType;
            private TextView psIdcard;
            private TextView seatType;
            private TextView seatPrice;
            private TextView odt_status;
            private TextView odt_ordernum;
        }
    }

    /**
     * 获取某路线的停留时间
     */
    public static String getStayTime(String arriveTime, String startTime) {
        String[] ahm = arriveTime.split(":");
        String[] shm = startTime.split(":");
        int ah = Integer.parseInt(ahm[0]);
        int am = Integer.parseInt(ahm[1]);
        int sh = Integer.parseInt(shm[0]);
        int sm = Integer.parseInt(shm[1]);

        if (sm < am) {
            sm += 60;
            sh--;
        }
        if (sh < ah) {
            sh += 24;
        }
        if (sh != ah) {
            return (sh - ah) + "时" + (sm - am) + "分";
        } else {
            return (sm - am) + "分";
        }
    }
}
