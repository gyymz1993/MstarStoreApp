package discard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.Scrollable.CanScrollVerticallyDelegate;
import com.qx.mstarstoreapp.Scrollable.ConfigurationFragmentCallbacks;
import com.qx.mstarstoreapp.Scrollable.OnFlingOverListener;
import com.qx.mstarstoreapp.Scrollable.OnScrollChangedListener;
import com.qx.mstarstoreapp.Scrollable.ScrollableLayout;
import com.qx.mstarstoreapp.Scrollable.TabsLayout;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public class Test1Activity extends BaseActivity implements ConfigurationFragmentCallbacks ,CanScrollVerticallyDelegate, OnFlingOverListener{


    private static final String ARG_LAST_SCROLL_Y = "arg.LastScrollY";

    private ScrollableLayout mScrollableLayout;
    private ListView mListView;
    List<String> strs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity);

        for (int i = 0; i < 20; i++) {
            strs.add( "data-----" + i);
        }
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return strs.size();
            }

            @Override
            public String getItem(int i) {
                return strs.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView txt = new TextView(context);
                txt.setBackgroundColor(Color.WHITE);
                int padding = UIUtils.dip2px(15);
                txt.setPadding(padding, padding, padding, padding);
                txt.setText(getItem(i));
                return txt;
            }
        });


        final View header = findViewById(R.id.header);
        final TabsLayout tabs = (TabsLayout) findViewById(R.id.tabs);
        tabs.setViewPager();

        mScrollableLayout = (ScrollableLayout) findViewById(R.id.scrollable_layout);
        mScrollableLayout.setDraggableView(tabs);


        mScrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
               // return adapter.canScrollVertically(viewPager.getCurrentItem(), direction);
                return false;
            }
        });
        mScrollableLayout.setOnFlingOverListener(new OnFlingOverListener() {
            @Override
            public void onFlingOver(int y, long duration) {
               // adapter.getItem(viewPager.getCurrentItem()).onFlingOver(y, duration);
            }
        });

        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {

                final float tabsTranslationY;
                if (y < maxY) {
                    tabsTranslationY = .0F;
                } else {
                    tabsTranslationY = y - maxY;
                }

                tabs.setTranslationY(tabsTranslationY);

                header.setTranslationY(y / 2);
            }
        });

        if (savedInstanceState != null) {
            final int y = savedInstanceState.getInt(ARG_LAST_SCROLL_Y);
            mScrollableLayout.post(new Runnable() {
                @Override
                public void run() {
                    mScrollableLayout.scrollTo(0, y);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_LAST_SCROLL_Y, mScrollableLayout.getScrollY());
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onFrictionChanged(float friction) {
        mScrollableLayout.setFriction(friction);
    }

    @Override
    public void openDialog(float friction) {
//        final ScrollableDialog dialog = ScrollableDialog.newInstance(friction);
//        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void openActivity(Intent intent) {
        startActivity(intent);
    }


    @Override
    public boolean canScrollVertically(int direction) {
        return mListView != null && mListView.canScrollVertically(direction);
    }

    @Override
    public void onFlingOver(int y, long duration) {
        if (mListView != null) {
            mListView.smoothScrollBy(y, (int) duration);
        }
    }
}
