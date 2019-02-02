package com.example.jessie.focusing.Utils;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 23:39
 */
public class AppConstants {

    public final static long ONE_DAY = 86400000;
    public static final String LOCK_STATE = "app_lock_state"; //应用锁开关(状态，true开，false关)
    public static final String LOCK_IS_FIRST_LOCK = "is_locked"; //是否加过锁
    public static final String LOCK_PACKAGE_NAME = "lock_package_name"; //点开的锁屏应用的包名
    public static final String APP_PACKAGE_NAME = "com.example.jessie.focusing"; //包名
    public static final String BACK_TO_FINISH = "lock_from_finish"; //解锁后转跳的action是finish
}

