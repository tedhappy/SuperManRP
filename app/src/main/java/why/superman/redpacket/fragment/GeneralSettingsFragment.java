package why.superman.redpacket.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import why.superman.redpacket.R;

/**
 * 创建者     Ted
 * 创建时间   2017/1/8 20:27
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GeneralSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_preferences);
        setPrefListeners();
    }

    private void setPrefListeners() {
        // Check for updates
        Preference updatePref = findPreference("pref_etc_check_update");
        updatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
//                new UpdateTask(getActivity().getApplicationContext(), true).update();
                Toast.makeText(getActivity(), "已经是最新版本了~", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        Preference excludeWordsPref = findPreference("pref_watch_exclude_words");
        String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
        String value = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_watch_exclude_words", "");
        if (value.length() > 0) excludeWordsPref.setSummary(summary + ":" + value);

        excludeWordsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
                if (o != null && o.toString().length() > 0) {
                    preference.setSummary(summary + ":" + o.toString());
                } else {
                    preference.setSummary(summary);
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GeneralSettingsFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GeneralSettingsFragment");
    }
}
