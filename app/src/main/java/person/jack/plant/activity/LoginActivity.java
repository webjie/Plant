package person.jack.plant.activity;

import android.os.Bundle;
import android.view.View;

import person.jack.plant.R;
import person.jack.plant.ui.swipebacklayout.SwipeBackActivity;


/**
 * 5.登录界面-没有实现远程登录功能-chenle
 */
public class LoginActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
