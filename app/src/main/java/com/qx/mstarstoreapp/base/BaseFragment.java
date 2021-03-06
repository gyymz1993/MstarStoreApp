package com.qx.mstarstoreapp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.activity.LoginActivity;
import com.qx.mstarstoreapp.utils.SpUtils;
import com.qx.mstarstoreapp.viewutils.LoadingDialog;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpTaskHandler;


/**
 *
 *  @action:  
 *  @author:  YangShao
 *  @date: 2015/12/29 @time: 9:00
 */
public class BaseFragment extends Fragment implements HttpCycleContext {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }
    }
    private LoadingDialog loadingDialog;
    protected void baseShowWatLoading() {
        if(loadingDialog==null){
            loadingDialog = new LoadingDialog(getActivity(), getString(R.string.pull_to_refresh_footer_refreshing_label));
            loadingDialog.show();
        }
    }

    public void baseHideWatLoading() {
        if (loadingDialog==null)return;
        if (loadingDialog != null || loadingDialog.isShowing()) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }

    /*跳转到登录页面  登录成功回调到刚刚页面*/
    public void loginToServer(Class<?> jumpTo) {
        BaseApplication.spUtils.saveString(SpUtils.key_tokenKey,"");
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        if (jumpTo == null)
            getActivity().startActivityForResult(loginIntent, 2);
        else {
            loginIntent.putExtra(LoginActivity.JUMP_TO_ACTIVITY, jumpTo);
            getActivity().startActivity(loginIntent);
        }
    }


    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
