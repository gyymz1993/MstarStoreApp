//package discard;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.qx.mstarstoreapp.R;
//import com.qx.mstarstoreapp.activity.ClassifyActivity;
//import com.qx.mstarstoreapp.activity.StyleInfromationActivity;
//import com.qx.mstarstoreapp.base.AppURL;
//import com.qx.mstarstoreapp.base.BaseActivity;
//import com.qx.mstarstoreapp.base.BaseApplication;
//import com.qx.mstarstoreapp.base.MyAction;
//import com.qx.mstarstoreapp.inter.ClassifyOnSeachListener;
//import com.qx.mstarstoreapp.inter.OnSeachListener;
//import com.qx.mstarstoreapp.json.ClassTypeFilerEntity;
//import com.qx.mstarstoreapp.json.ModeListResult;
//import com.qx.mstarstoreapp.json.SearchValue;
//import com.qx.mstarstoreapp.json.TypeFiler;
//import com.qx.mstarstoreapp.net.VolleyRequestUtils;
//import com.qx.mstarstoreapp.utils.L;
//import com.qx.mstarstoreapp.utils.StringUtils;
//import com.qx.mstarstoreapp.utils.ToastManager;
//import com.qx.mstarstoreapp.dialog.GridMenuDialog;
//import com.qx.mstarstoreapp.viewutils.GridViewWithHeaderAndFooter;
//import com.qx.mstarstoreapp.viewutils.ListMenuDialog;
//import com.qx.mstarstoreapp.viewutils.PullToRefreshView;
//import com.qx.mstarstoreapp.viewutils.SideFilterDialog;
//import com.qx.mstarstoreapp.viewutils.SquareImageView;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import zxing.activity.CaptureActivity;
//
///*
// * 创建人：Yangshao
// * 创建时间：2016/10/13 11:04
// * @version
// *
// */
//public class OrderActivity1 extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
//
//
//    private LinearLayout layAllOrder, layFilter, layGvFileter, layout1;
//    private GridViewWithHeaderAndFooter mCustomGridView;
//    private TextView tvCclassify, tvCurentOrder, id_tv_select;
//    private Context context;
//    private ImageView igNor;
//    private RelativeLayout layout2, root_view;
//    private SideFilterDialog filterDialog;
//    private ListMenuDialog listMenuDialog;
//    private PullToRefreshView mRefreshView;
//    private GridMenuDialog gridMenuDialog;
//    private List<ModeListResult.DataEntity.ModelEntity.ModelListEntity> data = new ArrayList<>();
//    private List<ClassTypeFilerEntity> typeListData;
//    /*推荐款  最新款*/
//    List<ModeListResult.DataEntity.CustomList> customList;
//
//    private static final int PULL_REFRESH = 1;
//    private static final int PULL_LOAD = 2;
//    private int tempCurpage = 1;
//    private int pullState = 1;
//    private int curpage = 1;
//    private int list_count;
//    @Bind(R.id.id_et_seach)
//    EditText idEtSeach;
//    @Bind(R.id.ig_btn_seach)
//    ImageView idIgSeach;
//    @Bind(R.id.id_tv_filter)
//    TextView idTvFilter;
//
//    @Bind(R.id.id_classify)
//    TextView idTvClassify;
//    /*搜索过的多选历史记录*/
//    public static  List<TypeFiler> hisCategoryFilterList =new ArrayList<>();
//    /*搜索过的单选历史记录*/
//    public static List<SearchValue> hisSearchKeywordList =new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order);
//        ButterKnife.bind(this);
//        context = this;
//        initView();
//        loadNetData(getInitUrl());
//    }
//
//    public void onBack(View view){
//        finish();
//    }
//
//    private String getInitUrl(){
//       String url = AppURL.URL_MODE_LIST + "tokenKey=" + BaseApplication.getToken()+ "&cpage=" + curpage;
//        return url;
//    }
//
//    private void loadNetData(String url) {
//        L.e("开启搜索"+url);
//        baseShowWatLoading();
//        VolleyRequestUtils.GetCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                L.e("loadNetData"+result);
//                try {
//                    JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
//                    String error=jsonResult.get("error").getAsString();
//                    if (error.equals("0")) {
//                        L.e("OrderActivity  loadNetData   成功");
//                        baseHideWatLoading();
//                        ModeListResult modeListResult = new Gson().fromJson(result, ModeListResult.class);
//                        L.e("pullState"+pullState);
//                        if (pullState != PULL_LOAD) {
//                            data.clear();
//                        }
//                        ModeListResult.DataEntity dataEntity = modeListResult.getData();
//                        ModeListResult.DataEntity.ModelEntity modeEntity = dataEntity.getMode();
//                        if (curpage==1){
//                            /*搜索过的多选历史记录*/
//                            hisCategoryFilterList = dataEntity.getCategoryFiler();
//                         /*搜索过的单选历史记录*/
//                            hisSearchKeywordList = dataEntity.getSearchKeyword();
//                            ClassifyActivity.hisSearchKeywordList=dataEntity.getSearchKeyword();
//                        }
//                        customList = dataEntity.getCustomList();
//                        list_count = Integer.valueOf(modeEntity.getList_count());
//                        if (list_count == 0) {
//                            data.clear();
//                            endNetRequest();
//                            return;
//                        }
//                        data.addAll(modeEntity.getModelList());
//                        endNetRequest();
//                        if (typeListData == null) {
//                            typeListData = dataEntity.getTypeList();
//                        }
//
//                        initDialog();
//                        initListener();
//                        L.e("解析成功");
//                    } if (error.equals("2")) {
//                        L.e("重新登录");
//                        ToastManager.showToastReal(jsonResult.get("message").getAsString());
//                        loginToServer(OrderActivity1.class);
//                    }else {
//                        baseHideWatLoading();
//                        String message =   jsonResult.get("message").getAsString();
//                        ToastManager.showToastReal(message);
//                    }
//
//                } catch (Exception e) {
//                    baseHideWatLoading();
//                    e.printStackTrace();
//                    L.e("Exception"+e);
//                    endNetRequest();
//
//                }
//            }
//
//
//        });
//    }
//
//    private void initDialog() {
//        filterDialog = new SideFilterDialog(context, typeListData, MyAction.filterDialogAction);
//        listMenuDialog = new ListMenuDialog(OrderActivity1.this,customList);
//    }
//
//
//
//    private void endNetRequest() {
//        mGvAdapter.notifyDataSetChanged();
//        tempCurpage = curpage;
//        if (pullState == PULL_LOAD) {
//            mRefreshView.onFooterRefreshComplete();
//        } else if (pullState == PULL_REFRESH) {
//            mRefreshView.onHeaderRefreshComplete();
//        }
//        pullState = 0;
//    }
//
//    View loadStateView;
//    TextView hint_txt;
//
//    public void initView() {
//        mRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
//        mRefreshView.setOnHeaderRefreshListener(this);
//        mRefreshView.setOnFooterRefreshListener(this);
//        mRefreshView.setVisibility(View.VISIBLE);
//        igNor = (ImageView) findViewById(R.id.id_ig_nor);
//        layAllOrder = (LinearLayout) findViewById(R.id.id_ly_all);
//        layGvFileter = (LinearLayout) findViewById(R.id.id_gv_fileter);
//        id_tv_select = (TextView) findViewById(R.id.id_tv_select);
//        root_view = (RelativeLayout) findViewById(R.id.root_view);
//        layout1 = (LinearLayout) findViewById(R.id.id_rel2);
//        layout2 = (RelativeLayout) findViewById(R.id.id_rel3);
//        //筛选
//        layFilter = (LinearLayout) findViewById(R.id.id_ly_filter);
//        tvCclassify = (TextView) findViewById(R.id.id_tv_classify);
//        tvCurentOrder = (TextView) findViewById(R.id.id_cur_order);
//        mCustomGridView = (GridViewWithHeaderAndFooter) findViewById(R.id.id_gv_menu);
//        loadStateView = View.inflate(this, R.layout.grid_food_layout, null);
//        hint_txt = (TextView) loadStateView.findViewById(R.id.tv_hint_txt);
//        mCustomGridView.addFooterView(loadStateView);
//        mCustomGridView.setAdapter(mGvAdapter);
//    }
//
//    String mcategory;   /*下啦筛选关键字*/
//    String myAction;   /*判断是哪个页面的action*/
//    public String getCheckBoxUrl(){
//        List<TypeFiler> filterList;
//        if (myAction.equals(MyAction.classifyActivityAction)){
//            filterList=ClassifyActivity.hisCategoryFilterList;
//        }else {
//            filterList= OrderActivity1.hisCategoryFilterList;
//        }
//        List<String> list=new ArrayList<>();
//        int count=filterList.size();
//        for (int i=0;i<count;i++){
//            TypeFiler typeFiler = filterList.get(i);
//            list.add(typeFiler.getGroupKey());
//           // L.e(""+typeFiler.toString());
//        }
//        HashSet<String> hs = new HashSet<>(list); //此时已经去掉重复的数据保存在hashset中
//        Iterator<String> iterator=hs.iterator();
//        StringBuffer sbbuf=new StringBuffer();
//        while(iterator.hasNext()){
//            StringBuffer sb=new StringBuffer();
//            String tag=iterator.next();
//            sb.append("&"+ tag+"=");
//            for (int i=0;i<count;i++){
//                TypeFiler typeFiler = filterList.get(i);
//                if (typeFiler.getGroupKey().equals(tag)){
//                    sb.append(typeFiler.getValue()+"|");
//                }
//            }
//            sb.deleteCharAt(sb.length() - 1);
//            sbbuf.append(sb.toString());
//        }
//        L.e("typeFiler  check"+sbbuf.toString());
//        return sbbuf.toString();
//    }
//
//    public String getRadioGroupUrl(){
//        List<SearchValue> keywordList;
//        if (myAction.equals(MyAction.classifyActivityAction)){
//            keywordList=ClassifyActivity.hisSearchKeywordList;
//        }else {
//            keywordList= OrderActivity1.hisSearchKeywordList;
//        }
//        String low,hig;
//        StringBuilder sbPrice = new StringBuilder();
//        if (keywordList!=null&&keywordList.size()!=0) {
//            for (SearchValue hisKey : keywordList) {
//                if (!StringUtils.isEmpty(hisKey.getValue())) {
//                    sbPrice.append("&" + hisKey.getName() + "=" + hisKey.getValue());
//                }else {
//                    low=hisKey.getLow();  hig=hisKey.getHig();
//                    if (!StringUtils.isEmpty(low)&&!StringUtils.isEmpty(hig)){
//                        sbPrice.append("&" + hisKey.getName()  + "=" + low+"|"+hig);
//                    }else if (!StringUtils.isEmpty(low)&&StringUtils.isEmpty(hig)){
//                        sbPrice.append("&" + hisKey.getName()  + "=" + low+"|");
//                    }else if (StringUtils.isEmpty(low)&&!StringUtils.isEmpty(hig)){
//                        sbPrice.append("&" + hisKey.getName()  + "=" +"|"+hig);
//                    }
//                }
//            }
//            L.e(sbPrice.toString());
//        }
//        L.e("typeFiler  radio"+sbPrice.toString());
//        return sbPrice.toString();
//    }
//
//    private void initListener() {
//        /*分类界面的确认搜索*/
//        ClassifyActivity.setOnClassifySeachListener(new ClassifyOnSeachListener() {
//            @Override
//            public void onSeach(String action) {
//                myAction=action;
//                curpage=1;
//                String url=getInitUrl();
//                url+= getCheckBoxUrl();
//                url+=getRadioGroupUrl();
//                loadNetData(url);
//            }
//        });
//
//        /*弹出筛选页面dialog*/
//        layFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                backgroundAlpha(0.7f);
//                filterDialog.showAsDropDown(root_view,getStatusBarHeight());
//            }
//        });
//
//        /*筛选界面      确认搜索事件*/
//        filterDialog.setOnSeachListener(new ClassifyOnSeachListener() {
//            @Override
//            public void onSeach(String action) {
//                myAction=action;
//                curpage=1;
//                String url=getInitUrl();
//                url+= getCheckBoxUrl();
//                url+=getRadioGroupUrl();
//                loadNetData(url);
//            }
//        });
//
//         /*筛选界面      关闭事件*/
//        filterDialog.setOnListMenuSelectCloseClick(new SideFilterDialog.OnListMenuSelectCloseClick() {
//            @Override
//            public void onClose() {
//                backgroundAlpha(1f);
//            }
//        });
//
//
//        /*弹出下拉别表搜索事件*/
//        layAllOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listMenuDialog.setOnListMenuSelectCloseClick(new ListMenuDialog.OnListMenuSelectCloseClick() {
//                    @Override
//                    public void onClose() {
//                        L.e("onClose");
//                        backgroundAlpha(1f);
//                        igNor.setImageResource(R.drawable.icon_list_nor);
//                    }
//                    @Override
//                    public void onSelect(final ModeListResult.DataEntity.CustomList select) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                id_tv_select.setText(StringUtils.idgui(select.getTitle()));
//                                mcategory=select.getId()+"";
//                                String url=getInitUrl();
//                                url+="&category="+mcategory;
//                                loadNetData(url);
//                            }
//                        });
//                    }
//                });
//                backgroundAlpha(0.7f);
//                listMenuDialog.showAsDropDown(layout1);
//                igNor.setImageResource(R.drawable.icon_list);
//            }
//        });
//
//
//        mCustomGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                data.get(position).getId();
//                Bundle pBundle=new Bundle();
//                pBundle.putString("id",data.get(position).getId());
//                openActivity(StyleInfromationActivity.class, pBundle);
//            }
//        });
//
//
//        tvCurentOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // openActivity(CustommadeInformationActivity.class, null);
//            }
//        });
//
//
//        /*弹出 已被选中的标签dialog  GridView*/
//        layGvFileter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gridMenuDialog = new GridMenuDialog(OrderActivity1.this);
//                gridMenuDialog.setOnListMenuSelectCloseClick(new GridMenuDialog.OnListMenuSelectCloseClick() {
//                    @Override
//                    public void onClose() {
//                        L.e("onClose");
//                        backgroundAlpha(1f);
//                        igNor.setImageResource(R.drawable.icon_list_nor);
//                    }
//
//                    @Override
//                    public void onSelect(final String select) {
//                        L.e("当前选择" + select);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                id_tv_select.setText(select);
//                            }
//                        });
//                    }
//                });
//                gridMenuDialog.setOnSeachListener(new OnSeachListener() {
//                    @Override
//                    public void onSeach(String seachUrl) {
//                        String url=getInitUrl();
//                        url+=seachUrl;
//                        loadNetData(url);
//                    }
//                });
//
//                backgroundAlpha(0.7f);
//                gridMenuDialog.showAsDropDown(layout2);
//                // igNor.setImageResource(R.drawable.icon_list);
//            }
//        });
//
//        /*开始搜索事件*/
//        idIgSeach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url=getInitUrl();
//                String keyWord = idEtSeach.getText().toString();
//                if (!StringUtils.isEmpty(keyWord)) {
//                    data.clear();
//                    keyWord = StringUtils.removeSpacesUrl(keyWord);
//                    idTvFilter.setVisibility(View.VISIBLE);
//                    idTvFilter.setText(keyWord);
//                }
//                url+=  "&keyWord=" + keyWord;
//                loadNetData(url);
//            }
//        });
//
//        /*关键字标签清空*/
//        idTvFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                idTvFilter.setVisibility(View.GONE);
//                idTvFilter.setText("");
//                idEtSeach.setText("");
//                loadNetData(getInitUrl());
//            }
//        });
//
//    }
//
//    public void opClassify(View view){
//        openActivity(ClassifyActivity.class,null);
//    }
//
//
//    private BaseAdapter mGvAdapter = new BaseAdapter() {
//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return data.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = LayoutInflater.from(OrderActivity1.this).inflate(R.layout.adapter_goods_list, parent, false);
//                holder.lay = (LinearLayout) convertView.findViewById(R.id.img_container);
//                holder.tv = (TextView) convertView.findViewById(R.id.name);
//                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
//                holder.ig = (SquareImageView) convertView.findViewById(R.id.product_img);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            holder.ig.setImageResource(R.drawable.no_image);
//            holder.tv.setText(data.get(position).getTitle());
//            holder.tvPrice.setText(data.get(position).getPrice());
//           // ImageLoader.getInstance().displayImage(data.get(position).getPic(), holder.ig, ImageLoadOptions.getOptions());
//            return convertView;
//        }
//
//        class ViewHolder {
//            LinearLayout lay;
//            SquareImageView ig;
//            TextView tv;
//            TextView tvPrice;
//        }
//    };
//
//
//    @Override
//    public void onFooterRefresh(PullToRefreshView view) {
//        if (data.size() < list_count) {
//            tempCurpage = curpage;
//            curpage++;
//            pullState = PULL_LOAD;
//            loadNetData(getInitUrl());
//        } else {
//            hint_txt.setText("已加载全部数据喲");
//            ToastManager.showToastReal("没有更多数据");
//            view.onFooterRefreshComplete();
//        }
//    }
//
//    @Override
//    public void onHeaderRefresh(PullToRefreshView view) {
//        tempCurpage = curpage;
//        curpage = 1;
//        pullState = PULL_REFRESH;
//        hint_txt.setText("上拉加载更多");
//        loadNetData(getInitUrl());
//    }
//
//
//
//
//    /*扫描二维码页面*/
//    public void scan(View view){
//        Intent inten=new Intent(OrderActivity1.this, CaptureActivity.class);
//        startActivityForResult(inten,0);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        L.e(requestCode+"");
//        if (requestCode==0&&data!=null){
//            Bundle bundle=data.getExtras();
//            String result=bundle.getString("result");
//            L.e("onActivityResult result"+result);
//        }
//    }
//
//}
