package com.qx.mstarstoreapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.AppURL;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.base.BaseApplication;
import com.qx.mstarstoreapp.bean.Type;
import com.qx.mstarstoreapp.json.ModelDetailResult;
import com.qx.mstarstoreapp.net.ImageLoadOptions;
import com.qx.mstarstoreapp.net.OKHttpRequestUtils;
import com.qx.mstarstoreapp.net.VolleyRequestUtils;
import com.qx.mstarstoreapp.utils.L;
import com.qx.mstarstoreapp.utils.StringUtils;
import com.qx.mstarstoreapp.utils.ToastManager;
import com.qx.mstarstoreapp.viewutils.CustomLV;
import com.qx.mstarstoreapp.viewutils.CustomSelectButton;
import com.qx.mstarstoreapp.viewutils.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/10/19 9:37
 * @version  款式信息
 *
 */
public class StyleInfromationActivity extends BaseActivity implements View.OnClickListener {


    List<ModelDetailResult.DataEntity.ModelEntity.StoneEntity> stoneEntities = new ArrayList<>();
    ModelDetailResult.DataEntity.ModelEntity.StoneEntity stone;
    ModelDetailResult.DataEntity.ModelEntity.StoneAEntity stoneA;
    ModelDetailResult.DataEntity.ModelEntity.StoneBEntity stoneB;
    ModelDetailResult.DataEntity.ModelEntity.StoneCEntity stoneC;


    List<ModelDetailResult.DataEntity.StoneTypeEntity> stoneTypeItme; //类型
    List<ModelDetailResult.DataEntity.StoneColorEntity> stoneColorItme;  //颜色
    List<ModelDetailResult.DataEntity.StonePurityEntity> stonePurityItme; //净度
    List<ModelDetailResult.DataEntity.StoneSpecEntity> stoneSpecItme;  //规格
    List<ModelDetailResult.DataEntity.StoneShapeEntity> stoneShapeItem;  //形状
    List<ModelDetailResult.DataEntity.ModelEntity.PicsEntity> pics;
    ModelDetailResult.DataEntity.ModelEntity modelEntity;
    List<ModelDetailResult.DataEntity.RemarksEntity> remarksEntity;//备注
    Type remar = new Type();  //提交的

