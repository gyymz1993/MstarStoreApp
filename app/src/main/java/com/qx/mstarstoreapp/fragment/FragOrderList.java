package com.qx.mstarstoreapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.activity.ConfirmOrderActivity;
import com.qx.mstarstoreapp.activity.CustomMadeActivity;
import com.qx.mstarstoreapp.activity.ProductionListActivity;
import com.qx.mstarstoreapp.adapter.BaseViewHolder;
import com.qx.mstarstoreapp.adapter.CommonAdapter;
import com.qx.mstarstoreapp.base.AppURL;
import com.qx.mstarstoreapp.base.BaseApplication;
import com.qx.mstarstoreapp.base.BaseFragment;
import com.qx.mstarstoreapp.json.OrderWaitResult;
import com.qx.mstarstoreapp.net.ImageLoadOptions;
import com.qx.mstarstoreapp.net.VolleyRequestUtils;
import com.qx.mstarstoreapp.utils.L;
import com.qx.mstarstoreapp.utils.ToastManager;
import com.qx.mstarstoreapp.utils.UIUtils;
import com.qx.mstarstoreapp.viewutils.CustomAdapter;
import com.qx.mstarstoreapp.viewutils.CustomListView;
import com.qx.mstarstoreapp.viewutils.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragOrderList extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private  List<OrderWaitResult.DataEntity.OrderListEntity.ListEntity> listData;
    private ListViewAdapter adapter;
    private PullToRefreshView oullRefreshView;
    private static final int PULL_REFRESH = 1;
    private static final int PULL_LOAD = 2;
    private int pullStatus;
    private int cpage = 1;
    private int tempCurpage;
    private int fragType;
    private int listCount = 0;
    private boolean isflag;
    public FragOrderList(int fragType) {
        this.fragType = fragType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_list_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listview);
        oullRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_refresh_view);
        oullRefreshView.setOnHeaderRefreshListener(this);
        oullRefreshView.setOnFooterRefreshListener(this);
        listData=new ArrayList<>();
        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                if (fragType == 1) {
                    intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    bundle.putString("id", listData.get(i).getId());
                    intent.putExtras(bundle);
                }
                if (fragType == 2) {
                    intent = new Intent(getActivity(), ProductionListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    bundle.putString("orderNum", listData.get(i).getOrderNum());
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && !isflag) {
            //LogUtils.e(TAG + "调用onActivityCreated" + indentState);
            // curpage = 1;
            //showDialog();
            loadNetData();
        }
        isflag = true; //仅第一次调用
    }

    /**
     * 当前用户看到一个fragment时可以执行一下代码
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isflag) {
            // LogUtils.e(TAG+"setUserVisibleHint是否在显示" + indentState);
//            curpage = 1;
//            showDialog();
            loadNetData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        loadNetData();
//    }

    private void loadNetData() {
        String url = null;
        if (fragType == 1) {
            url = AppURL.URL_ORDER_WAITCHECK + "tokenKey=" + BaseApplication.getToken();
        }
        if (fragType == 2) {
            url = AppURL.URL_ORDER_MODEL_LIST + "tokenKey=" + BaseApplication.getToken() + "&cpage=" + cpage;
            // ModelOrderProduceListPage?tokenKey=10b588002228fa805231a59bb7976bf4&cpage=2
        }
        L.e("获取地址" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(getActivity(), url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("loadNetData  " + result);
                JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
                String error = jsonResult.get("error").getAsString();
                if (error.equals("0")) {
                    OrderWaitResult orderWaitResult = new Gson().fromJson(result, OrderWaitResult.class);
                    OrderWaitResult.DataEntity.OrderListEntity orderList = orderWaitResult.getData().getOrderList();
                    List<OrderWaitResult.DataEntity.OrderListEntity.ListEntity> list = orderList.getList();
                    if (pullStatus != PULL_LOAD) {
                        listData.clear();
                    }
                    listData.addAll(list);
                    endNetRequest();
                }
                if (error.equals("2")) {
                    loginToServer(CustomMadeActivity.class);
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

    public void endNetRequest() {
        adapter.notifyDataSetChanged();
        tempCurpage = cpage;
        if (pullStatus == PULL_LOAD) {
            oullRefreshView.onFooterRefreshComplete();
        }
        if (pullStatus == PULL_REFRESH) {
            oullRefreshView.onHeaderRefreshComplete();
        }
        pullStatus = 0;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView pullToFootRefreshView) {
        L.e("listCount" + listCount + "adapter.getCount " + adapter.getCount());
        if (listCount > adapter.getCount()) {
            tempCurpage = cpage;
            cpage++;
            pullStatus = PULL_LOAD;
            loadNetData();
        } else {
            ToastManager.showToastReal("没有更多数据");
            pullToFootRefreshView.onFooterRefreshComplete();
        }

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView pullToFootRefreshView) {
        tempCurpage = cpage;
        cpage = 1;
        pullStatus = PULL_REFRESH;
        loadNetData();
    }


    public class ListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listData == null ? 0 : listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.adapter_order_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            OrderWaitResult.DataEntity.OrderListEntity.ListEntity listEntity = listData.get(position);
            viewHolder.tvOrderNumber.setText(listEntity.getOrderNum());
            viewHolder.idCusName.setText(listEntity.getCustomerName());
            viewHolder.idStartDate.setText(listEntity.getOrderDate());
            viewHolder.idEndDate.setText(listEntity.getModifyDate());
            viewHolder.idRemarks.setText(listEntity.getOtherInfo());
            viewHolder.tvTotalAmount.setText("参考总价 " + listEntity.getTotalPrice());
            viewHolder.idTvNeed.setText("定金 " + listEntity.getNeedPayPrice());
            CustomTypeListViewAdapter customListViewAdapter = new CustomTypeListViewAdapter(listEntity.getPicsEntity());
            viewHolder.customListView.setDividerHeight(10);
            viewHolder.customListView.setDividerWidth(10);
            // CustomTypeListViewAdapter1 customListViewAdapter1 = new CustomTypeListViewAdapter1(listEntity.getPicsEntity(),0);
            viewHolder.customListView.setAdapter(customListViewAdapter);

            return convertView;
        }


        class ViewHolder {
            @Bind(R.id.tv_order_number)
            TextView tvOrderNumber;
            @Bind(R.id.tv_situation)
            TextView tvSituation;
            @Bind(R.id.id_cus_name)
            TextView idCusName;
            @Bind(R.id.id_start_date)
            TextView idStartDate;
            @Bind(R.id.id_end_date)
            TextView idEndDate;
            @Bind(R.id.id_remarks)
            TextView idRemarks;
            @Bind(R.id.inner_lny_container)
            LinearLayout innerLnyContainer;
            @Bind(R.id.tv_total_amount)
            TextView tvTotalAmount;
            @Bind(R.id.sexangleView)
            CustomListView customListView;
            @Bind(R.id.linear_container_view)
            LinearLayout linearContainerView;
            @Bind(R.id.tv_need_amount)
            TextView idTvNeed;


            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }


        public class CustomTypeListViewAdapter extends CustomAdapter {

            private List<String> pic;

            public CustomTypeListViewAdapter(List<String> pic) {
                this.pic = pic;
            }


            @Override
            public int getCount() {
                return pic.size();
            }

            @Override
            public String getItem(int position) {
                return pic.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setBackgroundColor(Color.WHITE);
                int padding = UIUtils.dip2px(15);
                imageView.setPadding(padding, padding, padding, padding);
                // txt.setImageResource(getItem(position));
                ImageLoader.getInstance().displayImage(getItem(position), imageView, ImageLoadOptions.getOptions());
                imageView.setBackgroundResource(R.drawable.gv_selector);
                return imageView;
            }

        }

        public class CustomTypeListViewAdapter1 extends CommonAdapter {

            public CustomTypeListViewAdapter1(List<String> mDatas, int itemLayoutId) {
                super(mDatas, itemLayoutId);
            }

            @Override
            public void convert(BaseViewHolder helper, Object item) {
                //TextView textView = helper.getView(R.id.tv_total_amount);
                //view.setText(item);
                helper.setText(R.id.id_tv_title, "");
            }
        }
    }
}
