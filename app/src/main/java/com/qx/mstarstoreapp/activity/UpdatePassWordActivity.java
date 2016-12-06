package com.qx.mstarstoreapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.AppURL;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.base.BaseApplication;
import com.qx.mstarstoreapp.net.VolleyRequestUtils;
import com.qx.mstarstoreapp.utils.L;
import com.qx.mstarstoreapp.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/7.
 */
public class UpdatePassWordActivity extends BaseActivity {

    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.id_rel_title)
    RelativeLayout idRelTitle;
    @Bind(R.id.id_ed_code)
    EditText idEdCode;
    @Bind(R.id.id_ed_pwd)
    EditText idEdPwd;
    String pwd,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        initView();
    }

    public void onBack(View view) {
        finish();
    }

    protected void initView() {
        titleText.setText("修改密码");
    }


    public void onUpdatePwd(View view){
        netUpdatePwd();
    }

    public void netUpdatePwd() {
        pwd = idEdPwd.getText().toString();
        code = idEdCode.getText().toString();
        if (StringUtils.isEmpty(pwd)) {
            return;
        }
        if (StringUtils.isEmpty(code)) {
            return;
        }
        baseShowWatLoading();
        //userModifyPasswordDo?tokenKey=841bf597782dced089a6e5167bcf29de&password=123456&phoneCode=32656
        // 进行登录请求
        String url = AppURL.URL_UPDATE_PWD + "tokenKey=" + BaseApplication.getToken() + "&password=" + pwd + "&phoneCode=" + code;
        L.e("netLogin" + url);
        VolleyRequestUtils.getInstance().getCookieRequest(this, url, new VolleyRequestUtils.HttpStringRequsetCallBack() {
            @Override
            public void onSuccess(String result) {
                baseHideWatLoading();
                L.e(result);
                Gson gson = new Gson();
                String error = gson.fromJson(result, JsonObject.class).get("error").getAsString();
                if (error.equals("0")) {
                    finish();
                }if (error.equals("2")) {
                    loginToServer(UpdatePassWordActivity.class);
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

}