    @Bind(R.id.id_list)
    CustomLV listView;
    String id;
    ListAdapter adapter;
    LinearLayout lymenus;
    @Bind(R.id.id_store_title)
    TextView idStoreTitle;
    @Bind(R.id.id_store_information)
    TextView idStoreInformation;
    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.id_rel_title)
    RelativeLayout idRelTitle;
    @Bind(R.id.id_menus)
    LinearLayout idMenus;
    @Bind(R.id.id_cus_store_type)
    CustomSelectButton idCusStoreType;
    @Bind(R.id.id_tv_curorder)
    TextView idTvCurorder;

    @Bind(R.id.id_tv_add_order)
    TextView idTvAddOrder;
    @Bind(R.id.id_cus_store_number)
    EditText idCusStoreNumber;
    @Bind(R.id.id_cus_store_size)
    EditText idCusStoreSize;

    String categoryTitle, storeNumber, storeSize, categoryId;
    @Bind(R.id.id_cus_store_remarkid)
    CustomSelectButton idCusStoreRemarkid;
    @Bind(R.id.id_tv_store_remarks)
    EditText idTvStoreRemarks;

    @Bind(R.id.id_gv_image)
    MyGridView idGvImage;
    @Bind(R.id.lny_loading_layout)
    LinearLayout lnyLoadingLayout;

    int type = 0;

   public static   OnRefreshOrderListener onRefreshOrderListener;
    public interface OnRefreshOrderListener{
        void  onrefresh();
    }

    public static void setOnRefreshOrderListener(OnRefreshOrderListener onRefreshOrderListener) {
        StyleInfromationActivity.onRefreshOrderListener = onRefreshOrderListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_information);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        loadNetData();
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        type = extras.getInt("type");
    }


    private void initView() {
        titleText.setText("款式信息");
        lnyLoadingLayout.setVisibility(View.VISIBLE);
        idTvAddOrder.setText("添加到当前下单");
        idTvCurorder.setText("查看当前下单");
        if (type == 1 || type == 2) {
            idTvAddOrder.setText("确定");
            idTvCurorder.setText("取消");
        }
        lymenus = (LinearLayout) findViewById(R.id.id_menus);
        idTvAddOrder.setOnClickListener(this);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);
    }


    private void loadNetData() {
        String url;
        if (type == 1) {
            url = AppURL.URL_MODEL_UPDATE + "tokenKey=" + BaseApplication.getToken() + "&itemId=" + id;
        } else if (type == 2) {
            url = AppURL.URL_ORDER_MODEL_UPDATE + "tokenKey=" + BaseApplication.getToken() + "&itemId=" + id;
        } else {
            url = AppURL.URL_MODEL_DETAIL + "tokenKey=" + BaseApplication.getToken() + "&id=" + id;
            //款号页面
        }
        L.e("获取款号" + url + "Type   " + type);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                stoneEntities.clear();
                lnyLoadingLayout.setVisibility(View.GONE);
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    ModelDetailResult modelDetail = new Gson().fromJson(result, ModelDetailResult.class);
                    ModelDetailResult.DataEntity dataEntity = modelDetail.getData();
                    List<ModelDetailResult.DataEntity.GoldenPriceEntity> goldenPrice = dataEntity.getGoldenPrice();
                    remarksEntity = dataEntity.getRemarks();
                    modelEntity = dataEntity.getModel();
                    categoryTitle = modelEntity.getCategoryTitle();
                    categoryId = modelEntity.getCategoryId();
                    pics = modelEntity.getPics();
                    stone = modelEntity.getStone();
                    stoneA = modelEntity.getStoneA();
                    stoneB = modelEntity.getStoneB();
                    stoneC = modelEntity.getStoneC();
                    stoneEntities.add(stone);
                    stoneEntities.add(stoneA);
                    stoneEntities.add(stoneB);
                    stoneEntities.add(stoneC);
                    stoneTypeItme = dataEntity.getStoneType();
                    stoneColorItme = dataEntity.getStoneColor();
                    stonePurityItme = dataEntity.getStonePurity();
                    stoneSpecItme = dataEntity.getStoneSpec();
                    stoneShapeItem = dataEntity.getStoneShape();
                    setStorePrice(stone);
                    setStorePrice(stoneA);
                    setStorePrice(stoneB);
                    setStorePrice(stoneC);
                    L.e("categoryTitle" + categoryTitle);
                    idStoreTitle.setText(modelEntity.getTitle());
                    idCusStoreType.setTextName(categoryTitle);
                    // initView();
                    //layoutBtns();
                    initGvImage();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < goldenPrice.size(); i++) {
                        sb.append(goldenPrice.get(i).getTitle() + " " + goldenPrice.get(i).getPrice() + "      ");
                    }
                    idStoreInformation.setText(sb.toString());
                    adapter.notifyDataSetChanged();
                } else if (error == 2) {
                    ToastManager.showToastReal("数据错误");
                } else {
                    ToastManager.showToastReal("数据错误");
                }

            }

            @Override
            public void onFail(String fail) {

            }

        });


    }

    private void initGvImage() {
        final int mumber = pics.size();
        if (pics.size() < 4) {
            idGvImage.setNumColumns(pics.size());
        } else {
            idGvImage.setNumColumns(4);
        }

        idGvImage.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mumber;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolderGV viewHolder = null;
                if (view == null) {
                    viewHolder = new ViewHolderGV();
                    view = LayoutInflater.from(context).inflate(R.layout.item_gv_image, null);
                    viewHolder.ig = (ImageView) view.findViewById(R.id.btMenu);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolderGV) view.getTag();
                }
                ImageLoader.getInstance().displayImage(pics.get(i).getPic(), viewHolder.ig, ImageLoadOptions.getOptions());
                viewHolder.ig.setImageResource(R.drawable.no_image);
                viewHolder.ig.setBackgroundResource(R.drawable.selector_gridview_item);
                return view;
            }
        });


    }


    class ViewHolderGV {
        ImageView ig;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_add_order:
                confirmOrder();
                break;
        }
    }

    private void confirmOrder() {
        storeNumber = idCusStoreNumber.getText().toString();
        storeSize = idCusStoreSize.getText().toString();
        if (!StringUtils.isEmpty(categoryTitle) && !StringUtils.isEmpty(storeNumber) && !StringUtils.isEmpty(storeSize)) {
            if (objectisEmpty(stone) && objectisEmpty(stoneA) && objectisEmpty(stoneB) && objectisEmpty(stoneC)) {
                addCurentOrder();
            } else {
                ToastManager.showToastReal("请填写完整参数");
            }
        } else {
            ToastManager.showToastReal("请填写完整参数");
        }
    }


    private void addCurentOrder() {
        // OrderDoCurrentModelItemDo?productId=1&categoryId=8&number=2&handSize=3&stone=1|3|2|3|4|5&stoneA=1|2|2|3|4|5&
        // stoneB=1|2|3|3|4|5&stoneC=1|2|3|3|4|6&tokenKey=10b588002228fa805231a59bb7976bf4
        String url = "";
        String urlStroe = objectisEmpty("stone", stone);
        String urlStroeA = objectisEmpty("stoneA", stoneA);
        String urlStroeB = objectisEmpty("stoneB", stoneB);
        String urlStroeC = objectisEmpty("stoneC", stoneC);
        if (type == 1) {
            //OrderCurrentEditModelItemDo?itemId=1&number=2&handSize=8&stone=1|3|2|3|3|3&stoneA=1|2|2|3|3|3&stoneB=1|2|2|3|3|5&stoneC=1|2|3|3|3|3
            // &tokenKey=10b588002228fa805231a59bb7976bf4
            url = AppURL.URL_CURRENT_EDIT_ORDER + "tokenKey=" + BaseApplication.getToken() + "&itemId=" + id + "&number=" + storeNumber
                    + "&handSize=" + storeSize + urlStroe + urlStroeA + urlStroeB + urlStroeC;
        } else if (type == 2) {
            url = AppURL.URL_UPDATE_ORDER_WATET + "tokenKey=" + BaseApplication.getToken() + "&itemId=" + id + "&number=" + storeNumber
                    + "&handSize=" + storeSize + urlStroe + urlStroeA + urlStroeB + urlStroeC;
        } else {
            url = AppURL.URL_CURRENT_ORDER + "tokenKey=" + BaseApplication.getToken() + "&productId=" + id + "&categoryId=" + categoryId +
                    "&number=" + storeNumber + "&handSize=" + storeSize + urlStroe + urlStroeA + urlStroeB + urlStroeC;
        }
        L.e("提交订单" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e("提交订单 result" + result);
                if (new Gson().fromJson(result, JsonObject.class).get("error").getAsString().equals("0")) {
                    ToastManager.showToastReal("添加成功");
                    if (onRefreshOrderListener!=null){
                        onRefreshOrderListener.onrefresh();
                    }
                    //finish();
                }
            }

            @Override
            public void onFail(String fail) {

            }

        });


    }

    public boolean objectisEmpty(ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoEntity) {
        L.e("stoEntity" + stoEntity.toString());
        //全部为空
        if (StringUtils.isEmpty(stoEntity.getPrice()) && StringUtils.isEmpty(stoEntity.getNumber()) &&
                StringUtils.isEmpty(stoEntity.getPurityId()) && StringUtils.isEmpty(stoEntity.getColorId())
                && StringUtils.isEmpty(stoEntity.getTypeId()) && StringUtils.isEmpty(stoEntity.getSpecId())
                && StringUtils.isEmpty(stoEntity.getShapeId())) {
            return true;
        } else if (!StringUtils.isEmpty(stoEntity.getPrice()) && !StringUtils.isEmpty(stoEntity.getNumber()) &&
                !StringUtils.isEmpty(stoEntity.getPurityId()) && !StringUtils.isEmpty(stoEntity.getColorId())
                && !StringUtils.isEmpty(stoEntity.getTypeId()) && !StringUtils.isEmpty(stoEntity.getSpecId())
                && !StringUtils.isEmpty(stoEntity.getShapeId())) {
            return true;
        } else {
            return false;
        }
    }


    public String objectisEmpty(String key, ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoEntity) {
        List list = new ArrayList();
        if (!StringUtils.isEmpty(stoEntity.getPrice()) && !StringUtils.isEmpty(stoEntity.getNumber()) &&
                !StringUtils.isEmpty(stoEntity.getPurityId()) && !StringUtils.isEmpty(stoEntity.getColorId())
                && !StringUtils.isEmpty(stoEntity.getTypeId()) && !StringUtils.isEmpty(stoEntity.getSpecId())
                && !StringUtils.isEmpty(stoEntity.getShapeId())) {
            list.add(stoEntity.getTypeId());
            list.add(stoEntity.getSpecId());
            list.add(stoEntity.getShapeId());
            list.add(stoEntity.getColorId());
            list.add(stoEntity.getPurityId());
            list.add(stoEntity.getNumber());

        }
        return StringUtils.purUrlCut(key, list).toString();
    }

    public class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stoneEntities.size();
        }

        @Override
        public Object getItem(int i) {
            return stoneEntities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(StyleInfromationActivity.this).inflate(R.layout.item_styleinfromation, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            switch (i) {
                case 0:
                    viewHolder.idTvTitle.setText("主 石");
                    break;
                case 1:
                    viewHolder.idTvTitle.setText("副石A");
                    break;
                case 2:
                    viewHolder.idTvTitle.setText("副石B");
                    break;
                case 3:
                    viewHolder.idTvTitle.setText("副石C");
                    break;
            }
            ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoneEntity = stoneEntities.get(i);
            initStone(viewHolder, stoneEntity);
            initListener(viewHolder, stoneEntity);
            return view;
        }
    }

    private void initStone(ViewHolder viewHolder, ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoneEntity) {
        viewHolder.idStoreType.setTextName(stoneEntity.getTypeTitle());
        viewHolder.idStoreColor.setTextName(stoneEntity.getColorTitle());
        viewHolder.idStoreCut.setTextName(stoneEntity.getPurityTitle());  //净度
        viewHolder.idStoreNorm.setTextName(stoneEntity.getSpecTitle());  //规格
        viewHolder.idStoreShape.setTextName(stoneEntity.getShapeTitle());
        viewHolder.idStorePrice.setTextName(stoneEntity.getPrice());
    }


    private void initListener(ViewHolder viewHolder, final ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoneEntity) {
        idCusStoreRemarkid.setOnSelectData(new CustomSelectButton.OnselectData() {
            @Override
            public List<Type> getData() {
                List<Type> list = new ArrayList<>();
                for (int i = 0; i < remarksEntity.size(); i++) {
                    Type type = new Type();
                    type.setId(remarksEntity.get(i).getId());
                    type.setTypeName(remarksEntity.get(i).getTitle());
                    type.setContent(remarksEntity.get(i).getContent());
                    list.add(type);
                }
                return list;
            }

            @Override
            public void getSelectId(Type type) {
                remar = type;
                String curentRemar = idTvStoreRemarks.getText().toString();
                idTvStoreRemarks.setText(curentRemar + type.getContent());
            }

            @Override
            public String getTitle() {
                return "选择备注";
            }
        });


        viewHolder.idStoreType.setOnSelectData(new CustomSelectButton.OnselectData() {
            @Override
            public List<Type> getData() {
                List<Type> list = new ArrayList<Type>();
                for (int i = 0; i < stoneTypeItme.size(); i++) {
                    Type type = new Type();
                    type.setId(stoneTypeItme.get(i).getId());
                    type.setTypeName(stoneTypeItme.get(i).getTitle());
                    list.add(type);
                }
                return list;
            }

            @Override
            public void getSelectId(Type type) {
                stoneEntity.setTypeId(type.getId());
                stoneEntity.setTypeTitle(type.getTypeName());
                setStorePrice(stoneEntity);
            }

            @Override
            public String getTitle() {
                return "请选择类型";
            }
        });
        viewHolder.idStoreColor.setOnSelectData(new CustomSelectButton.OnselectData() {
            @Override
            public List<Type> getData() {
                List<Type> list = new ArrayList<Type>();
                for (int i = 0; i < stoneColorItme.size(); i++) {
                    Type type = new Type();
                    type.setId(stoneColorItme.get(i).getId());
                    type.setTypeName(stoneColorItme.get(i).getTitle());
                    list.add(type);
                }
                return list;
            }

            @Override
            public void getSelectId(Type type) {
                stoneEntity.setColorId(type.getId());
                stoneEntity.setColorTitle(type.getTypeName());
                setStorePrice(stoneEntity);
            }

            @Override
            public String getTitle() {
                return "请选择颜色";
            }
        });
        viewHolder.idStoreCut.setOnSelectData(new CustomSelectButton.OnselectData() {
            @Override
            public List<Type> getData() {
                List<Type> list = new ArrayList<Type>();
                for (int i = 0; i < stonePurityItme.size(); i++) {
                    Type type = new Type();
                    type.setId(stonePurityItme.get(i).getId());
                    type.setTypeName(stonePurityItme.get(i).getTitle());
                    list.add(type);
                }
                return list;
            }

            @Override
            public void getSelectId(Type type) {
                stoneEntity.setPurityId(type.getId());
                stoneEntity.setPurityTitle(type.getTypeName());
                setStorePrice(stoneEntity);
            }

            @Override
            public String getTitle() {
                return "请选择净度";
            }
        });
        viewHolder.idStoreNorm.setOnSelectData(new CustomSelectButton.OnselectData() {
            @Override
            public List<Type> getData() {
                List<Type> list = new ArrayList<Type>();
                for (int i = 0; i < stoneSpecItme.size(); i++) {
                    Type type = new Type();
                    type.setId(stoneSpecItme.get(i).getId());
                    type.setTypeName(stoneSpecItme.get(i).getTitle());
                    list.add(type);
                }
                return list;
            }

            @Override
            public void getSelectId(Type type) {
                stoneEntity.setSpecId(type.getId());
                stoneEntity.setSpecTitle(type.getTypeName());
                setStorePrice(stoneEntity);
            }

            @Override
            public String getTitle() {
                return "请选择规格";
            }
        });
        viewHolder.idStoreShape.setOnSelectData(new CustomSelectButton.OnselectData() {
            @Override
            public List<Type> getData() {
                List<Type> list = new ArrayList<Type>();
                for (int i = 0; i < stoneShapeItem.size(); i++) {
                    Type type = new Type();
                    type.setId(stoneShapeItem.get(i).getId());
                    type.setTypeName(stoneShapeItem.get(i).getTitle());
                    list.add(type);
                }
                return list;
            }

            @Override
            public void getSelectId(Type type) {
                stoneEntity.setShapeId(type.getId());
                stoneEntity.setShapeTitle(type.getTypeName());
            }

            @Override
            public String getTitle() {
                return "请选择形状";
            }
        });


        /*    查看当前订单*/

        idTvCurorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addCurentOrder();
                //  openActivity(ConfirmOrderActivity.class,null);
                if (type == 1 || type == 2) {
                    finish();
                } else {
                    Intent intent = new Intent(StyleInfromationActivity.this, ConfirmOrderActivity.class);
                    startActivityForResult(intent, 11);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            if (data != null) {
                id = data.getExtras().getString("id");
                type = 1;
                loadNetData();
            }
        }
    }

    public void setStorePrice(ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoneEntity) {
        if (!(StringUtils.isEmpty(stoneEntity.getColorId()) && StringUtils.isEmpty(stoneEntity.getPurityId())
                && StringUtils.isEmpty(stoneEntity.getSpecId()) && StringUtils.isEmpty(stoneEntity.getTypeId()))) {
            loadStonePrice(stoneEntity);
            stoneEntity.setNumber(1 + "");
        }
    }

    public void loadStonePrice(final ModelDetailResult.DataEntity.ModelEntity.StoneEntity stoneEntity) {
        String url = AppURL.URL_STONE_PRICE + "tokenKey=" + BaseApplication.getToken() + "&colorId=" + stoneEntity.getColorId() +
                "&categoryId=" + stoneEntity.getTypeId() + "&specId=" + stoneEntity.getSpecId() + "&purityId=" + stoneEntity.getPurityId();
        L.e("查询价格:    " + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                L.e(result);
                int error = OKHttpRequestUtils.getmInstance().getResultCode(result);
                if (error == 0) {
                    String price = new Gson().fromJson(result, JsonObject.class).getAsJsonObject("data").get("price").getAsString();
                    stoneEntity.setPrice(price);
                    adapter.notifyDataSetChanged();
                }
                if (error == 2) {
                    loginToServer(StyleInfromationActivity.class);
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
        @Bind(R.id.id_store_type)
        CustomSelectButton idStoreType;
        @Bind(R.id.id_store_norm)
        CustomSelectButton idStoreNorm;
        @Bind(R.id.id_store_shape)
        CustomSelectButton idStoreShape;
        @Bind(R.id.id_store_color)
        CustomSelectButton idStoreColor;
        @Bind(R.id.id_store_cut)
        CustomSelectButton idStoreCut;
        @Bind(R.id.id_store_price)
        CustomSelectButton idStorePrice;
        @Bind(R.id.id_tv_title)
        TextView idTvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void onBack(View v) {
        finish();
    }
}
