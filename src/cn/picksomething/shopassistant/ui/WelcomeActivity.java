package cn.picksomething.shopassistant.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.util.SharePrefUtils;

public class WelcomeActivity extends Activity {

    private final static int BEGIN_LOGIN = 1;
    private final static int BEGIN_GUIDE = 2;
    private final static int BEGIN_HOMEPAGE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (SharePrefUtils.getGuideFlag(this)) {
            SharePrefUtils.saveGuideFlagPreferece(this, false);
            begin(BEGIN_GUIDE);
        } else if (SharePrefUtils.getLoginFlag(this)) {
            begin(BEGIN_LOGIN);
        } else
            begin(BEGIN_HOMEPAGE);

    }

    public void begin(final int index) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                Intent intent;
                if (index == BEGIN_LOGIN)
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                else if (index == BEGIN_GUIDE)
                    intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                else
                    intent = new Intent(WelcomeActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
