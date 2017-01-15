package why.superman.redpacket.activity;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import why.superman.redpacket.R;
import why.superman.redpacket.fragment.GeneralSettingsFragment;

import static anetwork.channel.http.NetworkSdkSetting.context;

public class SettingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        loadUI();
        prepareSettings();
        initUm();
        handleMaterialStatusBar();
    }

    private void initUm() {
        PushAgent.getInstance(context).onAppStart();
    }

    private void prepareSettings() {
        String title, fragId;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            fragId = bundle.getString("frag_id");
        } else {
            title = "抢红包设置";
            fragId = "GeneralSettingsFragment";
        }

        TextView textView = (TextView) findViewById(R.id.settings_bar);
        textView.setText(title);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if ("GeneralSettingsFragment".equals(fragId)) {
            fragmentTransaction.replace(R.id.preferences_fragment, new GeneralSettingsFragment());
        }
        fragmentTransaction.commit();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadUI() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(0xffE46C62);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void performBack(View view) {
        super.onBackPressed();
    }

    public void enterAccessibilityPage(View view) {
        Toast.makeText(this, "找到【超人红包】，开启即可", Toast.LENGTH_LONG).show();
        Intent mAccessibleIntent =
                new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(mAccessibleIntent);
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
