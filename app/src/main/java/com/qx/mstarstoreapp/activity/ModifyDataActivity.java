package com.qx.mstarstoreapp.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.AppURL;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.base.BaseApplication;
import com.qx.mstarstoreapp.net.ImageLoadOptions;
import com.qx.mstarstoreapp.utils.L;
import com.qx.mstarstoreapp.utils.UIUtils;
import com.qx.mstarstoreapp.viewutils.BitmapUtils;
import com.qx.mstarstoreapp.dialog.ImageInitiDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/9/29 15:45
 * @version    修改资料界面
 *
 */
public class ModifyDataActivity extends BaseActivity {


    private final String TAG = "MystoreActivity";
    @Bind(R.id.content)
    LinearLayout content;
    Context mContext;
    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.splitbutton)
    ImageView splitbutton;
    @Bind(R.id.id_ig_userpic)
    ImageView idIgUserpic;
    @Bind(R.id.id_lay_root)
    LinearLayout idLayRoot;
    private LayoutInflater inflater;
    private String[] titles = new String[]{"修改密码", "修改手机号码", "管理用户地址"};
    private String temp_img_dir;
    private Uri mImageCaptureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifydata);
        context = this;
        ButterKnife.bind(this);
        initContent();
        initListener();
    }

    private Boolean isFlag = false;

    private void initListener() {
        splitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlag) {
                    splitbutton.setImageResource(R.drawable.icon_switch_off);
                } else {
                    splitbutton.setImageResource(R.drawable.icon_switch_on);
                }
                isFlag = !isFlag;
            }
        });
    }


    private static int LIGHT_MARGIN = UIUtils.convertPxtoDip(1);

    private void initContent() {
        titleText.setText("个人中心");
        temp_img_dir = Environment.getExternalStorageDirectory() + File.separator + "tempImage.jpg";
        ImageLoader.getInstance().displayImage(BaseApplication.getUserPic(), idIgUserpic, ImageLoadOptions.getOptions());
        content.removeAllViews();
        for (int i = 0; i < titles.length; i++) {
            getView();
            viewHoder.tv_title.setText(titles[i]);
            viewHoder.inf_rel.setTag(titles[i]);
            viewHoder.inf_rel.setOnClickListener(clickListener);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            if (i != 0) {
                lp.topMargin = LIGHT_MARGIN;
            }
            content.addView(viewHoder.inf_rel, lp);
        }

        idIgUserpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageInitiDialog imageInitiDialog = new ImageInitiDialog(ModifyDataActivity.this);
                imageInitiDialog.showDialog(idLayRoot);
                imageInitiDialog.setOnImageSelectListener(new ImageInitiDialog.OnImageSelectListener() {
                    @Override
                    public void onCamera() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mImageCaptureUri= Uri.fromFile(new File(temp_img_dir));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }

                    @Override
                    public void onPhotos() {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_FROM_FILE);
                    }
                });
            }
        });
    }

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e("requestCode" + requestCode + "resultCode" + resultCode);
        if (requestCode == PICK_FROM_CAMERA) {
            startPhotoZoom(Uri.parse(temp_img_dir));
           // refreshImg(temp_img_dir);
           // submitToServer(temp_img_dir);
        } else if (requestCode == PICK_FROM_FILE) {
            mImageCaptureUri = data.getData();
            //String path = BitmapUtils.getInstace().getRealPath(this, mImageCaptureUri);
            startPhotoZoom(mImageCaptureUri);
           // submitToServer(path);
        } else if (requestCode == 100) {
            Drawable drawable = null;
            if(data != null){
                drawable  = setPicToView(data);
            }
            File path = new File(getRealPathFromURI(mImageCaptureUri));
            if (path.exists() && drawable != null)
                submitToServer(path);
        }

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setData(mImageCaptureUri);
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 100);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private Drawable setPicToView(Intent picdata) {
        Drawable drawable = null;
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            drawable= new BitmapDrawable(photo);
            idIgUserpic.setBackgroundDrawable(drawable);
        }

        return drawable;
    }


    private void submitToServer(final File file) {
        String url = AppURL.URL_UPLOAD_PiC + "&tokenKey=" + BaseApplication.getToken();
        HttpUtils http = new HttpUtils();
        HttpRequest.HttpMethod method = HttpRequest.HttpMethod.POST;
        RequestParams params = new RequestParams();
        params.addBodyParameter("attachment", file, "multipart/form-data");
        L.e("上传URL" + url);
        http.send(method, url, params, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                Log.i("wcl", "current process -->" + current + "/" + total);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                L.e("submitToServer"+result);


                Gson gson = new Gson();
                String error = gson.fromJson(result, JsonObject.class).get("error").getAsString();
                if (error.equals("0")) {
                    String j = gson.fromJson(result, JsonObject.class).get("data").getAsJsonObject().get("headPic").getAsString();
                    L.e("submitToServer" + "成功");
                   // refreshImg(j);
                   // BaseApplication.setUserPic(j);
                    //ImageLoader.getInstance().displayImage(BaseApplication.getUserPic(), idIgUserpic, ImageLoadOptions.getOptions());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    private void refreshImg(final String path) {
        final String photo = BitmapUtils.getInstace().getCompressImagePaht(path);
        final Bitmap bmp = BitmapUtils.getInstace().compressBitmapFromSrc(photo, 100);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idIgUserpic.setImageBitmap(bmp);
            }
        });

    }


    public View getView() {
        viewHoder = new ViewHoder();
        if (inflater == null)
            inflater = getLayoutInflater();
        View inf_rel = inflater.inflate(R.layout.item_menu, null);
        viewHoder.tv_title = (TextView) inf_rel.findViewById(R.id.tv_menu_title);
        viewHoder.tv_openning_hint = (TextView) inf_rel.findViewById(R.id.tv_expand_hint);
        viewHoder.iv_menu_ic = (ImageView) inf_rel.findViewById(R.id.iv_below_menu_ic);
        viewHoder.inf_rel = inf_rel;
        return inf_rel;
    }

    private ViewHoder viewHoder;

    public class ViewHoder {
        View inf_rel;
        TextView tv_title;
        TextView tv_openning_hint;
        ImageView iv_menu_ic;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = v.getTag().toString();
            Intent intent = new Intent();
            switch (tag) {
                case "修改密码":
                    intent.setClass(ModifyDataActivity.this, UpdatePassWordActivity.class);
                    break;
                case "修改手机号码":
                    intent.setClass(ModifyDataActivity.this, UpdatePhoneNumber.class);
                    LogUtils.e(TAG + "商品管理ProductManageActivity");
                    break;
                case "管理用户地址":
                    intent.setClass(ModifyDataActivity.this, AddressListActivity.class);
                    LogUtils.e(TAG + "促销管理PromotionManageActivitys");
                    break;
            }
            startActivity(intent);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initView() {
        titleText.setText("修改资料");
    }



    public void onBack(View view) {
        finish();
    }
}
