package ts.trainticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ts.trainticket.fragement.Weather_fragment;


/**
 * Created by liuZOZO on 2018/3/24.
 */
public class WeatherActivity extends AppCompatActivity {

    private Weather_fragment weather_fragment;
    // head
    private Button head_back_btn = null;
    private TextView title_head_tv= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initHeads();
        initFragment();
        showFragment();
    }

    private void initHeads(){
        head_back_btn  = (Button) findViewById(R.id.common_head_back_btn);
        title_head_tv = (TextView) findViewById(R.id.title_head_tv);
        title_head_tv.setText("天气信息");
        head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                finish();
            }
        });
    }
    private void initFragment(){
        weather_fragment = new Weather_fragment();
    }
    private void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_weather_container,weather_fragment,"")
                .commit();
    }
}
