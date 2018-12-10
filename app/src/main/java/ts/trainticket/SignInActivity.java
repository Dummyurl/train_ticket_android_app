package ts.trainticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ts.trainticket.fragement.SignIn_Fragement;
import ts.trainticket.utils.ApplicationPreferences;

/**
 * Created by liuZOZO on 2018/3/12.
 */
public class SignInActivity extends AppCompatActivity {
    public static final int SIGN_UP_REQUEST_CODE = 0;
    private SignIn_Fragement signIn_fragement = null;
    private Button buttonHead = null;
    private TextView headText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initHeads();
        initFragment();
        showFragment();
    }
    private void initHeads() {
        buttonHead = (Button) findViewById(R.id.common_head_back_btn);
        buttonHead.setVisibility(View.GONE);
        headText = (TextView) findViewById(R.id.title_head_tv);

        boolean isOnline = ApplicationPreferences.isUserOnLine(this);
        if(isOnline){
            String userName = ApplicationPreferences.getOneInfo(this, ApplicationPreferences.USER_NAME);
            if(userName != null || userName != "")
                headText.setText("修改信息");
        }
        else
            headText.setText("注册");
    }

    private void initFragment(){
        signIn_fragement = new SignIn_Fragement();
//        String userName = ApplicationPreferences.getOneInfo(this, ApplicationPreferences.USER_NAME);
        boolean isOnline = ApplicationPreferences.isUserOnLine(this);

            // 初始化数据
            Bundle bundle = new Bundle();
            if(isOnline)
                bundle.putString("is_modify", "true");
            else
                bundle.putString("is_modify", "false");
            signIn_fragement.setArguments(bundle);

    }

    private void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_signin_container,signIn_fragement,"")
                .commit();
    }
}
