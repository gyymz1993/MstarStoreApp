package com.qx.mstarstoreapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.fragment.FragOrderList;
import com.qx.mstarstoreapp.viewutils.IndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
 * 创建人：Yangshao
 * 创建时间：2016/9/23 15:48
 * @version    成品订单
 *
 */
public class CustomMadeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    IndicatorView indicatorView;
    ViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<>();
    private static int SCREENWIDTH;
    List<TextView> tabTextViews = new ArrayList<>();

    @Bind(R.id.id_ig_back)
    ImageView idIgBack;
    @Bind(R.id.title_text)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_made);
        ButterKnife.bind(this);
        initView();
    }

    public void onBack(View view) {
        finish();
    }

    protected void initView() {
        titleText.setText("成品订单");
        FragOrderList fragOrderList = new FragOrderList(1);
        fragmentList.add(fragOrderList);
        FragOrderList fragOrderList1 = new FragOrderList(2);
        fragmentList.add(fragOrderList1);
        FragOrderList fragOrderList2 = new FragOrderList(3);
        fragmentList.add(fragOrderList2);
        FragOrderList fragOrderList3 = new FragOrderList(4);
        fragmentList.add(fragOrderList3);

        SCREENWIDTH = getScreenWidth();
        indicatorView = (IndicatorView) findViewById(R.id.id_indicatorview);
        viewPager = (ViewPager) findViewById(R.id.order_viewpager);
        TextView tab = (TextView) findViewById(R.id.tab0);
        TextView tab1 = (TextView) findViewById(R.id.tab1);
        TextView tab2 = (TextView) findViewById(R.id.tab2);
        TextView tab3 = (TextView) findViewById(R.id.tab3);
        tabTextViews.add(tab);
        tabTextViews.add(tab1);
        tabTextViews.add(tab2);
        tabTextViews.add(tab3);
        tab.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        CommentListPagerAdapter adapter = new CommentListPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicatorView.setX(SCREENWIDTH / 4 * (position + positionOffset));
    }

    @Override
    public void onPageSelected(int position) {
        indicatorView.setX(SCREENWIDTH / 4 * position);
        setTxtColor(tabTextViews.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setTxtColor(TextView textView) {
        for (int i = 0; i < tabTextViews.size(); i++) {
            tabTextViews.get(i).setTextColor(getResources().getColor(R.color.theme_text));
        }
        textView.setTextColor(getResources().getColor(R.color.theme_red));
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab0:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab1:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab2:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    public class CommentListPagerAdapter extends FragmentPagerAdapter {
        private List<?> fragments;

        public CommentListPagerAdapter(FragmentManager fm, List<?> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
