package person.jack.plant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import person.jack.plant.R;
import person.jack.plant.adapter.WareAdapter;
import person.jack.plant.db.entity.WarnRecord;

/**
 * Created by Administrator on 2018/6/4.
 */

public class WaringFragment extends Fragment {
    ListView listView;
    WareAdapter adapter;
    List<WarnRecord> list=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waring, container, false);
        listView = (ListView) view.findViewById(R.id.waring_list);
        for (int i=0;i<5;i++){
            WarnRecord warnRecord=new WarnRecord(i,"水分过少","",i,new Date());
            list.add(warnRecord);
        }
        adapter = new WareAdapter(getContext(), R.layout.waring_item, list);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
