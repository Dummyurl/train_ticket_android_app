package ts.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ts.trainticket.fragement.PathDetailFragment;

/**
 * Created by liuZOZO on 2018/1/21.
 * 车次详情
 */
public class BuyTicketActivity extends AppCompatActivity {

    private PathDetailFragment buyTicketFragment;

    // head
    private Button head_back_btn = null;
    private TextView headText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        initViews();
    }

    private void initViews(){
        initHead();
        initFragment();
        showFragment();

    }
    private void initHead(){
        headText = (TextView) findViewById(R.id.title_head_tv);
        headText.setText("Path Info");
        head_back_btn = (Button) findViewById(R.id.common_head_back_btn);
        head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                Intent cityIntent = new Intent(getApplication(), TravelPathActivity.class);
                startActivityForResult(cityIntent, CityChooseActivity.CITY_CHOOSE_REQUEST_CODE);
            }
        });
    }

    private void initFragment(){
        buyTicketFragment = new PathDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pathInfo",getIntent().getStringExtra("item_contact_path"));
        buyTicketFragment.setArguments(bundle);
    }
    private void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_byt_fragment_container,buyTicketFragment,"")
                .commit();
    }
}
