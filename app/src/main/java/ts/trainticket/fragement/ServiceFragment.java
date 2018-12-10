package ts.trainticket.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ts.trainticket.R;
import ts.trainticket.WeatherActivity;


public class ServiceFragment extends BaseFragment{
	private TextView headText;
	private LinearLayout weatherLayoutBtn = null;
	private Button common_head_back = null;

    private LinearLayout jiudian_idBtn = null;
    private LinearLayout nav_idBtn = null;
    private WebView webView;
    private String hotel = "http://m.tuniu.com";
    private final String  flight = "http://map.baidu.com";
    // http://m.ctrip.com/html5/flight/swift/index?mktuvslink=true&sepopup=114&sourceid=2614&allianceid=4897&sid=798181&utm_source=baidu&utm_medium=cpc&utm_campaign=baidu2614
    //  https://m.ctrip.com/webapp/hotel/?from=https%3A%2F%2Fm.ctrip.com%2Fhtml5%2F%3Fsourceid%3D497%26allianceid%3D4897%26sid%3D182042%26utm_source%3Dbaidu%26utm_medium%3Dcpc%26utm_campaign%3Dbaidu497%26sepopup%3D12%26mktuvslink%3Dtrue

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragement_service, container, false);
		initViews(view);
        initWebView();
        addClickListener();
		return view;
	}
	private void initViews(View view) {
		common_head_back = (Button) view.findViewById(R.id.common_head_back_btn);
		common_head_back.setVisibility(View.GONE);

		headText = (TextView) view.findViewById(R.id.title_head_tv);
		headText.setText("旅行服务");
		weatherLayoutBtn  = (LinearLayout) view.findViewById(R.id.weather_layout_id);
		weatherLayoutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转
				Intent cityIntent = new Intent(getContext(), WeatherActivity.class);
				startActivity(cityIntent);
			}
		});
        webView = (WebView) view.findViewById(R.id.web_view);
        jiudian_idBtn = (LinearLayout) view.findViewById(R.id.jiudian_id);
        nav_idBtn = (LinearLayout) view.findViewById(R.id.nav_id);
    }
    private void initWebView() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //webView.setWebViewClient(new WebViewClient());
    }
    private void addClickListener(){
        jiudian_idBtn.setOnClickListener(new ServcieClickListener());
        nav_idBtn.setOnClickListener(new ServcieClickListener());
    }
    private class ServcieClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (jiudian_idBtn.getId() == v.getId()) {
                webView.loadUrl(hotel);
            }else if(nav_idBtn.getId() == v.getId()){
                webView.loadUrl(flight);
            }
        }
    }

}
