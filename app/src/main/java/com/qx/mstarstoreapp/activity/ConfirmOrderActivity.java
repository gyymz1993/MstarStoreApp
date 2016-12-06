package com.qx.mstarstoreapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.Scrollable.CanScrollVerticallyDelegate;
import com.qx.mstarstoreapp.Scrollable.OnFlingOverListener;
import com.qx.mstarstoreapp.Scrollable.OnScrollChangedListener;
import com.qx.mstarstoreapp.Scrollable.ScrollableLayout;
import com.qx.mstarstoreapp.Scrollable.TabsLayout;
import com.qx.mstarstoreapp.base.AppURL;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.base.BaseApplication;
import com.qx.mstarstoreapp.bean.Type;
import com.qx.mstarstoreapp.inter.AdapterCallBack;
import com.qx.mstarstoreapp.json.AddressEntity;
import com.qx.mstarstoreapp.json.CustomerEntity;
import com.qx.mstarstoreapp.json.OrderListResult;
import com.qx.mstarstoreapp.json.PriceResult;
import com.qx.mstarstoreapp.net.ImageLoadOptions;
import com.qx.mstarstoreapp.net.OKHttpRequestUtils;
import com.qx.mstarstoreapp.net.VolleyRequestUtils;
import com.qx.mstarstoreapp.utils.L;
import com.qx.mstarstoreapp.utils.StringUtils;
import com.qx.mstarstoreapp.utils.ToastManager;
import com.qx.mstarstoreapp.viewutils.CustomSelectButton;
import com.qx.mstarstoreapp.viewutils.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/9/23 15:35
 * @version     确认订单页面
 *
 */
