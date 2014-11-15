package cn.picksomething.shopassistant.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.util.SharePrefUtils;

public class WelcomeActivity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		Log.d("tag2","r is "+SharePrefUtils.getWizardFlag(this));
		if (SharePrefUtils.getWizardFlag(this)) {
			SharePrefUtils.saveWizardFlagPreferece(this, false);			
			begin(1);
		}
		else
			begin(2);
		
	}
	
	public void begin(final int index){
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				Intent intent;
				if(index==1)
					intent = new Intent(WelcomeActivity.this,GuideActivity.class);
				else
					intent = new Intent(WelcomeActivity.this,HomePage.class);
				startActivity(intent);
				finish();
			}
		}, 5000);
	}
}
