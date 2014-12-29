package cn.picksomething.shopassistant;

import android.app.Application;

public class ShoppingApplication extends Application {
    public static String username;
    public static boolean loginFlat = false;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String name) {
        username = name;
    }

    public static boolean hasLogined() {
        return loginFlat;
    }

    public static void setLogin(boolean flat) {
        loginFlat = flat;
    }


}
