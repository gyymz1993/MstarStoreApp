package discard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qx.mstarstoreapp.R;
import com.qx.mstarstoreapp.base.BaseActivity;
import com.qx.mstarstoreapp.utils.UIUtils;
import com.qx.mstarstoreapp.viewutils.CustomLV;
import com.qx.mstarstoreapp.viewutils.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */
public class TestActivity extends BaseActivity implements ObservableScrollView.Callbacks{

    private CustomLV lv;
    List<String> strs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        for (int i = 0; i < 20; i++) {
            strs.add( "data-----" + i);
        }
        lv = (CustomLV) findViewById(R.id.listview);
        lv.setAdapter(new BaseAdapter() {
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

        initScrollView();
    }

    private View stopView;
    private ObservableScrollView scrollView;
    public void initScrollView(){
        stopView =findViewById(R.id.stopView);
        scrollView = (ObservableScrollView)findViewById(R.id.sv_bottom);
        scrollView.setCallbacks(this);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(scrollView.getScrollY());
                    }
                });
        // 滚动范围
        scrollView.scrollTo(0, 0);
        scrollView.smoothScrollTo(0, 0);//设置scrollView默认滚动到顶部

    }

    @Override
    public void onScrollChanged(int scrollY) {
        (findViewById(R.id.stickyView))
                .setTranslationY(Math.max(stopView.getTop(), scrollY));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }
}
