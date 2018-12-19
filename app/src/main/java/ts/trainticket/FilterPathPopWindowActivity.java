package ts.trainticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ts.trainticket.domain.Station;


public class FilterPathPopWindowActivity extends Activity implements View.OnClickListener {

    String startCity = "";
    String arriveCity = "";
    private RecyclerView startStation_recycleView = null;
    private RecyclerView endStation_recycleView = null;

    boolean[] isSelect_station1 = new boolean[10];
    boolean[] isSelect_station2 = new boolean[10];
    private Button sure_filterBtn = null;
    List<Station> t1;
    List<Station> t2;
    List<Station> t1_1 = new ArrayList<>();
    List<Station> t2_2= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popwindow);
        initView();
    }

    private void initView() {
        startStation_recycleView = (RecyclerView) findViewById(R.id.startStation_recycleView);
        endStation_recycleView = (RecyclerView) findViewById(R.id.endStation_recycleView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        startStation_recycleView.setLayoutManager(manager);
        startStation_recycleView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getApplicationContext());
        endStation_recycleView.setLayoutManager(manager2);
        endStation_recycleView.setItemAnimator(new DefaultItemAnimator());
         showContactPath();
        sure_filterBtn = (Button) findViewById(R.id.sure_filter_btn);
        sure_filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < t1.size(); i++) {
                    if(isSelect_station1[i] == true)
                        t1_1.add(t1.get(i));
                }
                for (int i = 0; i < t2.size(); i++) {
                    if(isSelect_station2[i] == true)
                        t2_2.add(t2.get(i));
                }
                Gson gson = new Gson();
                Intent intent = new Intent();
                intent.putExtra("startCity",gson.toJson(t1_1));
                intent.putExtra("arriveCity", gson.toJson(t2_2));
                setResult(TravelPathActivity.PATH_CHOOSE_RESULT, intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        startCity = getIntent().getStringExtra("startCity");
        arriveCity = getIntent().getStringExtra("arriveCity");


        String result1 = "";
        Gson gson = new Gson();
        ArrayList<Station> list1 = new ArrayList<Station>();
        ArrayList<Station> list2 = new ArrayList<Station>();
        Type listType = new TypeToken<List<Station>>() {}.getType();



        list1 = gson.fromJson(startCity, listType);
        list2 = gson.fromJson(arriveCity, listType);

        t1 = list1;
        t2 = list2;
        //  t1 = initData();
        StationListAdapter myAdapter1 = new StationListAdapter(list1);
        startStation_recycleView.setAdapter(myAdapter1);

     //   t2 = initData2();
        StationListAdapter2 myAdapter2 = new StationListAdapter2(list2);
        endStation_recycleView.setAdapter(myAdapter2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    public List<Station> initData() {

        List<Station> temp = new ArrayList<>();
        Station station1 = new Station("合肥", "合肥 ", "高铁,动车,普通列车停靠");
        temp.add(station1);
        Station station2 = new Station("合肥北城", "合肥 ", "普通列车停靠");
        temp.add(station2);
        Station station3 = new Station("合肥南", "合肥 ", "高铁,动车停靠");
        temp.add(station3);

        return temp;
    }

    public List<Station> initData2() {
        List<Station> temp = new ArrayList<>();
        Station station1 = new Station("上海", "上海 ", "高铁,动车,普通列车停靠");
        temp.add(station1);
        Station station2 = new Station("上海南", "上海 ", "普通列车停靠");
        temp.add(station2);
        Station station3 = new Station("上海虹桥", "上海 ", "高铁,动车停靠");
        temp.add(station3);
        Station station4 = new Station("上海西", "上海 ", "高铁,动车,普通列车停靠");
        temp.add(station4);

        return temp;
    }

    private void showContactPath() {
        List<Station> t1 = initData();
        StationListAdapter myAdapter1 = new StationListAdapter(t1);
        startStation_recycleView.setAdapter(myAdapter1);

        List<Station> t2 = initData2();
        StationListAdapter2 myAdapter2 = new StationListAdapter2(t2);
        endStation_recycleView.setAdapter(myAdapter2);
    }

    class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.StationHolder> {
        private List<Station> list;

        public StationListAdapter(List<Station> list) {
            this.list = list;
        }

        @Override
        public StationHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pop_station, viewGroup, false);
            return new StationHolder(view);
        }

        @Override
        public void onBindViewHolder(final StationHolder holder, final int i) {
            holder.setIsRecyclable(false);
            holder.stationTv.setText(list.get(i).getStationName());

            holder.descTv.setText(list.get(i).getStationDesc());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.chosedImg.getVisibility() == View.VISIBLE) {
                        holder.chosedImg.setVisibility(View.GONE);
                        isSelect_station1[i] = false;
                    } else {
                        isSelect_station1[i] = true;
                        holder.chosedImg.setVisibility(View.VISIBLE);
                    }
                }
            });
            //  holder.typeImg.setBackgroundResource(getIconImg(list.get(i).getType()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class StationHolder extends RecyclerView.ViewHolder {
            private TextView stationTv;
            private TextView descTv;
            private ImageView chosedImg;

            public StationHolder(View itemView) {
                super(itemView);
                stationTv = (TextView) itemView.findViewById(R.id.pop_station_id);
                descTv = (TextView) itemView.findViewById(R.id.pop_stationdesc_id);
                chosedImg = (ImageView) itemView.findViewById(R.id.pop_isSelected);
            }
        }
    }


    class StationListAdapter2 extends RecyclerView.Adapter<StationListAdapter2.StationHolder2> {
        private List<Station> list;

        public StationListAdapter2(List<Station> list) {
            this.list = list;
        }

        @Override
        public StationHolder2 onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pop_station, viewGroup, false);
            return new StationHolder2(view);
        }

        @Override
        public void onBindViewHolder(final StationHolder2 holder, final int i) {
            holder.setIsRecyclable(false);
            holder.stationTv.setText(list.get(i).getStationName());
            holder.descTv.setText(list.get(i).getStationDesc());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.chosedImg.getVisibility() == View.VISIBLE) {
                        holder.chosedImg.setVisibility(View.GONE);
                        isSelect_station2[i] = false;
                    } else {
                        isSelect_station2[i] = true;
                        holder.chosedImg.setVisibility(View.VISIBLE);
                    }
                }
            });
            //  holder.typeImg.setBackgroundResource(getIconImg(list.get(i).getType()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class StationHolder2 extends RecyclerView.ViewHolder {
            private TextView stationTv;
            private TextView descTv;
            private ImageView chosedImg;

            public StationHolder2(View itemView) {
                super(itemView);
                stationTv = (TextView) itemView.findViewById(R.id.pop_station_id);
                descTv = (TextView) itemView.findViewById(R.id.pop_stationdesc_id);
                chosedImg = (ImageView) itemView.findViewById(R.id.pop_isSelected);
            }
        }
    }
}
