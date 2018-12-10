package ts.trainticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ts.trainticket.fragement.WeatherDetail_fragment;

/**
 * Created by liuZOZO on 2018/3/24.
 */
public class WeatherDetailActivty extends AppCompatActivity {
    private WeatherDetail_fragment weatherDetail_fragment;
    // head
    private Button head_back_btn = null;
    private TextView title_head_tv= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherdetail);
        initHeads();
        initFragment();
        showFragment();
    }

    private void initHeads(){
        title_head_tv = (TextView) findViewById(R.id.title_head_tv);
        title_head_tv.setText("天气动态");
        head_back_btn  = (Button) findViewById(R.id.common_head_back_btn);
        head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
              finish();
            }
        });
    }
    private void initFragment(){
        // 初始化数据
        Bundle bundle = new Bundle();
        bundle.putString("search_city", getIntent().getStringExtra("search_city"));
        weatherDetail_fragment = new WeatherDetail_fragment();
        weatherDetail_fragment.setArguments(bundle);
    }
    private void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_weatherdetail_container,weatherDetail_fragment,"")
                .commit();
    }
}
