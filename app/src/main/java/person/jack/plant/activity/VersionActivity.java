package person.jack.plant.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import person.jack.plant.R;
import person.jack.plant.ui.UIHelper;

public class VersionActivity extends BaseFragmentActivity {
    TextView version;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        version=(TextView)findViewById(R.id.tv_version);
        back=(Button)findViewById(R.id.btn_version_back);
        String ver=getVersion();
        version.setText(ver);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showMember(VersionActivity.this);
            }
        });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
