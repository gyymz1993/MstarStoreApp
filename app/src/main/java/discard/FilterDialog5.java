//package com.qx.mstarstoreapp.viewutils;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ExpandableListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.qx.mstarstoreapp.R;
//import com.qx.mstarstoreapp.adapter.ExpandableGridAdapter;
//import com.qx.mstarstoreapp.bean.FilterBean;
//import com.qx.mstarstoreapp.json.ModeListResult;
//import com.qx.mstarstoreapp.utils.L;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Administrator on 2016/9/20.
// * <p>
// * 显示隐藏的二级菜单
// */
//public class SideFilterDialog{
//    TextView idTvConfirfilterr;
//    TextView idTvResetfilter;
//    private View mPopView;
//    Context mContext;
//    public static List<FilterBean> checkBoxSelectFilter=new ArrayList<>();
//    public static HashMap<String, String> radioSelectFilter  = new HashMap<>();;
//
//    List<ModeListResult.DataEntity.TypeListEntity> mTypeListData;
//    List<String> getGroupKey=new ArrayList<>();
//    public SideFilterDialog(Context context, List<ModeListResult.DataEntity.TypeListEntity> typeListData) {
//        this.mContext = context;
//        this.mTypeListData=typeListData;
//        init(context);
//        initModle();
//        setListener();
//    }
//
//
//
//    public String [] getString(List<String> list){
//        String strings[]=new String[list.size()];
//        for(int i=0,j=list.size();i<j;i++){
//            strings[i]=list.get(i);
//        }
//        return strings;
//    }
//
//    public  String [][] getArray(){
//        String strTwo[][] = new String[mTypeListData.size()][];
//        for (int i=0;i<mTypeListData.size();i++){
//            List<ModeListResult.DataEntity.TypeListEntity.AttributeListEntity> attributeList = mTypeListData.get(i).getAttributeList();
//            getGroupKey.add(mTypeListData.get(i).getGroupKey());
//            List list=new ArrayList();
//            for (int j=0;j<attributeList.size();j++){
//                //  L.e("getArray:"+attributeList.get(j).getTitle());
//                list.add(attributeList.get(j).getTitle());
//            }
//            strTwo[i]=getString(list);
//        }
//        return strTwo;
//    }
//
//    public  String [][] getValue(){
//        String strTwo[][] = new String[mTypeListData.size()][];
//        for (int i=0;i<mTypeListData.size();i++){
//            List<ModeListResult.DataEntity.TypeListEntity.AttributeListEntity> attributeList = mTypeListData.get(i).getAttributeList();
//            List list=new ArrayList();
//            for (int j=0;j<attributeList.size();j++){
//                list.add(attributeList.get(j).getValue());
//            }
//            strTwo[i]=getString(list);
//        }
//        return strTwo;
//    }
//    PopupWindow popupWindow;
//    private void init(Context context) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mPopView = inflater.inflate(R.layout.dialog_filter_dialog, null);
//        expandableGridView = (ExpandableListView) mPopView.findViewById(R.id.list);
//        idTvConfirfilterr = (TextView) mPopView.findViewById(R.id.id_tv_confirfilterr);
//        idTvResetfilter = (TextView) mPopView.findViewById(R.id.id_tv_resetfilter);
//        // child_text_array = EXPANDABLE_MOREGRIDVIEW_TXT;
//        child_text_array = getArray();
//        child_text_value = getValue();
//
//        popupWindow = new PopupWindow(mPopView, getWindowWidth()-150, ViewGroup.LayoutParams.MATCH_PARENT);
//        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）//设置监听
////        this.setContentView(mPopView);
////        this.setWidth(getWindowWidth() - 150);
////        this.setHeight(height);
//        // 设置SelectPicPopupWindow弹出窗体可点击
//        popupWindow.setFocusable(true);
////        // 点击外面的控件也可以使得PopUpWindow dimiss
//        popupWindow.setOutsideTouchable(false);
////        // 设置SelectPicPopupWindow弹出窗体动画效果
////        this.setAnimationStyle(R.style.Fiter_PopupAnimation);
////        ColorDrawable dw = new ColorDrawable(0xffffffff);//0xb0000000
////        // ColorDrawable dw = new ColorDrawable(0x00000000);
////        // 设置SelectPicPopupWindow弹出窗体的背景
////        this.setBackgroundDrawable(dw);//半透明颜色
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                L.e("List_noteTypeActivity:", "我是关闭事件");
//                onListMenuCloseClick.onClose();
//            }
//        });
//    }
//
//    // 隐藏菜单
//    public void dismiss() {
//        if (onListMenuCloseClick!=null){
//            popupWindow.dismiss();
//            onListMenuCloseClick.onClose();
//        }
//
//    }
//
//    public void setOnListMenuSelectCloseClick(OnListMenuSelectCloseClick onListMenuCloseClick) {
//        this.onListMenuCloseClick = onListMenuCloseClick;
//    }
//
//    OnListMenuSelectCloseClick onListMenuCloseClick;
//    public interface OnListMenuSelectCloseClick{
//        void onClose();
//    }
//    // 下拉式 弹出 pop菜单 parent 右下角
//    public void showAsDropDown(RelativeLayout parent, int y) {
//        popupWindow.showAtLocation(parent, Gravity.RIGHT, 0, y);
//    }
//
//
//    public int getWindowWidth() {
//        WindowManager wm = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        return width;
//    }
//
//    //http://www.tuicool.com/articles/nueeIv  设置不同样式
//    //http://blog.csdn.net/zjp776/article/details/51213329
//    private ExpandableListView expandableGridView;
//    ExpandableGridAdapter adapter;
//
//    private List<Map<String, Object>> list;
//    private String[][] child_text_array;
//    private String[][] child_text_value;
//
//
//    private String priceLow,priceHig,widthLow,widthHig;
//    public interface  OnSeachListener{
//        void onSeach(String priceLow, String priceHig, String widthLow, String widthHig);
//    }
//    OnSeachListener onSeachListener;
//    public  void setOnSeachListener(OnSeachListener onSeachListener) {
//        this.onSeachListener=onSeachListener;
//    }
//
//    private void setListener() {
//        expandableGridView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v,
//                                        int groupPosition, long id) {
//                if (groupSelct.get(groupPosition)) {
//                    expandableGridView.collapseGroup(groupPosition);
//                    groupSelct.put(groupPosition, false);
//                } else {
//                    expandableGridView.expandGroup(groupPosition);
//                    groupSelct.put(groupPosition, true);
//                }
//                return true;
//            }
//        });
//
//        idTvConfirfilterr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                for (int i=0;i<checkBoxSelectFilter.size();i++){
////                    //L.e("当前选择："+checkBoxSelectFilter.get(i).getName());
////                }
//                if (onSeachListener!=null){
//                    onSeachListener.onSeach(priceLow,priceHig,widthLow,widthHig);
//                }
//                dismiss();
//            }
//        });
//
//        adapter.setOnSeachPriceforEditText(new ExpandableGridAdapter.OnSeachPriceforEditText() {
//            @Override
//            public void onEditToLow(String low) {
//                priceLow=low;
//            }
//
//            @Override
//            public void onEditToHig(String hig) {
//                priceHig=hig;
//            }
//        });
//
//
//
//        adapter.setOnSeachWidthforEditText(new ExpandableGridAdapter.OnSeachWidthforEditText() {
//            @Override
//            public void onEditToLow(String low) {
//                widthLow=low;
//            }
//
//            @Override
//            public void onEditToHig(String hig) {
//                widthHig=hig;
//            }
//        });
//
//
//        idTvResetfilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                radioSelectFilter.clear();
//                checkBoxSelectFilter.clear();
//                adapter.isResetGridTextAdapter();
//            }
//        });
//
//    }
//
//    // 父listview的文本数据数组
//    public static String[] EXPANDABLE_GRIDVIEW_TXT = new String[]{"新闻", "军事",
//            "微博"};
//    // 子listview的文本数据
//    public static String[][] EXPANDABLE_MOREGRIDVIEW_TXT = {
//            {"国内", "社会", "国际", "评论", "传媒", "排行"},
//            {"新闻", "图片", "中国军情", "专栏", "视频"},
//            {"注册", "名人堂", "人气热榜", "客户端", "热门微博", "随便看看"}
//    };
//
//    Map<Integer, Boolean> groupSelct = new HashMap<>();
//
//    Map<Integer, Integer> isSelectType = new HashMap<>();
//    private void initModle() {
//
//        list = new ArrayList<>();
//        for (int i = 0; i < mTypeListData.size(); i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("txt", mTypeListData.get(i).getTitle());
//            list.add(map);
//        }
//
//        for (int i=0;i<mTypeListData.size();i++){
//            isSelectType.put(i, mTypeListData.get(i).getMulSelect());
//        }
//
//
//        adapter = new ExpandableGridAdapter(mContext, list, child_text_array,child_text_value,isSelectType,getGroupKey);
//        expandableGridView.setAdapter(adapter);
//        //默认全部展开
//        for (int i = 0; i < list.size(); i++) {
//            expandableGridView.expandGroup(i);
//            groupSelct.put(i, true);
//        }
//
//    }
//
//
//}
