package ts.trainticket;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import ts.trainticket.fragement.AccountFragement;
import ts.trainticket.fragement.ReserveFragment;
import ts.trainticket.fragement.ServiceFragment;
import ts.trainticket.fragement.StationFragment;


public class MainActivity extends AppCompatActivity {
    public static final int CITY_CHOOSE_RESULT = 1;
    public static final int SIGN_IN_RESULT = 2;
    //定义一个FragmentTabHost对象
    private FragmentTabHost mTabHost;
    //定义一个布局DefaultPassword
    private LayoutInflater layoutInflater;
    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {ReserveFragment.class, StationFragment.class,  AccountFragement.class};
    //定义数组来存放导航图标
    private int imageViewArray[] = {R.drawable.one_change_icon_image, R.drawable.two_change_icon_image, R.drawable.four_change_icon_image};
    //Tab 选项卡的文字
     private String textViewArray[] = {"ticket reserve","station search","account"};
    // ServiceFragment.class,  R.drawable.three_change_icon_image, "travel service",

    public static final String SERVICE_CLASSNAME = "de.eclipsemagazin.mqtt.push.MQTTService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 初始化组件
        initView();
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象,得到Tabhost
        mTabHost = (FragmentTabHost) findViewById(R.id.id_tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.id_nav_table_content);

        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(textViewArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
    }

    // 给Tab 按钮设置图标和文字
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.item_bottom_nav, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.nav_icon_iv);
        imageView.setImageResource(imageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.nav_text_tv);
        textView.setText(textViewArray[index]);
        return view;
    }
}