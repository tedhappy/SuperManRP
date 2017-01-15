package why.superman.redpacket.activity;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import cn.sharesdk.framework.ShareSDK;
import why.superman.redpacket.utils.LogUtils;


/**
 * 创建者     Ted
 * 创建时间   2017/1/9 15:30
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.sf("deviceToken: "+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        //第三方分享
        ShareSDK.initSDK(this);

        //友盟统计
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
    }
}