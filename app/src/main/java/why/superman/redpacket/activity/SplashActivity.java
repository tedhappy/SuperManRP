package why.superman.redpacket.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import why.superman.redpacket.MainActivity;
import why.superman.redpacket.R;

import static anetwork.channel.http.NetworkSdkSetting.context;

/**
 * Splash界面
 */
public class SplashActivity extends Activity implements Animation.AnimationListener {

    private RelativeLayout mIv_splash;
    private AlphaAnimation mAa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initEvent();

        PushAgent.getInstance(context).onAppStart();

        handleMaterialStatusBar();
    }

    private void initEvent() {
        //开启动画
        startAnimation();

        //动画的监听
        mAa.setAnimationListener(this);
    }

    private void startAnimation() {
        //透明度动画
        mAa = new AlphaAnimation(0, 1);

        //持续时间
        mAa.setDuration(1000);

        mIv_splash.startAnimation(mAa);
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        mIv_splash = (RelativeLayout) findViewById(R.id.iv_splash);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    //在动画播放结束的时候,跳转
    @Override
    public void onAnimationEnd(Animation animation) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity");
        MobclickAgent.onPause(this);
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(0xffE46C62);

    }

}
