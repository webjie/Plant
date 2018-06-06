package person.jack.plant.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import person.jack.plant.R;
import person.jack.plant.adapter.WaterRecordAdaper;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.WaterRecordDao;
import person.jack.plant.db.entity.WaterRecord;

/**
 * 灌溉记录列表
 * Created by yanxu on 2018/6/4.
 */

public class WaterFragment extends Fragment {
    private WaterRecordAdaper waterRecordAdaper;
    private List<WaterRecord> waterList = new ArrayList<>();
    private ListView lv_water;
    private WaterRecordDao waterRecordDao;
    private Spinner spn_water;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);
        waterRecordDao = new WaterRecordDao(AppContext.getInstance());
        initView(view);

        waterList = waterRecordDao.findAll();
        if (waterList.size() == 0 || waterList == null) {
            init();
        }
        List<String> stringList =new ArrayList<>();
        for(int i=0;i<waterList.size();i++){
            stringList.add(waterList.get(i).getName());
        }

        stringList.add("查询所有记录");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,stringList);
        spn_water.setAdapter(arrayAdapter);


        return view;
    }

    public void init() {
        WaterRecord water1 = new WaterRecord(1, "广玉兰", new Date());
        WaterRecord water2 = new WaterRecord(1, "发财树", new Date());

        WaterRecord water3 = new WaterRecord(1, "金钱树", new Date());
        WaterRecord water4 = new WaterRecord(1, "金钱树", new Date());
        waterRecordDao.add(water1);
        waterRecordDao.add(water2);
        waterRecordDao.add(water3);
        waterRecordDao.add(water4);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        lv_water = (ListView) view.findViewById(R.id.lv_water);
        spn_water = (Spinner) view.findViewById(R.id.spn_water);
        spn_water.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


              String name=spn_water.getSelectedItem().toString();

                if(name.equals("查询所有记录")){
                    waterList.clear();
                    waterList = waterRecordDao.findAll();
                    waterRecordAdaper = new WaterRecordAdaper(getContext(), R.layout.item_water_record, waterList);
                    lv_water.setAdapter(waterRecordAdaper);
                }else{
                    waterList.clear();
                    waterList = waterRecordDao.findForName(name);
                    if(waterList.size()!=0){
                        waterRecordAdaper = new WaterRecordAdaper(getContext(), R.layout.item_water_record, waterList);
                        lv_water.setAdapter(waterRecordAdaper);

                    }else{
                        waterList.clear();
                        waterRecordAdaper = new WaterRecordAdaper(getContext(), R.layout.item_water_record, waterList);
                        lv_water.setAdapter(waterRecordAdaper);
                        Toast.makeText(getContext(), "暂无"+name+"的灌溉记录", Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
