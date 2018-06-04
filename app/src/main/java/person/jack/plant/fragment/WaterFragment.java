package person.jack.plant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);
        waterRecordDao=new WaterRecordDao(AppContext.getInstance());
        initView(view);

       waterList=waterRecordDao.findAll();
       if(waterList.size()==0||waterList==null){
           init();
           waterList=waterRecordDao.findAll();
           waterRecordAdaper =new WaterRecordAdaper(getContext(),R.layout.item_water_record,waterList);
           lv_water.setAdapter(waterRecordAdaper);

       }else{
           waterRecordAdaper =new WaterRecordAdaper(getContext(),R.layout.item_water_record,waterList);
           lv_water.setAdapter(waterRecordAdaper);
           Log.d("sutdent3","有数据");
       }

        return view;
    }
    public void init(){
        WaterRecord water1=new WaterRecord(1,"广玉兰",new Date());
        WaterRecord water2=new WaterRecord(1,"发财树",new Date());
        WaterRecord water3=new WaterRecord(1,"金钱树",new Date());
        waterRecordDao.add(water1);
        waterRecordDao.add(water2);
        waterRecordDao.add(water3);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        lv_water = (ListView) view.findViewById(R.id.lv_water);
    }
}