public class ConfirmOrderActivity extends BaseActivity implements PullToRefreshView.OnFooterRefreshListener,
        PullToRefreshView.OnHeaderRefreshListener, CanScrollVerticallyDelegate,
        OnFlingOverListener, AdapterCallBack {

    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.id_lay_address)
    RelativeLayout layAddress;
    @Bind(R.id.ck_checkall)
    CheckBox ckCheckall;
    @Bind(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @Bind(R.id.bt_go_pay)
    Button btGoPay;
    @Bind(R.id.id_rel_title)
    RelativeLayout idRelTitle;
    @Bind(R.id.id_tv1)
    TextView idTv1;
    @Bind(R.id.id_tv_name)
    TextView idTvName;
    @Bind(R.id.id_tv_address)
    TextView idTvAddress;
    @Bind(R.id.id_cs_color)
    CustomSelectButton idCsColor;
    @Bind(R.id.id_cs_mass)
    CustomSelectButton idCsMass;
    @Bind(android.R.id.list)
    ListView list;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshView pullRefreshView;
    @Bind(R.id.rel_shopping_car_bottom_action)
    RelativeLayout relShoppingCarBottomAction;
    /*选择成色的ItME*/
    List<OrderListResult.DataEntity.ModelColorEntity> modelColorItme;
    /*选择质量等级的Item*/
    List<OrderListResult.DataEntity.ModelQualityEntity> modelQualityItem;
    List<OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> listData;
    @Bind(R.id.id_lay_order_detail)
    LinearLayout idLayOrderDetail;
    @Bind(R.id.id_et_seach)
    EditText idEtSeach;
    @Bind(R.id.id_view_line)
    View idViewLine;
    @Bind(R.id.ig_btn_seach)
    ImageView igBtnSeach;
    @Bind(R.id.id_rl1)
    RelativeLayout idRl1;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.lny_loading_layout)
    LinearLayout lnyLoadingLayout;
    AddressEntity isDefaultAddress;
    CustomerEntity isDefaultCustomer;
    @Bind(R.id.id_ed_word)
    EditText idEdWord;
    @Bind(R.id.id_lay_price1)
    LinearLayout idLayPrice1;
    @Bind(R.id.id_tv_need_price)
    TextView idTvNeedPrice;
    @Bind(R.id.id_tv_total_price)
    TextView idTvTotalPrice;
    @Bind(R.id.id_lay_price2)
    LinearLayout idLayPrice2;
    @Bind(R.id.scrollable_layout)
    ScrollableLayout scrollableLayout;
    @Bind(R.id.id_cancle_order)
    Button idCancleOrder;
    protected ListView lv_list;
    private int tempCurpage = 1;
    private int pullState = 1;
    private int curpage = 1;
    private ConfirOrderAdapter confirOrderAdapter;
    private int type = 0;   //type=2表示审核订单中过来的页面
    private String orderId;
    private boolean isFirst;
    private int list_count = -1;
    private static final int PULL_REFRESH = 1;
    private static final int PULL_LOAD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        ButterKnife.bind(this);
        getActivityType();
        initView();
        initScroll();
        loadNetData();
        isFirst = true;
        L.e("ConfirmOrderActivity onCreate");
    }


    @Override
    protected void onResume() {
        super.onResume();
        L.e("ConfirmOrderActivity  onResume");
    }

    public int getActivityType() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return 0;
        }
        type = bundle.getInt("type");
        orderId = bundle.getString("id");
        return type;
    }


    private void loadNetData() {
        String url;
        if (type == 2) {
            url = AppURL.URL_ORDER_DETAIL + "tokenKey=" + BaseApplication.getToken() + "&cpage=" + curpage + "&orderId=" + orderId;
        } else {
            url = AppURL.URL_ORDER_LIST + "tokenKey=" + BaseApplication.getToken() + "&cpage=" + curpage;
        }
        L.e("获取订单信息" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                lnyLoadingLayout.setVisibility(View.GONE);
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    OrderListResult orderListResult = new Gson().fromJson(result, OrderListResult.class);
                    if (pullState != PULL_LOAD) {
                        confirOrderAdapter.cancelAll();
                        listData.clear();
                    }
                    OrderListResult.DataEntity dataEntity = orderListResult.getData();
                    OrderListResult.DataEntity.CurrentOrderlListEntity currentOrderlList = dataEntity.getCurrentOrderlList();
                    list_count = Integer.valueOf(currentOrderlList.getList_count());
                    if (curpage == 1) {
                        modelColorItme = dataEntity.getModelColor();
                        modelQualityItem = dataEntity.getModelQuality();
                    }
                    if (list_count == 0) {
                        ToastManager.showToastReal("没有数据");
                    } else {
                        List<OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> list = currentOrderlList.getList();
                        listData.addAll(list);
                    }
                    if (isFirst) {
                        isDefaultAddress = dataEntity.getAddress();
                        if (isDefaultAddress != null) {
                            idTvName.setText(isDefaultAddress.getName());
                            idTvAddress.setText(isDefaultAddress.getAddr());
                            phoneTv.setText(isDefaultAddress.getPhone());
                        } else if (type == 2) {
                            OrderListResult.DataEntity.OrderInfoEntity orderInfo = dataEntity.getOrderInfo();
                            Double totalPrice = dataEntity.getTotalPrice();
                            Double totalNeedPayPrice = dataEntity.getTotalNeedPayPrice();
                            L.e("orderInfo" + orderInfo.toString() + "totalPrice:" + totalPrice + "totalNeedPayPrice:" + totalNeedPayPrice);
                            idCsColor.setTextName(orderInfo.getPurityName());
                            idCsMass.setTextName(orderInfo.getQualityName());
                            idEdWord.setText(orderInfo.getWord());
                            idEtSeach.setText(orderInfo.getCustomerName());
                            tvTotalPrice.setText(totalNeedPayPrice + "--定金   " + totalPrice + "(合计)");
                            idTvNeedPrice.setText("定金 :" + totalNeedPayPrice);
                            idTvTotalPrice.setText("合计 :" + totalPrice);
                        } else {
                            isDefaultCustomer = dataEntity.getCustomer();
                            if (isDefaultCustomer != null) {
                                idEtSeach.setText(isDefaultCustomer.getCustomerName());
                            }
                        }
                        isFirst = false;
                    }
                    if (priceList != null && priceList.size() != 0) {
                        for (int i = 0; i < priceList.size(); i++) {
                            for (int j = 0; j < listData.size(); j++) {
                                if (listData.get(j).getId().equals(priceList.get(i).getId())) {
                                    listData.get(j).setPrice(priceList.get(i).getPrice() + "");
                                }
                            }
                        }
                    }
                    if (type==2){
                        btGoPay.setVisibility(View.INVISIBLE);
                        idLayOrderDetail.setVisibility(View.VISIBLE);
                        idLayPrice1.setVisibility(View.INVISIBLE);
                        idLayPrice2.setVisibility(View.VISIBLE);
                    }else {
                        btGoPay.setVisibility(View.VISIBLE);
                        idLayOrderDetail.setVisibility(View.INVISIBLE);
                        idLayPrice1.setVisibility(View.VISIBLE);
                        idLayPrice2.setVisibility(View.INVISIBLE);
                    }
                    L.e("解析成功");
                    endNetRequest();
                    initListener();
                    L.e("mchecked" + mchecked.size());
                    Set<Map.Entry<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity>> entries = mchecked.entrySet();
                    for (Map.Entry<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> entry : entries) {
                        OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity entity = entry.getValue();
                        L.e(entity.toString());
                    }
                    changeState(mchecked);
                }
                if (error == 2) {
                    loginToServer(OrderActivity.class);
                }

            }

            @Override
            public void onFail(String fail) {

            }

        });
    }


    private void initListener() {
        if (idCsColor != null)
            idCsColor.setOnSelectData(new CustomSelectButton.OnselectData() {
                @Override
                public List<Type> getData() {
                    List<Type> list = new ArrayList<>();
                    for (int i = 0; i < modelColorItme.size(); i++) {
                        Type type = new Type();
                        type.setId(modelColorItme.get(i).getId());
                        type.setTypeName(modelColorItme.get(i).getTitle());
                        list.add(type);
                    }
                    return list;
                }

                @Override
                public void getSelectId(Type type) {
                    idCsColor.setText(type.getTypeName());
                    purityId = type.getId();
                    loadPrice(qualityId, purityId);
                }

                @Override
                public String getTitle() {
                    return "选择成色";
                }
            });

        if (idCsMass != null)
            idCsMass.setOnSelectData(new CustomSelectButton.OnselectData() {
                @Override
                public List<Type> getData() {
                    List<Type> list = new ArrayList<>();
                    for (int i = 0; i < modelQualityItem.size(); i++) {
                        Type type = new Type();
                        type.setId(modelQualityItem.get(i).getId());
                        type.setTypeName(modelQualityItem.get(i).getTitle());
                        list.add(type);
                    }
                    return list;
                }

                @Override
                public void getSelectId(Type type) {
                    idCsMass.setText(type.getTypeName());
                    qualityId = type.getId();
                    loadPrice(qualityId, purityId);
                }

                @Override
                public String getTitle() {
                    return "选择质量等级";
                }
            });


        ckCheckall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ckCheckall.isChecked()) {
                    confirOrderAdapter.selectAll();
                } else {
                    confirOrderAdapter.cancelAll();
                }
                confirOrderAdapter.notifyDataSetChanged();
            }
        });



        /*搜索用户时间  失去焦点*/
        idEtSeach.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (!StringUtils.isEmpty(idEtSeach.getText().toString()))
                        seachCustom(idEtSeach.getText().toString());
                }
            }
        });
        idEdWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    updateCustomorWord(idEtSeach.getText().toString(), idEdWord.getText().toString());
                }
            }
        });
        /*确定支付*/
        btGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openActivity(ModeOfPaymentActivity.class, null);
                submitOrder();
            }
        });


        /*选择地址*/
        layAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openActivity(ShopingAddressActivity.class,null);
                Intent intent = new Intent(ConfirmOrderActivity.this, ShopingAddressActivity.class);
                startActivityForResult(intent, 12);
            }
        });

        /*取消订单*/
        idCancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancalOrder();
            }
        });

    }

    /*
    * @version  取消订单
    */
    public void cancalOrder() {
        // ModelOrderWaitCheckCancelDo?orderId=10&tokenKey=10b588002228fa805231a59bb7976bf4
        String url = AppURL.URL_ORDER_WAIT_CANCEL + "tokenKey=" + BaseApplication.getToken() + "&orderId=" + orderId;
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error==0) {
                    ToastManager.showToastReal("提交成功");
                    finish();
                }
                if (error==2) {
                    loginToServer(OrderActivity.class);
                }
            }

            @Override
            public void onFail(String fail) {

            }

        });


    }


    List<PriceResult.DataEntity.PriceListEntity> priceList;

    public void loadPrice(String qualityId, String purityId) {
        String url;
        if (type == 2) {
            //ModelOrderWaitCheckGetOrderPricePageListDo?purityId=1&qualityId=1&orderId=13&tokenKey=10b588002228fa805231a59bb7976bf4
            url = AppURL.URL_WATI_ORDER_PRICE + "tokenKey=" + BaseApplication.getToken() + "&purityId=" + purityId +
                    "&qualityId=" + qualityId + "&orderId=" + orderId;
        } else {
            url = AppURL.URL_ORDER_PRICE + "tokenKey=" + BaseApplication.getToken() + "&purityId=" + purityId +
                    "&qualityId=" + qualityId + "&cpage=" + curpage;
        }
        L.e("价格变化" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    PriceResult priceResult = new Gson().fromJson(result, PriceResult.class);
                    priceList = priceResult.getData().getPriceList();
                    if (priceList != null && priceList.size() != 0) {
                        for (int i = 0; i < priceList.size(); i++) {
                            for (int j = 0; j < listData.size(); j++) {
                                if (listData.get(j).getId().equals(priceList.get(i).getId())) {
                                    listData.get(j).setPrice(priceList.get(i).getPrice() + "");
                                }
                            }
                        }
                    }
                    endNetRequest();
                    if (type == 2) {
                        Double total = .0;
                        Double needPrice = .0;
                        for (OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity cartItem : listData) {
                            total += Double.valueOf(cartItem.getPrice()) * Double.valueOf(cartItem.getNumber());
                            needPrice += Double.valueOf(cartItem.getNeedPayPrice()) * Double.valueOf(cartItem.getNumber());
                            L.e("needPrice" + needPrice);
                        }
                        //价格
                        tvTotalPrice.setText(StringUtils.formatedPrice(total));
                        idLayPrice1.setVisibility(View.INVISIBLE);
                        idLayPrice2.setVisibility(View.VISIBLE);
                        idTvNeedPrice.setText("定金 :" + needPrice);
                        idTvTotalPrice.setText("合计 :" + total);
                    } else {
                        confirOrderAdapter.updatePrice();
                    }
                }
                if (error == 2) {
                    loginToServer(StyleInfromationActivity.class);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }


    public void onBack(View view) {
        finish();
    }


    protected void initView() {
        lnyLoadingLayout.setVisibility(View.VISIBLE);
        listData = new ArrayList<>();
        if (type == 2) {
            ckCheckall.setVisibility(View.GONE);
            titleText.setText("订单确认");
            idCancleOrder.setText("取消订单");
        } else {
            titleText.setText("确认订单");
        }
        lv_list = (ListView) findViewById(android.R.id.list);
        confirOrderAdapter = new ConfirOrderAdapter();
        confirOrderAdapter.setOnAdapterCallBack(this);
        lv_list.setEmptyView(findViewById(R.id.lny_no_result));
        lv_list.setAdapter(confirOrderAdapter);
        pullRefreshView.setOnFooterRefreshListener(this);
        pullRefreshView.setOnHeaderRefreshListener(this);

        StyleInfromationActivity.setOnRefreshOrderListener(new StyleInfromationActivity.OnRefreshOrderListener() {
            @Override
            public void onrefresh() {
                loadNetData();
            }
        });
        View view=LayoutInflater.from(this).inflate(R.layout.order_header_view,null);
        lv_list.addHeaderView(view);
    }

    //OrderCurrentSubmitDo?itemId=1|2|3&addressId=1&purityId=3&qualityId=2&tokenKey=944df2f27ffce557042887589986c193
    String purityId, qualityId;
    List<String> mcheckId;


    /*
     * @version  提交订单
     */
    public void submitOrder() {
        String url = AppURL.URL_ORDER_SUBMIT + "&tokenKey=" + BaseApplication.getToken();
        String word = idEdWord.getText().toString();
        String addressId = isDefaultAddress.getId();
        String customerID = isDefaultCustomer.getCustomerID() + "";
        String itemurl = "";
        if (mcheckId == null || mcheckId.size() == 0) {
            ToastManager.showToastReal("请选择订单");
        } else {
            itemurl = StringUtils.purUrlCut("itemId", mcheckId).toString();
        }
        L.e("word" + word + "   addressId" + addressId + "");
        if (StringUtils.isEmpty(word) && StringUtils.isEmpty(addressId) && StringUtils.isEmpty(customerID) && StringUtils.isEmpty(itemurl)) {
            ToastManager.showToastReal("请填写完整资料");
            return;
        }
        url += itemurl + "&word=" + word + "&purityId=" + purityId + "&addressId=" + addressId + "&qualityId=" + qualityId + "&customerID=" + customerID;
        L.e("submitOrder" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack(){
            @Override
            public void onSuccess(String result) {
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    ToastManager.showToastReal("提交成功");
                    loadNetData();
                }
                if (error == 2) {
                    loginToServer(OrderActivity.class);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }


    /*
     * @version  搜索时候有客户
     */
    private void seachCustom(final String keyWord) {
        String url = AppURL.URL_HAVE_CUSTOMER + "tokenKey=" + BaseApplication.getToken() + "&keyword=" + keyWord;
        //keyword=广西|平果&tokenKey=944df2f27ffce557042887589986c193
        L.e("seachCustom" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack(){
            @Override
            public void onSuccess(String result) {
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
                    JsonObject jsonObject = jsonResult.get("data").getAsJsonObject();
                    int state = jsonObject.get("state").getAsInt();
                    if (state == 0) {
                        ToastManager.showToastReal("没有此客户");
                    }
                    if (state == 1) {
                        ToastManager.showToastReal("有此客户");
                        updateCustomorWord(keyWord, idEdWord.getText().toString());
                    }
                    if (state == 2) {
                        Intent intent = new Intent(ConfirmOrderActivity.this, CustomersListActivity.class);
                        intent.putExtra("keyWord", keyWord);
                        startActivityForResult(intent, 11);
                    }
                }
                if (error == 2) {
                    loginToServer(OrderActivity.class);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }

    /*
     * @version  修改客户和自印
     */
    public void updateCustomorWord(String cus, String word) {
        // ModelOrderWaitCheckDetailModifyInfoDo?orderId=10&customerId=2990&tokenKey=10b588002228fa805231a59bb7976bf4
        String url = "";
        if (type != 2) {
            return;
        }
        url = AppURL.URL_WATI_ORDER_MODIINFO + "tokenKey=" + BaseApplication.getToken() + "&orderId=" + orderId +
                "&customerId=" + cus + "&word=" + word;
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                L.e(result);
                if (error == 0) {
                    ToastManager.showToastReal("更新成功");
                }
                if (error == 2) {
                    loginToServer(StyleInfromationActivity.class);
                }
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }


    Map<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> mchecked = new HashMap<>();

    @Override
    public void changeState(Map<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> checked) {
        List<OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> checkedGoods = new ArrayList<>();
        if (confirOrderAdapter.isAllSelected()) {
            ckCheckall.setChecked(true);
        } else {
            ckCheckall.setChecked(false);
        }
        mchecked.putAll(checked);
        Set<Map.Entry<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity>> entries = checked.entrySet();
        for (Map.Entry<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> entry : entries) {
            OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity entity = entry.getValue();
            checkedGoods.add(entity);
        }
        Double total = .0;
        for (OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity cartItem : checkedGoods) {
            total += Double.valueOf(cartItem.getPrice()) * Double.valueOf(cartItem.getNumber());
        }
        //价格
        tvTotalPrice.setText(StringUtils.formatedPrice(total));
        if (checkedGoods.size() == 0) {
            btGoPay.setEnabled(false);
        } else {
            btGoPay.setEnabled(true);
        }
        if (checkedGoods.size() != 0) {
            mcheckId = new ArrayList<>();
            for (int i = 0; i < checkedGoods.size(); i++) {
                mcheckId.add(checkedGoods.get(i).getId());
            }
        }
    }


    private void endNetRequest() {
        ckCheckall.setChecked(confirOrderAdapter.isAllSelected());
        //changeState(mchecked);
        confirOrderAdapter.notifyDataSetChanged();
        tempCurpage = curpage;
        if (pullState == PULL_LOAD) {
            pullRefreshView.onFooterRefreshComplete();
        } else if (pullState == PULL_REFRESH) {
            pullRefreshView.onHeaderRefreshComplete();
        }
        pullState = 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e("onActivityResult" + requestCode);
        if (data == null) {
            return;
        }
        if (requestCode == 11) {
            String customerName = data.getStringExtra("CustomerName");
            int customerID = data.getIntExtra("CustomerID", -1);
            idEtSeach.setText(customerName);
            if (isDefaultCustomer == null) {
                isDefaultCustomer = new CustomerEntity();
            }
            isDefaultCustomer.setCustomerID(customerID);
            updateCustomorWord(customerID + "", idEdWord.getText().toString());
        }
        if (requestCode == 12) {
            String phoneNumber = data.getStringExtra("phoneNumber");
            String name = data.getStringExtra("name");
            String address = data.getStringExtra("address");
            String addressId = data.getStringExtra("addressId");

            /*设置默认地址ID*/
            if (isDefaultAddress == null) {
                isDefaultAddress = new AddressEntity();
            }
            isDefaultAddress.setId(addressId);
            idTvName.setText(name);
            idTvAddress.setText(address);
            phoneTv.setText(phoneNumber);
        }
    }


    private static final String ARG_LAST_SCROLL_Y = "arg.LastScrollY";
    private ScrollableLayout mScrollableLayout;
    private TabsLayout tabs;

    public void initScroll() {
        final View header = findViewById(R.id.header);
        tabs = (TabsLayout) findViewById(R.id.tabs);
        tabs.setViewPager();
        mScrollableLayout = (ScrollableLayout) findViewById(R.id.scrollable_layout);
        mScrollableLayout.setDraggableView(tabs);
        mScrollableLayout.setCanScrollVerticallyDelegate(this);
        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {

                final float tabsTranslationY;
                if (y < maxY) {
                    tabsTranslationY = .0F;
                } else {
                    tabsTranslationY = y - maxY;
                }
                tabs.setTranslationY(tabsTranslationY);
                header.setTranslationY(y / 2);
            }
        });
    }

    @Override
    public boolean canScrollVertically(int direction) {
        boolean isSroll = (lv_list != null && lv_list.canScrollVertically(direction));
        if (isSroll) {
            tabs.setImageView(R.drawable.icon_down);
        } else {
            tabs.setImageView(R.drawable.icon_up);
        }
        return isSroll;
    }

    @Override
    public void onFlingOver(int y, long duration) {
        if (lv_list != null) {
            lv_list.smoothScrollBy(y, (int) duration);
        }
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (listData.size() < list_count) {
            tempCurpage = curpage;
            curpage++;
            pullState = PULL_LOAD;
            loadNetData();
        } else {
            ToastManager.showToastReal("没有更多数据");
            view.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        tempCurpage = curpage;
        curpage = 1;
        pullState = PULL_REFRESH;
        loadNetData();
    }


    public class ConfirOrderAdapter extends BaseAdapter {
        private Map<String, OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity> checkedState;
        public ConfirOrderAdapter() {
            checkedState = new HashMap<>();
            L.e("ConfirOrderAdapter");
        }


        public void selectAll() {
            for (OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity t : listData) {
                checkedState.put(t.getId(), t);
            }
            if (callBack != null)
                callBack.changeState(checkedState);
        }

        public void cancelAll() {
            checkedState.clear();
            if (callBack != null)
                callBack.changeState(checkedState);
        }

        public void updatePrice() {
            if (callBack != null)
                callBack.changeState(checkedState);
        }


        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity getItem(int i) {
            return listData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(ConfirmOrderActivity.this).inflate(R.layout.layout_order, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            if (type == 2) {
                vh.ckCheckone.setVisibility(View.GONE);
            }

            OrderListResult.DataEntity.CurrentOrderlListEntity.ListEntity listEntity = listData.get(position);
            if (checkedState.get(getItem(position).getId()) != null) {
                vh.ckCheckone.setChecked(true);
            } else {
                vh.ckCheckone.setChecked(false);
            }
            vh.ckCheckone.setTag(position);
            vh.ckCheckone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkedState.get(getItem(position).getId()) != null) {
                        checkedState.remove(getItem(position).getId());
                    } else {
                        checkedState.put(getItem(position).getId(), getItem(position));
                    }
                    if (callBack != null)
                        callBack.changeState(checkedState);
                }
            });
            vh.productName.setText(listEntity.getTitle());
            vh.productPrice.setText(listEntity.getPrice());
            vh.idTvInformation.setText(listEntity.getInfo());
            vh.productNorms.setText(listEntity.getBaseInfo());
            vh.productNumber.setText(listEntity.getNumber());
            vh.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.putExtra("id", listData.get(position).getId());
//                    setResult(11, intent);
//                    finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", listData.get(position).getId());
                    if (type == 2) {
                        // ModelOrderWaitCheckDetailModifyInfoDo?orderId=10&customerId=2990&tokenKey=10b588002228fa805231a59bb7976bf4
                        bundle.putInt("type", 2);
                    } else {
                        bundle.putInt("type", 1);
                    }
                    openActivity(StyleInfromationActivity.class, bundle);

                }
            });
            ImageLoader.getInstance().displayImage(listData.get(position).getPic(), vh.productImg, ImageLoadOptions.getOptions());
            vh.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteOrder(listData.get(position).getId());
                }
            });
            return convertView;
        }

        private AdapterCallBack callBack;

        public void setOnAdapterCallBack(AdapterCallBack callBack) {
            this.callBack = callBack;
        }

        public boolean isAllSelected() {
            return checkedState.size() == listData.size() && listData.size() != 0;
        }

        class ViewHolder {
            @Bind(R.id.ck_checkone)
            CheckBox ckCheckone;
            @Bind(R.id.btn_edit)
            TextView btnEdit;
            @Bind(R.id.btn_delete)
            TextView btnDelete;
            @Bind(R.id.item_button_layout)
            RelativeLayout itemButtonLayout;
            @Bind(R.id.line)
            View line;
            @Bind(R.id.product_img)
            ImageView productImg;
            @Bind(R.id.product_name)
            TextView productName;
            @Bind(R.id.product_norms)
            TextView productNorms;
            @Bind(R.id.product_price)
            TextView productPrice;
            @Bind(R.id.product_number)
            TextView productNumber;
            @Bind(R.id.id_oder_type)
            TextView idOderType;
            @Bind(R.id.id_lay1)
            LinearLayout idLay1;
            @Bind(R.id.id_view_line)
            View idViewLine;
            @Bind(R.id.id_tv_information)
            TextView idTvInformation;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        /*
         * @version    删除订单
         *
         */
        public void deleteOrder(String id) {
            String url;
            if (type == 2) {
                if (listData.size() <= 1) {
                    ToastManager.showToastReal("请点击取消订单");
                    return;
                }
                url = AppURL.URL_ORDER_WAIT_DELETE + "&tokenKey=" + BaseApplication.getToken() + "&itemId=" + id;
            } else {
                url = AppURL.URL_ORDER_DELETE + "&tokenKey=" + BaseApplication.getToken() + "&itemId=" + id;
            }
            L.e("删除订单" + url);
            VolleyRequestUtils.getInstance().getCookieRequest(ConfirmOrderActivity.this, url, new VolleyRequestUtils.HttpStringRequsetCallBack(){
                @Override
                public void onSuccess(String result) {
                    L.e(result);
                    int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                    if (error == 0) {
                        ToastManager.showToastReal("删除成功");
                        loadNetData();
                    }
                    if (error == 2) {
                        loginToServer(OrderActivity.class);
                    }
                }

                @Override
                public void onFail(String fail) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.requestQueue.cancelAll(this);
    }
}
