package person.jack.plant.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.ValueSetDao;
import person.jack.plant.db.entity.ValueSet;
/**
 * yx阈值设置
 */
public class ValueSetActivity extends BaseFragmentActivity implements View.OnClickListener{
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    private EditText et_temMin;
    private EditText et_temMax;
    private EditText et_humMin;
    private EditText et_humMax;
    private EditText et_lightMin;
    private EditText et_lightMax;
    private EditText et_nitrogenMin;
    private EditText et_nitrogenMax;
    private EditText et_phosphorMin;
    private EditText et_phosphorMax;
    private EditText et_potassiumMin;
    private EditText et_potassiumMax;
    private Button btn_save;
    private ValueSetDao valueSetDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_set);
        ButterKnife.bind(this);
        initView();
        valueSetDao = new ValueSetDao(AppContext.getInstance());
        //初始化显示阈值
        startValue();

    }



    private void initView() {
        textHeadTitle.setText("阈值设置");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_temMin = (EditText) findViewById(R.id.et_temMin);
        et_temMax = (EditText) findViewById(R.id.et_temMax);
        et_humMin = (EditText) findViewById(R.id.et_humMin);
        et_humMax = (EditText) findViewById(R.id.et_humMax);
        et_lightMin = (EditText) findViewById(R.id.et_lightMin);
        et_lightMax = (EditText) findViewById(R.id.et_lightMax);
        et_nitrogenMin = (EditText)findViewById(R.id.et_nitrogenMin);
        et_nitrogenMax = (EditText)findViewById(R.id.et_nitrogenMax);
        et_phosphorMin = (EditText) findViewById(R.id.et_phosphorMin);
        et_phosphorMax = (EditText) findViewById(R.id.et_phosphorMax);
        et_potassiumMin = (EditText) findViewById(R.id.et_potassiumMin);
        et_potassiumMax = (EditText) findViewById(R.id.et_potassiumMax);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
        et_temMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                submit();

                break;
        }
    }
    /**
     * 初始化阈值
     */
    public void startValue(){
        //查温度值
        ValueSet temValue=valueSetDao.findValueName("温度");
        if(temValue==null){
            et_temMin.setHint("暂未设置");
            et_temMax.setHint("暂未设置");
        }else{
            et_temMin.setText(temValue.getMin()+"");
            et_temMax.setText(temValue.getMax()+"");
        }
        //湿度
        ValueSet humValue=valueSetDao.findValueName("湿度");
        if(humValue==null){
            et_humMin.setHint("暂未设置");
            et_humMax.setHint("暂未设置");
        }else{
            et_humMin.setText(humValue.getMin()+"");
            et_humMax.setText(humValue.getMax()+"");
        }
        //光照
        ValueSet lightValue=valueSetDao.findValueName("光照");
        if(lightValue==null){
            et_lightMin.setHint("暂未设置");
            et_lightMax.setHint("暂未设置");
        }else{
            et_lightMin.setText(lightValue.getMin()+"");
            et_lightMax.setText(lightValue.getMax()+"");

        }
    }

    private void submit() {
        String temMin = et_temMin.getText().toString().trim();
        String temMax = et_temMax.getText().toString().trim();
        String humMin = et_humMin.getText().toString().trim();
        String humMax = et_humMax.getText().toString().trim();
        String lightMin = et_lightMin.getText().toString().trim();
        String lightMax = et_lightMax.getText().toString().trim();
        String nitrogenMin = et_nitrogenMin.getText().toString().trim();
        String nitrogenMax = et_nitrogenMax.getText().toString().trim();
        String phosphorMin = et_phosphorMin.getText().toString().trim();
        String phosphorMax = et_phosphorMax.getText().toString().trim();
        String potassiumMin = et_potassiumMin.getText().toString().trim();
        String potassiumMax = et_potassiumMax.getText().toString().trim();
        if (temMin.equals("")||temMax.equals("")|| humMin.equals("")
                || humMax.equals("")|| lightMin.equals("")|| lightMax.equals("")) {
            Toast.makeText(AppContext.getInstance(), "输入的值不能为空", Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(temMin)>Integer.parseInt(temMax)){
            Toast.makeText(AppContext.getInstance(), "温度的最小值不能大于最大值", Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(humMin)>Integer.parseInt(humMax)){
            Toast.makeText(AppContext.getInstance(), "湿度的最小值不能大于最大值", Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(lightMin)>Integer.parseInt(lightMax)){
            Toast.makeText(AppContext.getInstance(), "光照的最小值不能大于最大值", Toast.LENGTH_SHORT).show();
        }
        else{
            //判断数据表是否已经存在温度阈值，如果不存在直接添加，如果存在更新。
            ValueSet temValue=valueSetDao.findValueName("温度");
            if(temValue==null){
                temValue=new ValueSet(1, "温度", Integer.parseInt(temMin), Integer.parseInt(temMax));
                valueSetDao.add(temValue);
                Log.d("student3","执行添加温度");
            }else{
                temValue.setName("温度");
                temValue.setMin(Integer.parseInt(temMin));
                temValue.setMax( Integer.parseInt(temMax));
                valueSetDao.update(temValue);
                Log.d("student3","执行更新温度");
            }
            //湿度
            ValueSet humValue=valueSetDao.findValueName("湿度");
            if(humValue==null){
                humValue=new ValueSet(1, "湿度", Integer.parseInt(humMin), Integer.parseInt(humMax));
                valueSetDao.add(humValue);
                Log.d("student3","执行添加湿度");
            }else{
                humValue.setName("湿度");
                humValue.setMin(Integer.parseInt(humMin));
                humValue.setMax( Integer.parseInt(humMax));
                valueSetDao.update(humValue);
                Log.d("student3","执行更新湿度");
            }
            //光照
            ValueSet lightValue=valueSetDao.findValueName("光照");
            if(lightValue==null){
                lightValue= new ValueSet(1, "光照", Integer.parseInt(lightMin), Integer.parseInt(lightMax));
                valueSetDao.add(lightValue);
                Log.d("student3","执行添加光照");
            }else{
                lightValue.setName("光照");
                lightValue.setMin(Integer.parseInt(lightMin));
                lightValue.setMax( Integer.parseInt(lightMax));
                valueSetDao.update(lightValue);
                Log.d("student3","执行更新光照");

            }
            Toast.makeText(AppContext.getInstance(), "保存成功", Toast.LENGTH_SHORT).show();
            startValue();
        }




    }
}
