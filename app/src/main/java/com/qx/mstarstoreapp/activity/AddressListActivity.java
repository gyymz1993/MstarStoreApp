package com.qx.mstarstoreapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.AppURL;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.base.BaseApplication;
import com.qx.mstarstoreapp.json.AddressListResult;
import com.qx.mstarstoreapp.net.OKHttpRequestUtils;
import com.qx.mstarstoreapp.net.VolleyRequestUtils;
import com.qx.mstarstoreapp.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/9/29 16:04
 * @version    地址列表
 *    
 */
public class AddressListActivity extends BaseActivity {


    ListView listView;
    ListViewAdapter adapter;
    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.id_rel_title)
    RelativeLayout idRelTitle;
    @Bind(R.id.id_lv_adress)
    ListView idLvAdress;
    List<AddressListResult.DataEntity.AddressListEntity> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        ButterKnife.bind(this);
        initView();
        loadNetData();
    }


    private void loadNetData() {
        String url = AppURL.URL_ADDRESS_LIST + "tokenKey=" + BaseApplication.getToken();
        L.e("获取地址" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    AddressListResult address = new Gson().fromJson(result, AddressListResult.class);
                    addressList = address.getData().getAddressList();
                    adapter.notifyDataSetChanged();
                }
                if (error == 2) {
                    loginToServer(AddressListActivity.class);
                } else {
                    String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                    L.e(message);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });


    }

    protected void initView() {
        titleText.setText("收货地址管理");
        listView = (ListView) findViewById(R.id.id_lv_adress);
        LinearLayout addaderss = (LinearLayout) findViewById(R.id.id_lay_add);
        addaderss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });
        View view = View.inflate(this, R.layout.lv_below_add, null);
        view.findViewById(R.id.id_lay_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                startActivityForResult(intent, 11);
            }
        });
        listView.addFooterView(view);// 为listview添加footview
        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);
        AddAddressActivity.setOnRefurbishListener(new AddAddressActivity.OnRefurbishListener() {
            @Override
            public void onRefush() {
                loadNetData();
            }
        });
    }

    public class ListViewAdapter extends BaseAdapter {

        int temp;

        @Override

        public int getCount() {
            return addressList.size();
        }

        @Override
        public Object getItem(int i) {
            return addressList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(AddressListActivity.this).inflate(R.layout.adapter_adress_item, null);
                viewHolder.idTvName = (TextView) view.findViewById(R.id.id_tv_name);
                viewHolder.idTvAddress = (TextView) view.findViewById(R.id.id_tv_address);
                viewHolder.idCheck = (TextView) view.findViewById(R.id.id_check);
                viewHolder.idLyEdit = (LinearLayout) view.findViewById(R.id.id_ly_edit);
                viewHolder.idLyDel = (LinearLayout) view.findViewById(R.id.id_ly_del);
                viewHolder.id_lay_bg = (RelativeLayout) view.findViewById(R.id.id_lay_bg);
                // viewHolder.addressListEntity=addressList.get(i);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            AddressListResult.DataEntity.AddressListEntity addressListEntity = addressList.get(i);
            // viewHolder.id_lay_bg.setBackgroundColor(getResources().getColor(R.color.theme_bg));
            viewHolder.idTvName.setText(addressListEntity.getName());
            viewHolder.idTvAddress.setText(addressListEntity.getAddr());
            Drawable drawable = getResources().getDrawable(R.drawable.iocn_lsel_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            Drawable drawable1 = getResources().getDrawable(R.drawable.iocn_lsel_nor);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight()); //设置边界
            if (addressListEntity.getIsDefault().equals("1")) {
                viewHolder.idCheck.setCompoundDrawables(null, null, drawable, null);//画在右边
            } else {
                viewHolder.idCheck.setCompoundDrawables(null, null, drawable1, null);//画在右边
            }
            viewHolder.idCheck.setId(i);
            viewHolder.idLyDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // netDelAddress(addressList.get(i));
                }
            });

            /*编辑*/
            viewHolder.idLyEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle pBundle = new Bundle();
                    pBundle.putBoolean("IS_MODIFY_ADDRESS", true);
                    pBundle.putString("ID", addressList.get(i).getId());
                    openActivity(AddAddressActivity.class, pBundle);
                }
            });

            /*viewHolder.idCheck.setOnCheckedChangeListener()替换为   避免多次调用*/
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.idCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    //控制不许反选  必须有默认
//                    if (addressList.get(i).getIsDefault().equals("1")) {
//                        finalViewHolder.idCheck.setChecked(true);
//                        return;
//                    }
//                    addressList.get(i).setIsDefault(finalViewHolder.idCheck.isChecked() ? "1" : "0");
//                    if (finalViewHolder.idCheck.isChecked()) {
//                        for (int j = 0; j < addressList.size(); j++) {
//                            //把其他的checkbox设置为false
//                            if (j != i) {
//                                addressList.get(j).setIsDefault("0");
//                            }
//                        }
//                    }
//                    // notifyDataSetChanged();
                    netSetDefaultAddress(addressList.get(i));
                }
            });
            return view;
        }


        public void netSetDefaultAddress(final AddressListResult.DataEntity.AddressListEntity addressListEntity) {
            String url = AppURL.URL_DEFAULT_ADDRESS + "tokenKey=" + BaseApplication.getToken() + "&id=" + addressListEntity.getId();
            L.e(url);
            VolleyRequestUtils.getInstance().getCookieRequest(AddressListActivity.this, url, new VolleyRequestUtils.HttpStringRequsetCallBack(){
                @Override
                public void onSuccess(String result) {
                    int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                    if (error == 0) {
                        //通知适配器更改
                        L.e("设置默认地址成功");
                        loadNetData();
                    }
                    if (error == 2) {
                        loginToServer(AddressListActivity.class);
                    } else {
                        String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                        L.e(message);
                    }
                }

                @Override
                public void onFail(String fail) {

                }
            });


        }

        public void netDelAddress(final AddressListResult.DataEntity.AddressListEntity addressListEntity) {
            String url = AppURL.URL_DEL_ADDRESS + "tokenKey=" + BaseApplication.getToken() + "&id=" + addressListEntity.getId();
            L.e(url);
            VolleyRequestUtils.getInstance().getCookieRequest(AddressListActivity.this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
                @Override
                public void onSuccess(String result) {
                    int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                    if (error == 0) {
                        addressList.remove(addressListEntity);
                        addressList.get(0).setIsDefault("1");
                        adapter.notifyDataSetChanged();
                    }
                    if (error == 2) {
                        loginToServer(AddressListActivity.class);
                    } else {
                        String message = new Gson().fromJson(result, JsonObject.class).get("message").getAsString();
                        L.e(message);
                    }
                }

                @Override
                public void onFail(String fail) {

                }
            });

        }

        public class ViewHolder {
            TextView idTvName;
            TextView idTvAddress;
            TextView idCheck;
            LinearLayout idLyEdit;
            LinearLayout idLyDel;
            RelativeLayout id_lay_bg;
        }
    }


    public void onBack(View view) {
        finish();
    }
}
