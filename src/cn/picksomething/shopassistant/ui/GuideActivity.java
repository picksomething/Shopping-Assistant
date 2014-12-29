package cn.picksomething.shopassistant.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.picksomething.shopassistant.R;

import com.actionbarsherlock.app.SherlockActivity;

public class GuideActivity extends SherlockActivity {
    private LayoutInflater mInflater;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewpPagerAdapter;
    private List<View> mViewList;
    private ImageView guideImage;
    private Button finishButton;
    private int[] imgs = {R.drawable.guide_bg1, R.drawable.guide_bg2, R.drawable.guide_bg3};

    // 底部小点图片
    private ImageView[] mDots;

    // 记录当前选中位置
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 初始化页面
        buildViews();

        // 初始化底部小点
        buildDots();
    }

    private void buildViews() {
        mInflater = LayoutInflater.from(this);

        mViewList = new ArrayList<View>();
        for (int i = 0; i < imgs.length; i++) {
            View view = mInflater.inflate(R.layout.guide_layout, null);
            guideImage = (ImageView) view.findViewById(R.id.guide_image);
            guideImage.setBackgroundResource(imgs[i]);
            mViewList.add(view);
        }


        // 初始化Adapter
        mViewpPagerAdapter = new ViewPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewpPagerAdapter);

        // 预加载
        mViewPager.setOffscreenPageLimit(1);

        // 绑定回调
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            public void onPageSelected(int arg0) {
                // 设置底部小点选中状态
                setCurrentDot(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void buildDots() {
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.layout_dots);

        mDots = new ImageView[mViewList.size()];

        // 循环取得小点图片
        for (int i = 0; i < mViewList.size(); i++) {
            mDots[i] = (ImageView) ll.getChildAt(i);
        }

        mCurrentIndex = 0;
        mDots[mCurrentIndex].setImageResource(R.drawable.dot_l);
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > mViewList.size() - 1 || mCurrentIndex == position) {
            return;
        }
        mDots[position].setImageResource(R.drawable.dot_l);
        mDots[mCurrentIndex].setImageResource(R.drawable.dot_s);
        mCurrentIndex = position;
    }

    class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;

        public ViewPagerAdapter(Context context) {
            this.mContext = context;
        }

        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public void finishUpdate(View arg0) {
        }


        @Override
        public int getCount() {
            if (mViewList != null) {
                return mViewList.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            if (position == mViewList.size() - 1) {
                finishButton = (Button) mViewList.get(position).findViewById(R.id.finish_button);
                finishButton.setVisibility(View.VISIBLE);
                finishButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        mContext.startActivity(intent);
                        finish();
                    }
                });
            }
            return mViewList.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 回退键实现Home键功能
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
