package person.jack.plant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;

/**
 * 故障报警界面， yanxu
 */
public class FailureWarnActivity extends BaseFragmentActivity {

    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure_warn);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        textHeadTitle.setText(getString(R.string.btn_alert));
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
