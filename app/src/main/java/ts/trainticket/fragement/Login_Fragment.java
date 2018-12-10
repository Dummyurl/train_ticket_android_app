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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.LoginActivity;
import ts.trainticket.MainActivity;
import ts.trainticket.R;
import ts.trainticket.SignUpActivity;
import ts.trainticket.domain.Account;
import ts.trainticket.domain.LoginInfo;
import ts.trainticket.domain.LoginResult;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;

/**
 * Created by liuZOZO on 2018/3/12.
 */
public class Login_Fragment extends BaseFragment implements View.OnClickListener {

    private EditText userName = null;
    private EditText passWord = null;

    private Button loginBtn = null;
    private Button signinBtn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        userName = (EditText) view.findViewById(R.id.userName_acId);
        passWord = (EditText) view.findViewById(R.id.password_psId);

        loginBtn = (Button) view.findViewById(R.id.login_acId);
        loginBtn.setOnClickListener(this);
        signinBtn = (Button) view.findViewById(R.id.signin_acId);
        signinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_acId:
                loginGetData();
                break;
            case R.id.signin_acId:
                startActivityForResult(new Intent(getActivity(), SignUpActivity.class), SignUpActivity.SIGN_UP_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    public void loginGetData() {
        // {"email": "fdse_microservices@163.com", "password": "DefaultPassword", "verificationCode": "abcd"}
//        String users = userName.getText().toString();
//        String password = passWord.getText().toString();

        String users = "fdse_microservices@163.com";
        String password = "DefaultPassword";

        System.out.println(users + "==23===" + password);
//        try {
//            users = URLEncoder.encode(users, "UTF-8");
//            password = URLEncoder.encode(password, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if ((users == null || users == "") || (password == null || password == "")) {
            Toast.makeText(getContext(), "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            String getUserUri = UrlProperties.clientIstioIp + UrlProperties.login;
            final LoginInfo loginInfo = new LoginInfo("fdse_microservices@163.com", "DefaultPassword", "abcd");

            System.out.println(new Gson().toJson(loginInfo).toString() + "=0loginInfo");

            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(loginInfo));

            subscription = RxHttpUtils.getDataPost(getUserUri, requestBody, getContext())
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

                                LoginResult loginResult = gson.fromJson(responseResult, LoginResult.class);
                                System.out.println(loginResult.toString() + "====---00");
                                if (loginResult.getStatus()) {
                                    Account account = loginResult.getAccount();
                                    // todo password is always null -> token
                                    ApplicationPreferences.setUserInfo(getContext(), account.getName(),
                                            account.getDocumentNum(), loginResult.getToken(),
                                            account.getEmail(), account.getId().toString(),
                                            "http://10.141.211.161:31380/assets/img/user01.png");

                                    Toast.makeText(getContext(), "Login Success", Toast.LENGTH_SHORT).show();

                                    ApplicationPreferences.setOneInfo(getContext(), ApplicationPreferences.ACCOUNT_STATE, ApplicationPreferences.STATE_ONLINE);

                                    getActivity().setResult(MainActivity.SIGN_IN_RESULT);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                                }
//                                JSONObject jsonObj = JSON.parseObject(responseResult);
//                                boolean status = Boolean.parseBoolean(jsonObj.getString("status"));
//                                System.out.println(status+"-----status");
//                                if(!status){
//                                    Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
//                                }else  if (status) {
//                                    JSONObject userInfo = jsonObj.getJSONObject("data");
//                                    // String userName, String userRealName,String password,String userPhone,String realIcard
//                                    ApplicationPreferences.setUserInfo(getContext(), userInfo.getString("userName"),
//                                            userInfo.getString("realName"), userInfo.getString("password"),
//                                            userInfo.getString("userPhone"), userInfo.getString("realIcard"),
//                                            userInfo.getString("headPic") );
//                                    Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
//
//                                    ApplicationPreferences.setOneInfo(getContext(), ApplicationPreferences.ACCOUNT_STATE, ApplicationPreferences.STATE_ONLINE);
//
//                                    getActivity().setResult(MainActivity.SIGN_IN_RESULT);
//                                    getActivity().finish();
//                                }
                            } else {
                                Toast.makeText(getContext(), "login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SignUpActivity.SIGN_UP_REQUEST_CODE == requestCode && LoginActivity.SIGN_IN_RESULT_CODE == resultCode && null != data) {
            userName.setText(data.getStringExtra(LoginActivity.KEY_ACCOUNT_NAME));
            passWord.setText(data.getStringExtra(LoginActivity.KEY_ACCOUNT_PASSWORD));
        }
    }

    @Override
    public void onStop() {
        if (null != subscription && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        unlockClick();
        super.onStop();
    }
}