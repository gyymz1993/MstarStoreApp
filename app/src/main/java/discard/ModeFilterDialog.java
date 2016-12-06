//package com.qx.mstarstoreapp.viewutils;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ExpandableListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.qx.mstarstoreapp.R;
//import com.qx.mstarstoreapp.base.BaseFilterData;
//import com.qx.mstarstoreapp.bean.FilterBean;
//import com.qx.mstarstoreapp.json.ModeListResult;
//import com.qx.mstarstoreapp.utils.L;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by Administrator on 2016/9/20.
// * <p>
// * 显示隐藏的二级菜单
// */
//public class ModeFilterDialog extends BaseFilterData{
//    TextView idTvConfirfilterr;
//    TextView idTvResetfilter;
//    Context mContext;
//    public static List<FilterBean> checkBoxSelectFilter=new ArrayList<>();
//    public static HashMap<String, String> radioSelectFilter  = new HashMap<>();;
//    private ExpandableListView expandableGridView;
//    List<ModeListResult.DataEntity.TypeListEntity> mTypeListData;
//
//    PopupWindow popupWindow;
//    public ModeFilterDialog(Context context, List<ModeListResult.DataEntity.TypeListEntity> typeListData) {
//        super(typeListData);
//        this.mContext = context;
//        this.mTypeListData=typeListData;
//        init();
//        setListener();
//    }
//
//
//    private void init() {
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View mPopView = inflater.inflate(R.layout.dialog_filter_dialog, null);
//        popupWindow = new PopupWindow(mPopView, getWindowWidth() - 150, ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）//设置监听
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        expandableGridView = (ExpandableListView) mPopView.findViewById(R.id.list);
//        idTvConfirfilterr = (TextView) mPopView.findViewById(R.id.id_tv_confirfilterr);
//        idTvResetfilter = (TextView) mPopView.findViewById(R.id.id_tv_resetfilter);
//
//        expandableGridView.setAdapter(adapter);
//        //默认全部展开
//        for (int i = 0; i < list.size(); i++) {
//            expandableGridView.expandGroup(i);
//            groupSelct.put(i, true);
//        }
//
//    }
//
//    public int getWindowWidth() {
//        WindowManager wm = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        return width;
//    }
//
//    private void setListener() {
//
//        idTvResetfilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                radioSelectFilter.clear();
//                checkBoxSelectFilter.clear();
//               // adapter.isResetGridTextAdapter();
//            }
//        });
//
//    }
//
//
//    // 下拉式 弹出 pop菜单 parent 右下角
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public void showAsDropDown(View anchor, int height) {
//        popupWindow.showAsDropDown(anchor, Gravity.RIGHT, 0, height);
//        // 使其聚集
//        popupWindow.setFocusable(true);
//        // 设置允许在外点击消失
//        popupWindow.setOutsideTouchable(true);
//        // 刷新状态
//        ColorDrawable dw = new ColorDrawable(0xffffffff);//0xb0000000
//        popupWindow.setBackgroundDrawable(dw);//半透明颜色
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                L.e("List_noteTypeActivity:", "我是关闭事件");
//               // onListMenuCloseClick.onClose();
//            }
//        });
//        popupWindow.update();
//    }
//
//}
