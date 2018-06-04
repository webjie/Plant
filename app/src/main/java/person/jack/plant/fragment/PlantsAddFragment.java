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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import person.jack.plant.R;

/**
 * Created by yanxu on 2018/6/4.
 */

public class PlantsAddFragment extends Fragment implements View.OnClickListener {
    private EditText et_plantId;
    private EditText et_plantName;
    private Spinner spn_plantLive;
    private Button spn_plantDate;
    private Button spn_plantSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_plantadd, container, false);
        initView(view);
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
        spn_plantDate = (Button) view.findViewById(R.id.spn_plantDate);
        spn_plantSave = (Button) view.findViewById(R.id.spn_plantSave);

        spn_plantDate.setOnClickListener(this);
        spn_plantSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spn_plantDate:
              final  Calendar c=Calendar.getInstance();
                DatePickerDialog dialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year,monthOfYear+1,dayOfMonth);
                        Log.d("student3--",DateFormat.format("yyyy年MM月dd日",c)+"");
                       // System.out.print(DateFormat.format("yyyy-MM-dd",c));

                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                 dialog.show();
                break;
            case R.id.spn_plantSave:

                break;
        }
    }

    private void submit() {
        // validate
        String plantId = et_plantId.getText().toString().trim();
        if (TextUtils.isEmpty(plantId)) {
            Toast.makeText(getContext(), "plantId不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String plantName = et_plantName.getText().toString().trim();
        if (TextUtils.isEmpty(plantName)) {
            Toast.makeText(getContext(), "plantName不能为空", Toast.LENGTH_SHORT).show();
            return;
        }



    }
}
