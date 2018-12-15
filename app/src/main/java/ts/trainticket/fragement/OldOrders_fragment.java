package ts.trainticket.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.R;
import ts.trainticket.databean.COrders;
import ts.trainticket.databean.COrdersPageResponse;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ServerConstValues;

/**
 * Created by liuZOZO on 2018/3/20.
 */
public class OldOrders_fragment extends BaseFragment {
    private ImageView odd_topImg = null;
    private TextView odd_topTipsTv = null;

    private TextView odd_ordernumTv = null;

    private TextView odd_beginDateTv = null;
    private TextView odd_endDateTv = null;

    private TextView odd_beginTimeTv = null;
    private TextView odd_beginPlaceTv = null;

    private TextView odd_pathNameTv = null;
    private TextView odd_duraTimeTv = null;

    private TextView odd_endTimeTv = null;
    private TextView odd_endPlaceTv = null;

    private TextView odd_nameTv = null;
    private TextView odd_typeTv = null;
    private TextView odd_seatTypeTv = null;
    private TextView odd_idcardTv = null;
    private TextView odd_priceTv = null;
    private TextView odd_statesTv = null;

    private Button odd_bottomBtn = null;
    // 待支付0  状态1（待出发）  2（已取消）  3（退改签） 4(已完成
    private String[] tipText = new String[]{"待支付", "待完成", "已取消", "已改签", "已发车"};
    private int[] imagesTip = new int[]{
            R.drawable.hb_youji_icon,
            R.drawable.tab_ticket_selected_new,
            R.drawable.delete_bg,
            R.drawable.hb_yiquxiao_icon,
            R.drawable.cb_checked
    };
    private String[] tipBottomBtnText = new String[]{"去支付", "已购票", "重新购票", "重新购票", "去购票"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oldorders, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initViews(View view) {
        odd_topImg = (ImageView) view.findViewById(R.id.odd_top_image);
        odd_topTipsTv = (TextView) view.findViewById(R.id.odd_top_tips);

        odd_ordernumTv = (TextView) view.findViewById(R.id.odd_ordernum_id);

        odd_beginDateTv = (TextView) view.findViewById(R.id.odd_beginDate_id);
        odd_endDateTv = (TextView) view.findViewById(R.id.odd_endDate_id);

        odd_beginTimeTv = (TextView) view.findViewById(R.id.odd_beginTime_id);
        odd_beginPlaceTv = (TextView) view.findViewById(R.id.odd_beginPlace_id);

        odd_pathNameTv = (TextView) view.findViewById(R.id.odd_pathName_id);
        odd_duraTimeTv = (TextView) view.findViewById(R.id.odd_duraTime_id);

        odd_endTimeTv = (TextView) view.findViewById(R.id.odd_endTime_id);
        odd_endPlaceTv = (TextView) view.findViewById(R.id.odd_endPlace_id);

        odd_nameTv = (TextView) view.findViewById(R.id.odd_name_sid);
        odd_typeTv = (TextView) view.findViewById(R.id.odd_type_sid);
        odd_seatTypeTv = (TextView) view.findViewById(R.id.odd_seatType_oid);
        odd_idcardTv = (TextView) view.findViewById(R.id.odd_idcard_oid);
        odd_priceTv = (TextView) view.findViewById(R.id.odd_price_oid);
        odd_statesTv = (TextView) view.findViewById(R.id.odd_states);

        odd_bottomBtn = (Button) view.findViewById(R.id.odd_bottom_btn);
    }

    private void initData() {
        String ordersId = getArguments().getString("ordersId");
        getDataFromServer(ordersId);
    }

    private void showState(int status) {
        odd_topImg.setBackgroundResource(imagesTip[status]);
        odd_topTipsTv.setText(tipText[status]);
        odd_bottomBtn.setText(tipBottomBtnText[status]);
    }

    private void showData(COrders cOrders) {
        odd_ordernumTv.setText(cOrders.getOrderId());

        odd_beginDateTv.setText(cOrders.getPathStartDate());
        odd_endDateTv.setText(cOrders.getPathArriveDate());

        odd_beginTimeTv.setText(cOrders.getTakeTime().substring(0, 5));
        odd_beginPlaceTv.setText(cOrders.getStartStationName());

        odd_pathNameTv.setText(cOrders.getPathName());
        odd_duraTimeTv.setText("2时21分");

        odd_endTimeTv.setText(cOrders.getArriveTime().substring(0, 5));
        odd_endPlaceTv.setText(cOrders.getArriveStationName());

        odd_nameTv.setText(cOrders.getPassengerName());
        odd_typeTv.setText(ServerConstValues.EASY_PASSENGER_TYPES[Integer.parseInt(cOrders.getPasType())]);
        odd_seatTypeTv.setText(ServerConstValues.SEAT_TYPES[Integer.parseInt(cOrders.getSeatType())]);
        odd_idcardTv.setText(cOrders.getPasIdCard());
        odd_priceTv.setText("¥" + cOrders.getTicketPrice());
        odd_statesTv.setText(ServerConstValues.STATUSES[Integer.parseInt(cOrders.getStatus())]);
        showState(Integer.parseInt(cOrders.getStatus()));
    }

    private void getDataFromServer(String orderId) {
        // 准备数据
        try {
            orderId = URLEncoder.encode(orderId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 请求连接
        String listStationUri = UrlProperties.getOneOrderByOrderId + "/" + orderId;
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
                            COrdersPageResponse cOrders = gson.fromJson(responseResult, COrdersPageResponse.class);
                            showData(cOrders.getcOrderses().get(0));
                            //  Toast.makeText(getActivity(), cOrders.getcOrderses().get(0).getPathName() + "==-=", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Request data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
