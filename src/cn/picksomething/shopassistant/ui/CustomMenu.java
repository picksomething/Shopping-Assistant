package cn.picksomething.shopassistant.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.ShoppingApplication;
import cn.picksomething.shopassistant.adapter.MenuAdapter;
import cn.picksomething.shopassistant.model.ItemSettingModel;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class CustomMenu extends Fragment implements OnClickListener {
	private View mView;
	private Context mContext;
	private ListView listview_common;
	private ListView listview_setting;
	private View mHeadView;
	private ImageView mAvatar;
	private TextView mName;
	private List<ItemSettingModel> commonModels;  //常用列表的Item集合
	private List<ItemSettingModel> settingModels;   //设置列表的item集合
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == mView) {
			mView = inflater.inflate(R.layout.slidemenu_layout, container,
					false);
			initView();
			initValidata();
			bindData();
		}
		return mView;
	}
	/**
	 * 初始化界面元素
	 */
	private void initView() {
		listview_common = (ListView) mView.findViewById(R.id.listview_common);
		listview_setting = (ListView) mView.findViewById(R.id.listview_setting);
		mAvatar=(ImageView) mView.findViewById(R.id.img_icon_top);	
		mName=(TextView) mView.findViewById(R.id.tv_name_top);
		if(ShoppingApplication.hasLogined()==true){
			mName.setText(ShoppingApplication.getUsername());
		}
		mAvatar.setOnClickListener(this);
		mName.setOnClickListener(this);
		
	}
	/**
	 * 初始化变量
	 */
	private void initValidata() {
		mContext = mView.getContext();
		commonModels=new ArrayList<ItemSettingModel>();
		settingModels=new ArrayList<ItemSettingModel>();
		
		
		//1:进行构造常用列表中的数据,图标,名称,数量
		Integer[] common_icon_id = new Integer[] {R.drawable.message_icon,R.drawable.share_icon};
		String[] arrays_commom=mContext.getResources().getStringArray(R.array.arrays_commom);
		for(int i=0;i<common_icon_id.length;i++)
		{
			ItemSettingModel commcon=new ItemSettingModel(common_icon_id[i], arrays_commom[i]);
			commonModels.add(commcon);
		}
		
		
		//2:进行构造设置列表中的数据,图标,名称
		Integer[] setting_icon_id=new Integer[]{R.drawable.setting_icon};
		String[] arrays_setting=mContext.getResources().getStringArray(R.array.arrays_setting);
		for(int i=0;i<setting_icon_id.length;i++)
		{
			ItemSettingModel setting=new ItemSettingModel(setting_icon_id[i],arrays_setting[i]);
			settingModels.add(setting);
		}		
	}

	/**
	 * 绑定数据
	 */
	private void bindData() {
       //创建适配器并且进行绑定数据到listview中
		listview_common.setAdapter(new MenuAdapter(mContext, commonModels));
		listview_setting.setAdapter(new MenuAdapter(mContext, settingModels));
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.img_icon_top:
		case R.id.tv_name_top:
			if(ShoppingApplication.hasLogined()==false){
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
			}
			((SlidingFragmentActivity)getActivity()).toggle();
			break;
		}
		
	}

}