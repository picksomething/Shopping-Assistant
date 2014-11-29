package cn.picksomething.shopassistant;

import android.app.Application;

public class ShoppingApplication extends Application {
	public static String username;

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		ShoppingApplication.username = username;
	}
	
	
}
