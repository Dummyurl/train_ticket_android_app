package ts.trainticket.fragement;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.net.URLEncoder;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.R;
import ts.trainticket.domain.AddContactsInfo;
import ts.trainticket.domain.AddContactsResult;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;

/**
 * Created by liuZOZO on 2018/3/14.
 */
public class AddPs_Fragement extends BaseFragment {

    private LinearLayout idtypeLay = null;
    private LinearLayout pstype = null;
    private TextView addpsTv = null;
    private TextView userType_psid = null;
    private Button addPs_btn = null;

    private EditText add_userName = null;
    private EditText add_num = null;
    private EditText add_phonenum = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addps, container, false);

        initView(view);
        return view;
    }


    private void initView(View view) {
        add_userName = (EditText) view.findViewById(R.id.add_userName_id);
        addpsTv = (TextView) view.findViewById(R.id.idcard_addps);
        add_num = (EditText) view.findViewById(R.id.add_num_id);
        userType_psid = (TextView) view.findViewById(R.id.userType_psid);
        add_phonenum = (EditText) view.findViewById(R.id.add_phonenum_id);

        idtypeLay = (LinearLayout) view.findViewById(R.id.idtype_asid);
        pstype = (LinearLayout) view.findViewById(R.id.pstype_asid);
        addPs_btn =  (Button) view.findViewById(R.id.addPs_btn_id);
        addPs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registUserToServer();
            }
        });
        idtypeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIdTypeDialog();
            }
        });
        pstype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserType();
            }
        });
    }

    private void registUserToServer() {

        String userName = add_userName.getText().toString();
         //String userIType = addpsTv.getText().toString();
        String cardNum = add_num.getText().toString();
        // String userType = getUserTypeNum(userType_psid.getText().toString());
        String userPhone = add_phonenum.getText().toString();

        int  documentType = 1;
        final AddContactsInfo addContactsInfo = new AddContactsInfo(userName, documentType, cardNum, userPhone);
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(addContactsInfo));

        // 请求连接
        String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");

        String addContactsUri = UrlProperties.clientIstioIp + UrlProperties.createContacts;
        subscription = RxHttpUtils.postWithHeader(addContactsUri, loginId, token, requestBody, getContext())
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
                        System.out.println(responseResult + "====--0");
                        if (responseResult != null && !responseResult.equals("")) {
                            Gson gson = new Gson();
                            AddContactsResult addContactsResult = gson.fromJson(responseResult, AddContactsResult.class);

                            if(addContactsResult.isStatus()){
                                Toast.makeText(getContext(), "Add contacts success!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Add contacts failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Add contacts failed, Unknown reason!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setUserType() {
        final AlertDialog alertDialog;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View vd = inflater.inflate(R.layout.item_pstype, null);
        final RadioGroup groupBroadCast = (RadioGroup) vd.findViewById(R.id.group_asid);
        final RadioButton rb5 = (RadioButton) vd.findViewById(R.id.rb5_asid);
        final RadioButton rb6 = (RadioButton) vd.findViewById(R.id.rb6_asid);
        final RadioButton rb7 = (RadioButton) vd.findViewById(R.id.rb7_asid);
        final RadioButton rb8 = (RadioButton) vd.findViewById(R.id.rb8_asid);
        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(vd)
                .create();
        groupBroadCast.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb5.getId()) {
                    userType_psid.setText(rb5.getText().toString());
                    alertDialog.dismiss();
                } else if (checkedId == rb6.getId()) {
                    userType_psid.setText(rb6.getText().toString());
                    alertDialog.dismiss();
                } else if (checkedId == rb7.getId()) {
                    userType_psid.setText(rb7.getText().toString());
                    alertDialog.dismiss();
                } else if (checkedId == rb8.getId()) {
                    userType_psid.setText(rb8.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();


    }

    private void setIdTypeDialog() {
        final AlertDialog alertDialog;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View vd = inflater.inflate(R.layout.item_idtype, null);
        final RadioGroup groupBroadCast = (RadioGroup) vd.findViewById(R.id.group_asid);
        final RadioButton rb1 = (RadioButton) vd.findViewById(R.id.rb1_asid);
        final RadioButton rb2 = (RadioButton) vd.findViewById(R.id.rb2_asid);
//        final RadioButton rb3 = (RadioButton) vd.findViewById(R.id.rb3_asid);
//        final RadioButton rb4 = (RadioButton) vd.findViewById(R.id.rb4_asid);
        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(vd)
                .create();
        groupBroadCast.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb1.getId()) {
                    addpsTv.setText(rb1.getText().toString());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "暂不支持此种类型购票", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    public String getUserTypeNum(String type) {
        System.out.println(type + "-=009089");
        if ("ID Card".equals(type)) {
            return "0";
        } else if ("Passport".equals(type)) {
            return "1";
        } else {
            return "4";
        }
    }

}
