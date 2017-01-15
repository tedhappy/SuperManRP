package why.superman.redpacket;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import why.superman.redpacket.activity.HelpActivity;
import why.superman.redpacket.activity.SettingActivity;
import why.superman.redpacket.utils.BitmapUtils;
import why.superman.redpacket.utils.SPUtil;

import static anetwork.channel.http.NetworkSdkSetting.context;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AccessibilityManager.AccessibilityStateChangeListener {

    private android.widget.TextView maingonglue;
    private android.widget.TextView mainsetting;
    private android.widget.ImageView maincaishen;
    private android.widget.TextView maindashang;
    private android.widget.TextView mainshare;
    private android.widget.TextView mainhelp;
//    private android.widget.TextView mainrecord;
    private AnimationDrawable mCaishen_anim;
    private ImageView maintoptishi;
    private ImageView maindot;
    private android.widget.LinearLayout maintopqianghongbao;
    private AnimationDrawable mDot_anim;
    private AccessibilityManager accessibilityManager;
    private SPUtil mSp;
    private Dialog mTipsDialog;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
        initData();
        initListener();
        initUm();

    }

    private void initUm() {
        PushAgent.getInstance(context).onAppStart();
    }

    private void init() {
        exitTime = 0;

        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);

        explicitlyLoadPreferences();
        handleMaterialStatusBar();
    }


    /**
     * 初始化view
     */
    public void initView() {
//        this.mainrecord = (TextView) findViewById(R.id.main_record);
        this.mainhelp = (TextView) findViewById(R.id.main_help);
        this.mainshare = (TextView) findViewById(R.id.main_share);
        this.maindashang = (TextView) findViewById(R.id.main_dashang);
        this.maincaishen = (ImageView) findViewById(R.id.main_caishen);
        this.maintopqianghongbao = (LinearLayout) findViewById(R.id.main_top_qianghongbao);
        this.maindot = (ImageView) findViewById(R.id.main_dot);
        this.maintoptishi = (ImageView) findViewById(R.id.main_top_tishi);
        this.mainsetting = (TextView) findViewById(R.id.main_setting);
        this.maingonglue = (TextView) findViewById(R.id.main_gonglue);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mSp = new SPUtil(this);

    }

    /**
     * 初始化监听
     */
    public void initListener() {


//        mainrecord.setOnClickListener(this);
        mainhelp.setOnClickListener(this);
        mainshare.setOnClickListener(this);
        maindashang.setOnClickListener(this);
        maincaishen.setOnClickListener(this);
        mainsetting.setOnClickListener(this);
        maingonglue.setOnClickListener(this);
    }

    private void explicitlyLoadPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.general_preferences, false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_gonglue://点击了红包攻略
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.main_setting://点击了设置
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                settingsIntent.putExtra("title", "抢红包设置");
                settingsIntent.putExtra("frag_id", "GeneralSettingsFragment");
                startActivity(settingsIntent);
                break;
            case R.id.main_dashang://点击了打赏
                showDonateDialog();
                break;
            case R.id.main_share://点击了分享
                showShare();
                break;
            case R.id.main_help://点击了帮助
                startActivity(new Intent(this, HelpActivity.class));
                break;
/*
            case R.id.main_record://点击了红包记录

                break;
*/

            default:
                break;
        }
    }

    private void showDonateDialog() {
        final Dialog dialog = new Dialog(this, R.style.QR_Dialog_Theme);
        View view = getLayoutInflater().inflate(R.layout.donate_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
//                File output = new File(android.os.Environment.getExternalStorageDirectory(), "superman_wechatpay.jpg");
//                if(!output.exists()) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wechatpay_qr);
//                    BitmapUtils.saveBitmap(MainActivity.this, output, bitmap);
//                }
                File output = BitmapUtils.saveImageToGallery(MainActivity.this, bitmap);
                Toast.makeText(MainActivity.this, "已保存到:" + output.getAbsolutePath(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
                return true;
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("MainActivity");
        MobclickAgent.onResume(this);

        updateServiceStatus();

        boolean isAgree = mSp.getBoolean("isAgree", false);
        if (!isAgree) {
            showAgreementDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity");
        MobclickAgent.onPause(this);
    }

    public void openAccessibility(View view) {
        try {
            Toast.makeText(this, "找到【超人红包】，开启即可", Toast.LENGTH_LONG).show();
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(this, "遇到一些问题,请手动打开系统设置>无障碍服务>微信红包(ฅ´ω`ฅ)", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(this);
        mTipsDialog = null;
    }

    /**
     * 更新当前 HongbaoService 显示状态
     */
    private void updateServiceStatus() {
        mCaishen_anim = (AnimationDrawable) maincaishen.getDrawable();
        mDot_anim = (AnimationDrawable) maindot.getDrawable();
        if (isServiceEnabled()) {
            //服务开启，提示消失，开始抢红包
            maintoptishi.setVisibility(View.GONE);
            maintopqianghongbao.setVisibility(View.VISIBLE);

            //正在抢红包点的动画
            mDot_anim.start();

            //财神的动画
            mCaishen_anim.start();

            if (mTipsDialog != null) {
                mTipsDialog.dismiss();
            }
        } else {
            //服务未开启，提示消失，开始抢红包
            maintoptishi.setVisibility(View.VISIBLE);
            maintopqianghongbao.setVisibility(View.GONE);

            //正在抢红包点的动画
            mDot_anim.stop();

            //财神的动画
            mCaishen_anim.stop();

            //显示要开启的对话框
            showOpenAccessibilityServiceDialog();
        }
    }

    /**
     * 获取 HongbaoService 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.service.HongbaoService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 显示免责声明的对话框
     */
    private void showAgreementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.agreement_title);
        builder.setMessage(getString(R.string.agreement_message));
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //保存sp
                mSp.putBoolean("isAgree", true);
            }
        });
        builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "呵呵，再见！！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();
    }

    /**
     * 显示未开启辅助服务的对话框
     */
    private void showOpenAccessibilityServiceDialog() {
        if (mTipsDialog != null && mTipsDialog.isShowing()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_tips_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibility(null);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.open_service_title);
        builder.setView(view);
        builder.setPositiveButton(R.string.open_service_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibility(null);
            }
        });
        mTipsDialog = builder.show();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("分享超人红包到--");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("最近用【超人红包】软件自动抢了好多红包，再也不用担心手速了！推荐你也下载使用一下~~");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://img2.ph.126.net/39wEEPBSzJXiq81xmAppnw==/6608685007492804931.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

}
