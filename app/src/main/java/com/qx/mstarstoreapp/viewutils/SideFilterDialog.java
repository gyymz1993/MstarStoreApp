package com.qx.mstarstoreapp.viewutils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.activity.OrderActivity;
import com.qx.mstarstoreapp.base.BaseFilterData;
import com.qx.mstarstoreapp.base.MyAction;
import com.qx.mstarstoreapp.json.ClassTypeFilerEntity;

import java.util.List;


/*
 * 创建人：Yangshao
 * 创建时间：2016/10/24 18:02
 * @version    侧滑筛选菜单
 *
 */

public class SideFilterDialog extends BaseFilterData {

    private View mPopView;
    private Context mContext;

    private PopupWindow popupWindow;
    private ExpandableListView expandableGridView;
    private List<ClassTypeFilerEntity> mTypeListData;
    /*确定和重置按钮*/
    private TextView idTvConfirfilterr;
    private TextView idTvResetfilter;

    public SideFilterDialog(Context context, List<ClassTypeFilerEntity> typeListData, String action) {
        this.mContext = context;
        this.mTypeListData = typeListData;
        initView(context);
        initBaseFilterData(context, action);
        initListener();
    }

    private void initListener() {
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                L.e("SideFilterDialog  消失");
//                dismiss();
//            }
//        });
        idTvResetfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*单选始终有默认值  只需要情况里面的Value*/
                for (int i = 0; i < OrderActivity.singleKey.size(); i++) {
                    OrderActivity.singleKey.get(i).setValue("");
                }
                OrderActivity.multiselectKey.clear();
                adapter.isResetGridTextAdapter();
            }
        });


        /*提交  开始搜索*/
        idTvConfirfilterr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSeachListener != null) {
                    onSeachListener.onSeach(MyAction.filterDialogAction);
                }
                //getCheckBoxUrl();
                // getRadioGroupUrl();
                dismiss();
            }
        });


    }


    public void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopView = inflater.inflate(R.layout.dialog_filter_dialog, null);
        expandableGridView = (ExpandableListView) mPopView.findViewById(R.id.list);
        idTvConfirfilterr = (TextView) mPopView.findViewById(R.id.id_tv_confirfilterr);
        idTvResetfilter = (TextView) mPopView.findViewById(R.id.id_tv_resetfilter);
        popupWindow = new PopupWindow(mPopView, getWindowWidth() - 150, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        popupWindow.setOutsideTouchable(true);


    }

    @Override
    public ExpandableListView getExpandableGridView() {
        return expandableGridView;
    }

    @Override
    public List<ClassTypeFilerEntity> getTypeListData() {
        return mTypeListData;
    }

    // 隐藏菜单
    public void dismiss() {
        if (onListMenuCloseClick != null) {
            popupWindow.dismiss();
            onListMenuCloseClick.onClose();
        }

    }

    public void setOnListMenuSelectCloseClick(OnListMenuSelectCloseClick onListMenuCloseClick) {
        this.onListMenuCloseClick = onListMenuCloseClick;
    }

    OnListMenuSelectCloseClick onListMenuCloseClick;

    public interface OnListMenuSelectCloseClick {
        void onClose();
    }

    // 下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(RelativeLayout parent, int y) {
        popupWindow.showAtLocation(parent, Gravity.RIGHT, 0, y);
    }

    public int getWindowWidth() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

}
