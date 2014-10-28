package cn.picksomething.shopassistant.ui;


import android.os.Bundle;
import cn.picksomething.shopassistant.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomePage extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		initSlidingMenu(savedInstanceState);
	}

	/*** 初始化侧滑菜单 Begin ***/
	private void initSlidingMenu(Bundle savedInstanceState){
		setBehindContentView(R.layout.menu_frame);
		getFragmentManager().beginTransaction().replace(R.id.menu_frame, new CustomMenu()).commit();		

		SlidingMenu menu = getSlidingMenu();
		// 设置滑动阴影的宽度
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		menu.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.SLIDING_CONTENT);			


	}
	/*** 初始化侧滑菜单 End ***/
	
}
