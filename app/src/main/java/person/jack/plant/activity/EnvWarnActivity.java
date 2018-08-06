package person.jack.plant.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.adapter.WarAdapter;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.ValueSetDao;
import person.jack.plant.db.dao.WarnRecordDao;
import person.jack.plant.db.entity.ValueSet;
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
    private ValueSetDao valueSetDao;
    Spinner spinner;
    ArrayAdapter<String>spinAdapter;
    List<String>spinList=new ArrayList<>();
    private TextView waring_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_warn);
        initView();
        ButterKnife.bind(this);
        valueSetDao = new ValueSetDao(AppContext.getInstance());
        textHeadTitle.setText("环境警告");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        warnRecordDao = new WarnRecordDao(AppContext.getInstance());
        listView = (ListView) findViewById(R.id.waring_list);

        list = warnRecordDao.findAll();
        if (list.size() == 0 || list == null) {

        } else {
            for (int i=0;i<list.size();i++){
                spinList.add(list.get(i).getName());
            }
         spinAdapter=new ArrayAdapter<String>(EnvWarnActivity.this,android.R.layout.simple_spinner_item,spinList);
            spinner.setAdapter(spinAdapter);
            adapter = new WarAdapter(AppContext.getInstance(), R.layout.fragment_warn_item, list);
            listView.setAdapter(adapter);
            Log.d("sutdent1", "有数据");
        }
        ValueSet valueTem = valueSetDao.findValueName("温度");
        ValueSet valueHum = valueSetDao.findValueName("湿度");
        ValueSet valueLight = valueSetDao.findValueName("光照");
        if (valueHum != null && valueLight != null && valueTem != null) {
            waring_value.setText("正常数值范围：温度"+valueTem.getMin()+"℃--"+valueTem.getMax()+"℃" +
                    "、光照"+valueLight.getMin()+"xl--"+valueLight.getMax()+"xl、湿度"+valueHum.getMin()+"%--"+valueHum.getMax()+"%");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(EnvWarnActivity.this);
                builder.setMessage("确认删除吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WarnRecord warnRecord=warnRecordDao.findByNameAndType(list.get(position).getName(),list.get(position).getType());
                        warnRecordDao.delete(warnRecord);
                        list.clear();
                        list = warnRecordDao.findAll();
                        if (list.size() == 0 || list == null) {

                        } else {

                            adapter = new WarAdapter(AppContext.getInstance(), R.layout.fragment_warn_item, list);
                            listView.setAdapter(adapter);
                            Log.d("sutdent1", "有数据");
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });

    }

    private void initView() {

        waring_value = (TextView) findViewById(R.id.waring_value);
        spinner=(Spinner)findViewById(R.id.spin_name);
    }
}
