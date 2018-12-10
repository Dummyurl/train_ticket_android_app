package ts.trainticket;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ts.trainticket.messagepush.MQTTService;
import ts.trainticket.utils.ApplicationPreferences;


/**
 * Created by liuZOZO on 2018/4/21.
 */
public class SettingsActivity  extends AppCompatActivity implements View.OnClickListener{

    public static final String SERVICE_CLASSNAME = "ts.trainticket.messagepush.MQTTService";

    private Button common_head_back_btn = null;
    private TextView title_head_tv = null;
    private TextView tip_msgid = null;

    // 消息开关
    private ImageView messageBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initHeads();
    }
    private void initHeads() {
        common_head_back_btn = (Button) findViewById(R.id.common_head_back_btn);
        common_head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_head_tv = (TextView) findViewById(R.id.title_head_tv);
        title_head_tv.setText("系统设置");

        messageBtn = (ImageView) findViewById(R.id.message_on_off);
        messageBtn.setOnClickListener(this);

        tip_msgid = (TextView) findViewById(R.id.tip_msgid);
        updateButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String  tag = ApplicationPreferences.getOneInfo(this,"messageOnOff");
        if((tag != null && tag != "") || "true".equals(tag)) {
            messageBtn.setBackgroundResource(R.drawable.switch_on);
            ApplicationPreferences.setOneInfo(this,"messageOnOff","true");
        }else{
            messageBtn.setBackgroundResource(R.drawable.switch_off);
            ApplicationPreferences.setOneInfo(this,"messageOnOff","false");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_on_off:
                String  tag = ApplicationPreferences.getOneInfo(this,"messageOnOff");
                System.out.println(tag+"-0000000000");
                if(tag != null && tag != "" && "false".equals(tag)) {
                    messageBtn.setBackgroundResource(R.drawable.switch_on);
                    ApplicationPreferences.setOneInfo(this,"messageOnOff","true");
                }else{
                    messageBtn.setBackgroundResource(R.drawable.switch_off);
                    ApplicationPreferences.setOneInfo(this,"messageOnOff","false");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButton();
    }

    private void updateButton() {
        if (serviceIsRunning()) {
            tip_msgid.setText("开始接收消息");
            messageBtn.setBackgroundResource(R.drawable.switch_on);
            messageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tip_msgid.setText("停止接收消息");
                    messageBtn.setBackgroundResource(R.drawable.switch_off);
                    stopBlackIceService();
                    updateButton();
                }
            });

        } else {
            tip_msgid.setText("停止接收消息");
            messageBtn.setBackgroundResource(R.drawable.switch_off);
            messageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tip_msgid.setText("开始接收消息");
                    messageBtn.setBackgroundResource(R.drawable.switch_on);
                    startBlackIceService();
                    updateButton();
                }
            });
        }
    }

    private void startBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        startService(intent);
    }

    private void stopBlackIceService() {

        final Intent intent = new Intent(this, MQTTService.class);
        stopService(intent);
    }

    private boolean serviceIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
