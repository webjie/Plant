package person.jack.plant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import person.jack.plant.R;
import person.jack.plant.adapter.WarAdapter;
import person.jack.plant.common.AppContext;
import person.jack.plant.db.dao.WarnRecordDao;
import person.jack.plant.db.entity.WarnRecord;

/**
 * 警告记录
 * Created by Administrator on 2018/6/4.
 */

public class WarnFragment extends Fragment {
    ListView listView;
    WarAdapter adapter;
    List<WarnRecord> list = new ArrayList<>();
    WarnRecordDao warnRecordDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warn, container, false);
        warnRecordDao = new WarnRecordDao(AppContext.getInstance());
        listView = (ListView) view.findViewById(R.id.waring_list);

        list = warnRecordDao.findAll();
        if (list.size() == 0 || list == null) {
            init();
            list = warnRecordDao.findAll();
            adapter = new WarAdapter(getContext(), R.layout.fragment_warn_item, list);
            listView.setAdapter(adapter);

        } else {
            adapter = new WarAdapter(getContext(), R.layout.fragment_warn_item, list);
            listView.setAdapter(adapter);
            Log.d("sutdent1", "有数据");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        WarnRecord record1 = new WarnRecord(1, "辣椒", "温度", 42, new Date());
        WarnRecord record2 = new WarnRecord(1, "白掌", "湿度", 67, new Date());
        warnRecordDao.add(record1);
        warnRecordDao.add(record2);
    }

}
