package person.jack.plant.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.adapter.WarAdapter;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.WarnRecordDao;
import person.jack.plant.db.entity.WarnRecord;

/**
 * 环境报警yx
 */
public class EnvWarnActivity extends BaseFragmentActivity {
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    ListView listView;
    WarAdapter adapter;
    List<WarnRecord> list = new ArrayList<>();
    WarnRecordDao warnRecordDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_warn);
        ButterKnife.bind(this);

        textHeadTitle.setText("环境警告");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        warnRecordDao = new WarnRecordDao(AppContext.getInstance());
        listView = (ListView)findViewById(R.id.waring_list);

        list = warnRecordDao.findAll();
        if (list.size() == 0 || list == null) {

        } else {
            adapter = new WarAdapter(AppContext.getInstance(), R.layout.fragment_warn_item, list);
            listView.setAdapter(adapter);
            Log.d("sutdent1", "有数据");
        }
    }
}