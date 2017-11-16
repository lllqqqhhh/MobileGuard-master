package cn.edu.gdmec.android.mobileguard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by kvic on 2017/10/21.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        correctSIM();
    }
    public void correctSIM() {
        //检查SIM卡是够发生变化
        SharedPreferences sp = getSharedPreferences("config",
                Context.MODE_PRIVATE);
        //采取防盗保护状态
        boolean protecting = sp.getBoolean("protecting",true);
        if(protecting){
            //得到绑定的sim卡串号
            String bindsim = sp.getString("sim","");
            //得到手机现在的sim卡串号
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            //为了测试在手机的序列号上data 已模拟SIM卡被更换的情况
            String realsim = tm.getSimSerialNumber();
            //因为虚拟机无法更换sim卡，所以使用虚拟机测试要有此代码，真机测试要注释此代码。
            realsim = "999";
            if(bindsim.equals(realsim)){
                Log.i("","sim卡未发生变化，还是您的手机");
            } else {
                Log.i("","SIM卡变化了");
                // 由于系统版本的原因，这里的发短信可能和其他手机版本不兼容
                String safenumber = sp.getString("safephone","");
                if(!TextUtils.isEmpty(safenumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber,null,
                            "你的亲友手机的SIM卡已经被更换！",null,null);
                }
            }
        }
    }
}
