package com.qx.mstarstoreapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.bean.Type;
import com.qx.mstarstoreapp.viewutils.CustomSelectButton;
import com.qx.mstarstoreapp.viewutils.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18.
 */
public class TestActivity extends BaseActivity {


    ListView lv;
    //类型和筛选数据
    private Map<String,List<Type>> mTypeItme=new HashMap<>();
    private List<String> mType;  //对应类型的键
    private List<String> listCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        lv = (ListView) findViewById(R.id.id_view_list);
        TestAdapter adapter=new TestAdapter(this,listCount);
        lv.setAdapter(adapter);
    }

    protected void initView() {
        mType=new ArrayList<>();
        listCount=new ArrayList<>();
        listCount.add("分类1");
        listCount.add("分类2");
        listCount.add("分类3");

        List<Type> mType1=new ArrayList<>();
        Type ty1=new Type("选项1");
        Type ty2=new Type("选项2");
        Type ty3=new Type("选项3");
        Type ty4=new Type("选项4");
        mType1.add(ty1); mType1.add(ty2);
        mType1.add(ty3);mType1.add(ty4);
        mTypeItme.put("选择1",mType1);
        mType.add("选择1");
//
//        List<Type> mType2=new ArrayList<>();
//        Type y21=new Type("钻戒1");
//        Type y22=new Type("钻戒2");
//        Type y23=new Type("钻戒3");
//        Type y24=new Type("钻戒4");
//        mType2.add(y21); mType2.add(y22);
//        mType2.add(y23);mType2.add(y24);
//        mTypeItme.put("选择2",mType2);
//        mType.add("选择2");
//
//        List<Type> mType3=new ArrayList<>();
//        mType3.add(y21); mType3.add(y22);
//        mType3.add(y23);mType3.add(y24);
//        mTypeItme.put("选择3",mType3);
//        mType.add("选择3");
//
//
//        List<Type> mType4=new ArrayList<>();
//        mType4.add(y21); mType4.add(y22);
//        mType4.add(y23);mType4.add(y24);
//        mTypeItme.put("选择4",mType4);
//        mType.add("选择4");
//
//        List<Type> mType5=new ArrayList<>();
//        mType5.add(y21); mType5.add(y22);
//        mType5.add(y23);mType5.add(y24);
//        mTypeItme.put("选择5",mType5);
//        mType.add("选择5");
//
//        List<Type> mType6=new ArrayList<>();
//        mType6.add(y21); mType6.add(y22);
//        mType6.add(y23);mType6.add(y24);
//        mTypeItme.put("选择6",mType6);
//        mType.add("选择6");


    }



    public class TestAdapter extends BaseAdapter{

        private List<String> mListCout;
        private Context mContext;
        private int sw;
        private ViewGroup.LayoutParams paramsImg;
        LayoutInflater inflater;

        public  TestAdapter(Context context,List<String> mListCount){
            this.mContext=context;
            this.mListCout=mListCount;
            inflater=LayoutInflater.from(mContext);
            sw = getScreen().widthPixels;
            int w = context.getResources().getDimensionPixelSize(
                    R.dimen.xxx_img_total_width);
            sw = sw - w;
            paramsImg = new ViewGroup.LayoutParams(sw / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        @Override
        public int getCount() {
            return mListCout.size();
        }

        @Override
        public Object getItem(int position) {
            return mListCout.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null||((ViewHolder) convertView.getTag()).fg != position){
                viewHolder=new ViewHolder();
                viewHolder.fg=position;
                convertView= inflater.inflate(R.layout.adapter_custinfr_item,null);
                viewHolder.flowLayout= (FlowLayout) convertView.findViewById(R.id.id_custom_select_btn);
                viewHolder.textView= (TextView) convertView.findViewById(R.id.goods_norms_tv);
                addFlowView( viewHolder.flowLayout,mType);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(mListCout.get(position));
            return convertView;
        }

        private void addFlowView(FlowLayout flowLayout, final List<String> mType) {
            for ( int i=0;i<mType.size();i++){
                CustomSelectButton v = (CustomSelectButton) inflater.inflate(
                        R.layout.select_btn_layout, null);//这个layout里面就只有一个imageview空间，特别简单
                final int j=i;
                v.setTextName(mType.get(i));
                v.setOnSelectData(new CustomSelectButton.OnselectData() {
                    @Override
                    public List<Type> getData() {
                        return mTypeItme.get(mType.get(j)) ;
                    }
                    @Override
                    public void getSelectId(Type str) {

                    }
                    @Override
                    public String getTitle() {
                        return "请选择";
                    }
                });
                v.setLayoutParams(paramsImg);
                flowLayout.addView(v);
            }


        }


        public class ViewHolder{
            FlowLayout flowLayout;
            TextView textView;
            int fg;
        }
    }
}
