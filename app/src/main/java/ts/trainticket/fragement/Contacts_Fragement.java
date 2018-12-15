package ts.trainticket.fragement;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import ts.trainticket.R;
import ts.trainticket.databean.Contacts;
import ts.trainticket.databean.ContactsPageResponse;
import ts.trainticket.httpUtils.RxHttpUtils;
import ts.trainticket.httpUtils.UrlProperties;
import ts.trainticket.utils.ApplicationPreferences;
import ts.trainticket.utils.ServerConstValues;

/**
 * Created by liuZOZO on 2018/3/11.
 */
public class Contacts_Fragement extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swiper = null; // 下拉刷新控件
    private RecyclerView recyclerView = null; // 常用联系人列表
    private ImageView animationIV_addp = null;
    private AnimationDrawable animationDrawable;
    private ImageView contacts_tipsImg = null;
    private TextView contacts_tipsTv = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_contacts, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromServer();
    }

    private void initViews(View view) {

        swiper = (SwipeRefreshLayout) view.findViewById(R.id.refresh_contacts_table);
        swiper.setOnRefreshListener(this);
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView = (RecyclerView) view.findViewById(R.id.view_contacts_table);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        animationIV_addp = (ImageView) view.findViewById(R.id.animationIV_addpas);
        animationIV_addp.setImageResource(R.drawable.pic_loading);
        animationDrawable = (AnimationDrawable) animationIV_addp.getDrawable();
        animationDrawable.start();

        contacts_tipsImg = (ImageView) view.findViewById(R.id.contacts_tips_img);
        contacts_tipsTv = (TextView) view.findViewById(R.id.contacts_tips);
    }

    // 删除常用联系人
    public void contactsByIdcard(String idCard) {
        // 请求连接
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");

        DeleteContactsInfo deleteContactsInfo = new DeleteContactsInfo(idCard, token);
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, new Gson().toJson(deleteContactsInfo));

        String listStationUri = UrlProperties.clientIstioIp + UrlProperties.deleteContacts;

        subscription = RxHttpUtils.getDataPost(listStationUri, requestBody, getContext())
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
                            DeleteContactsResult deleteContactsResult = gson.fromJson(responseResult, DeleteContactsResult.class);
                            if(deleteContactsResult.isStatus())
                                getDataFromServer();
                            else
                                Toast.makeText(getActivity(), "delete contacts failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "no contacts info", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getDataFromServer() {

        String listStationUri = UrlProperties.clientIstioIp + UrlProperties.findContacts;

        String loginId = ApplicationPreferences.getOneInfo(getContext(), "realIcard");
        String token = ApplicationPreferences.getOneInfo(getContext(), "accountPassword");

        subscription = RxHttpUtils.getWithHeader(listStationUri, loginId, token, getContext())
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

                            List<Contacts> contactsList = new ArrayList<>();
                            JSONArray jsonArray = JSON.parseArray(responseResult);
                            Iterator it = jsonArray.iterator();

                            while (it.hasNext()) {
                                JSONObject consObj = (JSONObject) it.next();
                                Contacts contacts1 = new Contacts(consObj.getString("accountId"), consObj.getString("name"), consObj.getString("id"),
                                        consObj.getString("phoneNumber"), consObj.getInteger("documentType"));
                                contactsList.add(contacts1);
                            }
//                            private String userName;// 账户名  必须对应accout 表下的 userName ，隶属关系
//                            private String contactRealName; // 常用联系人真实姓名
//                            private String contactRealIcard;// 常用联系人真实身份证号
//                            private String contactPhone;// 常用联系人手机号
//                            private Integer conatctType;// 常用联系人类型
//                            private String orderId;

                            if (contactsList.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                showTable(contactsList);
                                changeState(true);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                changeState(false);
                            }
                        } else {
                            changeState(false);
                            // Toast.makeText(getActivity(), "查询常用联系人失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // 展示常用联系人
    private void showTable(List<Contacts> contactses) {
        ContactsAdapter myAdapter = new ContactsAdapter(contactses);
        myAdapter.setHasStableIds(true);
        recyclerView.setAdapter(myAdapter);
    }

    private void changeState(boolean tag) {
        if (tag) {
            animationDrawable.stop();
            animationIV_addp.setVisibility(View.GONE);
            contacts_tipsImg.setVisibility(View.GONE);
            contacts_tipsTv.setVisibility(View.GONE);
        } else {
            animationDrawable.stop();
            animationIV_addp.setVisibility(View.GONE);
            contacts_tipsImg.setVisibility(View.VISIBLE);
            contacts_tipsTv.setVisibility(View.VISIBLE);
        }
    }

    // 每个item 对应一个adapter , 一个adapter 对应一个viewholder
    class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

        private List<Contacts> list;

        public ContactsAdapter(List<Contacts> list) {
            this.list = list;
        }

        @Override
        public ContactsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contacts, viewGroup, false);
            return new ContactsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactsViewHolder cpholder, int i) {
            cpholder.contactsName.setText(list.get(i).getContactRealName());
            cpholder.contactsType.setText(ServerConstValues.EASY_PASSENGER_TYPES[list.get(i).getConatctType()]);


            char[] idcards = list.get(i).getContactRealIcard().toCharArray();
            String tempidcard = "";
            for (int j = 0; j < idcards.length; j++) {
                if (j > 6 && j < 13)
                    tempidcard = tempidcard + "*";
                else if (j > 12 && j < 23) {

                } else {
                    tempidcard = tempidcard + idcards[j] + "";
                }
            }
            cpholder.sidCard.setText(tempidcard);


            final int pop = i;
            cpholder.add_del_passe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactsByIdcard(list.get(pop).getContactRealIcard());
                }
            });
            final int position = i;
            cpholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击每一个路线进行跳转
//                    Intent intent = new Intent(getActivity(), BuyTicketActivity.class);
//                    Gson gson = new Gson();
//                    intent.putExtra("item_contact_path",gson.toJson(list.get(position)));
//                    Toast.makeText(getActivity(), gson.toJson(list.get(position)), Toast.LENGTH_LONG).show();
//                    startActivity(intent);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        // 对应item 里面的每一个元素
        class ContactsViewHolder extends RecyclerView.ViewHolder {
            private TextView contactsName;
            private TextView contactsType;
            private TextView sidCard;
            private TextView add_del_passe;

            public ContactsViewHolder(View itemView) {
                super(itemView);
                contactsName = (TextView) itemView.findViewById(R.id.add_passenger);
                contactsType = (TextView) itemView.findViewById(R.id.add_peger_type);
                sidCard = (TextView) itemView.findViewById(R.id.add_pesger_idcard);
                add_del_passe = (TextView) itemView.findViewById(R.id.add_del_passenger);
            }
        }
    }

    @Override
    public void onRefresh() {
        // 刷新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataFromServer();
                swiper.setRefreshing(false);// 结束后停止刷新
            }
        }, 3000);
    }
}
