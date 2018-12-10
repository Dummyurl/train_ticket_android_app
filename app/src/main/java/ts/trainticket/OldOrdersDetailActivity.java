package ts.trainticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ts.trainticket.fragement.OldOrders_fragment;

/**
 * Created by liuZOZO on 2018/3/20.
 */
public class OldOrdersDetailActivity extends AppCompatActivity {

    private OldOrders_fragment oldOrders_fragment = null;
    private Button buttonHead = null;
    private TextView headText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oldorders);

        initHeads();
        initFragment();
        showFragment();

    }
    private void initHeads() {
        buttonHead = (Button) findViewById(R.id.common_head_back_btn);
        buttonHead.setVisibility(View.GONE);
        headText = (TextView) findViewById(R.id.title_head_tv);
        headText.setText("订单详情页");
    }

    private void initFragment(){
        oldOrders_fragment = new OldOrders_fragment();

        // 初始化数据
        Bundle bundle = new Bundle();
        bundle.putString("ordersId", getIntent().getStringExtra("ordersId"));
        oldOrders_fragment.setArguments(bundle);

    }
    private void showFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_oldorders_container,oldOrders_fragment,"")
                .commit();
    }
}
