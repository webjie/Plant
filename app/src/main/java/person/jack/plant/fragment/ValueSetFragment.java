package person.jack.plant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.ValueSetDao;
import person.jack.plant.db.entity.ValueSet;

/**
 * 阈值设置界面
 * Created by yanxu on 2018/6/4.
 */

public class ValueSetFragment extends Fragment implements View.OnClickListener {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_valueset, container, false);
        initView(view);
        valueSetDao = new ValueSetDao(AppContext.getInstance());
        //初始化显示阈值
        startValue();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        et_temMin = (EditText) view.findViewById(R.id.et_temMin);
        et_temMax = (EditText) view.findViewById(R.id.et_temMax);
        et_humMin = (EditText) view.findViewById(R.id.et_humMin);
        et_humMax = (EditText) view.findViewById(R.id.et_humMax);
        et_lightMin = (EditText) view.findViewById(R.id.et_lightMin);
        et_lightMax = (EditText) view.findViewById(R.id.et_lightMax);
        et_nitrogenMin = (EditText) view.findViewById(R.id.et_nitrogenMin);
        et_nitrogenMax = (EditText) view.findViewById(R.id.et_nitrogenMax);
        et_phosphorMin = (EditText) view.findViewById(R.id.et_phosphorMin);
        et_phosphorMax = (EditText) view.findViewById(R.id.et_phosphorMax);
        et_potassiumMin = (EditText) view.findViewById(R.id.et_potassiumMin);
        et_potassiumMax = (EditText) view.findViewById(R.id.et_potassiumMax);
        btn_save = (Button) view.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
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
            Toast.makeText(getContext(), "输入的值不能为空", Toast.LENGTH_SHORT).show();
        }else{
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
            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            startValue();
        }




    }
}
