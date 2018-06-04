package person.jack.plant.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import person.jack.plant.R;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.PlantsDao;
import person.jack.plant.db.entity.Plants;

/**
 * Created by yanxu on 2018/6/4.
 */

public class PlantsAddFragment extends Fragment implements View.OnClickListener {
    private EditText et_plantId;
    private EditText et_plantName;
    private Spinner spn_plantLive;
    private TextView tv_plantDate;
    private Button btn_plantSave;
    private String growthState="";
    private PlantsDao plantsDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_plantadd, container, false);
        initView(view);
        plantsDao=new PlantsDao(AppContext.getInstance());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(View view) {
        et_plantId = (EditText) view.findViewById(R.id.et_plantId);
        et_plantName = (EditText) view.findViewById(R.id.et_plantName);
        spn_plantLive = (Spinner) view.findViewById(R.id.spn_plantLive);
   tv_plantDate = (TextView) view.findViewById(R.id.tv_plantDate);
        btn_plantSave = (Button) view.findViewById(R.id.btn_plantSave);

       tv_plantDate.setOnClickListener(this);
        btn_plantSave.setOnClickListener(this);
        spn_plantLive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                growthState=spn_plantLive.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           case R.id.tv_plantDate:
              final  Calendar c=Calendar.getInstance();
                DatePickerDialog dialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year,monthOfYear+1,dayOfMonth);
                        tv_plantDate.setText(DateFormat.format("yyyy-MM-dd",c)+"");

                          ;


                        Log.d("student3--",DateFormat.format("yyyy年MM月dd日",c)+"");
                       // System.out.print(DateFormat.format("yyyy-MM-dd",c));

                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                 dialog.show();
                break;
            case R.id.btn_plantSave:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String plantId = et_plantId.getText().toString().trim();
        String plantName = et_plantName.getText().toString().trim();
        if (plantName.equals("")) {
            Toast.makeText(getContext(), "植物名称不能为空", Toast.LENGTH_SHORT).show();
        }
        else if(tv_plantDate.getText().toString().equals("选择日期")){
            Toast.makeText(getContext(), "请选择日期", Toast.LENGTH_SHORT).show();
        }

        else{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            Date date=null;
            try {
                date=format.parse(tv_plantDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Plants plants=new Plants(1,R.drawable.default_image,plantName,growthState,date);
            plantsDao.add(plants);
            Toast.makeText(getContext(), "添加植物信息成功", Toast.LENGTH_SHORT).show();
            tv_plantDate.setText("选择日期");
            et_plantName.setText("");






        }



    }
}
