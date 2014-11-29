package cn.picksomething.shopassistant.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.ShoppingApplication;
import cn.picksomething.shopassistant.http.HttpTools;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText mUserEditText;
	private EditText mPwdEditText;
	private TextView mForgetText;
	private TextView mRegisterText;
	private Button mLoginButton;
	private String mPassword;
	private String mUserEmail;
	private MyHandler mHandler;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}
	
	public void initView(){
		mUserEditText=(EditText) findViewById(R.id.email);
		mPwdEditText=(EditText) findViewById(R.id.password);
		mForgetText=(TextView) findViewById(R.id.forget_password_text);
		mRegisterText=(TextView) findViewById(R.id.register_text);
		mLoginButton=(Button) findViewById(R.id.login_button);
		mHandler=new MyHandler(getMainLooper());
		mLoginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button:
			new Thread(new Runnable() {

				@Override
				public void run() {
					mUserEmail=mUserEditText.getText().toString();
					mPassword=mPwdEditText.getText().toString();
					String url = "http://10.0.2.2:8080/shopping/LoginServlet";
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
					nameValuePair.add(new BasicNameValuePair("email", mUserEmail));
					nameValuePair.add(new BasicNameValuePair("password", mPassword));
					JSONObject jsonObject=null;
					try {
						String retStr = HttpTools.getStringResult(nameValuePair, url);
						 jsonObject = new JSONObject(retStr);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = mHandler.obtainMessage();    
		            msg.obj = jsonObject;  
		            mHandler.sendMessage(msg);  
				}
			}).start();
			break;
	   }
	}
	
	class MyHandler extends Handler{
		public MyHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			try {
				if(((JSONObject)msg.obj).getString("msg").equals("ok")){
					Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(LoginActivity.this,HomePage.class);
					String username=((JSONObject)msg.obj).getString("username");
					ShoppingApplication.setUsername(username);
					startActivity(intent);
					finish();
				}
				else{
					Toast.makeText(LoginActivity.this,"login error",Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
