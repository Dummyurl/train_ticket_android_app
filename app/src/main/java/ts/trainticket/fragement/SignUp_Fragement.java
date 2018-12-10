package ts.trainticket.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;


import java.net.URLEncoder;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import ts.trainticket.LoginActivity;
import ts.trainticket.R;
import ts.trainticket.httpUtils.ResponseResult;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;

/**
 * Created by liuZOZO on 2018/3/12.
 */
public class SignUp_Fragement extends BaseFragment  {

    private EditText userNameTv = null;
    private EditText userPwdTv = null;
    private EditText reUsrPwdTv = null;
    private EditText userRealNameTv = null;
    private EditText IcCardTv = null;
    private EditText userPhoneTv = null;
    private Button userPwd_btn = null;
    private Button re_userPwd_btn = null;

    private Button btnSignUp;

    // 管理多个订阅者
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_signup, container, false);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        userNameTv = (EditText) view.findViewById(R.id.userName_sid);
        userPwdTv = (EditText) view.findViewById(R.id.userPwd_sid);
        reUsrPwdTv = (EditText) view.findViewById(R.id.re_userPwd_sid);
        userRealNameTv = (EditText) view.findViewById(R.id.userRealName_sid);
        IcCardTv = (EditText) view.findViewById(R.id.ic_card_sid);
        userPhoneTv = (EditText) view.findViewById(R.id.userPhone_sid);

        userPwd_btn = (Button) view.findViewById(R.id.userPwd_btn_sid);
        re_userPwd_btn = (Button) view.findViewById(R.id.re_userPwd_btn_sid);
        btnSignUp = (Button) view.findViewById(R.id.signin_btn_sid);
        btnSignUp.setOnClickListener(new SignInListener());
    }
    public  void initData(){
        String seatType = getArguments().getString("is_modify");
        System.out.println(seatType+ "=-=088776565");
        if("true".equals(seatType)){
            setModifyState();
        }
    }
    private void setModifyState(){
        userNameTv.setCursorVisible(false);
        userNameTv.setFocusable(false);
        userNameTv.setFocusableInTouchMode(false);

        userRealNameTv.setCursorVisible(false);
        userRealNameTv.setFocusable(false);
        userRealNameTv.setFocusableInTouchMode(false);

        IcCardTv.setCursorVisible(false);
        IcCardTv.setFocusable(false);
        IcCardTv.setFocusableInTouchMode(false);

        btnSignUp.setText("修改");
    }
    private class SignInListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            lockClick();
            // 准备数据
            if("修改".equals(btnSignUp.getText().toString())){
                modifyUserInfo();
            }else {
                signInToServer();
            }
        }
    }
    private void modifyUserInfo() {
        String password = userPwdTv.getText().toString();
        String userPhone = userPhoneTv.getText().toString();
        String icCard  =  IcCardTv.getText().toString();
        try {
            password = URLEncoder.encode(password, "UTF-8");
            icCard = URLEncoder.encode(icCard, "UTF-8");
            userPhone = URLEncoder.encode(userPhone, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 请求连接
        String listStationUri = UrlProperties.modifyUSerInfo + "/" + password + "/" + userPhone + "/" + icCard;


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
                            ResponseResult result = gson.fromJson(responseResult, ResponseResult.class);
                            if(result.isStatus())
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Modify failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Modify failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void signInToServer(){
            String userName = userNameTv.getText().toString();
            String password = userPwdTv.getText().toString();
            String userRealName = userRealNameTv.getText().toString();
            String icCard = IcCardTv.getText().toString();
            String userPhone = userPhoneTv.getText().toString();
            try {
                userName = URLEncoder.encode(userName, "UTF-8");
                password = URLEncoder.encode(password, "UTF-8");
                userRealName = URLEncoder.encode(userRealName, "UTF-8");
                icCard = URLEncoder.encode(icCard, "UTF-8");
                userPhone = URLEncoder.encode(userPhone, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 请求连接
            String listStationUri = UrlProperties.addUser + "/" + icCard + "/" + userRealName + "/" + userName + "/" + userPhone + "/" + password;
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
                                Intent intent = new Intent();
                                intent.putExtra(LoginActivity.KEY_ACCOUNT_NAME, userNameTv.getText().toString());
                                intent.putExtra(LoginActivity.KEY_ACCOUNT_PASSWORD, userPwdTv.getText().toString());
                                Toast.makeText(getContext(), "register success", Toast.LENGTH_SHORT).show();
                                getActivity().setResult(LoginActivity.SIGN_IN_RESULT_CODE, intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "register failed, This server Api not ready", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }


    @Override
    public void onStart() {
        super.onStart();
        compositeSubscription = new CompositeSubscription();

        boolean isOnline = ApplicationPreferences.isUserOnLine(getContext());
        if(isOnline) {
            String userName = ApplicationPreferences.getOneInfo(getContext(), ApplicationPreferences.USER_NAME);
            String userIdCard = ApplicationPreferences.getOneInfo(getContext(), ApplicationPreferences.REAL_ICARD);
            String userPhone = ApplicationPreferences.getOneInfo(getContext(), ApplicationPreferences.USER_PHONE);
            String userRealName = ApplicationPreferences.getOneInfo(getContext(), ApplicationPreferences.USERREALNAME_NAME);
            if (userName != null && userName != "") {
                userNameTv.setText(userName);
                IcCardTv.setText(userIdCard);
                userPhoneTv.setText(userPhone);
                userRealNameTv.setText(userRealName);
            }
        }
    }

    @Override
    public void onStop() {
        if(compositeSubscription.hasSubscriptions()){
            compositeSubscription.unsubscribe();
        }
        unlockClick();
        super.onStop();
    }
}
